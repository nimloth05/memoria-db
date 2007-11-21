package org.memoriadb.core;

import java.io.IOException;

import org.memoriadb.IObjectStore;
import org.memoriadb.core.file.*;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.handler.list.CollectionHandler;
import org.memoriadb.core.load.ObjectLoader;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.*;

public class Bootstrap {

  public static IObjectStore open(OpenConfig config, IMemoriaFile file) {
    if (file == null) throw new IllegalArgumentException("File was null");

    FileReader fileReader = new FileReader(file);
    FileHeader header = readHeader(fileReader);

    IDefaultInstantiator defaultInstantiator = header.loadDefaultInstantiator();
    ObjectRepo repo = ObjectRepoFactory.create(header.loadIdFactory());
    long headRevision = ObjectLoader.readIn(fileReader, repo, config.getBlockManager(), defaultInstantiator, config.getDBMode());

    TransactionWriter writer = new TransactionWriter(repo, config, file, headRevision);
    
    TrxHandler trxHandler = new TrxHandler(defaultInstantiator, writer, header);
    
    ObjectStore objectStore = new ObjectStore();
    
    if (headRevision == Constants.INITIAL_HEAD_REVISION) {
      objectStore.beginUpdate();
      addDefaultMetaClasses(trxHandler, ((CreateConfig)config).getCustomHandlers());
      objectStore.endUpdate();
    }
    
    return objectStore;
  }
  
  private static void addCustomHandlers(TrxHandler trxHandler, Iterable<String> customHandlers) {
    for (String className : customHandlers) {
      registerHandler(trxHandler, (ISerializeHandler)ReflectionUtil.createInstance(className));
    }
  }
  

  private static void addDefaultMetaClasses(TrxHandler trxHansdler, Iterable<String> customHandlers) {
    // These classObjects don't need a fix known ID.
    IMemoriaClassConfig objectMemoriaClass = MemoriaFieldClassFactory.createMetaClass(Object.class, trxHansdler.getMemoriaFieldMetaClass());
    //repo.add(objectMemoriaClass, objectMemoriaClass.getMemoriaClassId());
    trxHansdler.save(objectMemoriaClass);

    registerHandler(trxHansdler, new CollectionHandler.ArrayListHandler());
    registerHandler(trxHansdler, new CollectionHandler.LinkedListHandler());
    registerHandler(trxHansdler, new CollectionHandler.CopyOnWriteListHandler());
    registerHandler(trxHansdler, new CollectionHandler.StackHandler());
    registerHandler(trxHansdler, new CollectionHandler.VectorHandler());

    addCustomHandlers(trxHansdler, customHandlers);
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
  private static void registerHandler(TrxHandler trxHandler, ISerializeHandler handler) {
    IMemoriaClassConfig classConfig = new MemoriaHandlerClass(handler, trxHandler.getIdFactory().getHandlerMetaClass());
    trxHandler.save(classConfig);
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
  

}
