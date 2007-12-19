package org.memoriadb.test.core;

import org.memoriadb.CreateConfig;
import org.memoriadb.block.maintenancefree.MaintenanceFreeBlockManager;
import org.memoriadb.core.exception.FileCorruptException;
import org.memoriadb.core.file.*;
import org.memoriadb.id.IObjectId;
import org.memoriadb.testutil.*;

public class RecoveryTest extends AbstractMemoriaTest {

  public void test_corrupt_block_size_in_last_written() {
    Object o1 = new Object();
    
    IObjectId id = save(o1);
    save(o1);
    save(o1);
    // |o1''|~o1'|
    
    FileStructure fs = new FileStructure(getFile());
    corruptFile(fs.getBlock(1).getPosition() + FileLayout.BLOCK_TAG_LEN);
    
    reopenFails();
  }
  
  public void test_corrupt_block_tag_in_last_written() {
    Object o1 = new Object();
    
    IObjectId id = save(o1);
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

  public void test_corrupt_garbage_has_no_effect() {
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
    assertEquals(getBlockManager().getBlock(1), getObjectInfo(id1).getCurrentBlock());
    assertEquals(100, getBlockManager().getBlock(2).getInactiveRatio());
    assertEquals(getBlockManager().getBlock(3), getObjectInfo(id2).getCurrentBlock());
    
    // block1 is 92 bytes wide, garbage starting at byte 72. corrupt bytes in between
    FileStructure fs = new FileStructure(getFile());
    corruptFile(fs.getBlock(1).getPosition()+76);
    corruptFile(fs.getBlock(1).getPosition()+95);

    reopen();

    assertEquals(2, fObjectStore.query(Object.class).size());
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
    
    assertEquals(4, getObjectInfo(id).getRevision());
    reopen();
    
    assertEquals(3, getObjectInfo(id).getRevision());
  }

  public void test_corrupt_transaction_in_last_written_with_garbage() {
    Object o1 = new Object();
    
    beginUpdate();
      IObjectId id = save(o1);
      save(new Object());
    endUpdate();
    // |o1, o|
    
    save(o1);
    // |~o1, o|o1'|
    
    save(o1);
    // |o1'',garbage|~o1'|o'|
    
    assertEquals(4, getBlockManager().getBlockCount());
    assertBlocks(getBlock(1),getObjectInfo(id).getCurrentBlock());
    
    FileStructure fs = new FileStructure(getFile());
    corruptFile(fs.getBlock(1).getBodyStartPosition());
    
    assertEquals(5, getObjectInfo(id).getRevision());
    reopen();
    
    assertEquals(4, getObjectInfo(id).getRevision());
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

  public void test_repair_corrupt_trx_in_last_appended() {
    save(new Object());

    FileStructure fs = new FileStructure(getFile());
    corruptFile(fs.getBlock(1).getBodyStartPosition());

    reopen();

    assertEquals(0, fObjectStore.query(Object.class).size());
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
