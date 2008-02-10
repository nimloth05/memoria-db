package org.memoriadb.ui.moodel;

import javax.swing.tree.DefaultMutableTreeNode;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.ui.controls.tree.ILabelProvider;

public class ClassModelLabelProvider implements ILabelProvider {

  @Override
  public String getLabel(Object element) {
    return getLabelOfObject(element);
  }
  
  private String getLabelOfObject(Object treeNode) {
    if (treeNode == null) return "";
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeNode;
    IMemoriaClass memoriaClass = (IMemoriaClass) node.getUserObject();
    if (memoriaClass == null) return "";
    return shortenClassName(memoriaClass.getJavaClassName());
  }

  private String shortenClassName(String longName) {
    int indexOf = longName.lastIndexOf('.');
    if (indexOf == -1) return longName;
    return longName.substring(indexOf+1);
  }

}
