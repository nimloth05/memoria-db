package org.memoriadb.model;

import java.net.URL;
import java.util.*;

import org.memoriadb.util.URLUtil;

/**
 * This class holds the filesystem dependant configuration.
 * @author nienor
 *
 */
public final class Configuration {
  
  private final Set<URL> fClassPath = new LinkedHashSet<URL>();
  private String fDbPath;
  
  public void addClassPath(String path) {
    if (path == null) throw new IllegalArgumentException();
    
    fClassPath.add(URLUtil.createURL(path));
  }
  
  public URL[] getClassPaths() {
    return fClassPath.toArray(new URL[fClassPath.size()]);
  }
  
  public String getDbPath() {
    return fDbPath;
  }

  public void setDbPath(String dbPath) {
    fDbPath = dbPath;
  }
  
}
