package org.memoriadb.core.block;

import org.memoriadb.block.Block;
import org.memoriadb.core.file.*;

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
  public long blockSizeCorrupt(Block block) {
    return readToEndOfFile(block);
  }

  @Override
  public long blockTagCorrupt(Block block) {
    return readToEndOfFile(block);
  }

  @Override
  public void transactionCorrupt(Block block) {
    readToEndOfFile(block);
  }

  private long readToEndOfFile(Block block) {
    long size = fFile.getSize()-block.getPosition();
    block.setWholeSize(size);
    block.setIsFree(); 
    fFileReaderHandler.block(block);
    return size;
  }

}
