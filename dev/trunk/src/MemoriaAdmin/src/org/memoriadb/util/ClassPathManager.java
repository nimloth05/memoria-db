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

package org.memoriadb.util;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Resets the Thread.contextClassLoader with a new URLClassloader so that meomria can load specific Handlers.
 * @author nienor
 *
 */
public final class ClassPathManager {

  private ClassLoader fOldClassLoader;

  public void configure(URL[] classPaths) {
    fOldClassLoader = Thread.currentThread().getContextClassLoader();
    URLClassLoader classLoader = new URLClassLoader(classPaths, fOldClassLoader);
    Thread.currentThread().setContextClassLoader(classLoader);
  }

  public void resetClassLoader() {
    if (fOldClassLoader == null) return;
    Thread.currentThread().setContextClassLoader(fOldClassLoader);
  }

}
