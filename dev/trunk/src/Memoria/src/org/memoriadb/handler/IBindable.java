package org.memoriadb.handler;

import org.memoriadb.core.load.IReaderContext;


public interface IBindable {

  public void bind(IReaderContext context) throws Exception;

}