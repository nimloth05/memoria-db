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

package org.memoriadb.loadtests;

import org.memoriadb.CreateConfig;
import org.memoriadb.IObjectStore;
import org.memoriadb.block.maintenancefree.MaintenanceFreeBlockManager;
import org.memoriadb.id.IObjectId;
import org.memoriadb.testutil.AbstractMemoriaTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * a pretty complex graph is created and many times updated with different BlockManager-configurations.
 * @author msc
 *
 */
public class ComplexGraphTest extends AbstractMemoriaTest {

  public static class IntArrayContainer implements IUpdatable {

    private Object[] fArray = new Object[] { 0, 1, 2, 3, 4, 5 };

    @Override
    public boolean equals(Object obj) {
      IntArrayContainer other = (IntArrayContainer) obj;
      return Arrays.equals(fArray, other.fArray);
    }

    @Override
    public void print(int indent) {
      System.out.println(getSpaces(indent) + Arrays.toString(fArray));
    }

    @Override
    public void update(IObjectStore store) {
      List<Object> list = Arrays.asList(fArray);
      Collections.shuffle(list);
      fArray = list.toArray();
      store.saveAll(this);
    }

  }

  public static class IntListContainer implements IUpdatable {

    private final List<Object> fList = new ArrayList<Object>();

    {
      for (int i = 0; i < 5; ++i) {
        fList.add(i);
      }
    }

    @Override
    public boolean equals(Object obj) {
      IntListContainer other = (IntListContainer) obj;
      return fList.equals(other.fList);
    }
    
    @Override
    public void print(int indent) {
      System.out.println(getSpaces(indent) + fList);
    }

    @Override
    public void update(IObjectStore store) {
      Collections.shuffle(fList);
      store.save(fList);
    }

  }

  public static interface IUpdatable {
    public void print(int indent);

    public void update(IObjectStore store);
  }

  public static class ObjectArrayContainer implements IUpdatable {

    private final Object[] fArray = new Object[] { 0, 1, 2, 3, 4, 5 };

    @Override
    public boolean equals(Object obj) {
      ObjectArrayContainer other = (ObjectArrayContainer) obj;
      return Arrays.equals(fArray, other.fArray);
    }

    public Object get(int index) {
      return fArray[index];
    }

    @Override
    public void print(int indent) {
      System.out.println(getSpaces(indent)+ "ObjectArrayContainer");
      for (Object obj: fArray) {
        if ((obj instanceof IUpdatable)) {
          ((IUpdatable)obj).print(indent+1);
        }
        else {
          System.out.println(getSpaces(indent+1) + obj);
        }
      }
    }

    public void set(int index, Object obj) {
      fArray[index] = obj;
    }

    @Override
    public void update(IObjectStore store) {
      Object o0 = fArray[0];
      fArray[0] = fArray[4];
      fArray[4] = o0;
      store.save(fArray);
    }

  }

  public static class ObjectListContainer implements IUpdatable {

    private final List<Object> fList = new ArrayList<Object>();

    public ObjectListContainer() {
      for (int i = 0; i < 5; ++i) {
        fList.add(i);
      }
    }

    public ObjectListContainer(Object obj) {
      this();
      fList.set(0, obj);
    }

    @Override
    public boolean equals(Object obj) {
      ObjectListContainer other = (ObjectListContainer) obj;
      return fList.equals(other.fList);
    }

    public Object get(int index) {
      return fList.get(index);
    }

    @Override
    public void print(int indent) {
      System.out.println(getSpaces(indent)+ "ObjectListContainer");
      for (int i = 0; i < fList.size(); ++i) {
        if ((fList.get(i) instanceof IUpdatable)) {
          ((IUpdatable)fList.get(i)).print(indent+1);
        }
        else {
          System.out.println(getSpaces(indent+1) + fList.get(i));
        }
      }
    }

    public void set(int index, Object obj) {
      fList.set(index, obj);
    }

