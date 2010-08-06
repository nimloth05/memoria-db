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

package org.memoriadb.test.core;

import org.memoriadb.CreateConfig;
import org.memoriadb.block.maintenancefree.MaintenanceFreeBlockManager;
import org.memoriadb.core.exception.FileCorruptException;
import org.memoriadb.core.file.FileLayout;
import org.memoriadb.core.file.InMemoryFile;
import org.memoriadb.id.IObjectId;
import org.memoriadb.testutil.AbstractMemoriaTest;
import org.memoriadb.testutil.FileStructure;

public class RecoveryTest extends AbstractMemoriaTest {

  public void test_corrupt_block_size_in_last_written() {
    Object o1 = new Object();

    save(o1);
    save(o1);
    save(o1);
    // |o1''|~o1'|

    FileStructure fs = new FileStructure(getFile());
    corruptFile(fs.getBlock(1).getPosition() + FileLayout.BLOCK_TAG_LEN);

    reopenFails();
  }

  public void test_corrupt_block_tag_in_last_written() {
    Object o1 = new Object();

    save(o1);
    save(o1);
    save(o1);
    // |o1''|~o1'|

    FileStructure fs = new FileStructure(getFile());
    corruptFile(fs.getBlock(1).getPosition());

    reopenFails();
  }

  public void test_corrupt_bootstrap_block() {
    // add an other block
    save(new Object());

    FileStructure fs = new FileStructure(getFile());

    corruptFile(fs.getBlock(0).getPosition() + FileLayout.BLOCK_TAG_LEN);

    reopenFails();

  }

  public void test_corrupt_bootstrap_block_startTag() {
    FileStructure fs = new FileStructure(getFile());

    corruptFile(fs.getBlock(0).getPosition());

    reopenFails();
  }

  public void test_corrupt_header() {

    corruptFile(FileLayout.MEMORIA_TAG.length);

    reopenFails();
  }

  public void test_corrupt_memoria_tag() {

    corruptFile(0);

    reopenFails();

  }

  public void test_corrupt_transaction_in_last_written() {
    Object o1 = new Object();

    IObjectId id = save(o1);
    save(o1);
    save(o1);
    // |o1''|~o1'|

    FileStructure fs = new FileStructure(getFile());
    corruptFile(fs.getBlock(1).getBodyStartPosition());

    assertEquals(4, getRevision(id));
    reopen();

    assertEquals(3, getRevision(id));
  }

  public void test_corrupt_transaction_in_last_written_with_garbage() {
    Object o1 = new Object();
    Object o2 = new Object();

    beginUpdate();
    IObjectId id1 = save(o1);
    IObjectId id2 = save(o2);
    endUpdate();
    // |o1, o2|
    assertEquals(2, getBlockManager().getBlockCount());
    assertBlocks(getBlock(1), getBlockForObjectId(id1));
    assertBlocks(getBlock(1), getBlockForObjectId(id2));
    assertEquals(2, getRevision(id1));
    assertEquals(2, getRevision(id2));

    save(o1);
    // |~o1, o2|o1'|
    assertEquals(3, getBlockManager().getBlockCount());
    assertBlocks(getBlock(2), getBlockForObjectId(id1));
    assertBlocks(getBlock(1), getBlockForObjectId(id2));
    assertEquals(3, getRevision(id1));
    assertEquals(2, getRevision(id2));

    save(o1);
    // |o1'',garbage|~o1'|o2'|
    assertEquals(4, getBlockManager().getBlockCount());
    assertBlocks(getBlock(1), getBlockForObjectId(id1));
    assertBlocks(getBlock(3), getBlockForObjectId(id2));
    assertEquals(4, getRevision(id1));
    assertEquals(5, getRevision(id2));

    FileStructure fs = new FileStructure(getFile());
    corruptFile(fs.getBlock(1).getBodyStartPosition());

    reopen();

    assertEquals(3, getRevision(id1));
    assertEquals(5, getRevision(id2));
  }

  public void test_file_too_small() {

    getFile().shrink(2);

    reopenFails();

  }

  public void test_fileStructure() {
    FileStructure fs = new FileStructure(getFile());
    assertEquals(fs.getHeader().getHeaderSize(), fs.getBlock(0).getPosition());
  }

  public void test_repair_corrupt_block_size_in_last_appended() {
    save(new Object());

    FileStructure fs = new FileStructure(getFile());
    corruptFile(fs.getBlock(1).getPosition() + FileLayout.BLOCK_TAG_LEN);

    reopen();

    assertEquals(0, fObjectStore.query(Object.class).size());
  }

