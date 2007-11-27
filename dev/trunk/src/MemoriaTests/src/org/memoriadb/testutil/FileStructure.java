package org.memoriadb.testutil;

import java.io.IOException;
import java.util.*;

import org.memoriadb.core.block.Block;
import org.memoriadb.core.file.*;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.HydratedObject;
import org.memoriadb.exception.MemoriaException;

/**
 * Stores the structure of a IMemoriaFile for test and debug reasons.
 * 
 * @author msc
 */
public class FileStructure {

  public static class BlockInfo {
    private final Block fBlock;
    private final List<ObjectInfo> fObjectInfos = new ArrayList<ObjectInfo>();

    public BlockInfo(Block block) {
      fBlock = block;
    }

    public void add(ObjectInfo info) {
      fObjectInfos.add(info);
    }

    public Block getBlock() {
      return fBlock;
    }
    
    public ObjectInfo getObject(int index) {
      return fObjectInfos.get(index);
    }

    public List<ObjectInfo> getObjectInfos() {
      return fObjectInfos;
    }

    public long getPosition() {
      return fBlock.getPosition();
    }
    
    public long getSize() {
      return fBlock.getSize();
    }

  }

  public static class ObjectInfo {

    static final int DEL_MARKER = -1;

    boolean fIsClass;
    IObjectId fId;
    long fVersion;
    int fSize;

    public ObjectInfo(boolean isClass, IObjectId id, long version) {
      this(isClass, id, version, DEL_MARKER);
    }

    public ObjectInfo(boolean isClass, IObjectId id, long version, int size) {
      this.fIsClass = isClass;
      this.fId = id;
      this.fVersion = version;
      this.fSize = size;
    }

    public IObjectId getId() {
      return fId;
    }

    public int getSize() {
      return fSize;
    }

    public long getVersion() {
      return fVersion;
    }

    public boolean isClass() {
      return fIsClass;
    }

    public boolean isDeleteMarker() {
      return getSize() == DEL_MARKER;
    }

  }

  private Header fHeader;

  private final List<BlockInfo> fBlocks = new ArrayList<BlockInfo>();
  private BlockInfo fCurrentBlock;

  public FileStructure(IMemoriaFile file) {
    FileReader reader = new FileReader(file);

    try {
      readFile(reader);
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }

  }

  public BlockInfo getBlock(int index){
    return fBlocks.get(index);
  }
  
  public Object getBlockCount() {
    return fBlocks.size();
  }
  
  public List<BlockInfo> getBlocks() {
    return fBlocks;
  }

  private void readFile(FileReader reader) throws IOException {
    fHeader = reader.readHeader();
    reader.readBlocks(
        fHeader.loadIdFactory(), new IFileReaderHandler(){
 
      @Override
      public void block(Block block) {
        fCurrentBlock = new BlockInfo(block);
        fBlocks.add(fCurrentBlock);
      }

      @Override
      public void memoriaClass(HydratedObject metaClass, IObjectId id, long version, int size) {
        fCurrentBlock.add(new ObjectInfo(true, id, version, size));
      }

      @Override
      public void memoriaClassDeleted(IObjectId id, long version) {
        fCurrentBlock.add(new ObjectInfo(true, id, version));
      }

      @Override
      public void object(HydratedObject object, IObjectId id, long version, int size) {
        fCurrentBlock.add(new ObjectInfo(false, id, version, size));
      }

      @Override
      public void objectDeleted(IObjectId id, long version) {
        fCurrentBlock.add(new ObjectInfo(false, id, version));
      }
      
    });
  }
}
