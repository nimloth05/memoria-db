package org.memoriadb.handler.map;

import java.util.Map;

import org.memoriadb.id.IObjectId;

public class MapDataObject implements IMapDataObject {
  
  private final IObjectId fId;
  private final Map<Object, Object> fMap;

  public MapDataObject(Map<Object, Object> map, IObjectId id) {
    fMap = map;
    fId = id;
  }

  @Override
  public Map<Object, Object> getMap() {
    return fMap;
  }

  @Override
  public IObjectId getMemoriaClassId() {
    return fId;
  }


  @Override
  public String toString() {
    return fMap.toString();
  }

}
