package org.memoriadb.test.core;

import org.memoriadb.core.exception.FileCorruptException;
import org.memoriadb.core.file.*;
import org.memoriadb.testutil.*;

public class RecoveryTest extends AbstractMemoriaTest {

  public void test_corrupt_bootstrap_block() {
    // add an other block
    save(new Object());
    
    FileStructure fs = new FileStructure(getFile());

    corruptFile(fs.getBlock(0).getPosition() + FileLayout.BLOCK_TAG_LEN);

    try {
      reopen();
      fail("start-tag in bootstrap-block is corrupt");
    }
    catch (FileCorruptException e) {
      // pass
    }

  }
  
  public void test_corrupt_bootstrap_block_startTag() {
    FileStructure fs = new FileStructure(getFile());

    corruptFile(fs.getBlock(0).getPosition());

    try {
      reopen();
      fail("start-tag in bootstrap-block is corrupt");
    }
    catch (FileCorruptException e) {
      // pass
    }

  }
  
  public void test_corrupt_header() {

    corruptFile(FileLayout.MEMORIA_TAG.length);

    try {
      reopen();
      fail("start-tag in bootstrap-block is corrupt");
    }
    catch (FileCorruptException e) {
      // pass
    }

  }
  
  public void test_corrupt_memoria_tag() {

    corruptFile(0);

    try {
      reopen();
      fail("file is corrupt");
    }
    catch (FileCorruptException e) {
      // pass
    }

  }
  
  public void test_file_too_small() {

    getFile().shrink(2);

    try {
      reopen();
      fail("file is corrupt");
    }
    catch (FileCorruptException e) {
      // pass
    }

  }

  public void test_fileStructure() {
    FileStructure fs = new FileStructure(getFile());
    assertEquals(fs.getHeader().getHeaderSize(), fs.getBlock(0).getPosition());
  }

  public void test_repair_with_invalid_block() {
    save(new Object());
    
    FileStructure fs = new FileStructure(getFile());
    corruptFile(fs.getBlock(1).getPosition());
    
    reopen();
    
    assertEquals(0, fObjectStore.query(Object.class));
  }
  
  @Override
  protected InMemoryFile getFile() {
    return (InMemoryFile) super.getFile();
  }

  private void corruptFile(long position) {
    InMemoryFile file = getFile();

    byte b = file.getByte(position);

    // invert byte
    b = (byte) (b ^ 0xFF);

    file.write(new byte[] { b }, position);
  }
  
}
