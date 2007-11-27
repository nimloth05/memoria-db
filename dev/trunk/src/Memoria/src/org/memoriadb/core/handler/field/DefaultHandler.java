package org.memoriadb.core.handler.field;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.load.binder.ObjectFieldReference;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.*;

public class DefaultHandler implements ISerializeHandler {

  private final FieldbasedMemoriaClass fClassObject;

  public DefaultHandler(FieldbasedMemoriaClass classObject) {
    fClassObject = classObject;
  }

  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {
    if (!instantiator.canInstantiateObject(className)) throw new SchemaException("Can not instantiate Object of type: " + className);
  }

  @Override
  public Object deserialize(final DataInputStream input, final IReaderContext context, IObjectId typeId) throws IOException {
    final IFieldObject result = createObject(context, typeId);
    
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
    IFieldObject fieldObject = getFieldObject(obj);
    
    for(MemoriaField metaField: (fClassObject).getFields()) {
      output.writeInt(metaField.getId());
      Object value = fieldObject.get(metaField.getName());
      metaField.getFieldType().writeValue(output, value, context);
    }
    
    if (fClassObject.getSuperClass() == null) return;
    DefaultHandler superHandler = (DefaultHandler) fClassObject.getSuperClass().getHandler();
    superHandler.serialize(obj, output, context);
  }

  @Override
  public void traverseChildren(final Object obj, final IObjectTraversal traversal) {
    final IFieldObject fFieldObject = getFieldObject(obj);
    
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

  private IFieldObject createObject(IReaderContext context, IObjectId typeId) {
    if (context.isInDataMode()) {
      return new FieldMapDataObject(typeId);
    }
    return new FieldObject(context.getDefaultInstantiator().newInstance(fClassObject.getClassName()));
  }

  private IFieldObject getFieldObject(Object obj) {
    if (obj instanceof IFieldObject) {
      return (IFieldObject) obj;
    }
    
    return new FieldObject(obj);
  }

  private void superDeserialize(Object object, DataInputStream input, IReaderContext context) throws IOException {
    IFieldObject result = getFieldObject(object);
    
    for(int i = 0; i < (fClassObject).getFieldCount(); ++i) {
      int fieldId = input.readInt();
      final MemoriaField field = (fClassObject).getField(fieldId);
      field.getFieldType().readValue(input, context, new TypeVisitorHelper<Void, IFieldObject>(result, context){

        @Override
        public void visitClass(Type type, IObjectId objectId) {
          if (fContext.isNullReference(objectId)) return;
          fContext.objectToBind(new ObjectFieldReference(fMember, field.getName(), objectId));
        }

        @Override
        public void visitPrimitive(Type type, Object value) {
          fMember.set(field.getName(), value);
        }
        
      });
    }
    
    if (fClassObject.getSuperClass() == null) return;
    DefaultHandler superHandler = (DefaultHandler) fClassObject.getSuperClass().getHandler();
    superHandler.superDeserialize(object, input, context);
  }

}
