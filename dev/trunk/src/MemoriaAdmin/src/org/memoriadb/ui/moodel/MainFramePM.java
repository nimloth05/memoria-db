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

package org.memoriadb.ui.moodel;

import com.google.inject.Inject;
import org.memoriadb.IDataStore;
import org.memoriadb.ITypeInfo;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.core.util.disposable.IDisposable;
import org.memoriadb.core.util.disposable.ListenerList;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.services.presenter.IClassRenderer;
import org.memoriadb.services.presenter.IClassRendererService;
import org.memoriadb.services.presenter.ITableModelDecorator;
import org.memoriadb.services.presenter.TableModel;
import org.memoriadb.services.store.IChangeListener;
import org.memoriadb.services.store.IDataStoreService;
import org.memoriadb.ui.controls.tree.EmptyTreeModel;
import org.memoriadb.ui.controls.tree.ILabelProvider;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

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
