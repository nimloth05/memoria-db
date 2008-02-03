package org.memoriadb.ui.frames;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.*;

import net.miginfocom.swing.MigLayout;

import org.memoriadb.IDataStore;
import org.memoriadb.core.util.disposable.IDisposable;
import org.memoriadb.services.store.*;
import org.memoriadb.ui.controls.tree.*;
import org.memoriadb.util.SwingUtil;

import com.google.inject.Inject;

public final class MainFrame {

  private static final int FRAME_WIDTH = 800;

  private final JFrame fFrame;
  
  private final IDatastoreService fDataStoreService;

  private JTree fClassTree;
  private IDisposable fListenerDisposable;

  @Inject
  public MainFrame(IDatastoreService service) {
    fDataStoreService = service;
    fFrame = new JFrame();
    fFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    fFrame.setSize(FRAME_WIDTH, 600);
    fFrame.setLocation(SwingUtil.calculateCenter(fFrame.getSize()));
    createControls();
    addListeners();
  }

  public void show() {
    fFrame.setVisible(true);
  }

  private void addDatabaseServiceListener() {
    fListenerDisposable = fDataStoreService.addListener(new IChangeListener() {

      @Override
      public void postOpen(IDataStore newStore) {
        fClassTree.setModel(new DefaultTreeModel(ClassModelFactory.createClassModel(newStore.getTypeInfo())));
      }

      @Override
      public void preClose() {
        fClassTree.setModel(new EmptyTreeModel());
      }
    });
  }

  private void addListeners() {
    addDatabaseServiceListener();
    addWindowListener();
  }

  private void addWindowListener() {
    fFrame.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosing(WindowEvent e) {
        fListenerDisposable.dispose();
      }
      
    });
  }

  private JComponent asScrollable(JComponent component) {
    return new JScrollPane(component);
  }

  private JComponent createClassTree() {
    fClassTree = new JLabelTree(ClassModelFactory.createLabelProvider());
    
    //FIXME: Bilder laden sollte fail-safe sein.
    ImageIcon icon = new ImageIcon(loadImage("/org/memoriadb/ui/icons/class_obj.gif"));
    if (icon != null) {
      DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
      
      renderer.setLeafIcon(icon);
      renderer.setOpenIcon(icon);
      renderer.setClosedIcon(icon);
      
      fClassTree.setCellRenderer(renderer);
    }

    return asScrollable(fClassTree);
  }

  private void createControls() {
    fFrame.getContentPane().setLayout(new MigLayout("fill"));

    JComponent tree = createClassTree();
    JComponent table = asScrollable(new JTable());

    JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, tree, table);
    splitter.setBorder(null);
    splitter.setDividerLocation((FRAME_WIDTH - 10) / 2);
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
