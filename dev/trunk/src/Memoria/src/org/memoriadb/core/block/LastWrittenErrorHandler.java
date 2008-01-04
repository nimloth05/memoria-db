package org.memoriadb.core.block;

import java.io.DataInputStream;

import org.memoriadb.block.Block;
import org.memoriadb.core.file.read.IFileReaderHandler;

public class LastWrittenErrorHandler extends AbstractBlockErrorHandler {

  public LastWrittenErrorHandler(IFileReaderHandler fileReaderHandler) {
    super(fileReaderHandler);
  }
  
  @Override
  public void transactionCorrupt(DataInputStream input, Block block, long transactionSize) {
   //BlockReader.skip(input, block.getBodySize() - transactionSize - FileLayout.TRX_OVERHEAD);
  }

}
