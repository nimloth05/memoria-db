package org.memoriadb;

import javax.swing.SwingUtilities;

public final class Main {
  
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        ApplicationController.start();
      }
      
    });
  }

}
