package org.memoriadb.core.block;

import org.memoriadb.core.file.IFileReaderHandler;

public class LastWrittenErrorHandler extends AbstractBlockErrorHandler {

  public LastWrittenErrorHandler(IFileReaderHandler fileReaderHandler) {
    super(fileReaderHandler);
  }

}
