package org.memoriadb.frames;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;

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

  private JComponent createClassTree() {
    JTree tree = new JTree(ClassModelFactory.createClassModel(fStore.getTypeInfo()));

    ImageIcon icon = new ImageIcon(loadImage("/org/memoriadb/icons/class_obj.gif"));
    if (icon != null) {
      DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
      
      renderer.setLeafIcon(icon);
      renderer.setOpenIcon(icon);
      renderer.setClosedIcon(icon);
      
      tree.setCellRenderer(renderer);
    }

    return asScrollable(tree);
  }

  private void createControls() {
    fFrame.getContentPane().setLayout(new MigLayout("fill"));

    JComponent tree = createClassTree();
    JComponent table = asScrollable(new JTable());

    JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, tree, table);
    splitter.setBorder(null);
    fFrame.getContentPane().add(splitter, "grow");
  }

  private BufferedImage loadImage(String path) {
    try {
      return ImageIO.read(getClass().getResource(path));
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
