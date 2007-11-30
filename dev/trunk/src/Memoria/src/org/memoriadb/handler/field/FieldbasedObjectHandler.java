package org.memoriadb.handler.field;

import java.io.*;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.exception.*;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.meta.*;
import org.memoriadb.handler.IHandler;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

public class FieldbasedObjectHandler implements IHandler {

  private final FieldbasedMemoriaClass fClassObject;

  public FieldbasedObjectHandler(FieldbasedMemoriaClass classObject) {
    fClassObject = classObject;
  }

  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {
    if (!instantiator.canInstantiateObject(className)) throw new SchemaException("Can not instantiate Object of type: " + className);
  }

  @Override
  public Object deserialize(final DataInputStream input, final IReaderContext context, IObjectId typeId) throws IOException {
    final IFieldbasedObject result = createObject(context, typeId);
    
    superDeserialize(result, input, context);
    if (input.available() > 0) throw new MemoriaException("Object not fully deserialized: " + result);
    
    return result.getObject();
  }

  @Override
  public String getClassName() {
    throw new MemoriaException("not implemented");
  }

  @Override
  public void serialize(final Object obj, final DataOutputStream output, final ISerializeContext context) throws Exception {
    IFieldbasedObject fieldObject = getFieldObject(obj);
    
    for(MemoriaField metaField: (fClassObject).getFields()) {
      output.writeInt(metaField.getId());
      Object value = fieldObject.get(metaField.getName());
      metaField.getFieldType().writeValue(output, value, context);
    }
    
    if (fClassObject.getSuperClass() == null) return;
    FieldbasedObjectHandler superHandler = (FieldbasedObjectHandler) fClassObject.getSuperClass().getHandler();
    superHandler.serialize(obj, output, context);
  }

  @Override
  public void traverseChildren(final Object obj, final IObjectTraversal traversal) {
    final IFieldbasedObject fFieldObject = getFieldObject(obj);
    
    for(MemoriaField field: fClassObject.getFields()) {
      if(field.getFieldType() != Type.typeClass) continue;
      Object referencee = fFieldObject.get(field.getName());
      if(referencee == null) continue;
      try {
        traversal.handle(referencee);
      }
      catch (Exception e) {
        throw new MemoriaException("Exception during object traversel. Java Class: '"+fClassObject.getJavaClassName()+"' Java-Field: '"+field+"' type of the field: '"+field.getFieldType()+"'", e);
      }
    }
    
    IMemoriaClass superClass = fClassObject.getSuperClass();
    if (superClass != null) {
      superClass.getHandler().traverseChildren(obj, traversal);
    }
  }

  private IFieldbasedObject createObject(IReaderContext context, IObjectId typeId) {
    if (context.isInDataMode()) {
      return new FieldbasedDataObject(typeId);
    }
    return new FieldbasedObject(context.getDefaultInstantiator().newInstance(fClassObject.getClassName()));
  }

  private IFieldbasedObject getFieldObject(Object obj) {
    if (obj instanceof IFieldbasedObject) {
      return (IFieldbasedObject) obj;
    }
    
    return new FieldbasedObject(obj);
  }

  private void superDeserialize(Object object, DataInputStream input, final IReaderContext context) throws IOException {
    final IFieldbasedObject result = getFieldObject(object);
    
    for(int i = 0; i < (fClassObject).getFieldCount(); ++i) {
      int fieldId = input.readInt();
      final MemoriaField field = (fClassObject).getField(fieldId);
      field.getFieldType().readValue(input, context, new ITypeVisitor(){

        @Override
        public void visitClass(Type type, IObjectId objectId) {
          context.objectToBind(new ObjectFieldReference(result, field.getName(), objectId));
        }

        @Override
        public void visitNull() {
          result.set(field.getName(), null);
        }

        @Override
        public void visitPrimitive(Type type, Object value) {
          result.set(field.getName(), value);
        }
        
      });
    }
    
    if (fClassObject.getSuperClass() == null) return;
    FieldbasedObjectHandler superHandler = (FieldbasedObjectHandler) fClassObject.getSuperClass().getHandler();
    superHandler.superDeserialize(object, input, context);
  }

}
