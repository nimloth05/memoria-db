package org.memoriadb.model;

import java.util.*;

/**
 * This class holds the filesystem dependant configuration.
 * @author nienor
 *
 */
public final class Configuration {
  
  private final List<String> fClassPath = new ArrayList<String>();
  private String fDbPath;
  
  public void addClassPath(String path) {
    if (path == null) throw new IllegalArgumentException();
    fClassPath.add(path);
  }
  
  public Iterable<String> getClassPaths() {
    return fClassPath;
  }
  
  public String getDbPath() {
    return fDbPath;
  }

  public void setDbPath(String dbPath) {
    fDbPath = dbPath;
  }
  
}
