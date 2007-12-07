package org.memoriadb.core.block;

import org.memoriadb.block.Block;
import org.memoriadb.core.exception.FileCorruptException;
import org.memoriadb.core.file.IFileReaderHandler;

/**
 * Default-behaviour for when a part of a block is corrupt: throw
 * 
 * @author msc
 *
 */
public abstract class AbstractBlockErrorHandler implements IBlockErrorHandler {

  protected final IFileReaderHandler fFileReaderHandler;
  
  public AbstractBlockErrorHandler(IFileReaderHandler fileReaderHandler) {
    fFileReaderHandler = fileReaderHandler;
  }

  @Override
  public long blockSizeCorrupt(Block block) {
    throw new FileCorruptException("block size corrupt: " + block);
  }

  @Override
  public long blockTagCorrupt(Block block) {
    throw new FileCorruptException("block tag corrupt: " + block);
  }

  @Override
  public void transactionCorrupt(Block block) {
    throw new FileCorruptException("transaction corrupt in block: " + block);
  }

}
