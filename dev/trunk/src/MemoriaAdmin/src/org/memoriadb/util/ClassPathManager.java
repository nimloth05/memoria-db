package org.memoriadb.util;

import java.io.File;
import java.net.*;

public final class ClassPathManager {

  public static class ExpandeableClassLoader extends URLClassLoader {

    public ExpandeableClassLoader(URL[] urls, ClassLoader parent) {
      super(urls, parent);
    }

    @Override
    public void addURL(URL url) {
      super.addURL(url);
    }
  }

  private final ClassLoader fOldClassLoader;

  public ClassPathManager() {
    fOldClassLoader = Thread.currentThread().getContextClassLoader();
  }

  public void configure(Iterable<String> classPaths) {
    ExpandeableClassLoader classLoader = new ExpandeableClassLoader(new URL[0], fOldClassLoader);
    for (String classPath : classPaths) {
      classLoader.addURL(createURL(classPath));
    }
    Thread.currentThread().setContextClassLoader(classLoader);
  }

  public void resetClassLoader() {
    Thread.currentThread().setContextClassLoader(fOldClassLoader);
  }

  private URL createURL(String path) {
    try {
      return new File(path).toURI().toURL();
    }
    catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

}
