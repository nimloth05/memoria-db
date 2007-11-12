package org.memoriadb.exception;


/**
 * 
 *   
 * @author sandro
 *
 */
public class SchemaCorruptException extends MemoriaException {

  public SchemaCorruptException(Exception e) {
    super(e);
  }
  
  public SchemaCorruptException(String string) {
    super(string);
  }
  
  public SchemaCorruptException(String message, Exception e) {
    super(message, e);
  }
  
}
