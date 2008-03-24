package org.memoriadb.services.presenter;

import java.util.*;
import java.util.regex.*;

import javax.swing.RowFilter;

/**
 * This object will recreated each time a new filter was entered.
 * @author nienor
 *
 */
public final class RegexRowFilter extends RowFilter<TableModel, Object> {
  
  private final Matcher matcher;
  private final Set<String> fAcceptedValues = new HashSet<String>();

  RegexRowFilter(String regex) {
    if (regex == null) throw new IllegalArgumentException("regex is null");
    
    Pattern pattern = Pattern.compile(regex);
    matcher = pattern.matcher("");
  }

  public boolean containsValue(String value) {
    return fAcceptedValues.contains(value); 
  }
  
  @Override
  public boolean include(javax.swing.RowFilter.Entry<? extends TableModel, ? extends Object> entry) {
    int columnIndex = entry.getValueCount();
    while ( --columnIndex > -1 ) {
      String value = entry.getStringValue(columnIndex);
      if (fAcceptedValues.contains(value)) return true;
      
      if (include(value)) {
        fAcceptedValues.add(value);
        return true;
      }
    }
    return false;
  }
  
  protected boolean include(String value) {
    return matcher.reset(value).find();
  }

}
