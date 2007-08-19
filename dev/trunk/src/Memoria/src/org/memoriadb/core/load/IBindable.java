package org.memoriadb.core.load;

import org.memoriadb.core.IReaderContext;

public interface IBindable {

  public void bind(IReaderContext contex) throws Exception;

}