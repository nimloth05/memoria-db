/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.memoriadb.core;

/**
 * This class is a iterator which can traverse a clazz hierrachy. The first class which will be handled is the given startClass. 
 * 
 * @author sandro
 *
 */
public abstract class InheritanceTraverser {
  
  private boolean fAborted;

  public InheritanceTraverser(Class<?> startClazz) {
    traverse(startClazz);
  }

  protected final void abort() {
    fAborted = true;
  }
  
  protected abstract void handle(Class<?> clazz);
  
  private void traverse(Class<?> startClazz) {
    Class<?> clazz = startClazz;
    while (clazz != null) {
      handle(clazz);
      clazz = clazz.getSuperclass();
      if (fAborted) break;
    }
  }
  

}
