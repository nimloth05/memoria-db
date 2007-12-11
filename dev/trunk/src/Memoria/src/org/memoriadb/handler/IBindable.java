package org.memoriadb.handler;

import org.memoriadb.core.file.read.IReaderContext;


public interface IBindable {

  public void bind(IReaderContext context) throws Exception;

}