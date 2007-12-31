package org.memoriadb.frames;

import javax.swing.tree.*;

import org.memoriadb.ITypeInfo;


public final class ClassModelFactory {
  
  
  public static final TreeNode createClassModel(ITypeInfo info) {
    return new DefaultMutableTreeNode("Object");
  }
  
  

}
