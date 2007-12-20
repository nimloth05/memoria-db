package org.memoriadb.core.block;

import java.io.*;

import org.memoriadb.block.Block;
import org.memoriadb.core.exception.FileCorruptException;
import org.memoriadb.core.file.read.IFileReaderHandler;

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

  @SuppressWarnings("unused")
  @Override
  public void blockSizeCorrupt(DataInputStream input, Block block) throws IOException {
    throw new FileCorruptException("block size corrupt: " + block);
  }

  @SuppressWarnings("unused")
  @Override
  public void blockTagCorrupt(DataInputStream input, Block block) throws IOException {
    throw new FileCorruptException("block tag corrupt: " + block);
  }

  @SuppressWarnings("unused")
  @Override
  public void transactionCorrupt(DataInputStream input, Block block, long transactionSize) throws IOException {
    throw new FileCorruptException("transaction corrupt in block: " + block);
  }

}
