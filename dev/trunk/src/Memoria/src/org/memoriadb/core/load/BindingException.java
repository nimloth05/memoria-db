package org.memoriadb.core.load;

import org.memoriadb.core.exception.MemoriaException;

public class BindingException extends MemoriaException {
  
  public BindingException(Exception e) {
    super(e);
  }
  
  public BindingException(String string) {
    super(string);
  }
  
  public BindingException(String message, Exception e) {
    super(message, e);
  }


}
