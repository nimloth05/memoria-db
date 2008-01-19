package org.memoriadb.ui.moodel;

import javax.swing.DefaultListModel;
import javax.swing.text.*;

import org.memoriadb.model.Configuration;

public class ConfigurationPM {
  
  private final DefaultListModel fClassPath;
  private final Document fDBPath;

  public static ConfigurationPM createNew() {
    return new ConfigurationPM();
  }
  
  private ConfigurationPM() {
    fClassPath = new DefaultListModel();  
    fDBPath = new PlainDocument();
  }
  
  public Configuration createConfiguration() {
    Configuration configuration = new Configuration();
    configuration.setDbPath(getDbPathString());
    for(int i = 0; i < fClassPath.getSize(); ++i) {
      Object elementAt = fClassPath.getElementAt(i);
      //Element must be a string!
      configuration.addClassPath((String) elementAt);  
    }
    
    return configuration;
  }

  public DefaultListModel getClassPath() {
    return fClassPath;
  }
  
  public Document getDBPath() {
    return fDBPath;
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
