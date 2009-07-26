package org.memoriadb.handler;

import org.memoriadb.core.meta.IMemoriaClass;

/**
 * Additional configuration interface. Implement this interface if a handler needs to know what the current superClass is.
 */
public interface IHandlerConfig {


  /**
   * The superClass for this IHandler.
   * @param superClass
   */
  public void setSuperClass(IMemoriaClass superClass);
}
