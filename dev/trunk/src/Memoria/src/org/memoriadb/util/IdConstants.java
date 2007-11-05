/**
 * 
 */
package org.memoriadb.util;

public class IdConstants {
  
  /**
   * The ObjectId for the super class Reference if the MetaClass does not have a super class.
   */
  public static final long NO_SUPER_CLASS = -1;

  /**
   *  Those values are used instead of the metaClass id to mark an object as deleted.
   */
  public static final long METACLASS_DELETED = -2;
  public static final long OBJECT_DELETED = -3;

  /**
   * ObjectID for the Meta-MetaClass for the field based MetaClass.
   */
  public static final long METACLASS_OBJECT_ID = 1;
  
  /**
   * ObjectID for the Meta-MetaClass for the handler based MetaClass. 
   */
  public static final long HANDLER_META_CLASS_OBJECT_ID = 2;

  /**
   * ObjectID for the MetaClass which represents Arrays.
   */
  public static final long ARRAY_META_CLASS = 3;

  private IdConstants() {}
  
}