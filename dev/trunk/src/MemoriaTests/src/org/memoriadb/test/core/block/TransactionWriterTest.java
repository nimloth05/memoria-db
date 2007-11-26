package org.memoriadb.test.core.block;

import org.memoriadb.core.file.FileLayout;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.test.core.testclasses.OneInt;
import org.memoriadb.testutil.*;
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
    assertEquals(2, o1.getVersion());
    assertEquals(id, o1.getId());

    assertEquals(false, o2.isClass());
    assertEquals(true, o2.isDeleteMarker());
    assertEquals(3, o2.getVersion());
    assertEquals(id, o2.getId());
  }
  
  public void test_opo_and_opf() {
    save(new Object());
    save(new OneInt(0)); // add class
    save(new OneInt(0));
    
    FileStructure file = new FileStructure(getFile());
    assertEquals(getOPO(), file.getBlock(1).getObject(0).getSize());
    
    // opf ist 4 byte
    assertEquals(getOPO() + getOPF() + 4, file.getBlock(3).getObject(0).getSize());
    
  }
  
  /**
   * Tests the complete overhead needed for saving one object in one transaction (i.e. one block is written)
   */
  public void test_overhead_per_transaction() {
    save(new Object());
    save(new Object());    
    
    long opt = getBlockManager().getBlock(2).getPosition() - getBlockManager().getBlock(1).getPosition();
    assertEquals(60, opt);
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
  
}
