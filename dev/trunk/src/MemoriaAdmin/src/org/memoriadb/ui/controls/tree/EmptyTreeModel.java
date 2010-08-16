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

package org.memoriadb.ui.controls.tree;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

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
