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

package org.memoriadb.core.file.read;

import java.io.IOException;
import java.util.*;

import org.memoriadb.block.*;
import org.memoriadb.core.*;
import org.memoriadb.core.block.BlockRepository;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.ICompressor;
import org.memoriadb.core.mode.IModeStrategy;
import org.memoriadb.core.util.io.IDataInput;
import org.memoriadb.handler.IBindable;
import org.memoriadb.id.*;
import org.memoriadb.instantiator.IInstantiator;

public final class ObjectLoader implements IReaderContext {

  private Map<IObjectId, HydratedInfo> fHydratedObjects = new HashMap<IObjectId, HydratedInfo>();
  private Map<IObjectId, HydratedInfo> fHydratedMetaClasses = new HashMap<IObjectId, HydratedInfo>();

  private final List<IBindable> fGenOneBindings = new ArrayList<IBindable>();
  private final List<IBindable> fGenTwoBindings = new ArrayList<IBindable>();

  private final ObjectRepository fRepo;
  private final FileReader fFileReader;
  private final IBlockManager fBlockManager;
  private Block fCurrentBlock;
  private final IInstantiator fInstantiator;
  private final IObjectIdFactory fIdFactory;
  private final IModeStrategy fModeStrategy;
  private final ICompressor fCompressor;
  private final BlockRepository fBlockRepository;

  public static long readIn(FileReader fileReader, ObjectRepository repo, BlockRepository blockRepository, IBlockManager blockManager, IInstantiator instantiator, IModeStrategy store, ICompressor compressor) {
    return new ObjectLoader(fileReader, repo, blockRepository, blockManager, instantiator, store, compressor).read();
  }

  public ObjectLoader(FileReader fileReader, ObjectRepository repo, BlockRepository blockRepository, IBlockManager blockManager, IInstantiator instantiator, IModeStrategy mode, ICompressor compressor) {
    if (instantiator == null) throw new IllegalArgumentException("defaultInstantiator is null");
    if (fileReader == null) throw new IllegalArgumentException("fileReader is null");
    if (repo == null) throw new IllegalArgumentException("repo is null");
    if (blockRepository == null) throw new IllegalArgumentException("blockRepository is null");
    if (blockManager == null) throw new IllegalArgumentException("BlockManager is null");
    if (mode == null) throw new IllegalArgumentException("mode is null");
    if (compressor == null) throw new IllegalArgumentException("compressor is null");

    fModeStrategy = mode;
    fFileReader = fileReader;
    fRepo = repo;
    fBlockRepository = blockRepository;
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
    return fModeStrategy.isDataMode();
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
    catch (MemoriaException e) {
      throw e;
    }
    catch (Exception e) {
      throw new MemoriaException(e);
    }
  }

  @Override
  public IObjectId readObjectId(IDataInput input) throws IOException {
    return fRepo.getIdFactory().createFrom(input);
  }

  private void addDeletionMarker(Map<IObjectId, HydratedInfo> container, IObjectId id, IObjectId deletionTypeId) {
    fIdFactory.adjustId(id);

    HydratedInfo info = container.get(id);
    if (info == null) {
      container.put(id, new HydratedInfo(id, deletionTypeId, null, fCurrentBlock));
      return;
    }
    
    // object already loaded in other version, newer version survives
    info.update(fCurrentBlock, null, deletionTypeId);
  }

  /**
   * @param container
   * @param object
   *          null if deleteMarker was encountered
   * @param id
   */
  private void addHydratedObject(Map<IObjectId, HydratedInfo> container, HydratedObject object, IObjectId id) {
    fIdFactory.adjustId(id);

    HydratedInfo info = container.get(id);
    if (info == null) {
      container.put(id, new HydratedInfo(id, object.getTypeId(), object, fCurrentBlock));
      return;
    }
    
    // object already loaded in other version, newer version survives
    info.update(fCurrentBlock, object, object.getTypeId());
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
    ObjectInfo objectInfo = new ObjectInfo(info.getObjectId(), info.getMemoriaClassId(), info.getObject(this), info.getOldGenerationCount());
    
    fBlockRepository.add(info.getObjectId(), info.getCurrentBlock());
    
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
    Iterator<HydratedInfo> iterator = fHydratedObjects.values().iterator();
    while(iterator.hasNext()) {
      dehydrateObject(iterator.next());
      iterator.remove();
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
      public void memoriaClass(HydratedObject metaClass, IObjectId id, int size) {
        addHydratedObject(fHydratedMetaClasses, metaClass, id);
      }

      @Override
      public void memoriaClassDeleted(IObjectId id) {
        addDeletionMarker(fHydratedMetaClasses, id, fRepo.getIdFactory().getMemoriaClassDeletionMarker());
      }

      @Override
      public void object(HydratedObject object, IObjectId id, int size) {
        addHydratedObject(fHydratedObjects, object, id);
      }

      @Override
      public void objectDeleted(IObjectId id) {
        addDeletionMarker(fHydratedObjects, id, fRepo.getIdFactory().getObjectDeletionMarker());
      }

    });
  }

}
