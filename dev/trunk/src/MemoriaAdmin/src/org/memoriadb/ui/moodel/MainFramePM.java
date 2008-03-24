package org.memoriadb.ui.moodel;

import java.util.*;

import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.tree.*;

import org.memoriadb.*;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.core.util.disposable.*;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.services.presenter.*;
import org.memoriadb.services.store.*;
import org.memoriadb.ui.controls.tree.*;

import com.google.inject.Inject;

public class MainFramePM {
  
  private class FilterDocumentListener implements DocumentListener {
    
    private TableModel fModel;
    private Collection<Integer> fColumns;

    @Override
    public void changedUpdate(DocumentEvent e) {
      filterChanged(e);
    }
  
    public Collection<Integer> getColums() {
      return fColumns;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
      filterChanged(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
      filterChanged(e);
    }

    public void setModel(TableModel model) {
      fModel = model;
    }
    
    private void filterChanged(DocumentEvent event) {
      Document document = event.getDocument();
      String filterText;
      try {
        filterText = document.getText(0, document.getLength());
      }
      catch (BadLocationException e) {
        e.printStackTrace();
        return;
      }
      fModel.filter(filterText);
    }
  }
  
  private final ListenerList<IQueryListener> fQueryListeners = new ListenerList<IQueryListener>(); 
  
  private final Document fFilterDocument = new PlainDocument();
  private final IClassRendererService fService;
  private final IDataStoreService fDataStoreService;
  private IDataStore fOpenStore;

  private FilterDocumentListener fFilterListener;
  
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
    setUpFilter(newModel);
    
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

  public Document getFilterModel() {
    return fFilterDocument;
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
    ITableModelDecorator tableModelDecorator = renderer.getTableModelDecorator(memoriaClass);
    
    return TableModel.create(result, tableModelDecorator, fOpenStore);
  }

  private void notifyQueryExcuted(TableModel newModel) {
    for(IQueryListener listener: fQueryListeners) {
      listener.executed(newModel);
    }
  }

  private void setUpFilter(TableModel newModel) {
    if (fFilterListener == null) {
      fFilterListener = new FilterDocumentListener();
      fFilterDocument.addDocumentListener(fFilterListener);
    }
    fFilterListener.setModel(newModel);
  }
  
}
