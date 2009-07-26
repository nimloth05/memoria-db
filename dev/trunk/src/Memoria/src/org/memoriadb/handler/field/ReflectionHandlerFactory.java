package org.memoriadb.handler.field;

import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.id.IObjectId;

/**
 * Helper Factory for memoriaClass and Handlers which are reflection based.
 *
 * Internal Class.
 */
public final class ReflectionHandlerFactory {
  /**
   * Introspects the given klass and adds all fields. Used to initially create a MetaClass, when the first object of a
   * given type enters the memoria-reference-space.
   *
   * @param klass the java-type for the new memoria Class.
   * @param memoriaClassId id of the memoria-type of this class.
   *
   */
  public static FieldbasedMemoriaClass createNewType(Class<?> klass, IObjectId memoriaClassId) {
    return createNewType(klass, memoriaClassId, ReflectionUtil.hasValueObjectAnnotation(klass));
  }
 /**
   * Introspects the given klass and adds all fields. Used to initially create a MetaClass, when the first object of a
   * given type enters the memoria-reference-space.
   *
   * @param klass java type of the the new handler and memoriaClass.
   * @param memoriaClassId id of the memoriaClass which this class can be serialized.
   * @param isValueObject <tt>true</tt> if this class represent a valueObject.
   *
   */
  public static FieldbasedMemoriaClass createNewType(Class<?> klass, IObjectId memoriaClassId, boolean isValueObject) {
    FieldbasedObjectHandler handler = FieldbasedObjectHandler.createNewType(klass);
    return new FieldbasedMemoriaClass(handler, memoriaClassId, isValueObject);
  }
}
