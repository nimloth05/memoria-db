package org.memoriadb.ui.moodel;

import java.util.*;

import javax.swing.table.*;
import javax.swing.tree.*;

import org.memoriadb.*;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.core.util.disposable.*;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.ui.controls.tree.*;

public class MainFramePM {
  
  private final ListenerList<IQueryListener> fQueryListeners = new ListenerList<IQueryListener>();
  
  public IDisposable addQueryListener(IQueryListener queryListener) {
    return fQueryListeners.add(queryListener);
  }
  
  public ILabelProvider createLabelProvider() {
    return new ClassModelLabelProvider();
  }
  
  public void executeQuery(IDataStore store, String className) {
    List<IDataObject> query = store.query(className);
    TableModel newModel = createTableModel(store, query);
    notifyQueryExcuted(newModel);
  }

  //FIXME: Dieser Code ist ein erstes Wurf... so
  public TreeNode getClassTree(ITypeInfo info) {
    Map<IMemoriaClass, DefaultMutableTreeNode> createdNodes = new IdentityHashMap<IMemoriaClass, DefaultMutableTreeNode>();
    for(IMemoriaClass memClass: info.getAllClasses()) {
      DefaultMutableTreeNode node = new DefaultMutableTreeNode(memClass);
      createdNodes.put(memClass, node);
    }
    
    DefaultMutableTreeNode root = null;
    for(IMemoriaClass memClass: info.getAllClasses()) {
      DefaultMutableTreeNode node = createdNodes.get(memClass);
      IMemoriaClass superClass = memClass.getSuperClass();
      
      if (memClass.getJavaClassName().equals(Object.class.getName())) {
        root = node;
        continue;
      }
      
      if (superClass == null) {
        continue;
      }
      
      DefaultMutableTreeNode superNode = createdNodes.get(superClass);
      superNode.add(node);
    }
    
    return root;
  }

  public TreeModel getClassTreeModel(ITypeInfo typeInfo) {
    return new DefaultTreeModel(getClassTree(typeInfo));
  }

  public TreeModel getEmptyModel() {
    return new EmptyTreeModel();
  }

  private TableModel createTableModel(IDataStore store, List<IDataObject> result) {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("ObjectId");
    model.addColumn("Revision");
    model.addColumn("Class ObjectId");
    
    Object[] rowData = new Object[3];
    for(IDataObject dataObject: result) {
      rowData[0] = store.getId(dataObject);
      rowData[1] = -1;
      rowData[2] = dataObject.getMemoriaClassId();
      
      model.addRow(rowData);
    }
    
    return model;
  }
  
  private void notifyQueryExcuted(TableModel newModel) {
    for(IQueryListener listener: fQueryListeners) {
      listener.executed(newModel);
    }
  }
  
}
