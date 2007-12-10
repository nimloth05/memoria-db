package org.memoriadb.core.util.collection.identity;

import java.util.*;


public class IdentityHashSet {
  
  public static <T> Set<T> create() {
    return Collections.newSetFromMap(new MemoriaIdentityHashMap<T, Boolean>());
  }

}
