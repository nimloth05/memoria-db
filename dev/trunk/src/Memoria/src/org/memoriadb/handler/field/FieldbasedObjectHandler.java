package org.memoriadb.handler.field;

import java.io.*;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.exception.*;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
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
  public Object deserialize(final DataInputStream input, final IReaderContext context, IObjectId typeId) throws Exception {
    final IFieldbasedObject result = createObject(context, typeId);
    
    superDeserialize(result, input, context);
    
    return result.getObject();
  }

  @Override
  public String getClassName() {
    return fClassObject.getClassName();
  }

  @Override
  public void serialize(final Object obj, final DataOutput output, final IWriterContext context) throws Exception {
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
      if(field.isWeakRef()) continue;
      
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

  private void superDeserialize(Object object, DataInputStream input, final IReaderContext context) throws Exception {
    final IFieldbasedObject result = getFieldObject(object);
    
    System.out.println(fClassObject);
    
    for(int i = 0; i < (fClassObject).getFieldCount(); ++i) {
      int fieldId = input.readInt();
      final MemoriaField field = (fClassObject).getField(fieldId);
      field.getFieldType().readValue(input, context, new ITypeVisitor(){

        @Override
        public void visitClass(Type type, IObjectId objectId) {
          context.addGenOneBinding(new ObjectFieldReference(result, field.getName(), objectId));
        }

        @Override
        public void visitNull() {
          result.set(field.getName(), null);
        }

        @Override
        public void visitPrimitive(Type type, Object value) {
          result.set(field.getName(), value);
        }

        @Override
        public void visitValueObject(Object value) {
          result.set(field.getName(), value);
        }
        
      });
    }
    
    if (fClassObject.getSuperClass() == null) return;
    FieldbasedObjectHandler superHandler = (FieldbasedObjectHandler) fClassObject.getSuperClass().getHandler();
    superHandler.superDeserialize(object, input, context);
  }

}
