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

package org.memoriadb.testutil;

import org.memoriadb.block.Block;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.Header;
import org.memoriadb.core.file.IMemoriaFile;
import org.memoriadb.core.file.read.FileReader;
import org.memoriadb.core.file.read.HydratedObject;
import org.memoriadb.core.file.read.IFileReaderHandler;
import org.memoriadb.id.IObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public long getBodyStartPosition() {
      return fBlock.getBodyStartPosition();
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
      return fBlock.getBodySize();
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

  public BlockInfo getBlock(int index) {
    return fBlocks.get(index);
  }

  public Object getBlockCount() {
    return fBlocks.size();
  }

  public List<BlockInfo> getBlocks() {
    return fBlocks;
  }

  public Header getHeader() {
    return fHeader;
  }

  private void readFile(FileReader reader) throws IOException {
    fHeader = reader.readHeader();
    reader.readBlocks(fHeader.loadIdFactory(), fHeader.getCompressor(), new IFileReaderHandler() {

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
