package org.memoriadb.core;

import java.util.*;

import org.java.patched.PIdentityHashMap;
import org.memoriadb.block.Block;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.id.*;
import org.memoriadb.core.meta.*;

/**
 * Holds the main-indexes. 
 * 
 * Objects can be added with no regard to ongoing transactions. The appropriate indexes are updated.
 * 
 * @author msc
 *
 */
public class ObjectRepository implements IObjectRepository {

  /**
   * Holds all objectInfos for deleted objects. The reference to the Object in those ObjectInfos is always null. 
   */
  private final Map<IObjectId, ObjectInfo> fDeletedMap = new HashMap<IObjectId, ObjectInfo>();

  /**
   * Main-index
   */
  private final Map<IObjectId, ObjectInfo> fIdMap = new HashMap<IObjectId, ObjectInfo>();

  /**
   * Main-index
   */
  private final Map<Object, ObjectInfo> fObjectMap = new PIdentityHashMap<Object, ObjectInfo>();
  
  /**
   * MataClass index
   */
  private final Map<String, IMemoriaClassConfig> fMemoriaClasses = new HashMap<String, IMemoriaClassConfig>();
  
  private final IObjectIdFactory fIdFactory;
  

  public ObjectRepository(IObjectIdFactory idFactory) {
    fIdFactory = idFactory;
  }

  /**
   * This method is only used for bootstrapping
   */
  public void add(IObjectId id, IMemoriaClass object) {
    ObjectInfo result = new ObjectInfo(id, object.getMemoriaClassId(), object, Block.getDefaultBlock());
    internalPut(result);
  }

  /**
   * Adds a new object to the repo. A new ObjectInfo is created, with a new id and the version 0.
   * 
   * @return The new id
   */
  public ObjectInfo add(Object obj, IObjectId memoriaClassId) {
    IObjectId id = generateId();
    ObjectInfo result = new ObjectInfo(id, memoriaClassId, obj, Block.getDefaultBlock());
    internalPut(result);
    return result;
  }

  public void checkIndexConsistancy() {
    for (IObjectId id : fIdMap.keySet()) {
      Object object = fIdMap.get(id).getObject();

      IObjectId idInObjectMap = fObjectMap.get(object).getId();
      if (!id.equals(idInObjectMap)) throw new MemoriaException("diffrent IDs for object: id in id-Map " + id + " id in adress map "
          + idInObjectMap);
    }
  }

  public boolean contains(IObjectId id) {
    return fIdMap.containsKey(id);
  }

  public boolean contains(Object obj) {
    return fObjectMap.containsKey(obj);
  }
  
  @Override
  public ObjectInfo delete(Object obj) {
    ObjectInfo info = fObjectMap.remove(obj);
    if (info == null) throw new MemoriaException("object not found: " + obj);
    if (fIdMap.remove(info.getId()) == null) throw new MemoriaException("object not found: " + obj);
    fDeletedMap.put(info.getId(), info);
    info.setDeleted();
    return info;
  }

  @Override
  public Collection<IObjectInfo> getAllObjectInfos() {
    return Collections.<IObjectInfo>unmodifiableCollection(fObjectMap.values());
  }

  public Collection<Object> getAllObjects() {
    List<Object> result = new ArrayList<Object>(fObjectMap.size());
    for (IObjectInfo info : fObjectMap.values()) {
      result.add(info.getObject());
    }
    return result;
  }

  @Override
  public IObjectId getArrayMemoriaClass() {
    return fIdFactory.getArrayMemoriaClass();    
  }

  @Override
  public IObjectId getExistingId(Object obj) {
    // FIXME assertion
    return getId(obj);
  }

  public Object getExistingObject(IObjectId id) {
    // FIXME assertion
    return getObject(id);
  }

  @Override
  public IObjectId getFieldMetaClass() {
    return getIdFactory().getFieldMetaClass();
  }

  @Override
  public IObjectId getHandlerMetaClass() {
    return fIdFactory.getHandlerMetaClass();
  }
  

  /**
   * @param obj
   * @return
   * @throws MemoriaException
   *           if object can not be found
   */
  public IObjectId getId(Object obj) {
    IObjectInfo result = fObjectMap.get(obj);
    if(result == null) return null;
    //if (result == null) throw new MemoriaException("Unknown object '" + obj + "' -- using saveAll() instead of save() may solve the problem.");
    return result.getId();
  }

  @Override
  public IObjectIdFactory getIdFactory() {
    return fIdFactory;
  }

