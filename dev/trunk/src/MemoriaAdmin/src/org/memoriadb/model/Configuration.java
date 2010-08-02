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

package org.memoriadb.model;

import org.memoriadb.util.URLUtil;

import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

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
