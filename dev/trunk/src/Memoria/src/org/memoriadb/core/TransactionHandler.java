
package org.memoriadb.core;

import java.util.*;

import org.memoriadb.block.*;
import org.memoriadb.core.block.SurvivorAgent;
import org.memoriadb.core.exception.*;
import org.memoriadb.core.file.*;
import org.memoriadb.core.meta.*;
import org.memoriadb.core.mode.IModeStrategy;
import org.memoriadb.core.util.*;
import org.memoriadb.id.*;
import org.memoriadb.instantiator.IInstantiator;

public class TransactionHandler {

   private final IObjectRepository fObjectRepository;

   private final ITransactionWriter fTransactionWriter;

   // use IdentityHashSets for better performance
   private final Set<ObjectInfo> fAdd = new IdentityHashSet<ObjectInfo>();
   private final Set<ObjectInfo> fUpdate = new IdentityHashSet<ObjectInfo>();
   private final Set<ObjectInfo> fDelete = new IdentityHashSet<ObjectInfo>();

   private int fUpdateCounter = 0;
   private final IInstantiator fInstantiator;
   private final Header fHeader;
   private final IModeStrategy fModeStrategy;

   public TransactionHandler(IInstantiator instantiator, ITransactionWriter writer, Header header, IModeStrategy modeStrategy) {
      fInstantiator = instantiator;
      fTransactionWriter = writer;
      fHeader = header;
      fModeStrategy = modeStrategy;
      fObjectRepository = writer.getRepo();
   }

   public IObjectId addMemoriaClass(Class<?> clazz) {
      Class<?> type = clazz;

      if (clazz.isArray()) {
         type = ReflectionUtil.getComponentTypeInfo(clazz).getJavaClass();
      }
      if (Type.isPrimitive(type)) throw new MemoriaException("primitive can not be added " + clazz);

      IObjectId result = TypeHierarchyBuilder.addMemoriaClassIfNecessary(this, clazz, fModeStrategy);

      if (!isInUpdateMode()) writePendingChanges();
      return result;
   }

   public IObjectId addMemoriaClassIfNecessary(Object obj) {
      return fModeStrategy.addMemoriaClassIfNecessary(this, obj);
   }

   public void beginUpdate() {
      ++fUpdateCounter;
   }

   public void checkIndexConsistancy() {
      fObjectRepository.checkIndexConsistancy();
   }

   public void close() {
      fTransactionWriter.close();
   }

   public boolean contains(Object obj) {
      return fObjectRepository.contains(obj);
   }

   public boolean containsId(IObjectId id) {
      return fObjectRepository.contains(id);
   }

   public void delete(Object obj) {
      internalDelete(obj);
      if (!isInUpdateMode()) writePendingChanges();
   }

   public void deleteAll(Object root) {
      internalDeleteAll(root);
      if (!isInUpdateMode()) writePendingChanges();
   }

   public void endUpdate() {
      if (fUpdateCounter == 0) throw new MemoriaException("ObjectStore is not in update-mode");
      --fUpdateCounter;
      if (fUpdateCounter != 0) return;

      writePendingChanges();
   }

   public Collection<IObjectInfo> getAllObjectInfos() {
      return fObjectRepository.getAllObjectInfos();
   }

   public Iterable<Object> getAllObjects() {
      return fObjectRepository.getAllObjects();
   }

   public Iterable<Object> getAllUserSpaceObjects() {
      return fObjectRepository.getAllUserSpaceObjects();
   }

   public IObjectId getArrayMemoriaClassId() {
      return fObjectRepository.getIdFactory().getArrayMemoriaClass();
   }

   public IBlockManager getBlockManager() {
      return fTransactionWriter.getBlockManager();
   }

   public IIdProvider getDefaultIdProvider() {
      return fObjectRepository.getIdFactory();
   }

   public IObjectId getExistingId(Object obj) {
      return fObjectRepository.getExistingId(obj);
   }

   public IMemoriaFile getFile() {
      return fTransactionWriter.getFile();
   }

   public Header getHeader() {
      return fHeader;
   }

   public long getHeadRevision() {
      return fTransactionWriter.getHeadRevision();
   }

   public IObjectId getId(Object obj) {
      return fObjectRepository.getId(obj);
   }

   public int getIdSize() {
      return fObjectRepository.getIdFactory().getIdSize();
   }

   public IObjectId getMemoriaArrayClass() {
      return fObjectRepository.getIdFactory().getArrayMemoriaClass();
   }