  /**
   * @return the metaObject for the given object or null, if the metaClass does not exists
   */
  public IMemoriaClassConfig getMemoriaClass(String klass) {
    return fMemoriaClasses.get(klass);
  }
  
  @Override
  public IObjectId getMemoriaClassDeletionMarker() {
    return fIdFactory.getMemoriaClassDeletionMarker();
  }

  @Override
  public IObjectId getNullReference() {
    return fIdFactory.getNullReference();
  }

  /**
   * 
   * @param objectId
   * @return the object for the given id or null.
   */
  public Object getObject(IObjectId objectId) {
    IObjectInfo objectInfo = fIdMap.get(objectId);
    if (objectInfo == null) return null;
    //if (objectInfo == null) throw new MemoriaException("No Object for ID: " + objectId);
    return objectInfo.getObject();
  }

  @Override
  public IObjectId getObjectDeletionMarker() {
    return fIdFactory.getObjectDeletionMarker();
  }

  public ObjectInfo getObjectInfo(Object obj) {
    return fObjectMap.get(obj);
  }

  public ObjectInfo getObjectInfoForId(IObjectId id) {
    ObjectInfo result = fIdMap.get(id);
    if(result == null) result = fDeletedMap.get(id);
    return result;
  }

  @Override
  public IObjectId getPrimitiveClassId() {
    return fIdFactory.getPrimitiveClassId();
  }

  @Override
  public IObjectId getRootClassId() {
    return fIdFactory.getRootClassId();
  }

  /**
   * Adds an object after dehydration
   */
  public void handleAdd(ObjectInfo objectInfo) {
    internalPut(objectInfo);
  }

  /**
   * marks an object as deleted after dehydrating the delete-marker
   * @param objectInfo
   */
  public void handleDelete(ObjectInfo objectInfo){
    fDeletedMap.put(objectInfo.getId(), objectInfo);
  }

  @Override
  public boolean isMemoriaClass(Object obj) {
    return obj instanceof IMemoriaClass;
  }

  @Override
  public boolean isMemoriaClassDeletionMarker(IObjectId typeId) {
    return fIdFactory.isMemoriaClassDeletionMarker(typeId);
  }

  @Override
  public boolean isMemoriaFieldClass(IObjectId typeId) {
    return fIdFactory.isMemoriaFieldClass(typeId);
  }
  
  @Override
  public boolean isMemoriaHandlerClass(IObjectId typeId) {
    return fIdFactory.isMemoriaHandlerClass(typeId);
  }

  public boolean isNullReference(IObjectId objectId) {
    return fIdFactory.isNullReference(objectId);
  }

  @Override
  public boolean isObjectDeletionMarker(IObjectId typeId) {
    return fIdFactory.isObjectDeletionMarker(typeId);
  }

  @Override
  public boolean isRootClassId(IObjectId superClassId) {
    return fIdFactory.isRootClassId(superClassId);
  }

  public void updateObjectInfoAdded(Object obj, long revision) {
    ObjectInfo info = fObjectMap.get(obj);
    info.setRevision(revision);
  }

  @Override
  public void updateObjectInfoDeleted(IObjectId id, long revision) {
    //FIXME: Prüfen, ob das Element wieder aus der Map entfernt werden muss/kann.
    ObjectInfo info = fDeletedMap.get(id);
    internalUpdate(info, revision);
  }

  public void updateObjectInfoUpdated(Object obj, long revision) {
    ObjectInfo info = fObjectMap.get(obj);
    internalUpdate(info, revision);
  }

  private IObjectId generateId() {
    return fIdFactory.createNextId();
  }

  // TODO: Test for this assertions! which assertsion? msc
  private void internalPut(ObjectInfo info) {
    // adjustId here for bootstrapped objects
    fIdFactory.adjustId(info.getId());
    
    Object previousMapped = fObjectMap.put(info.getObject(), info);
    if (previousMapped != null) throw new MemoriaException("double registration in object-map " + info);

    previousMapped = fIdMap.put(info.getId(), info);
    if (previousMapped != null) throw new MemoriaException("double registration in id-Map " + info);

    if (info.getObject() instanceof IMemoriaClass) {
      IMemoriaClassConfig metaObject = (IMemoriaClassConfig) info.getObject();
      previousMapped = fMemoriaClasses.put(metaObject.getJavaClassName(), metaObject);
      if (previousMapped != null) throw new MemoriaException("double registration of memoria class: " + metaObject);
    }
  }

  private void internalUpdate(ObjectInfo info, long revision) {
    if (info == null) throw new IllegalArgumentException("Object not found");
    info.setRevision(revision);
    info.incrememntOldGenerationCount();
  }

}
