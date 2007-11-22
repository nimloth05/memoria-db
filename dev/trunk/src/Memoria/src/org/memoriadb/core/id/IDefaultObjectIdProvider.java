package org.memoriadb.core.id;

public interface IDefaultObjectIdProvider {

  public IObjectId getArrayMemoriaClass();

  public IObjectId getHandlerMetaClass();

  public IObjectId getMemoriaClassDeletionMarker();
  
  /**
   * ObjectID for the Meta-MetaClass for the field based MetaClass.
   */
  public IObjectId getMemoriaMetaClass();

  public IObjectId getNullReference();
  
  public IObjectId getObjectDeletionMarker();
  
  /**
   * @return Id of primitives (String, Integer, int, etc.). No MemoriaClass is created, 
   * the returned id is never contained in the store.
   */
  public IObjectId getPrimitiveClassId();
  
  public IObjectId getRootClassId();
  
  public boolean isMemoriaClassDeletionMarker(IObjectId typeId);

  public boolean isMemoriaFieldClass(IObjectId typeId);
  
  public boolean isMemoriaHandlerClass(IObjectId typeId);
  
  public boolean isNullReference(IObjectId objectId);
  
  public boolean isObjectDeletionMarker(IObjectId typeId);
  
  public boolean isRootClassId(IObjectId superClassId);

}