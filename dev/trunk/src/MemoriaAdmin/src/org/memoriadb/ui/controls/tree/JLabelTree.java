package org.memoriadb.ui.controls.tree;

import javax.swing.JTree;

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
