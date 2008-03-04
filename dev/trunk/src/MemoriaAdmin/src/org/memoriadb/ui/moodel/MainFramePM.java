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
import org.memoriadb.services.store.*;
import org.memoriadb.ui.controls.tree.*;

import com.google.inject.Inject;

public class MainFramePM {
  
  private final ListenerList<IQueryListener> fQueryListeners = new ListenerList<IQueryListener>();
  private final IClassRendererService fService;
  private final IDataStoreService fDataStoreService;
  private IDataStore fOpenStore;
  
  @Inject
  public MainFramePM(IClassRendererService service, IDataStoreService dataStoreService) {
    fService = service;
    fDataStoreService = dataStoreService;
    addDataStoreListener();
  }
  
  public IDisposable addDataStoreChangeListener(IChangeListener changeListener) {
    return fDataStoreService.addListener(changeListener);
  }

  public IDisposable addQueryListener(IQueryListener queryListener) {
    return fQueryListeners.add(queryListener);
  }
  
  public ILabelProvider createLabelProvider() {
    return new ClassModelLabelProvider();
  }
  
  public void executeQuery(IMemoriaClass memoriaClass) {
    if (fOpenStore == null) throw new IllegalStateException("There is no store open at this time.");
    
    List<IDataObject> query = fOpenStore.query(memoriaClass.getJavaClassName());
    
    TableModel newModel = createTableModel(memoriaClass, query);
    notifyQueryExcuted(newModel);
  }

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

  private void addDataStoreListener() {
    fDataStoreService.addListener(new IChangeListener() {

      @Override
      public void postOpen(IDataStore newStore) {
        fOpenStore = newStore;
      }

      @Override
      public void preClose() {
        fOpenStore = null;
      }
    });
  }
  
  private TableModel createTableModel(IMemoriaClass memoriaClass, List<IDataObject> result) {
    IClassRenderer renderer = fService.getRednerer(memoriaClass);
    ITableModelDecorator tableModelDecorator = renderer.getTableModelDecorator();
    
    DefaultTableModel model = new DefaultTableModel();
    
    model.addColumn("ObjectId");
    model.addColumn("Revision");
    model.addColumn("Class ObjectId");
    
    tableModelDecorator.addColumn(model, memoriaClass);
    
    
    for(IDataObject dataObject: result) {
      List<Object> rowData = new ArrayList<Object>(model.getColumnCount());
      
      IObjectInfo objectInfo = fOpenStore.getObjectInfo(dataObject);
      
      rowData.add(objectInfo.getId());
      rowData.add(objectInfo.getRevision());
      rowData.add(objectInfo.getMemoriaClassId());
      
      tableModelDecorator.addRow(dataObject, memoriaClass, rowData);
      
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
