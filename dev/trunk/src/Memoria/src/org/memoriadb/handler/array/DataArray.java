package org.memoriadb.handler.array;

import java.util.*;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.util.ArrayTypeInfo;

/**
 * Implementation for data-mode
 * 
 * @author msc
 *
 */
public class DataArray extends AbstractArray implements IDataArray {

  private final List<Object> fData = new ArrayList<Object>();
  private final ArrayTypeInfo fComponentTypeInfo;
  private int fLength;
  
  public DataArray(IObjectId arrayMemoriaClass, ArrayTypeInfo componentTypeInfo, int length) {
    super(arrayMemoriaClass);
    fComponentTypeInfo = componentTypeInfo;
    fLength = length;
    
    // make space in the list
    for(int i = 0; i < length; ++i) fData.add(null);
    
  }

  @Override
  public void add(Object obj) {
    fData.add(obj);
    fLength += 1;
  }

  @Override
  public Object get(int index) {
    return fData.get(index);
  }

  @Override
  public ArrayTypeInfo getComponentTypeInfo() {
    return fComponentTypeInfo;
  }

  @Override
  public Object getResult() {
    return this;
  }

  @Override
  public int length() {
    return fLength;
  }

  @Override
  public void set(int index, Object value) {
    fData.set(index, value);
  }

}
