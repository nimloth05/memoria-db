/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.memoriadb.ui.frames;

import com.google.inject.Inject;
import net.miginfocom.swing.MigLayout;
import org.memoriadb.IDataStore;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.core.util.disposable.IDisposable;
import org.memoriadb.services.presenter.FilterDefalutTableCellRenderer;
import org.memoriadb.services.store.IChangeListener;
import org.memoriadb.ui.controls.tree.JLabelTree;
import org.memoriadb.ui.moodel.IQueryListener;
import org.memoriadb.ui.moodel.MainFramePM;
import org.memoriadb.util.ImageLoader;
import org.memoriadb.util.SwingUtil;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class MainFrame {

  private static final int FRAME_HEIGHT = 600;

  private static final int FRAME_WIDTH = 800;

  private final JFrame fFrame;
  
  private JTree fClassTree;
  private IDisposable fListenerDisposable;

  private final MainFramePM fPM;

  @Inject
  public MainFrame(MainFramePM pm) {
    fPM = pm;
    
    fFrame = createFrame();
    createControls();
    addListeners();
  }

  public void show() {
    fFrame.setVisible(true);
  }

  protected void setUpCellRenderers(final JTable table) {
    for (int i = 0; i < table.getColumnModel().getColumnCount(); ++i) {
      TableColumn column = table.getColumnModel().getColumn(i);
      column.setCellRenderer(new FilterDefalutTableCellRenderer());
    }
  }

  private void addDatabaseServiceListener() {
    fListenerDisposable = fPM.addDataStoreChangeListener(new IChangeListener() {

      @Override
      public void postOpen(IDataStore newStore) {
        fClassTree.setModel(fPM.getClassTreeModel(newStore.getTypeInfo()));
      }

      @Override
      public void preClose() {
        fClassTree.setModel(fPM.getEmptyModel());
      }
    });
  }

  private void addListeners() {
    addDatabaseServiceListener();
    addWindowListener();
  }

  private void addSelectionListener() {
    fClassTree.addTreeSelectionListener(new TreeSelectionListener() {

      @Override
      public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getNewLeadSelectionPath().getLastPathComponent();
        IMemoriaClass memoriaClass = (IMemoriaClass) node.getUserObject();
        fPM.executeQuery(memoriaClass);
      }
    });
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
    fClassTree = new JLabelTree(fPM.createLabelProvider());
    
    setTreeIcon();
    addSelectionListener();

    return asScrollable(fClassTree);
  }

  private void createControls() {
    fFrame.getContentPane().setLayout(new MigLayout("fill"));

    JComponent tree = createClassTree();
    JComponent table = createTablePart();

    JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, tree, table);
    splitter.setBorder(null);
    splitter.setDividerLocation((FRAME_WIDTH - 10) / 2);
    fFrame.getContentPane().add(splitter, "grow");
  }

  private JFrame createFrame() {
    JFrame result = new JFrame();
    result.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    result.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    result.setLocation(SwingUtil.calculateCenter(result.getSize()));
    return result;
  }

  private JComponent createTablePart() {
    JPanel panel = new JPanel();
    panel.setLayout(new MigLayout("fill, insets 0 5 0 5"));
    
    JLabel label = new JLabel("Filter reg-ex supported");
    panel.add(label, "span");
    
    JTextField filterText = new JTextField();
    filterText.setDocument(fPM.getFilterModel());
    panel.add(filterText, "wrap, growx");
    
    final JTable table = new JTable();
    table.setAutoCreateRowSorter(false);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    
    fPM.addQueryListener(new IQueryListener() {

      @Override
      public void executed(org.memoriadb.services.presenter.TableModel model) {
        table.setModel(model);
        table.setRowSorter(model.getRowSorter());
        setUpCellRenderers(table);
      }
    });
    
    table.setAutoCreateRowSorter(true);
    panel.add(asScrollable(table), "grow");
    
    return panel;
  }

  private void setTreeIcon() {
    ImageIcon icon = ImageLoader.loadImageIcon("/org/memoriadb/ui/icons/class_obj.gif");
    DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
    
    renderer.setLeafIcon(icon);
    renderer.setOpenIcon(icon);
    renderer.setClosedIcon(icon);
    
    fClassTree.setCellRenderer(renderer);
  }


}
