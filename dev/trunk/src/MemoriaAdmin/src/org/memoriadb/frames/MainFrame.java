package org.memoriadb.frames;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

public final class MainFrame {
  
  private final JFrame fFrame;

  public MainFrame() {
    fFrame = new JFrame();
    fFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    fFrame.setSize(800, 600);
    createControls();
  }
  
  public void show() {
    fFrame.setVisible(true);
  }

  private JComponent asScrollable(JComponent component) {
    return new JScrollPane(component);
  }
  
  private void createControls() {
    fFrame.getContentPane().setLayout(new MigLayout("fill"));
    
    JComponent tree = asScrollable(new JTree());
    JComponent table = asScrollable(new JTable());
    
    JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, tree, table);
    splitter.setBorder(null);
    fFrame.getContentPane().add(splitter, "grow");
  }

}
