package org.memoriadb.frames;

import java.util.*;

import javax.swing.tree.*;

import org.memoriadb.ITypeInfo;
import org.memoriadb.core.meta.IMemoriaClass;


public final class ClassModelFactory {
  
  //FIXME: Dieser Code ist ein erstes Wurf... so
  public static final TreeNode createClassModel(ITypeInfo info) {
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

  public static String getLabelOfObject(Object value) {
    if (value == null) return "";
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
    IMemoriaClass memoriaClass = (IMemoriaClass) node.getUserObject();
    if (memoriaClass == null) return "";
    return shortenClassName(memoriaClass.getJavaClassName());
  }
  
  private static String shortenClassName(String longName) {
    int indexOf = longName.lastIndexOf('.');
    if (indexOf == -1) return longName;
    return longName.substring(indexOf+1);
  }
  
  

}
