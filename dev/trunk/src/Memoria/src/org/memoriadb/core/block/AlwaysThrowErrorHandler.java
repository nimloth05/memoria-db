package org.memoriadb.core.block;



public class AlwaysThrowErrorHandler extends AbstractBlockErrorHandler {

  public AlwaysThrowErrorHandler() {
    // pass null, because the IFileReaderHandler is never used
    super(null);
  }

}
