package org.memoriadb.core;

import org.memoriadb.core.meta.*;

public final class MemoriaFieldClassFactory {
  
  /**
   * @return creates the metaclass for the given <tt>obj</tt>
   */
  public static IMemoriaClassConfig createMetaClass(Class<?> klass) {
    if(klass.isArray()) throw new IllegalArgumentException("Array not expected");
    
    return new MemoriaFieldClass(klass);
  }
  
  
  private MemoriaFieldClassFactory() {}

}
