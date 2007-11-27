package org.memoriadb.core;

import java.io.IOException;

import org.memoriadb.core.file.*;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.handler.collection.*;
import org.memoriadb.core.load.ObjectLoader;
import org.memoriadb.core.meta.*;
import org.memoriadb.core.mode.*;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.ReflectionUtil;

public class Bootstrap {

  public static TransactionHandler openOrCreate(CreateConfig config, IMemoriaFile file, IModeStrategy strategy) {
    if (file == null) throw new IllegalArgumentException("File was null");
    
    if(file.isEmpty())  return createDb(config, file, strategy);
    
    return openDb(config, file, strategy);
  }
  
  private static void addCustomHandlers(TransactionHandler transactionHandler, Iterable<String> customHandlers) {
    for (String className : customHandlers) {
      registerHandler(transactionHandler, (ISerializeHandler)ReflectionUtil.createInstance(className));
    }
  }

  private static void addDefaultMetaClasses(TransactionHandler trxHansdler, Iterable<String> customHandlers) {
    // These classObjects don't need a fix known ID.
    IMemoriaClassConfig objectMemoriaClass = MemoriaFieldClassFactory.createMetaClass(Object.class, trxHansdler.getDefaultIdProvider().getFieldMetaClass());
    trxHansdler.save(objectMemoriaClass);

    registerHandler(trxHansdler, new CollectionHandler.ArrayListHandler());
    registerHandler(trxHansdler, new CollectionHandler.LinkedListHandler());
    registerHandler(trxHansdler, new CollectionHandler.CopyOnWriteListHandler());
    registerHandler(trxHansdler, new CollectionHandler.VectorHandler());
    registerHandler(trxHansdler, new CollectionHandler.StackHandler());

    registerHandler(trxHansdler, new CollectionHandler.HashSetHandler());
    registerHandler(trxHansdler, new CollectionHandler.LinkedHashSetHandler());
    registerHandler(trxHansdler, new CollectionHandler.TreeSetHandler());
    registerHandler(trxHansdler, new CollectionHandler.ConcurrentSkipListSetHandler());
    registerHandler(trxHansdler, new EnumSetHandler(EnumSetHandler.sJumboEnumSet));
    registerHandler(trxHansdler, new EnumSetHandler(EnumSetHandler.sRegularEnumSet));
    
    addCustomHandlers(trxHansdler, customHandlers);
  }

  private static TransactionHandler createDb(CreateConfig config, IMemoriaFile file, IModeStrategy strategy) {

    writeHeader(config, file);
    
    TransactionHandler transactionHandler = openDb(config, file, strategy);
    
    // bootstap memoriaClasses
    transactionHandler.beginUpdate();
    addDefaultMetaClasses(transactionHandler, (config).getCustomHandlers());
    transactionHandler.endUpdate();
    
    return transactionHandler;
    
  }

  private static TransactionHandler openDb(OpenConfig config, IMemoriaFile file, IModeStrategy strategy) {
    FileReader fileReader = new FileReader(file);
    Header header = readHeader(fileReader);

    IDefaultInstantiator defaultInstantiator = header.loadDefaultInstantiator();
    ObjectRepository repo = ObjectRepoFactory.create(header.loadIdFactory());
    long headRevision = ObjectLoader.readIn(fileReader, repo, config.getBlockManager(), defaultInstantiator, strategy);

    TransactionWriter writer = new TransactionWriter(repo, config, file, headRevision);
    TransactionHandler transactionHandler = new TransactionHandler(defaultInstantiator, writer, header, strategy);
    
    return transactionHandler;
  }

  private static Header readHeader(FileReader fileReader) {
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
  private static void registerHandler(TransactionHandler transactionHandler, ISerializeHandler handler) {
    IMemoriaClassConfig classConfig = new HandlerbasedMemoriaClass(handler, transactionHandler.getDefaultIdProvider().getHandlerMetaClass());
    transactionHandler.save(classConfig);
    
    Class<?> clazz = ReflectionUtil.getClass(classConfig.getJavaClassName());
    ObjectModeStrategy.recursiveAddTypeHierarchy(transactionHandler, clazz, classConfig);
  }
  
  
  private static void writeHeader(CreateConfig config, IMemoriaFile file) {
    try {
      HeaderHelper.writeHeader(file, config);
    }
    catch (IOException e) {
      throw new MemoriaException("error writing header " + file);
    }
  }


}
