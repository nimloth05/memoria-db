package org.memoriadb.frames;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

import org.memoriadb.IDataStore;

public final class MainFrame {
  
  private final JFrame fFrame;
  private final IDataStore fStore;

  public MainFrame(IDataStore store) {
    if (store == null) throw new IllegalArgumentException("store is null");
    fStore = store;
    
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
    
    JComponent tree = asScrollable(new JTree(ClassModelFactory.createClassModel(fStore.getTypeInfo())));
    JComponent table = asScrollable(new JTable());
    
    JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, tree, table);
    splitter.setBorder(null);
    fFrame.getContentPane().add(splitter, "grow");
  }

}
