package org.memoriadb.core;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.*;
import org.memoriadb.core.load.ObjectLoader;
import org.memoriadb.core.meta.*;
import org.memoriadb.core.mode.IModeStrategy;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.handler.IHandler;
import org.memoriadb.handler.collection.*;
import org.memoriadb.handler.map.MapHandler;
import org.memoriadb.instantiator.IInstantiator;

public class Bootstrap {

  public static TransactionHandler openOrCreate(CreateConfig config, IMemoriaFile file, IModeStrategy strategy) {
    if (file == null) throw new IllegalArgumentException("File was null");
    
    if(file.isEmpty())  return createDb(config, file, strategy);
    
    return openDb(config, file, strategy);
  }
  
  private static void addCustomHandlers(TransactionHandler transactionHandler, Iterable<String> customHandlers) {
    for (String className : customHandlers) {
      registerHandler(transactionHandler, (IHandler)ReflectionUtil.createInstance(className));
    }
  }

  private static void addDefaultMetaClasses(TransactionHandler trxHandler, Iterable<String> customHandlers) {
    // These classObjects don't need a fix known ID.
    IMemoriaClassConfig objectMemoriaClass = FieldbasedMemoriaClassFactory.createMetaClass(Object.class, trxHandler.getDefaultIdProvider().getFieldMetaClass());
    trxHandler.save(objectMemoriaClass);

    // FIXME den CollectionHandler auf mehr Generizität umschreiben (Ctor mit String-arg)
    registerHandler(trxHandler, new CollectionHandler.ListHandler(ArrayList.class));
    registerHandler(trxHandler, new CollectionHandler.ListHandler(LinkedList.class));
    registerHandler(trxHandler, new CollectionHandler.ListHandler(CopyOnWriteArrayList.class));
    registerHandler(trxHandler, new CollectionHandler.ListHandler(Vector.class));
    registerHandler(trxHandler, new CollectionHandler.ListHandler(Stack.class));

    registerHandler(trxHandler, new CollectionHandler.TreeSetHandler());
    registerHandler(trxHandler, new CollectionHandler.ConcurrentSkipListSetHandler());
    registerHandler(trxHandler, new CollectionHandler.SetHandler(HashSet.class));
    registerHandler(trxHandler, new CollectionHandler.SetHandler(LinkedHashSet.class));
    registerHandler(trxHandler, new CollectionHandler.SetHandler(CopyOnWriteArraySet.class));
    registerHandler(trxHandler, new EnumSetHandler(EnumSetHandler.sJumboEnumSet));
    registerHandler(trxHandler, new EnumSetHandler(EnumSetHandler.sRegularEnumSet));
    
    registerHandler(trxHandler, new MapHandler(HashMap.class));
    registerHandler(trxHandler, new MapHandler(ConcurrentHashMap.class));
    registerHandler(trxHandler, new MapHandler(ConcurrentSkipListMap.class));
    registerHandler(trxHandler, new MapHandler(IdentityHashMap.class));
    registerHandler(trxHandler, new MapHandler(LinkedHashMap.class));
    registerHandler(trxHandler, new MapHandler(TreeMap.class));
    registerHandler(trxHandler, new MapHandler(WeakHashMap.class));
    
    addCustomHandlers(trxHandler, customHandlers);
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

    IInstantiator instantiator = header.loadDefaultInstantiator();
    ObjectRepository repo = ObjectRepoFactory.create(header.loadIdFactory());
    long headRevision = ObjectLoader.readIn(fileReader, repo, config.getBlockManager(), instantiator, strategy);

    TransactionWriter writer = new TransactionWriter(repo, config, file, headRevision);
    TransactionHandler transactionHandler = new TransactionHandler(instantiator, writer, header, strategy);
    
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
  private static void registerHandler(TransactionHandler transactionHandler, IHandler handler) {
    IMemoriaClassConfig classConfig = new HandlerbasedMemoriaClass(handler, transactionHandler.getDefaultIdProvider().getHandlerMetaClass());
    transactionHandler.save(classConfig);
    
    Class<?> clazz = ReflectionUtil.getClass(classConfig.getJavaClassName());
    TypeHierarchyBuilder.recursiveAddTypeHierarchy(transactionHandler, clazz, classConfig);
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
