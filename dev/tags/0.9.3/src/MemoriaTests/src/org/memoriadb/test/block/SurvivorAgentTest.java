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

package org.memoriadb.test.block;

import org.memoriadb.CreateConfig;
import org.memoriadb.block.AppendBlockManager;
import org.memoriadb.block.Block;
import org.memoriadb.core.IObjectInfo;
import org.memoriadb.id.IObjectId;
import org.memoriadb.testutil.AbstractMemoriaTest;

import java.util.HashSet;

public class SurvivorAgentTest extends AbstractMemoriaTest {
  
  public void test() {
    beginUpdate();
      Object o1 = new Object();
      IObjectId id1 = save(o1);

      Object o2 = new Object();
      IObjectId id2 = save(o2);
    endUpdate();

    Block b1 = getBlockManager().getBlock(1);
    org.memoriadb.testutil.CollectionUtil.containsAll(getUpdates(b1), id1, id2);
    org.memoriadb.testutil.CollectionUtil.containsAll(getDeleteMarkers(b1));

    // delte-marker for o1 in b1
    delete(o1);
    Block b2 = getBlockManager().getBlock(2);
    org.memoriadb.testutil.CollectionUtil.containsAll(getUpdates(b1), id2);
    org.memoriadb.testutil.CollectionUtil.containsAll(getDeleteMarkers(b1));
    org.memoriadb.testutil.CollectionUtil.containsAll(getUpdates(b2));
    org.memoriadb.testutil.CollectionUtil.containsAll(getDeleteMarkers(b2), id1);

    delete(o2);

    Block b3 = getBlockManager().getBlock(3);
    org.memoriadb.testutil.CollectionUtil.containsAll(getUpdates(b1));
    org.memoriadb.testutil.CollectionUtil.containsAll(getDeleteMarkers(b2), id1);
    org.memoriadb.testutil.CollectionUtil.containsAll(getDeleteMarkers(b3), id2);
    
    IObjectId o1_g2 = save(o1);
    Block b4 = getBlockManager().getBlock(4);
    org.memoriadb.testutil.CollectionUtil.containsAll(getUpdates(b1));
    org.memoriadb.testutil.CollectionUtil.containsAll(getDeleteMarkers(b2), id1);
    org.memoriadb.testutil.CollectionUtil.containsAll(getDeleteMarkers(b3), id2);
    org.memoriadb.testutil.CollectionUtil.containsAll(getUpdates(b4), o1_g2);
  }
  

  @Override
  protected void configureOpen(CreateConfig config) {
    config.setBlockManager(new AppendBlockManager());
  }
  
  @Override
  protected void configureReopen(CreateConfig config) {
    config.setBlockManager(new AppendBlockManager());
  }

  private Iterable<IObjectId> getDeleteMarkers(Block block) {
    HashSet<IObjectId> result = new HashSet<IObjectId>();
    for(IObjectInfo info: fObjectStore.getSurvivorAgent(block).getActiveDeleteMarkers()){
      result.add(info.getId());
    }
    return result;
  }
  
  private Iterable<IObjectId> getUpdates(Block block) {
    HashSet<IObjectId> result = new HashSet<IObjectId>();
    for(IObjectInfo info: fObjectStore.getSurvivorAgent(block).getActiveObjectData()){
      result.add(info.getId());
    }
    return result;
  }
}
