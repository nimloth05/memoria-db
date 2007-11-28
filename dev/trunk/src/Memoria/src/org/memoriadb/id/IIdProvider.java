package org.memoriadb.id;


public interface IIdProvider {

  public IObjectId fromString(String string);

  public IObjectId getArrayMemoriaClass();
  
  /**
   * @return ObjectID of the MetaClass for field memoria classes.
   */
  public IObjectId getFieldMetaClass();

  /**
   * @return ObjectID of the MetaClass for handler memoria classes.
   */
  public IObjectId getHandlerMetaClass();
  
  /**
   * @return The number of bytes a IObjectId from thsi factory requires. The size must be equal for all ids.
   */
  public int getIdSize();
  
  public IObjectId getMemoriaClassDeletionMarker();
  
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