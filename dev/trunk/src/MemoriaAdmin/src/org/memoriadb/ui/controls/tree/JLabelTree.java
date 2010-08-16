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

import javax.swing.*;

/**
 * This Tree implementaiton accepts a LabelProvider like in JFace. Additionally, it will create and set an empty TreeModel.
 * @author Sandro
 *
 */
public class JLabelTree extends JTree {
  
  private final ILabelProvider fLabelProvider;
  
  public JLabelTree(ILabelProvider labelProvider) {
    super(new EmptyTreeModel());
    fLabelProvider = labelProvider;
  }
  
  @Override
  public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
    //The JTree does call a non final public method - convertValueToText before our constructor is finisched.
    if (fLabelProvider == null) return value.toString();
    return  fLabelProvider.getLabel(value);
  }

}
