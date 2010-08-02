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

package org.memoriadb.instantiator;


/**
 * Instantiates objects which are saved by the FieldbasedObjectHandler.
 */
public interface IInstantiator {
  
  /**
   * 
   * @param className the name of the Java Class for the object.
   * @throws Exception TODO
   * @throws CannotInstantiateException
   */
  public void checkCanInstantiateObject(String className) throws CannotInstantiateException;

  /**
   * Instantiate a new object.
   * @param <T>
   * @param className
   * @return
   */
  public <T> T newInstance(String className); 

}