    @Override
    public void update(IObjectStore store) {
      Collections.shuffle(fList);
      store.save(fList);

    }

  }

  private int fInactiveThreshold;

  private int fSizeThreshold;

  private static String getSpaces(int indent) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < indent*2; ++i)
      result.append(" ");
    return result.toString();
  }

  public void test() {
    for (int inactiveThreshold = 0; inactiveThreshold <= 100; inactiveThreshold += 19) {
      for (int sizeThreshold = 0; sizeThreshold <= 100; sizeThreshold += 23) {
        fInactiveThreshold = inactiveThreshold;
        fSizeThreshold = sizeThreshold;

        executeTest();

      }
    }
  }

  @Override
  protected void configureOpen(CreateConfig config) {
    configure(config);
  }

  @Override
  protected void configureReopen(CreateConfig config) {
    configure(config);
  }

  private void configure(CreateConfig config) {
    config.setBlockManager(new MaintenanceFreeBlockManager(fInactiveThreshold, fSizeThreshold));
  }

  private IUpdatable createChild0(List<IObjectId> updatables) {
    ObjectArrayContainer result = new ObjectArrayContainer();
    result.set(0, new IntArrayContainer());
    result.set(1, new IntListContainer());
    result.set(2, new ObjectListContainer("str"));
    updatables.add(saveAll(result));
    updatables.add(fObjectStore.getId(result.get(0)));
    updatables.add(fObjectStore.getId(result.get(1)));
    updatables.add(fObjectStore.getId(result.get(2)));
    return result;
  }

  private IUpdatable createChild1(List<IObjectId> updatables) {
    ObjectListContainer result = new ObjectListContainer();
    result.set(0, new IntArrayContainer());
    result.set(1, new IntListContainer());
    result.set(2, new ObjectListContainer(new ObjectListContainer(new IntArrayContainer())));
    updatables.add(saveAll(result));
    updatables.add(fObjectStore.getId(result.get(0)));
    updatables.add(fObjectStore.getId(result.get(1)));
    updatables.add(fObjectStore.getId(result.get(2)));
    return result;
  }

  private IUpdatable createChild2(List<IObjectId> updatables) {
    ObjectArrayContainer result = new ObjectArrayContainer();
    updatables.add(saveAll(result));
    return result;
  }

  private IUpdatable createChild3(List<IObjectId> updatables) {
    ObjectListContainer result = new ObjectListContainer();
    updatables.add(saveAll(result));
    return result;
  }

  private IUpdatable createChild4(List<IObjectId> updatables) {
    ObjectArrayContainer result = new ObjectArrayContainer();
    updatables.add(saveAll(result));
    return result;
  }

  private ObjectArrayContainer createRoot(List<IObjectId> updatables) {
    ObjectArrayContainer result = new ObjectArrayContainer();
    updatables.add(saveAll(result));
    result.set(0, createChild0(updatables));
    result.set(1, createChild1(updatables));
    result.set(2, createChild2(updatables));
    result.set(3, createChild3(updatables));
    result.set(4, createChild4(updatables));

    return result;
  }

  private void executeTest() {
    List<IObjectId> updatables = new ArrayList<IObjectId>();
    ObjectArrayContainer root = createRoot(updatables);
    IObjectId id = saveAll(root);

    reopen();
    IUpdatable l1_root = get(id);
    assertTrue(root.equals(l1_root));

    performUpdates(updatables);

    reopen();
    IUpdatable l2_root = get(id);
    
    assertTrue(l1_root.equals(l2_root));
    
  }

  private void performUpdates(List<IObjectId> updatables) {
    // update every element
    for (int i = 0; i < updatables.size(); ++i) {
      IUpdatable updatable = get(updatables.get(i));
      updatable.update(fObjectStore);
    }

    // update every third element
    for (int i = 0; i < updatables.size(); i += 3) {
      IUpdatable updatable = get(updatables.get(i));
      updatable.update(fObjectStore);
    }
  }

}
