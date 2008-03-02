package org.memoriadb.util;

import java.net.*;

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
