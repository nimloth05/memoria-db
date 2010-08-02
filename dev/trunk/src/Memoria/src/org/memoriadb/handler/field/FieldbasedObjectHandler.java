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

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.exception.SchemaException;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.core.meta.ITypeVisitor;
import org.memoriadb.core.meta.Type;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.handler.IHandler;
import org.memoriadb.handler.IHandlerConfig;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.CannotInstantiateException;
import org.memoriadb.instantiator.IInstantiator;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Persists normal objects via Java Reflection.
 */
public class FieldbasedObjectHandler implements IHandler, IHandlerConfig {

  private IMemoriaClass fSuperClass;

  private String fClassName;

  private final Map<Integer, MemoriaField> fFieldIdToInfo = new HashMap<Integer, MemoriaField>();
  private final Map<String, MemoriaField> fFieldNameToInfo = new HashMap<String, MemoriaField>();

  public FieldbasedObjectHandler(final String className) {
    fClassName = className;
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

    deserializeObject(result, input, context);

    return result.getObject();
  }

  @Override
  public String getClassName() {
    return fClassName;
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

  @Override
  public void traverseChildren(final Object obj, final IObjectTraversal traversal) {
    final IFieldbasedObject fFieldObject = getFieldObject(obj);

    for (MemoriaField field : getFields()) {
      if (field.getFieldType() != Type.typeClass) continue;
      if (field.isWeakRef()) continue;

      Object referencee = fFieldObject.get(field.getName());
      if (referencee == null) continue;

      //Primitives werden nicht mehr berücksichtigt beim Traversal. 
      //Das erhaltene Objekt kann immer noch ein Primitive oder ein enum sein. Auch in diesem Fall muss NICHT traversiert werden! (bug #1749) msc
      if (Type.isPrimitive(referencee)) continue;

      try {
        traversal.handle(referencee);
      }
      catch (Exception e) {
        throw new MemoriaException("Exception during object traversel. Java Class: '" + getClassName()
          + "' Java-Field: '" + field + "' type of the field: '" + field.getFieldType() + "'", e);
      }
    }

    if (fSuperClass != null) {
      fSuperClass.getHandler().traverseChildren(obj, traversal);
    }
  }

  private IFieldbasedObject createObject(IReaderContext context, IObjectId typeId) {
    if (context.isInDataMode()) { return new FieldbasedDataObject(typeId); }
    return new FieldbasedObject(context.getDefaultInstantiator().newInstance(getClassName()));
  }

  private IFieldbasedObject getFieldObject(Object obj) {
    if (obj instanceof IFieldbasedObject) { return (IFieldbasedObject) obj; }

    return new FieldbasedObject(obj);
  }
  
  private void deserializeObject(IFieldbasedObject object, DataInputStream input, final IReaderContext context) throws Exception {
    final IFieldbasedObject result = object;

    for (int i = 0; i < getFieldCount(); ++i) {
      int fieldId = input.readInt();
      final MemoriaField field = getField(fieldId);

      field.getFieldType().readValue(input, context, new ITypeVisitor() {

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

    if (fSuperClass == null) return;
    FieldbasedObjectHandler superHandler = (FieldbasedObjectHandler) fSuperClass.getHandler();
    superHandler.deserializeObject(object, input, context);
  }

  public void addMetaField(MemoriaField metaField) {
    fFieldIdToInfo.put(metaField.getId(), metaField);
    fFieldNameToInfo.put(metaField.getName(), metaField);
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

  public void setClassName(String name) {
    fClassName = name;
  }

  public static FieldbasedObjectHandler createNewType(Class<?> klass) {
    if(klass.isArray()) throw new IllegalArgumentException("Array not allowed. This is an internal error.");
    
    FieldbasedObjectHandler handler = new FieldbasedObjectHandler(klass.getName());
    handler.initializeMetaInfo(klass);
    return handler;
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

  @Override
  public void setSuperClass(IMemoriaClass superClass) {
    fSuperClass = superClass;
  }
}
