package org.memoriadb.handler.field;

import java.io.*;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.exception.*;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.core.meta.*;
import org.memoriadb.handler.IHandler;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.*;

public class FieldbasedObjectHandler implements IHandler {

  private final FieldbasedMemoriaClass fClassObject;

  public FieldbasedObjectHandler(FieldbasedMemoriaClass classObject) {
    fClassObject = classObject;
  }

  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {
    try {
      instantiator.checkCanInstantiateObject(className);
    }
    catch (CannotInstantiateException e) {
      throw new SchemaException("Cannot instantiate Object of type: " + className, e);
    }
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

    for (MemoriaField metaField : (fClassObject).getFields()) {
      output.writeInt(metaField.getId());
      Object value = fieldObject.get(metaField.getName());


      // FIXME habe folgende Zeile ersetzt, ist das korrekt? Wird Ist der Typ des Feldes überhaupt noch relevant?
      metaField.getFieldType().writeValue(output, value, context);
//      Type.writeValueWithType(output, value, context);
    }

    if (fClassObject.getSuperClass() == null) return;
    FieldbasedObjectHandler superHandler = (FieldbasedObjectHandler) fClassObject.getSuperClass().getHandler();
    superHandler.serialize(obj, output, context);
  }

  @Override
  public void traverseChildren(final Object obj, final IObjectTraversal traversal) {
    final IFieldbasedObject fFieldObject = getFieldObject(obj);

    for (MemoriaField field : fClassObject.getFields()) {
      if (field.getFieldType() != Type.typeClass) continue;
      if (field.isWeakRef()) continue;

      Object referencee = fFieldObject.get(field.getName());
      if (referencee == null) continue;

      // FIXME primitives werden nicht mehr berücksichtigt beim Traversal.
      // Das erhaltene Objekt kann immer noch ein Primitive oder ein enum sein. Auch in diesem Fall muss NICHT traversiert werden! (bug #1749) msc

      if (Type.isPrimitive(referencee)) continue;

      try {
        traversal.handle(referencee);
      }
      catch (Exception e) {
        throw new MemoriaException("Exception during object traversel. Java Class: '" + fClassObject.getJavaClassName() + "' Java-Field: '" + field + "' type of the field: '" + field.getFieldType()
            + "'", e);
      }
    }

    IMemoriaClass superClass = fClassObject.getSuperClass();
    if (superClass != null) {
      superClass.getHandler().traverseChildren(obj, traversal);
    }
  }

  private IFieldbasedObject createObject(IReaderContext context, IObjectId typeId) {
    if (context.isInDataMode()) { return new FieldbasedDataObject(typeId); }
    return new FieldbasedObject(context.getDefaultInstantiator().newInstance(fClassObject.getClassName()));
  }

  private IFieldbasedObject getFieldObject(Object obj) {
    if (obj instanceof IFieldbasedObject) { return (IFieldbasedObject) obj; }

    return new FieldbasedObject(obj);
  }

  private void superDeserialize(Object object, DataInputStream input, final IReaderContext context) throws Exception {
    final IFieldbasedObject result = getFieldObject(object);

    for (int i = 0; i < (fClassObject).getFieldCount(); ++i) {
      int fieldId = input.readInt();
      final MemoriaField field = (fClassObject).getField(fieldId);

      // FIXME Folgende Zeige
      //siehe Kommentar bei serialize
      field.getFieldType().readValue(input, context, new ITypeVisitor() {
      ///Type.readValueWithType(input, context, new ITypeVisitor() {

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
