package org.memoriadb.exception;

public class IllegalSchemaException extends MemoriaException {
  

  public IllegalSchemaException(Exception e) {
    super(e);
  }
  
  public IllegalSchemaException(String string) {
    super(string);
  }
  
  public IllegalSchemaException(String message, Exception e) {
    super(message, e);
  }

}
