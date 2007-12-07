package org.memoriadb.core.block;

import java.io.*;
import java.util.Set;

import org.memoriadb.block.Block;
import org.memoriadb.core.*;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.*;
import org.memoriadb.core.load.HydratedObject;
import org.memoriadb.core.util.IdentityHashSet;
import org.memoriadb.core.util.io.IOUtil;
import org.memoriadb.id.IObjectId;

/**
 * Computes the survivors in a block.
 * 
 * @author msc
 */
public class SurvivorAgent implements IFileReaderHandler  {
  
  // use IdentityHashSet for better performance
  private final Set<ObjectInfo> fUpdates = new IdentityHashSet<ObjectInfo>();
  private final Set<ObjectInfo> fDeleteMarkers = new IdentityHashSet<ObjectInfo>();
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
  }

  public Set<ObjectInfo> getDeleteMarkers() {
    return fDeleteMarkers;
  }

  public Set<ObjectInfo> getUpdates() {
    return fUpdates;
  }

  public boolean hasNoSurvivors() {
    return fUpdates.isEmpty() && fDeleteMarkers.isEmpty();
  }

  @Override
  public void memoriaClass(HydratedObject metaClass, IObjectId id, long revision, int size) {
    handleUpdate(fUpdates, id, revision);
  }
  
  @Override
  public void memoriaClassDeleted(IObjectId id, long revision) {
    handleUpdate(fDeleteMarkers, id, revision);
  }

  @Override
  public void object(HydratedObject object, IObjectId id, long revision, int size) {
    handleUpdate(fUpdates, id, revision);
  }

  @Override
  public void objectDeleted(IObjectId id, long revision) {
    handleUpdate(fDeleteMarkers, id, revision);
  }

  private void handleUpdate(Set<ObjectInfo> survivors, IObjectId id, long revision) {
    ObjectInfo info = fRepo.getObjectInfoForId(id);
    long revisionFromObjectRepo = info.getRevision();
    if(revision > revisionFromObjectRepo) throw new MemoriaException("ObjectRepo has wrong revision " + revisionFromObjectRepo + " expected " + revision);
    
    if(revisionFromObjectRepo == revision) survivors.add(info);
  }
  
  
  
}
