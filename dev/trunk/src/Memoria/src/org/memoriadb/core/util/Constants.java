package org.memoriadb.core.util;

import java.util.UUID;

public final class Constants {
  
  /**
   * When a memoria-db is a host db by itself, it has no host-uuid.
   */
  public static final UUID NO_HOST_UUID = new UUID(0, 0);

  /**
   * Initial Version for a newly added Object.
   */
  public static final long INITIAL_REVISION = 1;


  /**
   * The size of an int in bytes.
   */
  public static final int INT_LEN = 4;

  
  /**
   * The size of a long in bytes.
   */
  public static final int LONG_LEN = 8;

  public static final int DEFAULT_OBJECT_SIZE = 80;

  public static final long NO_HOST_BRANCH_REVISION = -1;

  /**
   * Marker, that a primtive Object such as Integer was null.
   */
  public static final byte NULL_VALUE = -1;

  /**
   * Marker, that a primtive Object such as Integer was not null.
   */
  public static final byte ASSIGNED_VALUE = 1;

  public static final long INITIAL_HEAD_REVISION = 0;

  /**
   * Default value for a enum ref which is null.
   */
  public static final int NO_ENUM_REF = -1;

  /**
   * The Object is a value object and will be saved inline.
   */
  public static final int VALUE_OBJECT = 1;
  
  /**
   * The object is a normal reference and the objectId will be saved.
   */
  public static final int OBJECT_REFERENCE = 2;

  private Constants() {}

}
