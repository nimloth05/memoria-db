package bootstrap.examples.jos;

import java.io.*;

public class LogObjectOutputStream extends ObjectOutputStream {

  public LogObjectOutputStream(OutputStream out) throws IOException {
    super(out);
    enableReplaceObject(true);
  }

  @Override
  protected Object replaceObject(Object obj) throws IOException {
    Object result = super.replaceObject(obj);
    System.out.println("replaced: " + obj.getClass().getSimpleName() +" -> " + result);
    return result;
  }
  
  

}
