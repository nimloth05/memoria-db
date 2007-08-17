
package bootstrap.examples.jos;

import java.io.*;

public class LogObjectInputStream extends ObjectInputStream {

  public LogObjectInputStream(InputStream in) throws IOException {
    super(in);
    enableResolveObject(true);
  }

  @Override
  protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
    //System.out.println("resolveClass: " + desc);
    return super.resolveClass(desc);
  }

  @Override
  protected Object resolveObject(Object obj) throws IOException {
    Object result = super.resolveObject(obj);
    System.out.println("resolveObject: " + obj + " -> " + result);
    if(result instanceof B) return null;
    return result;
  }
  
  

  @Override
  protected Class<?> resolveProxyClass(String[] interfaces) throws IOException, ClassNotFoundException {
    System.out.println("resolveProxyClass");
    return super.resolveProxyClass(interfaces);
  }

  
  
}
