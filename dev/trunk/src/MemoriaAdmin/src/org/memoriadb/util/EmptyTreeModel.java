package org.memoriadb.util;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.*;

public class EmptyTreeModel implements TreeModel {

  @Override
  public void addTreeModelListener(TreeModelListener l) {}

  @Override
  public Object getChild(Object parent, int index) {
    return null;
  }

  @Override
  public int getChildCount(Object parent) {
    return 0;
  }

  @Override
  public int getIndexOfChild(Object parent, Object child) {
    return 0;
  }

  @Override
  public Object getRoot() {
    return null;
  }

  @Override
  public boolean isLeaf(Object node) {
    return false;
  }

  @Override
  public void removeTreeModelListener(TreeModelListener l) {}

  @Override
  public void valueForPathChanged(TreePath path, Object newValue) {}

}
