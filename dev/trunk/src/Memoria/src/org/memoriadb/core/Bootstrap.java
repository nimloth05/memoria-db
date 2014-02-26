/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.memoriadb.core;

import org.memoriadb.CreateConfig;
import org.memoriadb.OpenConfig;
import org.memoriadb.core.block.BlockRepository;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.Header;
import org.memoriadb.core.file.HeaderHelper;
import org.memoriadb.core.file.ICompressor;
import org.memoriadb.core.file.IMemoriaFile;
import org.memoriadb.core.file.read.FileReader;
import org.memoriadb.core.file.read.ObjectLoader;
import org.memoriadb.core.file.write.TransactionWriter;
import org.memoriadb.core.meta.IMemoriaClassConfig;
import org.memoriadb.core.meta.MemoriaClass;
import org.memoriadb.core.mode.IModeStrategy;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.handler.IHandler;
import org.memoriadb.handler.collection.CollectionHandler;
import org.memoriadb.handler.collection.EnumSetHandler;
import org.memoriadb.handler.field.ReflectionHandlerFactory;
import org.memoriadb.handler.jdk.awt.color.ColorHandler;
import org.memoriadb.handler.jdk.date.DateHandler;
import org.memoriadb.handler.jdk.sql.SqlDateHandler;
import org.memoriadb.handler.jdk.sql.TimestampHandler;
import org.memoriadb.handler.jdk.url.URLHandler;
import org.memoriadb.handler.map.MapHandler;
import org.memoriadb.handler.value.LangValueObjectHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sandro
 */
public final class Bootstrap {

  public static TransactionHandler openOrCreate(CreateConfig config, IMemoriaFile file, IModeStrategy strategy) {
    if (file == null) throw new IllegalArgumentException("File was null");
    //file = new AsynchronousFile(file);

    if (file.isEmpty()) return createDb(config, file, strategy);

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
    registerHandler(trxHandler, new DateHandler(), true);
    registerHandler(trxHandler, new TimestampHandler(), true);
    registerHandler(trxHandler, new SqlDateHandler(), true);
  }

  private static void addValueClasses(TransactionHandler transactionHandler, Iterable<Class<?>> valueClasses) {
    // classes may already be registered, when they are handler-classes
    for (Class<?> clazz : valueClasses) {
      transactionHandler.addMemoriaClassIfNecessary(clazz);
    }

    for (Class<?> clazz : valueClasses) {
      ((MemoriaClass) transactionHandler.getMemoriaClass(clazz.getName())).setValueObject(true);
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
    BlockRepository blockRepo = new BlockRepository();
    long headRevision = ObjectLoader.readIn(fileReader, repo, blockRepo, config.getBlockManager(), header.getInstantiator(), strategy, compressor);

    TransactionWriter writer = new TransactionWriter(repo, blockRepo, config, file, headRevision, compressor);
    TransactionHandler transactionHandler = new TransactionHandler(writer, header, strategy);

    return transactionHandler;
  }

  private static Header readHeader(FileReader fileReader) {
    try {
      return fileReader.readHeader();
    } catch (IOException e) {
      throw new MemoriaException(e);
    }
  }

  /**
   * @param transactionHandler the current transactionHandler
   * @param handler            to handle objects of type <tt>className</tt>.
   */
  private static void registerHandler(TransactionHandler transactionHandler, IHandler handler) {
    registerHandler(transactionHandler, handler, false);
  }

  private static void registerHandler(TransactionHandler transactionHandler, IHandler handler, boolean isValueObject) {
    IMemoriaClassConfig classConfig = new MemoriaClass(handler, transactionHandler.getDefaultIdProvider().getHandlerMetaClass(), isValueObject);
    transactionHandler.save(classConfig);

    Class<?> clazz = ReflectionUtil.getClass(classConfig.getJavaClassName());
    TypeHierarchyBuilder.recursiveAddTypeHierarchy(transactionHandler, clazz, classConfig);
  }

  private static void writeHeader(CreateConfig config, IMemoriaFile file) {
    try {
      HeaderHelper.writeHeader(file, config);
    } catch (IOException e) {
      throw new MemoriaException("error writing header " + file);
    }
  }

  private Bootstrap() {
  }

}
