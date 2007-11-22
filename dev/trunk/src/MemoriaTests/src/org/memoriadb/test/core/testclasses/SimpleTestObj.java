package org.memoriadb.test.core.testclasses;

import org.memoriadb.core.handler.field.*;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.mode.*;

public class SimpleTestObj implements Comparable<SimpleTestObj> {
  
  private String fString;
  
  public static IFieldObject createFieldObject(IDataStoreExt dataStore, String stringValue) {
    IObjectId memoriaClassId = dataStore.getObjectId(dataStore.getMemoriaClass(SimpleTestObj.class));
    return setField(stringValue, memoriaClassId);
  }

  public static IFieldObject createFieldObject(IObjectStoreExt objectStore, String stringValue) {
    IObjectId memoriaClassId = objectStore.getObjectId(objectStore.getMemoriaClass(SimpleTestObj.class));
    return setField(stringValue, memoriaClassId);
  }
  
  private static IFieldObject setField(String stringValue, IObjectId memoriaClassId) {
    IFieldObject result = new FieldMapDataObject(memoriaClassId);
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
