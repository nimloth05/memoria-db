package org.memoriadb;

import java.io.*;
import java.util.List;

import org.memoriadb.core.*;
import org.memoriadb.core.file.*;
import org.memoriadb.core.file.FileReader;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.handler.list.ListHandler;
import org.memoriadb.core.load.ObjectLoader;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.*;

/**
 * 
 * Facade to a Memoria db
 * 
 * @author msc
 * 
 */
public final class Memoria {

  private static Version fVersion = new Version(0, 0, 0);
  private static int fFileLayoutVersion = 0;

  /**
   * @return The Version of the file-layout. Stays stable as long as possible.
   */
  public static int getFileLayoutVersion() {
    return fFileLayoutVersion;
  }

  /**
   * @return Memoria-Version.
   */
  public static Version getMemoriaVersion() {
    return fVersion;
  }

  /**
   * Creates an db backed with an in-memory file
   * 
   * @return An ObjectStore backed with an in-memory file
   */
  public static IObjectStore open(CreateConfig config) {
    return open(config, new InMemoryFile());
  }

  public static IObjectStore open(CreateConfig config, IMemoriaFile file) {
    if (file == null) throw new IllegalArgumentException("File was null");
    writeHeader(config, file);
    return open((OpenConfig) config, file);
  }

  public static IObjectStore open(CreateConfig config, String path) {
    if (new File(path).exists()) return open((OpenConfig) config, path);
    return open(config, new PhysicalFile(path));
  }

  public static IObjectStore open(OpenConfig config, IMemoriaFile file) {
    if (file == null) throw new IllegalArgumentException("File was null");

    FileReader fileReader = new FileReader(file);
    FileHeader header = readHeader(fileReader);

    IDefaultInstantiator defaultInstantiator = header.loadDefaultInstantiator();
    ObjectRepo repo = ObjectRepoFactory.create(header.loadIdFactory(), header.getCustomHandlers());
    long headRevision = ObjectLoader.readIn(fileReader, repo, config.getBlockManager(), defaultInstantiator, config.getDBMode());

    TransactionWriter writer = new TransactionWriter(repo, config, file, headRevision);
    ObjectStore objectStore = new ObjectStore(defaultInstantiator, writer, header);
    
    if (headRevision == Constants.INITIAL_HEAD_REVISION) {
      objectStore.beginUpdate();
      addDefaultMetaClasses(objectStore, header.getCustomHandlers());
      objectStore.endUpdate();
    }
    
    return objectStore;
  }

  /**
   * @return An ObjectStore backed with a file.
   * @pre The db-file must exist.
   */
  public static IObjectStore open(OpenConfig config, String path) {
    return open(config, new PhysicalFile(path));
  }

  private static void addCustomHandlers(ObjectStore store, List<String> customHandlers) {
    for (String className : customHandlers) {
      registerHandler(store, (ISerializeHandler)ReflectionUtil.createInstance(className));
    }
  }
  
  private static void addDefaultMetaClasses(ObjectStore store, List<String> customHandlers) {
    // These classObjects don't need a fix known ID.
    IMemoriaClassConfig objectMemoriaClass = MemoriaFieldClassFactory.createMetaClass(Object.class, store.getMemoriaFieldMetaClass());
    //repo.add(objectMemoriaClass, objectMemoriaClass.getMemoriaClassId());
    store.save(objectMemoriaClass);

    registerHandler(store, new ListHandler.ArrayListHandler());
    registerHandler(store, new ListHandler.LinkedListHandler());
    registerHandler(store, new ListHandler.CopyOnWriteListHandler());
    registerHandler(store, new ListHandler.StackHandler());
    registerHandler(store, new ListHandler.VectorHandler());

    addCustomHandlers(store, customHandlers);
    
  }

  
  private static FileHeader readHeader(FileReader fileReader) {
    try {
      return fileReader.readHeader();
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
  }


  /**
   * @param handler
   *          The handler to handle objects of type <tt>className</tt>.
   * @param className
   *          Name of the class the given <tt>handler</tt> can deal with.
   */
  private static void registerHandler(ObjectStore store, ISerializeHandler handler) {
    IMemoriaClassConfig classConfig = new MemoriaHandlerClass(handler, store.getHandlerMetaClass());
    store.save(classConfig);
  }
  
  private static void writeHeader(CreateConfig config, IMemoriaFile file) {
    if (file.isEmpty()) {
      try {
        FileHeaderHelper.writeHeader(file, config);
      }
      catch (IOException e) {
        throw new MemoriaException(e);
      }
    }
  }

  private Memoria() {}

}
