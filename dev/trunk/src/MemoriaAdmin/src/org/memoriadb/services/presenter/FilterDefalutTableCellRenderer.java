package org.memoriadb.services.presenter;

import java.awt.*;

import javax.swing.JTable;
import javax.swing.table.*;

public class FilterDefalutTableCellRenderer extends DefaultTableCellRenderer {

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    
    TableRowSorter<?> sorter = (TableRowSorter<?>) table.getRowSorter();
    if (sorter == null) return this;
    
    RegexRowFilter filter = (RegexRowFilter) sorter.getRowFilter();
    if (filter == null) return this;
    
    String stringValue = value == null ? "" : value.toString();
    
    if (filter.containsValue(stringValue)) {
      setBackground(Color.yellow);
    }
    else {
      setBackground(Color.white);
    }
    
    return this;
  }
  

}
