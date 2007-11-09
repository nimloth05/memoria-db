package org.memoriadb.core.block;

import java.io.*;
import java.util.*;

import org.memoriadb.core.IObjectRepo;
import org.memoriadb.core.file.*;
import org.memoriadb.core.id.*;
import org.memoriadb.core.load.HydratedObject;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.IOUtil;

/**
 * Computes the survivors in a block.
 * 
 * @author msc
 */
public class SurvivorAgent implements IFileReaderHandler  {
  
  private Set<IObjectId> fSurvivors;
  private final IObjectRepo fRepo;
  private final IMemoriaFile fFile;
  private final IObjectIdFactory fIdFactory;
  
  public SurvivorAgent(IObjectRepo repo, IMemoriaFile file, IObjectIdFactory idFactory) {
    fRepo = repo;
    fFile = file;
    fIdFactory = idFactory;
  }
   
  @Override
  public void block(Block block) {
  }

  public Iterable<IObjectId> getSurvivors(Block block) {
    fSurvivors = new HashSet<IObjectId>();
    DataInputStream stream = new DataInputStream(fFile.getInputStream(block.getPosition()));
    BlockReader reader = new BlockReader();
    
    try {
      reader.readBlock(stream, new Block(1), fIdFactory, this);
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
    long revisionFromObjectRepo = fRepo.getObjectInfo(id).getRevision();
    if(revision > revisionFromObjectRepo) throw new MemoriaException("ObjectRepo has wrong revision " + revisionFromObjectRepo + " expected " + revision);
    
    if(revisionFromObjectRepo == revision) fSurvivors.add(id);
  }
  
}
