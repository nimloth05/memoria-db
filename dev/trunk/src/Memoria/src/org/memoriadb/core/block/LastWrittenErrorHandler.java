package org.memoriadb.core.block;

import org.memoriadb.block.Block;
import org.memoriadb.core.file.IFileReaderHandler;

public class LastWrittenErrorHandler extends AbstractBlockErrorHandler {

  public LastWrittenErrorHandler(IFileReaderHandler fileReaderHandler) {
    super(fileReaderHandler);
  }

  @Override
  public long blockSizeCorrupt(Block block) {
    return 0;
  }

  @Override
  public long blockTagCorrupt(Block block) {
    return 0;
  }

  @Override
  public void transactionCorrupt(Block block) {
  }

}
