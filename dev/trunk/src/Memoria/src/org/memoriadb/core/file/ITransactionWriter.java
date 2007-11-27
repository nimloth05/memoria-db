package org.memoriadb.core.file;

import java.io.IOException;
import java.util.Set;

import org.memoriadb.block.IBlockManager;
import org.memoriadb.core.*;


public interface ITransactionWriter {

  public void close();

  public IBlockManager getBlockManager();

  public IMemoriaFile getFile();

  public long getHeadRevision();

  public IObjectRepository getRepo();
  
  public void write(Set<ObjectInfo> add, Set<ObjectInfo> update, Set<ObjectInfo> delete)  throws IOException;
  
}
