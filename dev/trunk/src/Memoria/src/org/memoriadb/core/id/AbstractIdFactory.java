package org.memoriadb.core.id;


public abstract class AbstractIdFactory implements IObjectIdFactory {

  @Override
  public boolean isMemoriaClassDeletionMarker(IObjectId typeId) {
    return getMemoriaClassDeletionMarker().equals(typeId);
  }
  
  @Override
  public boolean isMemoriaFieldClass(IObjectId typeId) {
    return getMemoriaMetaClass().equals(typeId);
  }

  public boolean isMemoriaHandlerClass(IObjectId typeId) {
    return getHandlerMetaClass().equals(typeId);
  }

  @Override
  public boolean isNullReference(IObjectId objectId) {
    return getNullReference().equals(objectId);
  }

  @Override
  public boolean isObjectDeletionMarker(IObjectId typeId) {
    return getObjectDeletionMarker().equals(typeId);
  }

  @Override
  public boolean isRootClassId(IObjectId superClassId) {
    return getRootClassId().equals(superClassId);
  }

}
