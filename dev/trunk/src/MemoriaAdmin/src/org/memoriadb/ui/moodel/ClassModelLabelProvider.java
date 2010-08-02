/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb.ui.moodel;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.ui.controls.tree.ILabelProvider;

import javax.swing.tree.DefaultMutableTreeNode;

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
