package org.memoriadb;

import org.memoriadb.frames.MainFrame;

public final class ApplicationController {

  public static void start() {
    MainFrame mainFrame = new MainFrame();
    mainFrame.show();
  }

}
