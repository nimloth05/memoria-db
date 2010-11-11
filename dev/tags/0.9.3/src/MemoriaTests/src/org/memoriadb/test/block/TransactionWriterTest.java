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
import org.memoriadb.core.file.FileLayout;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.IntObject;
import org.memoriadb.testutil.AbstractMemoriaTest;
import org.memoriadb.testutil.FileStructure;
import org.memoriadb.testutil.FileStructure.ObjectInfo;

public class TransactionWriterTest extends AbstractMemoriaTest {

  public void test_object_info(){
    Object obj = new Object();
    IObjectId id = save(obj);
    delete(obj);
    
    FileStructure file = new FileStructure(getFile());
    assertEquals(3, file.getBlockCount());
    
    ObjectInfo o1 = file.getBlock(1).getObject(0);
    ObjectInfo o2 = file.getBlock(2).getObject(0);
    
    assertEquals(false, o1.isClass());
    assertEquals(false, o1.isDeleteMarker());
    assertEquals(2, file.getBlock(1).getBlock().getRevision());
    assertEquals(id, o1.getId());

    assertEquals(false, o2.isClass());
    assertEquals(true, o2.isDeleteMarker());
    assertEquals(3, file.getBlock(2).getBlock().getRevision());
    assertEquals(id, o2.getId());
  }
  
  public void test_opo_and_opf() {
    save(new Object());
    save(new IntObject(0)); // add class
    save(new IntObject(0));
    
    FileStructure file = new FileStructure(getFile());
    assertEquals(getOPO(), file.getBlock(1).getObject(0).getSize());
    
    // opf ist 4 byte ---> 3 Byte? 13.04.2009, so
    assertEquals(getOPO() + getOPF() + 3, file.getBlock(3).getObject(0).getSize());
  }
  
  /**
   * Tests the complete overhead needed for saving one object in one transaction (i.e. one block is written)
   */
  public void test_overhead_per_transaction() {
    save(new Object());
    save(new Object());    
    
    long opt = getBlockManager().getBlock(2).getPosition() - getBlockManager().getBlock(1).getPosition();
    assertEquals(68, opt);
    assertEquals(opt, FileLayout.getOPO(fObjectStore) + FileLayout.TRX_OVERHEAD + FileLayout.BLOCK_OVERHEAD);
  }
  
  public void test_position() {
    long headerSize = getFile().getSize();
    
    save(new Object());
    save(new Object());

    FileStructure file = new FileStructure(getFile());
    
    long positionB0 = file.getBlock(1).getPosition();
    long positionB1 = file.getBlock(2).getPosition();
    
    long blockSize = positionB1 - positionB0;
    
    assertEquals(headerSize, positionB0);
    assertEquals(positionB0+blockSize, positionB1);
    assertEquals(positionB1+blockSize, getFile().getSize());
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
    config.setUseCompression(false);
  }
  
}
