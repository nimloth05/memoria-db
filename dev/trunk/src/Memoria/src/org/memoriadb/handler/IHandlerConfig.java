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

package org.memoriadb.handler;

import org.memoriadb.core.meta.IMemoriaClass;

/**
 * Additional configuration interface. Implement this interface if a handler needs to know what the current superClass is.
 */
public interface IHandlerConfig {


  /**
   * The superClass for this IHandler.
   * @param superClass
   */
  public void setSuperClass(IMemoriaClass superClass);
}
