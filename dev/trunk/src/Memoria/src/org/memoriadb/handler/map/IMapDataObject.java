package org.memoriadb.handler.map;

import java.util.Map;

import org.memoriadb.handler.IDataObject;

public interface IMapDataObject extends IDataObject {
  public Map<Object, Object> getMap();
}
