package org.memoriadb.core.block;

import org.memoriadb.block.Block;
import org.memoriadb.core.file.IFileReaderHandler;

/**
 * 
 * Used when the currently processed block is the last written block
 * 
 * @author msc
 *
 */
public class LastAppendedErrorHandler extends AbstractBlockErrorHandler {

  public LastAppendedErrorHandler(IFileReaderHandler fileReaderHandler) {
    super(fileReaderHandler);
  }

  @Override
  public long blockSizeCorrupt(Block block) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public long blockTagCorrupt(Block block) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void transactionCorrupt(Block block) {
  // TODO Auto-generated method stub

  }

}
