package org.memoriadb.core.load;

import java.io.*;
import java.util.*;

import org.memoriadb.core.*;
import org.memoriadb.core.block.*;
import org.memoriadb.core.file.*;
import org.memoriadb.core.file.FileReader;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.exception.MemoriaException;

public final class ObjectLoader implements IReaderContext {

  private Map<IObjectId, HydratedInfo> fHydratedObjects = new HashMap<IObjectId, HydratedInfo>();
  private Map<IObjectId, HydratedInfo> fHydratedMetaClasses = new HashMap<IObjectId, HydratedInfo>();

  private final Set<IBindable> fObjectsToBind = new LinkedHashSet<IBindable>();

  private final ObjectRepo fRepo;
  private final FileReader fFileReader;
  private final IBlockManager fBlockManager;
  private final DBMode fDbMode;

  public static long readIn(FileReader fileReader, ObjectRepo repo, IBlockManager blockManager, DBMode mode) {
    return new ObjectLoader(fileReader, repo, blockManager, mode).read();
  }

  public ObjectLoader(FileReader fileReader, ObjectRepo repo, IBlockManager blockManager, DBMode mode) {
    if (fileReader == null) throw new IllegalArgumentException("fileReader is null");
    if (repo == null) throw new IllegalArgumentException("repo is null");
    if (blockManager == null) throw new IllegalArgumentException("BlockManager is null");
    if (mode == null) throw new IllegalArgumentException("DBMode is null");
    
    fFileReader = fileReader;
    fRepo = repo;
    fBlockManager = blockManager;
    fDbMode = mode;
  } 

  @Override
  public IObjectId createFrom(DataInput input) throws IOException {
    return fRepo.getIdFactory().createFrom(input);
  }

  @Override
  public DBMode getMode() {
    return fDbMode;
  }

  @Override
  public Object getObjectById(IObjectId objectId) {
    return fRepo.getObject(objectId);
  }
  
  @Override
  public boolean isRootClassId(IObjectId superClassId) {
    return fRepo.isRootClassId(superClassId);
  }

  @Override
  public void objectToBind(IBindable bindable) {
    fObjectsToBind.add(bindable);
  }

  public long read() {
    try {
      long headRevision = readBlockData();
      
      dehydrateMetaClasses();
      bindObjects();
      dehydrateObjects();
      bindObjects();
      
      return headRevision;
    } 
    catch (Exception e) {
      throw new MemoriaException(e);
    }
  }

  //FIXME: Aus dieser Klasse verschieben
  public FileHeader readHeader() {
    try {
      return fFileReader.readHeader();
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
  }
  
  /**
   * @param object null if deleteMarker was encountered
   */
  private void addHydratedObject(Map<IObjectId, HydratedInfo> container, HydratedObject object, IObjectId id, long version) {
    HydratedInfo info = container.get(id);
    if (info == null) {
      container.put(id, new HydratedInfo(id, object, version));
      return;
    } 

    // object already loaded in other version, newer version survives
    info.update(object, version);
  }

  private void bindObjects() {
    try {
      for (IBindable bindable : fObjectsToBind) {
        bindable.bind(this);
      }
      fObjectsToBind.clear();
    }
    catch (Exception e) {
      throw new BindingException(e);
    }
  }

  private void dehydrateMetaClasses() throws Exception {
    for (HydratedInfo info : fHydratedMetaClasses.values()) {
      dehydrateObject(info);
    }
    fHydratedMetaClasses = null;
  }

  private void dehydrateObject(HydratedInfo info) throws Exception {
    ObjectInfo objectInfo = new ObjectInfo(info.getObjectId(), info.getMemoriaClassId(), info.getObject(this), info.getVersion(), info.getOldGenerationCount());
    
    if(info.isDeleted()){
      fRepo.handleDelete(objectInfo);
    }
    else {
      fRepo.handleAdd(objectInfo);
    }
  }

  private void dehydrateObjects() throws Exception {
    for (HydratedInfo info : fHydratedObjects.values()) {
      dehydrateObject(info);
    }
    fHydratedObjects = null;
  }
  
  private long readBlockData() throws IOException {
    return fFileReader.readBlocks(fRepo.getIdFactory(), new IFileReaderHandler() {

      @Override
      public void block(Block block) {
        fBlockManager.add(block);
      }

      @Override
      public void memoriaClass(HydratedObject metaClass, IObjectId id, long version, int size) {
        addHydratedObject(fHydratedMetaClasses, metaClass, id, version);
      }

      @Override
      public void memoriaClassDeleted(IObjectId id, long version) {
        addHydratedObject(fHydratedMetaClasses, null, id, version);
      }

      @Override
      public void object(HydratedObject object, IObjectId id, long version, int size) {
        addHydratedObject(fHydratedObjects, object, id, version);
      }

      @Override
      public void objectDeleted(IObjectId id, long version) {
        addHydratedObject(fHydratedObjects, null, id, version);
      }

    });
  }
  
}
