package org.memoriadb.core.file;

import java.io.IOException;

import org.memoriadb.core.block.*;


public interface ITransactionWriter {

  public void close();

  public IBlockManager getBlockManager();

  public IMemoriaFile getFile();

  /**
   * Saves the given object-data to the persistent store.
   * @return The Block in which the <tt>objectData</tt> was stored.
   */
  public Block write(byte[] objectData)  throws IOException;

}
