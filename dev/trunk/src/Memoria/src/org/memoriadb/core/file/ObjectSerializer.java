package org.memoriadb.core.file;

import java.io.IOException;

import org.memoriadb.core.*;
import org.memoriadb.core.meta.*;
import org.memoriadb.core.util.io.MemoriaObjectOutputStream;
import org.memoriadb.handler.IHandler;
import org.memoriadb.id.IObjectId;

/**
 * Call serialize(Object) for each object, then call getBytes(). After this, the ObjectSerializer is gone.
 * 
 * @author msc
 * 
 */
public final class ObjectSerializer {

  private class SerializeContext implements ISerializeContext {

    @Override
    public boolean contains(Object obj) {
      return fObjectRepository.contains(obj);
    }

    @Override
    public IObjectId getExistingtId(Object obj) {
      return fObjectRepository.getExistingId(obj);
    }

    @Override
    public IObjectId getMemoriaClassId(String javaClassName) {
      IMemoriaClassConfig memoriaClass = fObjectRepository.getMemoriaClass(javaClassName);
      return fObjectRepository.getId(memoriaClass);
    }

    @Override
    public IObjectId getNullReference() {
      return fObjectRepository.getIdFactory().getNullReference();
    }

    @Override
    public IObjectId getRootClassId() {
      return fObjectRepository.getIdFactory().getRootClassId();
    }
  }

  private final IObjectRepository fObjectRepository;
    private final MemoriaObjectOutputStream fStream;

  /**
   * @param repo
   */
  public ObjectSerializer(IObjectRepository repo) {
    fObjectRepository = repo;
    fStream = new MemoriaObjectOutputStream();
  }

  public byte[] getBytes() {
    return fStream.toByteArray();
  }

  public void markAsDeleted(IObjectInfo info) throws IOException {
    fStream.writeInt(2 * fObjectRepository.getIdFactory().getIdSize());
    IObjectId typeId = fObjectRepository.isMemoriaClass(info.getObject()) ? 
        fObjectRepository.getIdFactory().getMemoriaClassDeletionMarker() : 
        fObjectRepository.getIdFactory().getObjectDeletionMarker();
    typeId.writeTo(fStream);
    info.getId().writeTo(fStream);
  }

  public void serialize(ObjectInfo info) throws Exception {
    serializeObject(info);
  }

  private void serializeObject(IHandler handler, IObjectInfo info) throws Exception {
    IObjectId typeId = info.getMemoriaClassId();

    fStream.markObjectSizePosition();
    int current = fStream.size();

    typeId.writeTo(fStream);
    info.getId().writeTo(fStream);

    handler.serialize(info.getObject(), fStream, new SerializeContext());

    fStream.writeObjectSizeOnPosition(fStream.size() - current);
  }

  private void serializeObject(IObjectInfo info) throws Exception {
    IMemoriaClass memoriaClass = (IMemoriaClass) fObjectRepository.getExistingObject(info.getMemoriaClassId());
    serializeObject(memoriaClass.getHandler(), info);
  }

}
