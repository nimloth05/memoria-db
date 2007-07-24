package bootstrap.core;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class FileStore implements IContext {
  
  private static final byte[] BLOCK_START_TAG = new byte[] {1, 2,3, 4};
  private static final byte[] BLOCK_END_TAG = new byte[] {4, 3, 2, 1};
  
  private static final byte[] TRANSACTION_START_TAG = new byte[] {5, 6, 7, 8};
  private static final byte[] TRANSACTION_END_TAG = new byte[] {8, 7, 6, 5};
  
  private final MetaData fMetaData = new MetaData();
  private final ObjectRepo fObjectRepo = new ObjectRepo();
  
  private final File fFile;
  
  private final Set<HydratedObject> fHydratedObjects = new HashSet<HydratedObject>();
  private final List<ObjectReference> fObjectsToBind = new ArrayList<ObjectReference>();
  
  /**
   * Additional Objects that must be serialized to complete the aggregate.
   */
  private final List<Object> fObjectsToSerialize = new ArrayList<Object>();
  
  public FileStore(File file) {
    fFile = file;
    fMetaData.bootstrap(this);
  }
  
  public void checkSanity() {
    fObjectRepo.checkSanity();
  }

  @Override
  public boolean contains(Object obj) {
    return fObjectRepo.contains(obj);
  }

  public Collection<Object> getAllObjects() {
    return fObjectRepo.getAllObjects();
  }

  public Collection<MetaClass> getMetaClass() {
    return fObjectRepo.getMetaObejcts();
  }

  @Override
  public MetaClass getMetaObject(Class<?> javaType) {
    return fObjectRepo.getMetaObject(javaType);
  }

  @Override
  public Object getObejctById(long objectId) {
    return fObjectRepo.getObjectById(objectId);
  }

  @Override
  public long getObjectId(Object obj) {
    return fObjectRepo.getObjectId(obj);
  }

  @Override
  public void objectToBind(Object object, Field field, long targetId) {
    fObjectsToBind.add(new ObjectReference(object, field, targetId));
  }

  public void open() {
    try {
      readAllObjects();
      dehydrateObjects();
      bindObjects();
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public void put(long objectId, Object obj) {
    fObjectRepo.put(objectId, obj);
  }

  @Override
  public long register(Object object) {
    return fObjectRepo.register(object);
  }

  @Override
  public void serializeIfNotContained(Object referencee) throws Exception {
    fObjectsToSerialize.add(referencee);
  }

  public void serializeObject(DataOutput dataStream, Object object) throws Exception {
    Class<?> type = object.getClass();
    long objectId = fObjectRepo.register(object);
    
    MetaClass metaClass = fMetaData.register(this, dataStream, type);
    
    metaClass.writeObject(this, dataStream, object, objectId);
  }

  public void writeObject(List<Object> objects) {
    try {
      internalWriteObject(objects);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("exception occured during write back: " + e);
    }
  }

  private void append(byte[] data) throws IOException {
    RandomAccessFile file = new RandomAccessFile(fFile, "rw");
    
    long length = file.length();
    if (length > 0) {
      file.seek(file.length());
    }
    
    file.write(BLOCK_START_TAG);
    int size = data.length+TRANSACTION_START_TAG.length+4+TRANSACTION_END_TAG.length; //the block is as big as the transaction data.
    file.writeInt(size); 
    file.write(TRANSACTION_START_TAG);
    file.writeInt(data.length);
    
    file.write(data);
    
    file.write(TRANSACTION_END_TAG);
    file.write(BLOCK_END_TAG);
    
    file.getFD().sync();
    file.close();
  }

  private void bindObjects() throws Exception {
    for(ObjectReference ref: fObjectsToBind) {
      ref.bind(this);
    }
    fObjectsToBind.clear();
  }

  private void dehydrateObjects() throws Exception {
    for(HydratedObject object: fHydratedObjects) {
      object.dehydrate(this);
    }
    fHydratedObjects.clear();
  }

  private void internalWriteObject(List<Object> objects) throws Exception {
    byte[] data = serializeObjects(objects);
    byte[] additionalObjects = serializeObjects(fObjectsToSerialize);
    
    byte[] result = new byte[data.length + additionalObjects.length];
    
    System.arraycopy(data, 0, result, 0, data.length);
    System.arraycopy(additionalObjects, 0, result, data.length, additionalObjects.length);
    
    append(result);
  }

  private void readAllObjects() throws Exception {
    int bufferSize = (int)Runtime.getRuntime().freeMemory() / 16;
    DataInputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(fFile), bufferSize));

    byte[] blockTagBuffer = new byte[4];
    
    while (stream.available() > 0) {
      stream.read(blockTagBuffer);
      if (!Arrays.equals(blockTagBuffer, BLOCK_START_TAG)) throw new RuntimeException("Could not read block start");
      
      int blockSize = stream.readInt(); //the block size
      byte[] blockData = new byte[blockSize];
      stream.read(blockData); 
      readTransactionData(blockData);
      
      stream.read(blockTagBuffer);
      if (!Arrays.equals(blockTagBuffer, BLOCK_END_TAG)) throw new RuntimeException("Could not read block end Tag");
    }
    
    stream.close();
  }

  private void readObject(byte[] data, int offset, int size) throws Exception {
    DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data, offset, size));

    long typeId = stream.readLong();
    long objectId = stream.readLong();

    if(typeId == MetaClass.METACLASS_OBJECT_ID) {
      fMetaData.readMetaClass(this, stream, objectId);
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
    if (!Arrays.equals(fourByteBuffer, TRANSACTION_START_TAG));
    
    System.arraycopy(data, 4, fourByteBuffer, 0, 4);
    int transactionSize = Util.convertToInt(fourByteBuffer);
    
    System.arraycopy(data, transactionSize+8, fourByteBuffer, 0, 4);
    if (!Arrays.equals(fourByteBuffer, TRANSACTION_END_TAG)) throw new RuntimeException("could not read end transaction tag");
    
    readObjects(data, 8, transactionSize);
  }

  private byte[] serializeObjects(List<Object> objects) throws Exception {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    DataOutput stream = new DataOutputStream(buffer);
    for(Object object: objects) {
      serializeObject(stream, object);
    }
    return buffer.toByteArray();
  }
  
}
