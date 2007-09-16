package bootstrap.examples.jos;

import java.io.*;

public class IntStreamTest {
  
  public static void main(String[] args) throws Exception {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new LogObjectOutputStream(bos);
    IntObj obj = new IntObj();
    //obj.set(100);
    oos.writeObject(obj);
    
    ObjectInputStream ois = new LogObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
    IntObj obj_l1 = (IntObj) ois.readObject();
    System.out.println("int-value for laoded obj is: " +obj_l1.get());
   // System.out.println(ab.getB());
  }

}
