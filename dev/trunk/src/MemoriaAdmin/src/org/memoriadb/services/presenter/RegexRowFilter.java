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

package org.memoriadb.services.presenter;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
  public boolean include(javax.swing.RowFilter.Entry<? extends TableModel, ?> entry) {
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
