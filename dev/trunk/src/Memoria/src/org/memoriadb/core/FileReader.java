package org.memoriadb.core;

import java.io.*;
import java.util.*;

import org.memoriadb.core.backend.Block;
import org.memoriadb.core.binder.*;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.ByteUtil;


public final class FileReader implements IReaderContext {
  
  private final File fFile;
  private final Set<HydratedObject> fHydratedObjects = new HashSet<HydratedObject>();
  private final Set<IBindable> fObjectsToBind = new HashSet<IBindable>();
  private ObjectRepo fRepo;
  private final MemoriaFile fMemoriaFile;

  public static void readIn(File file, ObjectRepo repo) {
    new FileReader(file).read(repo);
  }
  
  private FileReader(File file) {
    if (file == null) throw new IllegalArgumentException("File for readIn was null");
    fFile = file;
    fMemoriaFile = new MemoriaFile();
  }
  
  @Override
  public Object getObjectById(long objectId) {
    return fRepo.getObjectById(objectId);
  }
  
  @Override
  public void objectToBind(IBindable bindable) {
    fObjectsToBind.add(bindable);
  }

  public void read(ObjectRepo repo) {
    if (!fFile.exists()) return;
    
    fRepo = repo;
    try {
      readBlockData();
      dehydrateObjects();
      bindObjects();
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new MemoriaException(e);
    }
  }

  private void bindObjects() {
    try {
      for(IBindable ref: fObjectsToBind) {
        ref.bind(this);
      }
      fObjectsToBind.clear();
    } catch (Exception e) {
      throw new BindingException(e);
    }
  }

  private void dehydrateObjects() throws Exception {
    for(HydratedObject object: fHydratedObjects) {
      fRepo.put(object.getObjectId(), object.dehydrate(this));
    }
    fHydratedObjects.clear();
  }

  private void readBlockData() throws Exception {
    int bufferSize = (int)Runtime.getRuntime().freeMemory() / 16;
    FileInputStream fileStream = new FileInputStream(fFile);
    DataInputStream stream = new DataInputStream(new BufferedInputStream(fileStream, bufferSize));

    while (stream.available() > 0) {
      long startPosition = fileStream.getChannel().position();
      
      HeaderUtil.assertTag(stream, HeaderUtil.BLOCK_START_TAG);
      
      int blockSize = stream.readInt(); //the block size
      byte[] blockData = new byte[blockSize];
      stream.read(blockData); 
      readTransactionData(blockData);

      HeaderUtil.assertTag(stream, HeaderUtil.BLOCK_END_TAG);
      
      fMemoriaFile.add(new Block(fileStream.getChannel().position()-startPosition, startPosition));
    }
    
    stream.close();
  }
  
  private void readMetaClass(DataInputStream stream, long objectId) throws Exception {
    HandlerMetaClass metaClassObject = (HandlerMetaClass) fRepo.getObjectById(IMetaClass.METACLASS_OBJECT_ID);
    MetaClass classObject = (MetaClass) metaClassObject.getHandler().desrialize(stream, this);
    fRepo.put(objectId, classObject);
  }

  private void readObject(byte[] data, int offset, int size) throws Exception {
    DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data, offset, size));

    long typeId = stream.readLong();
    long objectId = stream.readLong();

    if(MetaClass.isMetaClassObject(typeId)) {
      readMetaClass(stream, objectId);
      return;
    }
    fHydratedObjects.add(new HydratedObject(typeId, objectId, stream));
  }

  private void readObjects(byte[] data, int offset, int length) throws Exception {
    DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data, offset, length));
    while(stream.available() > 0) {
      int size = stream.readInt();
      
      readObject(data, offset+4, size);
      offset += 4 + size;
      if (stream.skip(size) != size) throw new RuntimeException("could not skip bytes: " + size);
    }
  }
  
  private void readTransactionData(byte[] data) throws Exception {
    HeaderUtil.assertTag(data, 0, HeaderUtil.TRANSACTION_START_TAG);
    int transactionSize = ByteUtil.readInt(data, 4);
    final int endTagStart = transactionSize+ByteUtil.INT_SIZE+HeaderUtil.HEADER_SIZE;
    HeaderUtil.assertTag(data, endTagStart, HeaderUtil.TRANSACTION_END_TAG);
    
    readObjects(data, 8, transactionSize);
  }
}
