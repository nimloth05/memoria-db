package org.memoriadb.core.file.async;

import org.memoriadb.core.file.IMemoriaFile;

interface IAsyncFileJob {
  
  public boolean isLast();

  public void run(IMemoriaFile file); 
  

}
