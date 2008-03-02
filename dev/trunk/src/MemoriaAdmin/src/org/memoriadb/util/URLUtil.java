package org.memoriadb.util;

import java.io.File;
import java.net.*;

public final class URLUtil {
  
  public static URL createURL(String path) {
    try {
      return new File(path).toURI().toURL();
    }
    catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }
  
  private URLUtil() {}

}
