package org.memoriadb.services.presenter;

import java.util.Comparator;

import org.memoriadb.id.*;
import org.memoriadb.id.loong.LongId;

public final class ObjectIdComparator implements Comparator<IObjectId> {
  
  @Override
  public int compare(IObjectId o1, IObjectId o2) {
    if (o1 instanceof IIntegralId && o2 instanceof IIntegralId) {
      LongId id1 = (LongId) o1;
      LongId id2 = (LongId)o2;
      return id1.getValue() > id2.getValue() ? 1 : (id1.getValue() < id2.getValue() ? -1 : 0);
    }
    
    return o1.toString().compareTo(o2.toString());
  }
  
}