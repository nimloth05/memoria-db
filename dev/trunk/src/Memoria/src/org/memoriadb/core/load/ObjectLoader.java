package org.memoriadb.core.load;

import java.io.IOException;
import java.util.*;

import org.memoriadb.core.*;
import org.memoriadb.core.block.Block;
import org.memoriadb.core.file.*;
import org.memoriadb.exception.MemoriaException;

public final class ObjectLoader implements IReaderContext {

  private Map<Long, HydratedInfo> fHydratedObjects = new HashMap<Long, HydratedInfo>();
  private Map<Long, HydratedInfo> fHydratedMetaClasses = new HashMap<Long, HydratedInfo>();

  private final Set<IBindable> fObjectsToBind = new LinkedHashSet<IBindable>();

  private ObjectRepo fRepo;
  private FileWriter fFileWriter;
  
  private final FileReader fFileReader;

  public static void readIn(IMemoriaFile file, ObjectRepo repo) {
    new ObjectLoader(file).read(repo);
  }

  private ObjectLoader(IMemoriaFile file) {
    if (file == null) throw new IllegalArgumentException("File was null");
    fFileReader = new FileReader(file);
  }

  @Override
  public Object getObjectById(long objectId) {
    return fRepo.getObject(objectId);
  }

  @Override
  public void objectToBind(IBindable bindable) {
    fObjectsToBind.add(bindable);
  }

  public void read(ObjectRepo repo) {
    fRepo = repo;
    try {
      // must be the first call to the fFileReader
      readHeader();
      
      readBlockData();
      dehydrateMetaClasses();
      bindObjects();
      dehydrateObjects();
      bindObjects();
    }
    catch (Exception e) {
      throw new MemoriaException(e);
    }
  }

  /**
   * @param object null if deleteMarker was encountered
   */
  private void addHydratedObject(Map<Long, HydratedInfo> container, HydratedObject object, long id, long version) {
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
    ObjectInfo objectInfo = new ObjectInfo(info.getObjectId(), info.getObject(this), info.getVersion(), info.getOldGenerationCount());
    
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
  
  private void readBlockData() throws IOException {
    fFileReader.readBlocks(new IFileReaderHandler() {

      @Override
      public void block(Block block) {
      }

      @Override
      public void memoriaClass(HydratedObject metaClass, long id, long version) {
        addHydratedObject(fHydratedMetaClasses, metaClass, id, version);
      }

      @Override
      public void memoriaClassDeleted(long id, long version) {
        addHydratedObject(fHydratedMetaClasses, null, id, version);
      }

      @Override
      public void object(HydratedObject object, long id, long version) {
        addHydratedObject(fHydratedObjects, object, id, version);
      }

      @Override
      public void objectDeleted(long id, long version) {
        addHydratedObject(fHydratedObjects, null, id, version);
      }

    });

  }

  private FileHeader readHeader() throws IOException {
    return fFileReader.readHeader();
  }

}