  public void test_repair_corrupt_block_tag_in_last_appended() {
    save(new Object());

    FileStructure fs = new FileStructure(getFile());
    corruptFile(fs.getBlock(1).getPosition());

    reopen();

    assertEquals(0, fObjectStore.query(Object.class).size());
  }

  public void test_repair_corrupt_garbage() {
    Object o1 = new Object();
    Object o2 = new Object();

    beginUpdate();
    IObjectId id1 = save(o1);
    IObjectId id2 = save(o2);
    endUpdate();
    // |o1,o2|

    save(o1);
    // |~o1,o2|o1'|
    assertEquals(3, getBlockManager().getBlockCount());

    save(o1);
    // |o1''|~o1'|o2'|

    assertEquals(4, getBlockManager().getBlockCount());
    assertEquals(1, getBlockManager().getBlock(1).getObjectDataCount());
    assertEquals(getBlockManager().getBlock(1), getBlockForObjectId(id1));
    assertEquals(100, getBlockManager().getBlock(2).getInactiveRatio());
    assertEquals(getBlockManager().getBlock(3), getBlockForObjectId(id2));

    // garbage is expected at position 78
    FileStructure fs = new FileStructure(getFile());
    corruptFile(fs.getBlock(1).getPosition() + 78);

    reopen();

    assertEquals(2, fObjectStore.query(Object.class).size());
  }

  public void test_repair_corrupt_trx_in_last_appended() {
    save(new Object());

    FileStructure fs = new FileStructure(getFile());
    corruptFile(fs.getBlock(1).getBodyStartPosition());

    reopen();

    assertEquals(0, fObjectStore.query(Object.class).size());

    assertEquals(68, getBlock(1).getWholeSize());
  }

  /**
   * length: 2822
   *
   * |__head__|__boot__|<pos>|__block_tag__|<pos>+8|__size_crc__|<pos>+24|__trxa__|<pos>+80|__crc__
   */
  public void test_shrunk_block_crc_in_last_appended_repairs() {
    beginUpdate();
      save(new Object());
      save(new Object());
    endUpdate();

    int pos = (int)getBlock(1).getPosition();

    assertEquals(2, getBlockManager().getBlockCount());

    getFile().shrink(pos+87);

    reopen();

    assertEquals(0, fObjectStore.query(Object.class).size());

    assertEquals(pos, getBlock(1).getPosition());
    assertEquals(87, getBlock(1).getWholeSize());
    assertTrue(getBlock(1).isFree());

    // add a new element, the corrupt block must be reused
    IObjectId id = save(new Object());

    assertBlocks(getBlock(1), getBlockForObjectId(id));
    assertEquals(87, getBlock(1).getWholeSize());
  }

  /**
   * length: 2822
   *
   * |__head__|__boot__|<pos>|__block_tag__|<pos>+8|__size_crc__|<pos>+24|__trxa__|<pos>+80|__crc__
   */
  public void test_shrunk_trx_in_last_appended_repairs_() {
    beginUpdate();
      save(new Object());
      save(new Object());
    endUpdate();

    int pos = (int)getBlock(1).getPosition();

    assertEquals(2, getBlockManager().getBlockCount());

    getFile().shrink(pos+79);

    reopen();

    assertEquals(0, fObjectStore.query(Object.class).size());

    assertEquals(pos, getBlock(1).getPosition());
    assertEquals(79, getBlock(1).getWholeSize());
    assertTrue(getBlock(1).isFree());

    // add a new element, the corrupt block must be reused
    IObjectId id = save(new Object());

    assertBlocks(getBlock(1), getBlockForObjectId(id));
    assertEquals(79, getBlock(1).getWholeSize());
  }

  @Override
  protected void configureOpen(CreateConfig config) {
    configure(config);
  }

  @Override
  protected void configureReopen(CreateConfig config) {
    configure(config);
  }

  @Override
  protected InMemoryFile getFile() {
    return (InMemoryFile) super.getFile();
  }

  private void configure(CreateConfig config) {
    config.setBlockManager(new MaintenanceFreeBlockManager(0, 0));
    config.setUseCompression(false);
  }

  private void corruptFile(long position) {
    InMemoryFile file = getFile();

    byte b = file.getByte(position);

    // invert byte
    b = (byte) (b ^ 0xFF);

    file.write(new byte[] { b }, position);
  }

  private void reopenFails() {
    try {
      reopen();
      fail("start-tag in bootstrap-block is corrupt");
    }
    catch (FileCorruptException e) {
      // pass
    }
  }

}
