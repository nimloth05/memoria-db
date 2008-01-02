package org.memoriadb.services.presenter;

import javax.swing.*;

public class FieldbasedObjectRenderer implements IClassRenderer {

  @Override
  public JComponent createControl() {
    return new JLabel("Field-Based Object Renderer");
  }

}