   public IMemoriaClass getMemoriaClass(Object object) {
      IObjectId id = getMemoriaClassId(object);
      if (id == null) return null;
      return (IMemoriaClass) fObjectRepository.getObject(id);
   }

   public IMemoriaClass getMemoriaClass(String className) {
      return fObjectRepository.getMemoriaClass(className);
   }

   public IObjectId getMemoriaClassId(Object object) {
      ObjectInfo info = getObjectInfo(object);
      if (info == null) return null;
      return info.getMemoriaClassId();
   }

   /**
    * @return The Class for the given <tt>obj</tt> or null.
    */
   public IObjectId getMemoriaClassId(String className) {
      return fObjectRepository.getId(getMemoriaClass(className));
   }

   @SuppressWarnings("unchecked")
   public <T> T getObject(IObjectId id) {
      return (T) fObjectRepository.getObject(id);
   }

   public ObjectInfo getObjectInfo(Object obj) {
      return fObjectRepository.getObjectInfo(obj);
   }

   public IObjectInfo getObjectInfoForId(IObjectId id) {
      return fObjectRepository.getObjectInfoForId(id);
   }

   public IObjectRepository getObjectRepo() {
      return fObjectRepository;
   }

   public SurvivorAgent getSurvivorAgent(Block block) {
      return new SurvivorAgent(fObjectRepository, fTransactionWriter.getFile(), block);
   }

   public TypeInfo getTypeInfo() {
      return new TypeInfo(this);
   }

   public IObjectId internalAddObject(Object obj) {
      if (contains(obj)) throw new MemoriaException("obj already added: " + obj);

      IObjectId memoriaClassId = addMemoriaClassIfNecessary(obj);
      fModeStrategy.checkCanInstantiateObject(this, memoriaClassId, fInstantiator);
      ObjectInfo result = fObjectRepository.add(obj, memoriaClassId);
      fAdd.add(result);
      return result.getId();
   }

   public void internalDelete(Object obj) {
      ObjectInfo info = getObjectInfo(obj);
      if (info == null) {
         return;
      }

      if (fAdd.remove(info)) {
         // object was added in current transaction, remove it from add-list and from the repo
         fObjectRepository.delete(obj);
         return;
      } 

      // if object was previously updated in current transaction, remove it from update-list
      fUpdate.remove(info);

      fObjectRepository.delete(obj);
      fDelete.add(info);
   }

   public IMemoriaClassConfig internalGetMemoriaClass(String klass) {
      return fObjectRepository.getMemoriaClass(klass);
   }

   /**
    * Saves the obj without considering if this ObjectStore is in update-mode or not.
    */
   public IObjectId internalSave(Object obj) {
      fModeStrategy.checkObject(obj);

      ObjectInfo info = getObjectInfo(obj);

      if (info != null) return internalUpdateObject(obj, info);
      return internalAddObject(obj);
   }

   public boolean isEnum(Object obj) {
      return fModeStrategy.isEnum(obj);
   }

   public boolean isInUpdateMode() {
      return fUpdateCounter > 0;
   }

   public IObjectId save(Object obj) {
      if (isEnum(obj)) throw new SchemaException("It is not possible to add enum-instances. They are automatically saved when referenced.");

      SaveTraversal traversal = new SaveTraversal(this);
      traversal.handle(obj);

      if (!isInUpdateMode()) writePendingChanges();
      return fObjectRepository.getExistingId(obj);
   }

   public IObjectId saveAll(Object root) {
      SaveAllTraversal traversal = new SaveAllTraversal(this);
      traversal.handle(root);

      if (!isInUpdateMode()) writePendingChanges();
      return fObjectRepository.getExistingId(root);
   }

   public void writePendingChanges() {
      if (fAdd.isEmpty() && fUpdate.isEmpty() && fDelete.isEmpty()) return;

      try {
         fTransactionWriter.write(fAdd, fUpdate, fDelete);
      }
      catch (Exception e) {
         throw new MemoriaException(e);
      }

      fAdd.clear();
      fUpdate.clear();
      fDelete.clear();
   }

   private void internalDeleteAll(Object root) {
      if (!contains(root)) return;
      DeleteTraversal traversal = new DeleteTraversal(this);
      traversal.handle(root);
   }

   private IObjectId internalUpdateObject(Object obj, ObjectInfo info) {
      if (fAdd.contains(info)) {
         // added in same transaction, don't update it
         return fObjectRepository.getExistingId(obj);
      }

      // object already in the store, perform update. info is replaced if several updates occur in same transaction.
      fUpdate.add(info);
      return info.getId();
   }
}
