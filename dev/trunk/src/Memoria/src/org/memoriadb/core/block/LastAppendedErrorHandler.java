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
  public long blockSizeCorrupt(DataInputStream input, Block block) throws IOException {
    return readToEndOfFile(input, block);
  }

  @Override
  public long blockTagCorrupt(DataInputStream input, Block block) throws IOException {
    return readToEndOfFile(input, block);
  }

  @Override
  public void transactionCorrupt(DataInputStream input, Block block, long transactionSize) throws IOException {
    readToEndOfFile(input, block);
  }

  private long readToEndOfFile(DataInputStream input, Block block) throws IOException {
    long size = fFile.getSize() - block.getPosition();
    block.setWholeSize(size);
    block.setIsFree();
    fFileReaderHandler.block(block);

    // skip stream to the end
    input.skip(Long.MAX_VALUE);
    
    return size;
  }

}
