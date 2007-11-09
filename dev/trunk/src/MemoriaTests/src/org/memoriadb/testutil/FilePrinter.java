package org.memoriadb.testutil;

import java.io.IOException;

import org.memoriadb.core.block.Block;
import org.memoriadb.core.file.*;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.HydratedObject;

public class FilePrinter {
  
  private static class PrintHandler implements IFileReaderHandler {
    
    @Override
    public void block(Block block) {
      System.out.println(block);      
    }

    @Override
    public void memoriaClass(HydratedObject metaClass, IObjectId id, long version, int size) {
      System.out.println("  class id " + id + " version " + version);
    }

    @Override
    public void memoriaClassDeleted(IObjectId id, long version) {
      System.out.println("  class deleted id " + id + " version " + version);
    }

    @Override
    public void object(HydratedObject object, IObjectId id, long version, int size) {
      System.out.println("  object id " + id + " version " + version);
    }

    @Override
    public void objectDeleted(IObjectId id, long version) {
      System.out.println("  object deleted id " + id + " version " + version);
    }
    
  }
  
  public static void print(IMemoriaFile file) {
    try {
      internalPrint(file);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private static void internalPrint(IMemoriaFile file) throws IOException {
    FileReader reader = new FileReader(file);
    
    FileHeader header = reader.readHeader();
    
    System.out.println("------------------------");

    reader.readBlocks(header.loadIdFactory(), new PrintHandler());
  };
}
