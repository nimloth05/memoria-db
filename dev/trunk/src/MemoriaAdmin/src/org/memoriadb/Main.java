package org.memoriadb;

import javax.swing.SwingUtilities;

public final class Main {
  
  public static void main(String[] args) {
    System.out.println("total mem: " + Runtime.getRuntime().totalMemory() / 1024);
    System.out.println("free mem: " + Runtime.getRuntime().freeMemory() / 1024);
    
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        ApplicationController.launch();
      }
      
    });
  }

}
