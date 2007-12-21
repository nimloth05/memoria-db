package org.memoriadb.core.file.read;

import java.io.*;
import java.util.*;

import org.memoriadb.block.*;
import org.memoriadb.core.*;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.ICompressor;
import org.memoriadb.core.mode.IModeStrategy;
import org.memoriadb.handler.IBindable;
import org.memoriadb.id.*;
import org.memoriadb.instantiator.IInstantiator;

public final class ObjectLoader implements IReaderContext {

  private Map<IObjectId, HydratedInfo> fHydratedObjects = new HashMap<IObjectId, HydratedInfo>();
  private Map<IObjectId, HydratedInfo> fHydratedMetaClasses = new HashMap<IObjectId, HydratedInfo>();

  private final Set<IBindable> fGenOneBindings = new LinkedHashSet<IBindable>();
  private final Set<IBindable> fGenTwoBindings = new LinkedHashSet<IBindable>();

  private final ObjectRepository fRepo;
  private final FileReader fFileReader;
  private final IBlockManager fBlockManager;
  private Block fCurrentBlock;
  private final IInstantiator fInstantiator;
  private final IObjectIdFactory fIdFactory;
  private final IModeStrategy fStore;
  private final ICompressor fCompressor;

  public static long readIn(FileReader fileReader, ObjectRepository repo, IBlockManager blockManager, IInstantiator instantiator, IModeStrategy store, ICompressor compressor) {
    return new ObjectLoader(fileReader, repo, blockManager, instantiator, store, compressor).read();
  }

  public ObjectLoader(FileReader fileReader, ObjectRepository repo, IBlockManager blockManager, IInstantiator instantiator, IModeStrategy store, ICompressor compressor) {
    if (instantiator == null) throw new IllegalArgumentException("defaultInstantiator is null");
    if (fileReader == null) throw new IllegalArgumentException("fileReader is null");
    if (repo == null) throw new IllegalArgumentException("repo is null");
    if (blockManager == null) throw new IllegalArgumentException("BlockManager is null");
    if (store == null) throw new IllegalArgumentException("store is null");
    if (compressor == null) throw new IllegalArgumentException("compressor is null");

    fStore = store;
    fFileReader = fileReader;
    fRepo = repo;
    fBlockManager = blockManager;
    fInstantiator = instantiator;
    fIdFactory = repo.getIdFactory();
    fCompressor = compressor;
  }

  @Override
  public void addGenOneBinding(IBindable bindable) {
    fGenOneBindings.add(bindable);
  }

  @Override
  public void addGenTwoBinding(IBindable bindable) {
    fGenTwoBindings.add(bindable);
  }

  @Override
  public IObjectId getArrayMemoriaClass() {
    return fRepo.getIdFactory().getArrayMemoriaClass();
  }

  @Override
  public IInstantiator getDefaultInstantiator() {
    return fInstantiator;
  }

  @Override
  public Object getExistingObject(IObjectId id) {
    return fRepo.getExistingObject(id);
  }

  @Override
  public Object getObject(IObjectId id) {
    return fRepo.getObject(id);
  }

  @Override
  public IObjectId getPrimitiveClassId() {
    return fRepo.getIdFactory().getPrimitiveClassId();
  }

  @Override
  public boolean isInDataMode() {
    return fStore.isDataMode();
  }

  @Override
  public boolean isNullReference(IObjectId objectId) {
    return fRepo.isNullReference(objectId);
  }

  @Override
  public boolean isRootClassId(IObjectId superClassId) {
    return fRepo.getIdFactory().isRootClassId(superClassId);
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
    // FIXME GenericMemoriaExceptions für nicht-Memoria Exceptions
    catch (MemoriaException e) {
      throw e;
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
    if (info.getVersion() != revision) throw new MemoriaException(id + ": DeletionMarker(" + revision + ") has lower revision then last objectData(" + info.getVersion() + ")");
  }

  /**
   * @param object
   *          null if deleteMarker was encountered
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
      for (IBindable bindable : fGenOneBindings) {
        bindable.bind(this);
      }
      fGenOneBindings.clear();
      
      for (IBindable bindable : fGenTwoBindings) {
        bindable.bind(this);
      }
      fGenTwoBindings.clear();
    }
    catch (Exception e) {
      throw new MemoriaException(e);
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

    if (info.isDeleted()) {
      fRepo.handleDelete(objectInfo);

      // if the info is a deletion-marker and no older generations exist, the deletionMarker is inactive
      if (info.getOldGenerationCount() == 0) {
        info.getCurrentBlock().incrementInactiveObjectDataCount();

      }
    }
    else {
      fRepo.handleAdd(objectInfo);
    }
  }

  private void dehydrateObjects() throws Exception {
    // FIXME über das entry-set iterieren und sofot entfernen, damit Speicher freigegeben wird.
    for (HydratedInfo info : fHydratedObjects.values()) {
      dehydrateObject(info);
    }
    fHydratedObjects = null;
  }

  private long readBlockData() throws IOException {
    return fFileReader.readBlocks(fRepo.getIdFactory(), fCompressor, new IFileReaderHandler() {

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
        addDeletionMarker(fHydratedMetaClasses, id, fRepo.getIdFactory().getMemoriaClassDeletionMarker(), version);
      }

      @Override
      public void object(HydratedObject object, IObjectId id, long version, int size) {
        addHydratedObject(fHydratedObjects, object, id, version);
      }

      @Override
      public void objectDeleted(IObjectId id, long version) {
        addDeletionMarker(fHydratedObjects, id, fRepo.getIdFactory().getObjectDeletionMarker(), version);
      }

    });
  }

}
