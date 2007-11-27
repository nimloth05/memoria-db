package org.memoriadb.core.exception;


/**
 * 
 *   
 * @author sandro
 *
 */
public class SchemaException extends MemoriaException {

  public SchemaException(Exception e) {
    super(e);
  }
  
  public SchemaException(String string) {
    super(string);
  }
  
  public SchemaException(String message, Exception e) {
    super(message, e);
  }
  
}
