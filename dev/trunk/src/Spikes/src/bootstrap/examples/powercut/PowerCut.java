package bootstrap.examples.powercut;

import java.io.*;

public class PowerCut {
  
  static final String FILE = "file";
  
  /**
   * Schreibt ein file, in mitten wird die VM gestoppt.
   * @param args
   * @throws IOException 
   */
  public static void main(String[] args) throws IOException {
    RandomAccessFile file = new RandomAccessFile(FILE, "rw");
    System.out.println(file.length() % 10000);
    
    new Thread(new Runnable(){

      @Override
      public void run() {
        Runtime.getRuntime().halt(0);
      }
      
    }).start();
    
    byte[] arr = new byte[10000];
    for(int i = 0; i < 10000; ++i) {
      arr[i] = (byte)i;
    }
    
    for(int i = 0; true; ++i) {
      file.write(arr);
    }
  }
  
}
