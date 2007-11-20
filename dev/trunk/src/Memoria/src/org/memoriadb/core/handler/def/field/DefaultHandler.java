package org.memoriadb.core.handler.def.field;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.load.binder.ObjectFieldReference;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.*;
import org.memoriadb.util.Constants;

public class DefaultHandler implements ISerializeHandler {

  private final MemoriaFieldClass fClassObject;

  public DefaultHandler(MemoriaFieldClass classObject) {
    fClassObject = classObject;
  }

  @Override
  public void checkCanInstantiateObject(String className, IDefaultInstantiator defaultInstantiator) {
    if (!defaultInstantiator.canInstantiateObject(className)) throw new SchemaCorruptException("Can not instantiate Object of type: " + className);
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
    
    new MetaClassInheritanceTraverser(fClassObject) {
      
      @Override
      protected void handle(IMemoriaClass metaObject) {
        for(MemoriaField field: ((MemoriaFieldClass) metaObject).getFields()) {
          if(field.getFieldType() != Type.typeClass) continue;
          
          try {
            traversal.handle(fFieldObject.get(field.getName()));
          }
          catch (Exception e) {
            throw new MemoriaException("Exception during object traversel. Java Class: '"+metaObject.getJavaClassName()+"' Java-Field: '"+field+"' type of the field: '"+field.getFieldType()+"'", e);
          }
        }
      }
    };
  }

  private IFieldObject createObject(IReaderContext context, IObjectId typeId) {
    if (context.getMode() == DBMode.clazz) {
      return new FieldObject(context.getDefaultInstantiator().newInstance(fClassObject.getClassName()));
    }
    return new FieldMapDataObject(typeId);
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
        public void visitEnum(Type type, int enumOrdinal) {
          if (enumOrdinal == Constants.NO_ENUM_REF) return;
          fMember.set(field.getName(), enumOrdinal);
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
