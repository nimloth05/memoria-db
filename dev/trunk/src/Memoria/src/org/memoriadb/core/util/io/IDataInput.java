/*
 * Copyright 2010 Micha Riser
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
package org.memoriadb.core.util.io;

import java.io.*;

public interface IDataInput extends DataInput {
  
  /**
   * @return the number of remaining bytes avaiable 
   */
  public int available() throws IOException;

  /**
   * Processes the next n bytes and creates a sub-input from these
   * @param byteCount the number of bytes to use
   */
  public IDataInput subInput(int byteCount) throws IOException;

}
