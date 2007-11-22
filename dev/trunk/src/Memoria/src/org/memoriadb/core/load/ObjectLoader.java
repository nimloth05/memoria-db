package org.memoriadb.core.load;

import java.io.*;
import java.util.*;

import org.memoriadb.core.*;
import org.memoriadb.core.block.*;
import org.memoriadb.core.file.*;
import org.memoriadb.core.file.FileReader;
import org.memoriadb.core.id.*;
import org.memoriadb.core.mode.IModeStrategy;
import org.memoriadb.exception.MemoriaException;

public final class ObjectLoader implements IReaderContext {

  private Map<IObjectId, HydratedInfo> fHydratedObjects = new HashMap<IObjectId, HydratedInfo>();
 
  private Map<IObjectId, HydratedInfo> fHydratedMetaClasses = new HashMap<IObjectId, HydratedInfo>();

  private final Set<IBindable> fObjectsToBind = new LinkedHashSet<IBindable>();
  private final ObjectRepo fRepo;
  private final FileReader fFileReader;
  private final IBlockManager fBlockManager;
  private Block fCurrentBlock;
  private final IDefaultInstantiator fDefaultInstantiator;
  private final IObjectIdFactory fIdFactory;
  private final IModeStrategy fStore;

  public static long readIn(FileReader fileReader, ObjectRepo repo, IBlockManager blockManager, IDefaultInstantiator defaultInstantiator, IModeStrategy store) {
    return new ObjectLoader(fileReader, repo, blockManager, defaultInstantiator, store).read();
  }

  public ObjectLoader(FileReader fileReader, ObjectRepo repo, IBlockManager blockManager, IDefaultInstantiator defaultInstantiator, IModeStrategy store) {
    if (defaultInstantiator == null) throw new IllegalArgumentException("defaultInstantiator is null");
    if (fileReader == null) throw new IllegalArgumentException("fileReader is null");
    if (repo == null) throw new IllegalArgumentException("repo is null");
    if (blockManager == null) throw new IllegalArgumentException("BlockManager is null");
    if (store == null) throw new IllegalArgumentException("store is null");
    
    fStore = store;
    fFileReader = fileReader;
    fRepo = repo;
    fBlockManager = blockManager;
    fDefaultInstantiator = defaultInstantiator;
    fIdFactory = repo.getIdFactory();
  } 

  @Override
  public IDefaultInstantiator getDefaultInstantiator() {
    return fDefaultInstantiator;
  }

  @Override
  public Object getObjectById(IObjectId objectId) {
    return fRepo.getObject(objectId);
  }
  
  @Override
  public boolean isInDataMode() {
    return fStore.isInDataMode();
  }

  @Override
  public boolean isNullReference(IObjectId objectId) {
    return fRepo.isNullReference(objectId);
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

  @Override
  public IObjectId readObjectId(DataInput input) throws IOException {
    return fRepo.getIdFactory().createFrom(input);
  }

  private void addDeletionMarker(Map<IObjectId, HydratedInfo> container, IObjectId id, IObjectId deletionTypeId, long revision) {
    fIdFactory.adjustId(id);
    
    HydratedInfo info = container.get(id);
    if (info == null) {
      container.put(id, new HydratedInfo(id, deletionTypeId, null, revision, fCurrentBlock));
      return;
    } 

    // object already loaded in other version, newer version survives
    info.update(fCurrentBlock, null, deletionTypeId, revision);
    if (info.getVersion() != revision) throw new MemoriaException(id + ": DeletionMarker("+revision+") has lower revision then last objectData("+info.getVersion()+")");
  }

  /**
   * @param object null if deleteMarker was encountered
   */
  private void addHydratedObject(Map<IObjectId, HydratedInfo> container, HydratedObject object, IObjectId id, long version) {
    fIdFactory.adjustId(id);
    
    HydratedInfo info = container.get(id);
    if (info == null) {
      container.put(id, new HydratedInfo(id, object.getTypeId(), object, version, fCurrentBlock));
      return;
    } 

    // object already loaded in other version, newer version survives
    info.update(fCurrentBlock, object, object.getTypeId(), version);
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
    ObjectInfo objectInfo = new ObjectInfo(info.getObjectId(), info.getMemoriaClassId(), info.getObject(this), info.getCurrentBlock(), info.getVersion(), info.getOldGenerationCount());
    
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
        fCurrentBlock = block;
      }

      @Override
      public void memoriaClass(HydratedObject metaClass, IObjectId id, long version, int size) {
        addHydratedObject(fHydratedMetaClasses, metaClass, id, version);
      }

      @Override
      public void memoriaClassDeleted(IObjectId id, long version) {
        addDeletionMarker(fHydratedMetaClasses, id, fRepo.getMemoriaClassDeletionMarker(), version);
      }

      @Override
      public void object(HydratedObject object, IObjectId id, long version, int size) {
        addHydratedObject(fHydratedObjects, object, id, version);
      }

      @Override
      public void objectDeleted(IObjectId id, long version) {
        addDeletionMarker(fHydratedObjects, id, fRepo.getObjectDeletionMarker(), version);
      }

    });
  }
  
}
