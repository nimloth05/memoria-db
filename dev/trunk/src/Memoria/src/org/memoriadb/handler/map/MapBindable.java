package org.memoriadb.handler.map;

import java.util.Map;

import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.handler.IBindable;


public class MapBindable implements IBindable {

  private final Map<Object, Object> fResult;
  private final IObjectResolver fKey;
  private final IObjectResolver fValue;

  public MapBindable(Map<Object, Object> result, IObjectResolver key, IObjectResolver value) {
    fResult = result;
    fKey = key;
    fValue = value;
  }

  @Override
  public void bind(IReaderContext context) throws Exception {
    fResult.put(fKey.getObject(context), fValue.getObject(context));
  }

  @Override
  public String toString() {
    return "map-bindable: "+ fResult + " object to add: " + fKey + "->" + fValue;
  }

}
