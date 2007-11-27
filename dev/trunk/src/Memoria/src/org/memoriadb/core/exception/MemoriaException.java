package org.memoriadb.core.exception;

public class MemoriaException extends RuntimeException {

  /**
   * This class will not be serialized. 
   */
  private static final long serialVersionUID = 1L;

  public MemoriaException(Exception e) {
    super(e);
  }
  
  public MemoriaException(String string) {
    super(string);
  }
  
  public MemoriaException(String message, Exception e) {
    super(message, e);
  }

}
