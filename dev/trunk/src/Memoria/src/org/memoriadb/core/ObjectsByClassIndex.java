package org.memoriadb.core;

import java.util.*;

import org.memoriadb.core.util.collection.identity.IdentityHashSet;

public class ObjectsByClassIndex {

   private final Map<Class<?>, Set<Object>> fObjectsByClass = new HashMap<Class<?>, Set<Object>>();
//  private final MultiMapUnOrdered<Class<?>, Object> fObjectsByClass = new MultiMapUnOrdered<Class<?>, Object>();

  public void add(Object object) {
    insertIntoClassIndex(object, object.getClass());
  }

  public Iterable<Object> getObjects(Class<?> clazz) {
    Set<Object> setByClass = fObjectsByClass.get(clazz);
    if (setByClass == null) { return new ArrayList<Object>(); }
    return setByClass;

  }

  public void remove(Object object) {
    removeFromClassIndex(object, object.getClass());
  }

  private void insertIntoClassIndex(Object object, Class<?> clazz) {
    if (clazz == null) { return; }
    Set<Object> set = fObjectsByClass.get(clazz);
    if (set == null) {
      set = IdentityHashSet.create();
      fObjectsByClass.put(clazz, set);
    }
    set.add(object);
    // fObjectsByClass.put(clazz, object);
    insertIntoClassIndex(object, clazz.getSuperclass());
    for (Class<?> intf : clazz.getInterfaces()) {
      insertIntoClassIndex(object, intf);
    }
  }

  private void removeFromClassIndex(Object object, Class<?> clazz) {
    if (clazz == null) { return; }
    fObjectsByClass.get(clazz).remove(object);
    removeFromClassIndex(object, clazz.getSuperclass());
    for (Class<?> intf : clazz.getInterfaces()) {
      removeFromClassIndex(object, intf);
    }
  }

}
