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

package org.memoriadb.id;

import java.io.DataOutput;
import java.io.IOException;

/**
 * This Interface represents an id for a object.
 * 
 * ATTENTION: The implementor must implement <tt>equals()</tt> and <tt>hashCode()</tt>
 * @author sandro
 *
 */
public interface IObjectId {

  public String asString();
  
  @Override
  public boolean equals(Object object);
  
  @Override
  public int hashCode();
  
  public void writeTo(DataOutput output) throws IOException;

}
