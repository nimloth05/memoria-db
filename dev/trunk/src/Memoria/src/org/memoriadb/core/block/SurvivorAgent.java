package org.memoriadb.core.block;

import java.io.*;
import java.util.*;

import org.memoriadb.block.Block;
import org.memoriadb.core.*;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.*;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.HydratedObject;
import org.memoriadb.core.util.IOUtil;

/**
 * Computes the survivors in a block.
 * 
 * @author msc
 */
public class SurvivorAgent implements IFileReaderHandler  {
  
  private Set<ObjectInfo> fSurvivors;
  private final IObjectRepository fRepo;
  private final IMemoriaFile fFile;
  
  public SurvivorAgent(IObjectRepository repo, IMemoriaFile file) {
    fFile = file;
    fRepo = repo;
  }
   
  @Override
  public void block(Block block) {
  }

  public Set<ObjectInfo> getSurvivors(Block block) {
    fSurvivors = new HashSet<ObjectInfo>();
    DataInputStream stream = new DataInputStream(fFile.getInputStream(block.getPosition()));
    BlockReader reader = new BlockReader();
    
    try {
      reader.readBlock(stream, new Block(1), fRepo.getIdFactory(), this);
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
    finally {
      IOUtil.close(stream);
    }
    
    return fSurvivors;
  }

  @Override
  public void memoriaClass(HydratedObject metaClass, IObjectId id, long revision, int size) {
    handleObject(id, revision);
  }

  @Override
  public void memoriaClassDeleted(IObjectId id, long revision) {
    handleObject(id, revision);
  }

  @Override
  public void object(HydratedObject object, IObjectId id, long revision, int size) {
    handleObject(id, revision);
  }

  @Override
  public void objectDeleted(IObjectId id, long revision) {
    handleObject(id, revision);
  }
  
  private void handleObject(IObjectId id, long revision) {
    ObjectInfo info = fRepo.getObjectInfoForId(id);
    long revisionFromObjectRepo = info.getRevision();
    if(revision > revisionFromObjectRepo) throw new MemoriaException("ObjectRepo has wrong revision " + revisionFromObjectRepo + " expected " + revision);
    
    if(revisionFromObjectRepo == revision) fSurvivors.add(info);
  }
  
}
