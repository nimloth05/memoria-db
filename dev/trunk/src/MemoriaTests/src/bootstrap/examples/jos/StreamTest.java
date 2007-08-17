package bootstrap.examples.jos;

import java.io.*;

public class StreamTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
	  ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new LogObjectOutputStream(bos);
    A a = new A();
    oos.writeObject(a);
    
    ObjectInputStream ois = new LogObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
    A ab = (A) ois.readObject();
   // System.out.println(ab.getB());
    
	}

}
