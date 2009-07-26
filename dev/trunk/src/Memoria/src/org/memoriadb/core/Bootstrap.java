package org.memoriadb.core;

import org.memoriadb.CreateConfig;
import org.memoriadb.OpenConfig;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.Header;
import org.memoriadb.core.file.HeaderHelper;
import org.memoriadb.core.file.ICompressor;
import org.memoriadb.core.file.IMemoriaFile;
import org.memoriadb.core.file.read.FileReader;
import org.memoriadb.core.file.read.ObjectLoader;
import org.memoriadb.core.file.write.TransactionWriter;
import org.memoriadb.core.meta.AbstractMemoriaClass;
import org.memoriadb.core.meta.HandlerbasedMemoriaClass;
import org.memoriadb.core.meta.IMemoriaClassConfig;
import org.memoriadb.core.mode.IModeStrategy;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.handler.IHandler;
import org.memoriadb.handler.collection.CollectionHandler;
import org.memoriadb.handler.collection.EnumSetHandler;
import org.memoriadb.handler.field.ReflectionHandlerFactory;
import org.memoriadb.handler.jdk.awt.color.ColorHandler;
import org.memoriadb.handler.jdk.url.URLHandler;
import org.memoriadb.handler.map.MapHandler;
import org.memoriadb.handler.value.LangValueObjectHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class Bootstrap {

  public static TransactionHandler openOrCreate(CreateConfig config, IMemoriaFile file, IModeStrategy strategy) {
    if (file == null) throw new IllegalArgumentException("File was null");
    //file = new AsynchronousFile(file);

    if(file.isEmpty())  return createDb(config, file, strategy);

    return openDb(config, file, strategy);
  }

  private static void addCustomHandlers(TransactionHandler transactionHandler, Iterable<IHandler> customHandlers) {
    for (IHandler handler : customHandlers) {
      registerHandler(transactionHandler, handler);
    }
  }

  private static void addDefaultMetaClasses(TransactionHandler trxHandler) {
    // These classObjects don't need a fix known ID.
    IMemoriaClassConfig objectMemoriaClass = ReflectionHandlerFactory.createNewType(Object.class, trxHandler.getDefaultIdProvider().getFieldMetaClass());
    trxHandler.save(objectMemoriaClass);
    
    registerHandler(trxHandler, new LangValueObjectHandler.BooleanValueObjectHandler(), true);
    registerHandler(trxHandler, new LangValueObjectHandler.CharacterValueObjectHandler(), true);
    registerHandler(trxHandler, new LangValueObjectHandler.ByteValueObjectHandler(), true);
    registerHandler(trxHandler, new LangValueObjectHandler.ShortValueObjectHandler(), true);
    registerHandler(trxHandler, new LangValueObjectHandler.IntegerValueObjectHandler(), true);
    registerHandler(trxHandler, new LangValueObjectHandler.LongValueObjectHandler(), true);
    registerHandler(trxHandler, new LangValueObjectHandler.FloatValueObjectHandler(), true);
    registerHandler(trxHandler, new LangValueObjectHandler.DoubleValueObjectHandler(), true);
    registerHandler(trxHandler, new LangValueObjectHandler.StringValueObjectHandler(), true);

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

    registerHandler(trxHandler, new URLHandler(), true);
    registerHandler(trxHandler, new ColorHandler(), true);
  }

  private static void addValueClasses(TransactionHandler transactionHandler, Iterable<Class<?>> valueClasses) {
    // classes may already be registered, when they are handler-classes
    for(Class<?> clazz: valueClasses) {
      transactionHandler.addMemoriaClassIfNecessary(clazz);
    }

    for(Class<?> clazz: valueClasses) {
      ((AbstractMemoriaClass)transactionHandler.getMemoriaClass(clazz.getName())).setValueObject(true);
    }

  }

  private static TransactionHandler createDb(CreateConfig config, IMemoriaFile file, IModeStrategy strategy) {
    writeHeader(config, file);

    TransactionHandler transactionHandler = openDb(config, file, strategy);

    // bootstap memoriaClasses
    transactionHandler.beginUpdate();
      addDefaultMetaClasses(transactionHandler);
      addCustomHandlers(transactionHandler, config.getCustomHandlers());
      addValueClasses(transactionHandler, config.getValueClasses());
    transactionHandler.endUpdate();

    return transactionHandler;

  }

  private static TransactionHandler openDb(OpenConfig config, IMemoriaFile file, IModeStrategy strategy) {
    FileReader fileReader = new FileReader(file);
    Header header = readHeader(fileReader);

    ICompressor compressor = header.getCompressor();

    ObjectRepository repo = ObjectRepoFactory.create(header.loadIdFactory());
    long headRevision = ObjectLoader.readIn(fileReader, repo, config.getBlockManager(), header.getInstantiator(), strategy, compressor);

    TransactionWriter writer = new TransactionWriter(repo, config, file, headRevision, compressor);
    TransactionHandler transactionHandler = new TransactionHandler(writer, header, strategy);

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
   * @param handler to handle objects of type <tt>className</tt>.
   * @param className Name of the class the given <tt>handler</tt> can deal with.
   */
  private static void registerHandler(TransactionHandler transactionHandler, IHandler handler) {
    registerHandler(transactionHandler, handler, false);
  }
  
  private static void registerHandler(TransactionHandler transactionHandler, IHandler handler, boolean isValueObject) {
    IMemoriaClassConfig classConfig = new HandlerbasedMemoriaClass(handler, transactionHandler.getDefaultIdProvider().getHandlerMetaClass(), isValueObject);
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
