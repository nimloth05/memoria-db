/*
 * Copyright 2010 Micha Riser
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
package org.memoriadb.core;

import java.util.*;

import org.memoriadb.core.util.collection.*;

public class ObjectsByClassIndex {
  
  private final MultiMapOrdered<Class<?>, Class<?>> fSubclasses = new MultiMapOrdered<Class<?>, Class<?>>();

  private final MultiMapUnOrdered<Class<?>, Object> fObjectsByClass = new MultiMapUnOrdered<Class<?>, Object>() {
    @Override
    protected java.util.Set<Object> createSet() {
      return Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>(8));
    };
  };
  
  public void add(Object object) {
    Class<? extends Object> clazz = object.getClass();
    if (!fObjectsByClass.contains(clazz)) {
      addClass(clazz);
    }
    fObjectsByClass.put(clazz, object);
  }
  
  public Iterable<Class<?>> getClassesWithRegisteredObjects() {
    return fObjectsByClass.keyIterable();
  }

  public Iterable<Object> getObjects(Class<?> clazz) {
    List<Iterable<Object>> iterables = new ArrayList<Iterable<Object>>();
    for(Class<?> subclazz: fSubclasses.get(clazz)) {
      iterables.add(fObjectsByClass.get(subclazz));
    }
    return new CompoundIterable<Object>(iterables);
  }

  public void remove(Object object) {
    Class<? extends Object> clazz = object.getClass();
    fObjectsByClass.remove(clazz, object);
    if (!fObjectsByClass.contains(clazz)) {
      removeClass(clazz);
    }
  }

  private void addClass(Class<?> clazz) {
    for(Class<?> superClassOrInterface: getAllSuperclassesAndinterfaces(clazz)) {
      fSubclasses.put(superClassOrInterface, clazz);
    }
  }

  private void addSuperclasses(Class<?> clazz, Set<Class<?>> result) {
    if (clazz == null || !result.add(clazz)) return;
    addSuperclasses(clazz.getSuperclass(), result);
    for (Class<?> intf : clazz.getInterfaces()) {
      addSuperclasses(intf, result);
    }
  }

  private Set<Class<?>> getAllSuperclassesAndinterfaces(Class<?> clazz) {
    Set<Class<?>> result = new HashSet<Class<?>>();
    result.add(Object.class);
    addSuperclasses(clazz, result);
    return result;
  }
  
  private void removeClass(Class<?> clazz) {
    for(Class<?> superClassOrInterface: getAllSuperclassesAndinterfaces(clazz)) {
      fSubclasses.remove(superClassOrInterface, clazz);
    }
  }

}
