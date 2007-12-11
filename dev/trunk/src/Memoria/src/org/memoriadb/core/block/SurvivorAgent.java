package org.memoriadb.core.block;

import java.io.*;
import java.util.Set;

import org.memoriadb.block.Block;
import org.memoriadb.core.*;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.*;
import org.memoriadb.core.load.HydratedObject;
import org.memoriadb.core.util.collection.CompoundIterator;
import org.memoriadb.core.util.collection.identity.IdentityHashSet;
import org.memoriadb.core.util.io.IOUtil;
import org.memoriadb.id.IObjectId;

/**
 * Computes the survivors in a block.
 * 
 * @author msc
 */
public class SurvivorAgent implements IFileReaderHandler  {
  
  // use IdentityHashSet for better performance
  private final Set<ObjectInfo> fUpdates = IdentityHashSet.create();
  private final Set<ObjectInfo> fDeleteMarkers =  IdentityHashSet.create();
  private final Set<ObjectInfo> fInactiveObjectDatas = IdentityHashSet.create();
  private final Set<ObjectInfo> fInactiveDeletinoMarkers = IdentityHashSet.create();
  
  private final IObjectRepository fRepo;
  private final IMemoriaFile fFile;
  
  public SurvivorAgent(IObjectRepository repo, IMemoriaFile file, Block block) {
    fFile = file;
    fRepo = repo;
    computeSurvivors(block);
  }
   
  @Override
  public void block(Block block) {
  }

  public void computeSurvivors(Block block) {
    
    DataInputStream stream = new DataInputStream(fFile.getInputStream(block.getPosition()));
    BlockReader reader = new BlockReader();
    
    try {
      reader.readBlock(stream, new Block(1), fRepo.getIdFactory(), this, new AlwaysThrowErrorHandler());
    }
    catch (IOException e) { 
      throw new MemoriaException(e);
    }
    finally {
      IOUtil.close(stream);
    }
    
    if(block.getObjectDataCount() != getObjectDataCount()) throw new MemoriaException("objectDataCount mismatch. File: " + getObjectDataCount() + " in memory: " + block.getObjectDataCount());
    if(block.getInactiveObjectDataCount() != getInactiveObjectDataCount()) throw new MemoriaException("inactiveObjectDataCount mismatch. File: " + getInactiveObjectDataCount() + " in memory: " + block.getInactiveObjectDataCount());
  }

  public Iterable<ObjectInfo> getAllObjectInfo() {
    return new CompoundIterator<ObjectInfo>(fDeleteMarkers, fInactiveDeletinoMarkers, fInactiveObjectDatas, fUpdates);
  }

  public Set<ObjectInfo> getDeleteMarkers() {
    return fDeleteMarkers;
  }
  
  public Set<ObjectInfo> getInactiveObjectDatas() {
    return fInactiveObjectDatas;
  }

  public Set<ObjectInfo> getUpdates() {
    return fUpdates;
  }

  public boolean hasNoSurvivors() {
    return fUpdates.isEmpty() && fDeleteMarkers.isEmpty();
  }

  @Override
  public void memoriaClass(HydratedObject metaClass, IObjectId id, long revision, int size) {
    handleUpdate(id, revision);
  }
  
  @Override
  public void memoriaClassDeleted(IObjectId id, long revision) {
    handleDelete(id, revision);
  }

  @Override
  public void object(HydratedObject object, IObjectId id, long revision, int size) {
    handleUpdate(id, revision);
  }

  @Override
  public void objectDeleted(IObjectId id, long revision) {
    handleDelete(id, revision);
  }

  private int getInactiveObjectDataCount() {
    return fInactiveDeletinoMarkers.size() + fInactiveObjectDatas.size();
  }

  private int getObjectDataCount() {
    return fDeleteMarkers.size() + fInactiveDeletinoMarkers.size() + fInactiveObjectDatas.size() + fUpdates.size();
  }
  
  private void handleDelete(IObjectId id, long revision) {
    ObjectInfo info = fRepo.getObjectInfoForId(id);
    long revisionFromObjectRepo = info.getRevision();
    if(revision != revisionFromObjectRepo) throw new MemoriaException("Wrong revision for DeletionMarker in repo: " + revisionFromObjectRepo + " expected " + revision);
    
    // check if deletionMarker must be saved.
    if(info.getOldGenerationCount() == 0) {
      // the DeletionMarker is not used anymore...
      fInactiveDeletinoMarkers.add(info);
    }
    else {
      fDeleteMarkers.add(info);
    }
  }

  private void handleUpdate(IObjectId id, long revision) {
    ObjectInfo info = fRepo.getObjectInfoForId(id);
    long revisionFromObjectRepo = info.getRevision();
    if(revision > revisionFromObjectRepo) throw new MemoriaException("Wrong revision for Object in repo: " + revisionFromObjectRepo + " expected " + revision);
    
    if(revisionFromObjectRepo == revision){
      fUpdates.add(info);
    }
    else {
      // return this object to adjust the oldGenerationCount
      fInactiveObjectDatas.add(info);
    }
  }
  
  
  
}
