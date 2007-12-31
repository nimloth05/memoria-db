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
      DefaultMutableTreeNode node = new DefaultMutableTreeNode(memClass.getJavaClassName());
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
  
  

}
