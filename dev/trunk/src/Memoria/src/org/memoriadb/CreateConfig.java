/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb;

import org.memoriadb.block.IBlockManager;
import org.memoriadb.handler.IHandler;
import org.memoriadb.id.loong.LongIdFactory;
import org.memoriadb.instantiator.DefaultInstantiator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//FIXME: API Doc
public class CreateConfig extends OpenConfig {

  private String fIdFactoryClassName;
  private boolean fUseCompression;

  private final String fDefaultInstantiatorClassName;
  private final List<IHandler> fCustomHandlers = new ArrayList<IHandler>();
  private final List<Class<?>> fValueClasses = new ArrayList<Class<?>>();

  public CreateConfig() {
    super();
    fIdFactoryClassName = LongIdFactory.class.getName();
    fDefaultInstantiatorClassName = DefaultInstantiator.class.getName();
    fUseCompression = true;
  }

  public CreateConfig(IBlockManager blockManager, String IdFactoryClassName, String defaultInstantiatorClassName) {
    super(blockManager);
    fIdFactoryClassName = IdFactoryClassName;
    fDefaultInstantiatorClassName = defaultInstantiatorClassName;
  }

  public CreateConfig(String idFactoryClassName, String defaultInstantiatorClassName) {
    super();
    fIdFactoryClassName = idFactoryClassName;
    fDefaultInstantiatorClassName = defaultInstantiatorClassName;
  }
  
  /**
   * @param handler
   *          The handler for a specific java-class.
   */
  public void addCustomHandler(IHandler handler) {
    fCustomHandlers.add(handler);
  }

  /**
   * Declares the given class as value-class (it's objects are inline stored value-objects).
   * @param clazz
   */
  public void addValueClass(Class<?> clazz) {
    fValueClasses.add(clazz);
  }

  public Iterable<IHandler> getCustomHandlers() {
    return Collections.unmodifiableCollection(fCustomHandlers);
  }

  public String getDefaultInstantiatorClassName() {
    return fDefaultInstantiatorClassName;
  }
  
  public String getIdFactoryClassName() {
    return fIdFactoryClassName;
  }

  public Iterable<Class<?>> getValueClasses() {
    return fValueClasses;
  }

  public boolean isUseCompression() {
    return fUseCompression;
  }

  public void setIdFactoryName(String name) {
    fIdFactoryClassName = name;
  }

  public void setUseCompression(boolean useCompression) {
    fUseCompression = useCompression;
  }

}
