package org.memoriadb.core;

import org.memoriadb.exception.MemoriaException;

public interface IBindable {

  public void bind(IReaderContext contex) throws Exception;

}