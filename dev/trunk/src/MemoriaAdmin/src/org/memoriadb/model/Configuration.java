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
  
  private final Set<String> fClassPath = new LinkedHashSet<String>();
  private String fDbPath;
  
  public void addClassPath(String path) {
    if (path == null) throw new IllegalArgumentException();
    
    fClassPath.add(path);
  }
  
  public String[] getClassPaths() {
    return fClassPath.toArray(new String[fClassPath.size()]);
  }
  
  public URL[] getClasspathsAsURL() {
    URL[] result = new URL[fClassPath.size()];
    Iterator<String> iterator = fClassPath.iterator();
    for(int i = 0; iterator.hasNext(); ++i) {
      result[i] = URLUtil.createURL(iterator.next());
    }
    return result;
  }
  
  public String getDbPath() {
    return fDbPath;
  }

  public void setDbPath(String dbPath) {
    fDbPath = dbPath;
  }
  
}
