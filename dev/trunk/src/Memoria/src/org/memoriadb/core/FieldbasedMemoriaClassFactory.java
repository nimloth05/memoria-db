package org.memoriadb.core;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.meta.*;

public final class FieldbasedMemoriaClassFactory {

  /**
   * @return creates the metaclass for the given <tt>obj</tt>
   */
  public static IMemoriaClassConfig createMetaClass(Class<?> klass, IObjectId memoriaClassId) {
    if(klass.isArray()) throw new IllegalArgumentException("Array not expected");
    
    FieldbasedMemoriaClass memoriaFieldClass = new FieldbasedMemoriaClass(klass, memoriaClassId);
    return memoriaFieldClass;
  }

  private FieldbasedMemoriaClassFactory() {}

}
