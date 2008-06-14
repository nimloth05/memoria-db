package org.memoriadb.test.testclasses;

import org.memoriadb.core.mode.*;
import org.memoriadb.handler.field.*;
import org.memoriadb.id.IObjectId;

public class SimpleTestObj implements Comparable<SimpleTestObj> {

  private String fString;

  public static IFieldbasedObject createFieldObject(DataStore dataStore, String stringValue) {
    IObjectId memoriaClassId = dataStore.getId(dataStore.getTypeInfo().getMemoriaClass(SimpleTestObj.class));
    return setField(stringValue, memoriaClassId);
  }

  public static IFieldbasedObject createFieldObject(ObjectStore objectStore, String stringValue) {
    IObjectId memoriaClassId = objectStore.getId(objectStore.getTypeInfo().getMemoriaClass(SimpleTestObj.class));
    return setField(stringValue, memoriaClassId);
  }

  private static IFieldbasedObject setField(String stringValue, IObjectId memoriaClassId) {
    IFieldbasedObject result = new FieldbasedDataObject(memoriaClassId);
    result.set("fString", stringValue);
    return result;
  }

  public SimpleTestObj() {}

  public SimpleTestObj(String string) {
    fString = string;
  }

  @Override
  public int compareTo(SimpleTestObj o) {
    return this.fString.compareTo(o.fString);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final SimpleTestObj other = (SimpleTestObj) obj;
    if (fString == null) {
      if (other.fString != null) return false;
    }
    else if (!fString.equals(other.fString)) return false;
    return true;
  }

  public String getString() {
    return fString;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fString == null) ? 0 : fString.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "s: "+fString;
  }

}
