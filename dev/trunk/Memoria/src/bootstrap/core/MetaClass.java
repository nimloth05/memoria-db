package bootstrap.core;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public final class MetaClass {

  private final long fId;
  private String fClassName;

  private Map<Integer, MetaField> fFieldIdToInfo = new HashMap<Integer, MetaField>();
  // this mapping must no be saved, its just a "helper"
  transient private Map<String, MetaField> fFieldNameToInfo = new HashMap<String, MetaField>();

  transient private boolean fHasInfoChanged;

  /**
   * This constructor should only be used if the class is already persistent.
   */
  private MetaClass(long typeId) {
    fId = typeId;
  }

  public boolean hasChanged() {
    return fHasInfoChanged;
  }

  public long getTypeId() {
    return fId;
  }

  public void writeToStream(DataOutputStream objectStream, Object object) throws Exception {
    Class<?> type = object.getClass();
    Field[] fields = type.getDeclaredFields();
    for (Field field : fields) {
      if (Modifier.isTransient(field.getModifiers())) continue;
      writeFieldToStream(objectStream, object, field);
    }
  }

  public void writeFieldToStream(DataOutput stream, Object object, Field field) throws Exception {
    MetaField metaField = registerField(field);
    metaField.writeField(stream, object, field);
  }

  private MetaField registerField(Field field) {
    MetaField metaField = fFieldNameToInfo.get(field.getName());

    if (metaField == null) {
      int currentFieldId = getCurrentFieldId();
      metaField = MetaField.create(++currentFieldId, field);

      putInMaps(metaField);
      fHasInfoChanged = true;
    }

    return metaField;
  }

  private void putInMaps(MetaField metaField) {
    fFieldIdToInfo.put(metaField.getId(), metaField);
    fFieldNameToInfo.put(metaField.getName(), metaField);
  }

  private int getCurrentFieldId() {
    Iterator<MetaField> iterator = fFieldIdToInfo.values().iterator();
    if (!iterator.hasNext()) return 0;

    MetaField result = iterator.next();

    while (iterator.hasNext()) {
      MetaField next = iterator.next();
      if (next.getId() > result.getId()) result = next;
    }
    return result.getId();
  }

  public static MetaClass create(long typeId) {
    return new MetaClass(typeId);
  }

  public void setClassName(String name) {
    fClassName = name;
    fHasInfoChanged = true;
  }

  public String getClassName() {
    return fClassName;
  }

  @Override
  public String toString() {
    return fClassName;
  }

  public void changesWritten() {
    fHasInfoChanged = false;
  }

  /**
   * This facatory method is for new classes.
   */
  public static MetaClass createNewInfo(long typeId) {
    MetaClass result = new MetaClass(typeId);
    result.fHasInfoChanged = true;
    return result;
  }

  public static MetaClass createOwnMetaInfo() {
    MetaClass result = new MetaClass(1);

    result.fClassName = MetaClass.class.getName();
    result.fHasInfoChanged = false;

    result.putInMaps(new MetaField(2, "fTypeId", FieldType.lng.ordinal()));
    result.putInMaps(new MetaField(3, "fClassName", FieldType.string.ordinal()));
    result.putInMaps(new MetaField(4, "fFieldIdToInfo", FieldType.clazz.ordinal()));
    return result;
  }

}
