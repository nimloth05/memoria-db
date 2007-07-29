package org.memoriadb.core;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

import org.memoriadb.exception.MemoriaException;


public final class FileReader implements IReaderContext {
  
  private final File fFile;
  private final Set<HydratedObject> fHydratedObjects = new HashSet<HydratedObject>();
  private final Set<ObjectReference> fObjectsToBind = new HashSet<ObjectReference>();
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
  public void objectToBind(Object object, Field field, long targetId) {
    fObjectsToBind.add(new ObjectReference(object, field, targetId));
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

  private void bindObjects() throws Exception {
    for(ObjectReference ref: fObjectsToBind) {
      ref.bind(this);
    }
    fObjectsToBind.clear();
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
    String className = stream.readUTF();
    MetaClass classObject = new MetaClass(className);
    readMetaFields(stream, classObject);
    fRepo.put(objectId, classObject);
  }

  
  private void readMetaFields(DataInputStream stream, MetaClass classObject) throws Exception {
    while (stream.available() > 0) {
      int fieldId = stream.readInt();
      String name = stream.readUTF();
      int type = stream.readInt();
      MetaField metaField = new MetaField(fieldId, name, type);
      classObject.addMetaField(metaField);
    }
  }
  
  private void readObject(byte[] data, int offset, int size) throws Exception {
    DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data, offset, size));

    long typeId = stream.readLong();
    long objectId = stream.readLong();

    if(typeId == MetaClass.METACLASS_OBJECT_ID) {
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
    byte[] fourByteBuffer = new byte[4];
    
    System.arraycopy(data, 0, fourByteBuffer, 0, 4);
    if (!Arrays.equals(fourByteBuffer, HeaderUtil.TRANSACTION_START_TAG));
    
    System.arraycopy(data, 4, fourByteBuffer, 0, 4);
    int transactionSize = Util.convertToInt(fourByteBuffer);
    
    System.arraycopy(data, transactionSize+8, fourByteBuffer, 0, 4);
    if (!Arrays.equals(fourByteBuffer, HeaderUtil.TRANSACTION_END_TAG)) throw new RuntimeException("could not read end transaction tag");
    
    readObjects(data, 8, transactionSize);
  }
}
