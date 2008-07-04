package org.memoriadb.core.exception;

public class FileCorruptException extends MemoriaException {
  
  /**
   * This Class should not be serialised!
   */
  private static final long serialVersionUID = -3795222392877432303L;

  public FileCorruptException(String str) {
    super(str);
  }

}
