/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb.handler.field;

import java.io.DataOutput;
import java.lang.reflect.Field;
import java.util.*;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.exception.*;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.core.meta.*;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.core.util.io.IDataInput;
import org.memoriadb.handler.*;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.*;

/**
 * Persists normal objects via Java Reflection.
 * 
 * NOTE: This class is performance critical while opening the repository.
 * Therefore, the deserialization method now first tests if it is in data mode.
 * If not it use a special method which sets the field using cached Fields from
 * the fFields map. It would be nicer to have different handlers for the different modes!
 * 
 */
public class FieldbasedObjectHandler implements IHandler, IHandlerConfig {

  private IMemoriaClass fSuperClass;

  private String fClassName;

  private final Map<Integer, MemoriaField> fFieldIdToInfo = new HashMap<Integer, MemoriaField>(4);
  private final Map<String, MemoriaField> fFieldNameToInfo = new HashMap<String, MemoriaField>(4);
  private final Map<Integer, Field> fFields = new HashMap<Integer, Field>(4);

  static int invocations = 0;

  static int cached = 0;

  public static FieldbasedObjectHandler createNewType(Class<?> klass) {
    if (klass.isArray()) throw new IllegalArgumentException("Array not allowed. This is an internal error.");

    FieldbasedObjectHandler handler = new FieldbasedObjectHandler(klass.getName());
    handler.initializeMetaInfo(klass);
    return handler;
  }

  public FieldbasedObjectHandler(final String className) {
    fClassName = className;
  }

  public void addMetaField(MemoriaField metaField) {
    fFieldIdToInfo.put(metaField.getId(), metaField);
    fFieldNameToInfo.put(metaField.getName(), metaField);
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
  public Object deserialize(final IDataInput input, final IReaderContext context, IObjectId typeId) throws Exception {

    if (context.isInDataMode()) {
      FieldbasedDataObject result = createDataObject(context, typeId);
      deserializeDataObject(result, input, context);
      return result;
    }

    Object result = createObject(context, typeId);
    deserializeObject(result, input, context);
    return result;
  }

  @Override
  public String getClassName() {
    return fClassName;
  }

  public MemoriaField getField(int fieldId) {
    return fFieldIdToInfo.get(fieldId);
  }

  public int getFieldCount() {
    return fFieldIdToInfo.values().size();
  }

  public Iterable<MemoriaField> getFields() {
    return fFieldIdToInfo.values();
  }

  @Override
  public void serialize(final Object obj, final DataOutput output, final IWriterContext context) throws Exception {
    IFieldbasedObject fieldObject = getFieldObject(obj);

    for (MemoriaField metaField : getFields()) {
      output.writeInt(metaField.getId());
      Object value = fieldObject.get(metaField.getName());

      metaField.getFieldType().writeValue(output, value, context);
    }

    if (fSuperClass == null) return;
    FieldbasedObjectHandler superHandler = (FieldbasedObjectHandler) fSuperClass.getHandler();
    superHandler.serialize(obj, output, context);
  }

  public void setClassName(String name) {
    fClassName = name;
  }

  @Override
  public void setSuperClass(IMemoriaClass superClass) {
    fSuperClass = superClass;
  }

  @Override
  public void traverseChildren(final Object obj, final IObjectTraversal traversal) {
    final IFieldbasedObject fFieldObject = getFieldObject(obj);

    for (MemoriaField field : getFields()) {
      if (field.getFieldType() != Type.typeClass) continue;
      if (field.isWeakRef()) continue;

      Object referencee = fFieldObject.get(field.getName());
      if (referencee == null) continue;

      // Primitives werden nicht mehr berücksichtigt beim Traversal.
      // Das erhaltene Objekt kann immer noch ein Primitive oder ein enum sein. Auch in diesem Fall muss NICHT
      // traversiert werden! (bug #1749) msc
      if (Type.isPrimitive(referencee)) continue;

      try {
        traversal.handle(referencee);
      }
      catch (Exception e) {
        throw new MemoriaException("Exception during object traversel. Java Class: '" + getClassName() + "' Java-Field: '" + field
            + "' type of the field: '" + field.getFieldType() + "'", e);
      }
    }

    if (fSuperClass != null) {
      fSuperClass.getHandler().traverseChildren(obj, traversal);
    }
  }

  private FieldbasedDataObject createDataObject(IReaderContext context, IObjectId typeId) {
    return new FieldbasedDataObject(typeId);
  }

  private Object createObject(IReaderContext context, IObjectId typeId) {
    return context.getDefaultInstantiator().newInstance(getClassName());
  }
  
  private void deserializeDataObject(IFieldbasedObject object, IDataInput input, final IReaderContext context) throws Exception {

    final IFieldbasedObject result = object;

    for (int i = 0; i < getFieldCount(); ++i) {
      int fieldId = input.readInt();
      final MemoriaField field = getField(fieldId);

      field.getFieldType().readValue(input, context, new ITypeVisitor() {

        @Override
        public void visitClass(Type type, IObjectId objectId) {
          context.addGenOneBinding(new DataObjectFieldReference(result, field.getName(), objectId));
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

    if (fSuperClass == null) return;
    FieldbasedObjectHandler superHandler = (FieldbasedObjectHandler) fSuperClass.getHandler();
    superHandler.deserializeDataObject(object, input, context);
  }
  
  private void deserializeObject(final Object result, IDataInput input, final IReaderContext context) throws Exception {

    for (int i = 0; i < getFieldCount(); ++i) {
      int fieldId = input.readInt();
      MemoriaField memoriaField = getField(fieldId);
      final Field field = getField(result, memoriaField);

      memoriaField.getFieldType().readValue(input, context, new ITypeVisitor() {

        @Override
        public void visitClass(Type type, IObjectId objectId) {
          context.addGenOneBinding(new ObjectFieldReference(result, field, objectId));
        }

        @Override
        public void visitNull() {
          try {
            field.set(result, null);
          }
          catch (Exception e) {
            throw new MemoriaException(e);
          }
        }

        @Override
        public void visitPrimitive(Type type, Object value) {
          try {
            field.set(result, value);
          }
          catch (Exception e) {
            throw new MemoriaException(e);
          }

        }

        @Override
        public void visitValueObject(Object value) {
          try {
            field.set(result, value);
          }
          catch (Exception e) {
            throw new MemoriaException(e);
          }
        }

      });
    }

    if (fSuperClass == null) return;
    FieldbasedObjectHandler superHandler = (FieldbasedObjectHandler) fSuperClass.getHandler();
    superHandler.deserializeObject(result, input, context);
  }

  private Field getField(Object result, MemoriaField memoriaField) {
    Field field = fFields.get(memoriaField.getId());
    if (field == null) {
      field = ReflectionUtil.getField(result.getClass(), memoriaField.getName());
      field.setAccessible(true);
      fFields.put(memoriaField.getId(), field);
    } 
    return field;
  }

  private IFieldbasedObject getFieldObject(Object obj) {
    if (obj instanceof IFieldbasedObject) { return (IFieldbasedObject) obj; }

    return new FieldbasedObject(obj);
  }

  private void initializeMetaInfo(Class<?> klass) {
    Field[] fields = klass.getDeclaredFields();
    int fieldId = 0;
    for (Field field : fields) {
      if (ReflectionUtil.isStatic(field)) continue;
      if (ReflectionUtil.isMemoriaTransient(field)) continue;

      MemoriaField metaField = MemoriaField.create(++fieldId, field);
      addMetaField(metaField);
    }
  }
}
