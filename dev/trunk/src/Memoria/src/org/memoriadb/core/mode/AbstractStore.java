package org.memoriadb.core.mode;

import org.memoriadb.*;
import org.memoriadb.block.*;
import org.memoriadb.core.*;
import org.memoriadb.core.block.SurvivorAgent;
import org.memoriadb.core.file.*;
import org.memoriadb.id.*;

/**
 * Abstract base class for a IStore implementation. 
 * This class has some additional extended functionality for test-purposes.
 * <b>It may be possible to corrupt the db when misusing the power of this functionality</b>.
 * 
 * @author sandro
 */
public abstract class AbstractStore implements IStore {
  
  protected final TransactionHandler fTransactionHandler;

  public AbstractStore(TransactionHandler handler) {
    fTransactionHandler = handler;
  }

  @Override
  public final void beginUpdate() {
    fTransactionHandler.beginUpdate();
  }

  public final void checkIndexConsistancy() {
    fTransactionHandler.checkIndexConsistancy();
  }

  @Override
  public final void close() {
    fTransactionHandler.close();
  }

  @Override
  public final boolean containsId(IObjectId id) {
    return fTransactionHandler.containsId(id);
  }

  @Override
  public final void endUpdate() {
    fTransactionHandler.endUpdate();
  }

  public final IBlockManager getBlockManager() {
    return fTransactionHandler.getBlockManager();
  }
  
  public final IMemoriaFile getFile() {
    return fTransactionHandler.getFile();
  }

  public final Header getHeader() {
    return fTransactionHandler.getHeader();
  }
  
  @Override
  public final long getHeadRevision() {
    return fTransactionHandler.getHeadRevision();
  }
  
  public final IIdProvider getIdFactory() {
    return fTransactionHandler.getDefaultIdProvider();
  }
  
  public final int getIdSize() {
    return fTransactionHandler.getIdSize();
  }
  
  /**
   * @return The stored ObjectInfo for the given object or null, if the given obj is unknown or deleted.
   */
  public final IObjectInfo getObjectInfo(Object obj) {
    return fTransactionHandler.getObjectInfo(obj);
  }
  
  /**
   * @return The stored ObjectInfo for the given id or null, if the given id is unknown. This method may work
   * even for deleted objects, if the delete-marker is still present.
   */
  public final IObjectInfo getObjectInfoForId(IObjectId id) {
    return fTransactionHandler.getObjectInfoForId(id);
  }
  
  public ObjectInformation getObjectInformation(Object object) {
    ObjectInfo objectInfo = fTransactionHandler.getObjectInfo(object);
    return new ObjectInformation(objectInfo.getId(), objectInfo.getMemoriaClassId(), objectInfo.getRevision());
  }

  public final SurvivorAgent getSurvivorAgent(Block block) {
    return fTransactionHandler.getSurvivorAgent(block);
  }
  
  @Override
  public final ITypeInfo getTypeInfo() {
    return fTransactionHandler.getTypeInfo();
  }
  
  @Override
  public final boolean isInUpdateMode() {
    return fTransactionHandler.isInUpdateMode();
  }


}
