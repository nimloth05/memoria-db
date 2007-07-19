package bootstrap.exception;

public class MemoriaException extends RuntimeException {

  /**
   * This class will not be serialized. 
   */
  private static final long serialVersionUID = 1L;

  public MemoriaException(String string) {
    super(string);
  }
  
  public MemoriaException(Exception e) {
    super(e);
  }

}
