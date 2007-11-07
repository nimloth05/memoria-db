package org.memoriadb.core.id;

public interface IDefaultObjectIdProvider {

  public IObjectId getArrayMemoriaClass();

  public IObjectId getHandlerMetaClass();

  public IObjectId getMemoriaClassDeletionMarker();

  /**
   * ObjectID for the Meta-MetaClass for the field based MetaClass.
   */
  public IObjectId getMemoriaMetaClass();

  public IObjectId getObjectDeletionMarker();
  
  public IObjectId getRootClassId();
  
  public boolean isMemoriaClassDeletionMarker(IObjectId typeId);
  
  public boolean isMemoriaClass(IObjectId typeId);

  public boolean isObjectDeletionMarker(IObjectId typeId);
  
  public boolean isRootClassId(IObjectId superClassId);

}