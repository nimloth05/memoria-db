package org.memoriadb.ui.moodel;

import java.util.*;

import javax.swing.table.*;
import javax.swing.tree.*;

import org.memoriadb.*;
import org.memoriadb.core.IObjectInfo;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.core.util.disposable.*;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.services.presenter.*;
import org.memoriadb.ui.controls.tree.*;

import com.google.inject.Inject;

public class MainFramePM {
  
  private final ListenerList<IQueryListener> fQueryListeners = new ListenerList<IQueryListener>();
  
  private final IClassRendererService fService;
  
  @Inject
  public MainFramePM(IClassRendererService service) {
    fService = service;
  }
  
  public IDisposable addQueryListener(IQueryListener queryListener) {
    return fQueryListeners.add(queryListener);
  }
  
  public ILabelProvider createLabelProvider() {
    return new ClassModelLabelProvider();
  }
  
  public void executeQuery(IDataStore store, IMemoriaClass memoriaClass) {
    List<IDataObject> query = store.query(memoriaClass.getJavaClassName());
    
    TableModel newModel = createTableModel(store, memoriaClass, query);
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

  private TableModel createTableModel(IDataStore store, IMemoriaClass memoriaClass, List<IDataObject> result) {
    IClassRenderer renderer = fService.getRednerer(memoriaClass);
    
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("ObjectId");
    model.addColumn("Revision");
    model.addColumn("Class ObjectId");
    renderer.addColumnsToTable(model, memoriaClass);
    
    List<Object> rowData = new ArrayList<Object>();
    for(IDataObject dataObject: result) {
      IObjectInfo objInformation = store.getObjectInfo(dataObject);
      
      rowData.add(objInformation.getId());
      rowData.add(objInformation.getRevision());
      rowData.add(objInformation.getMemoriaClassId());
      
      renderer.addToTableRow(dataObject, memoriaClass, rowData);
      
      model.addRow(rowData.toArray());
    }
    
    return model;
  }
  
  private void notifyQueryExcuted(TableModel newModel) {
    for(IQueryListener listener: fQueryListeners) {
      listener.executed(newModel);
    }
  }
  
}
