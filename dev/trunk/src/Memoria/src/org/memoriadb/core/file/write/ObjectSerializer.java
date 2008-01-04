package org.memoriadb.core.file.write;

import java.io.IOException;

import org.memoriadb.core.*;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.meta.*;
import org.memoriadb.core.mode.IModeStrategy;
import org.memoriadb.core.util.io.MemoriaDataOutputStream;
import org.memoriadb.handler.IHandler;
import org.memoriadb.id.IObjectId;

/**
 * Call serialize(Object) for each object, then call getBytes(). After this, the ObjectSerializer is gone.
 * 
 * @author msc
 * 
 */
public final class ObjectSerializer {

  private class SerializeContext implements IWriterContext {

    @Override
    public boolean contains(Object obj) {
      return fObjectRepository.contains(obj);
    }

    @Override
    public IObjectId getExistingtId(Object obj) {
      return fObjectRepository.getExistingId(obj);
    }

    @Override
    public IMemoriaClass getMemoriaClass(IObjectId id) {
      return (IMemoriaClass) fObjectRepository.getObject(id);
    }

    @Override
    public IMemoriaClass getMemoriaClass(Object object) {
      return fMode.getMemoriaClass(object, fObjectRepository);
    }

    @Override
    public IObjectId getMemoriaClassId(String javaClassName) {
      IMemoriaClass memoriaClass = fObjectRepository.getMemoriaClass(javaClassName);
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
    private final MemoriaDataOutputStream fStream;
    private final IModeStrategy fMode;

  /**
   * @param repo
   * @param mode 
   */
  public ObjectSerializer(IObjectRepository repo, IModeStrategy mode, MemoriaDataOutputStream stream) {
    fObjectRepository = repo;
    fMode = mode;
    fStream = stream;
  }

  public void markAsDeleted(IObjectInfo info) throws IOException {
    fStream.writeInt(2 * fObjectRepository.getIdFactory().getIdSize());
    IObjectId typeId = fObjectRepository.isMemoriaClass(info.getObject()) ? 
        fObjectRepository.getIdFactory().getMemoriaClassDeletionMarker() : 
        fObjectRepository.getIdFactory().getObjectDeletionMarker();
    typeId.writeTo(fStream);
    info.getId().writeTo(fStream);
  }

  public void serialize(IObjectInfo info) throws Exception {
    serializeObject(info);
  }

  private void serializeObject(IHandler handler, IObjectInfo info) throws Exception {
    IObjectId typeId = info.getMemoriaClassId();

    fStream.setMarker();
    int current = fStream.size();

    typeId.writeTo(fStream);
    info.getId().writeTo(fStream);

    handler.serialize(info.getObject(), fStream, new SerializeContext());

    fStream.writeAtMarker(fStream.size() - current);
  }

  private void serializeObject(IObjectInfo info) throws Exception {
    IMemoriaClass memoriaClass = (IMemoriaClass) fObjectRepository.getExistingObject(info.getMemoriaClassId());
    serializeObject(memoriaClass.getHandler(), info);
  }

}
