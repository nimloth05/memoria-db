package org.memoriadb.core.meta;


/**
 * FIXME Weshalb gibt e IMemoriaClass und IMemoriaClassConfig? müsste das nicht vereinheitlicht werden?
 * @author msc
 *
 */
public interface IMemoriaClassConfig extends IMemoriaClass {
  
  public void setSuperClass(IMemoriaClass memoriaClass);

}
