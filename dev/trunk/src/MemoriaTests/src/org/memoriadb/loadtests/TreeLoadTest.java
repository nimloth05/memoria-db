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

package org.memoriadb.loadtests;

import org.memoriadb.CreateConfig;
import org.memoriadb.TestMode;
import org.memoriadb.id.IObjectId;
import org.memoriadb.testutil.AbstractMemoriaTest;
import org.memoriadb.testutil.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * save and read a tree of Nodes. Depth and width of the tree are configurable 
 * 
 * @author msc
 * 
 */
public class TreeLoadTest extends AbstractMemoriaTest {

  public void test() {
    System.out.println("max VM Mem: "+ Runtime.getRuntime().maxMemory()/1000);
    long start1 = System.nanoTime();
    Node root = createTree(5, 2);
    IObjectId id = save(root);
    long start2 = System.nanoTime();
    System.out.println("create ms: " + (start2 - start1)/1000000);
    writeFootprint();
    root = null; 
    reopen();
    System.out.println("read ms:  " + (System.nanoTime() - start2)/1000000);
    writeFootprint();

    System.out.println();
    root = fObjectStore.get(id);
    System.out.println("Nodes  :" + root.countDescendants());
    System.out.println("objects: " + CollectionUtil.count(fObjectStore.getAllObjects()));
  }

  @Override
  protected void configureOpen(CreateConfig config) {
    super.configureOpen(config);
  }

  @Override
  protected TestMode getTestMode() {
    return TestMode.filesystem;
  }

  private void addChildren(Node parent, int currentDepth, int depth, int width) {
    if (currentDepth == depth) return;
    
    for (int i = 0; i < width; ++i) {
      Node child = new Node();
      saveAll(child);
      addChildren(child, currentDepth + 1, depth, width);
      parent.add(child);
      save(parent.getCollection());
    }
  }

  private Node createTree(int depth, int width) {
    Node root = new Node();
    saveAll(root);
    addChildren(root, 0, depth, width);
    return root;
  }

  private void writeFootprint() {
    System.gc();
    System.out.println("mem footprint " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000 + "kb");
  }
}

class Node {

  private final List<Node> fNodes = new ArrayList<Node>();

  public void add(Node child) {
    fNodes.add(child);
  }
  
  public int countDescendants() {
    return 1 + recursiveCountDescendants();
  }

  public List<Node> getCollection() {
    return fNodes;
  }

  private int recursiveCountDescendants() {
    int result = fNodes.size();

    for (Node child : fNodes) {
      result += child.recursiveCountDescendants();
    }

    return result;
  }
  
  
}
