package bootstrap.exception;

public class MemoriaException extends RuntimeException {

  public MemoriaException(String string) {
    super(string);
  }
  
  public MemoriaException(Exception e) {
    super(e);
  }

}
