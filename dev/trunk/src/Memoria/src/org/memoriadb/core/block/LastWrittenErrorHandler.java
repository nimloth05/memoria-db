package org.memoriadb.core.block;

import java.io.*;

import org.memoriadb.block.Block;
import org.memoriadb.core.file.read.IFileReaderHandler;

public class LastWrittenErrorHandler extends AbstractBlockErrorHandler {

  public LastWrittenErrorHandler(IFileReaderHandler fileReaderHandler) {
    super(fileReaderHandler);
  }
  
  @Override
  public void transactionCorrupt(DataInputStream input, Block block, long transactionSize) throws IOException {
   //BlockReader.skip(input, block.getBodySize() - transactionSize - FileLayout.TRX_OVERHEAD);
  }

}
