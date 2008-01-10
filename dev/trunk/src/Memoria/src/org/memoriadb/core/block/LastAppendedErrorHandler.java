package org.memoriadb.core.block;

import java.io.*;

import org.memoriadb.block.Block;
import org.memoriadb.core.file.IMemoriaFile;
import org.memoriadb.core.file.read.IFileReaderHandler;

/**
 * 
 * Used when the currently processed block is the last written block
 * 
 * @author msc
 * 
 */
public class LastAppendedErrorHandler extends AbstractBlockErrorHandler {

  private final IMemoriaFile fFile;

  public LastAppendedErrorHandler(IFileReaderHandler fileReaderHandler, IMemoriaFile file) {
    super(fileReaderHandler);
    fFile = file;
  }

  @Override
  public void blockSizeCorrupt(DataInputStream input, Block block) throws IOException {
    freeCorruptBlock(input, block);
    skipToEnd(input);
  }

  @Override
  public void blockTagCorrupt(DataInputStream input, Block block) throws IOException {
    freeCorruptBlock(input, block);
    skipToEnd(input);
  }

  @Override
  public void transactionCorrupt(DataInputStream input, Block block) throws IOException {
    freeCorruptBlock(input, block);
  }

  private void freeCorruptBlock(DataInputStream input, Block block) throws IOException {
    long size = fFile.getSize() - block.getPosition();
    block.setWholeSize(size);
    block.setIsFree();
    fFileReaderHandler.block(block);

    skipToEnd(input);
    
  }

  private void skipToEnd(DataInputStream input) throws IOException {
    // skip stream to the end
    input.skip(Long.MAX_VALUE);
  }

}
