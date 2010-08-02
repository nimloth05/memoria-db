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

import org.memoriadb.model.Configuration;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class ConfigurationPM {
  
  private final DefaultListModel fClassPath;
  private final Document fDBPath;

  public static ConfigurationPM createFrom(Configuration configuration) {
    ConfigurationPM result = createNew();
    
    result.setDbPathString(configuration.getDbPath());
    
    for(String entry: configuration.getClassPaths()) {
      result.addClasspathEntry(entry.toString());
    }
    
    return result;
  }
  
  public static ConfigurationPM createNew() {
    return new ConfigurationPM();
  }
  
  private ConfigurationPM() {
    fClassPath = new DefaultListModel();  
    fDBPath = new PlainDocument();
  }
  
  public void addClasspathEntry(String entry) {
    fClassPath.addElement(entry);
  }

  public void applyTo(Configuration configuration) {
    configuration.setDbPath(getDbPathString());
    for(int i = 0; i < fClassPath.getSize(); ++i) {
      Object elementAt = fClassPath.getElementAt(i);
      //Element must be a string!
      configuration.addClassPath((String) elementAt);  
    }
  }
  
  public DefaultListModel getClassPath() {
    return fClassPath;
  }
  
  public Document getDBPath() {
    return fDBPath;
  }

  public void removeClasspathEntry(String selectedValue) {
    fClassPath.removeElement(selectedValue);
  }
  
  public void setDbPathString(String string) {
    try {
      fDBPath.insertString(0, string, null);
    }
    catch (BadLocationException e) {
      throw new IllegalArgumentException(e);
    }
  }

  private String getDbPathString()  {
    try {
      return fDBPath.getText(0, fDBPath.getLength());
    }
    catch (BadLocationException e) {
      throw new RuntimeException(e);
    }
  }
  

}
