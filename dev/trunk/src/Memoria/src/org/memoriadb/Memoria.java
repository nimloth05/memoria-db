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

package org.memoriadb;

import org.memoriadb.core.Bootstrap;
import org.memoriadb.core.file.IMemoriaFile;
import org.memoriadb.core.file.InMemoryFile;
import org.memoriadb.core.file.PhysicalFile;
import org.memoriadb.core.mode.DataModeStrategy;
import org.memoriadb.core.mode.DataStore;
import org.memoriadb.core.mode.ObjectModeStrategy;
import org.memoriadb.core.mode.ObjectStore;
import org.memoriadb.core.util.Version;

import java.io.File;
import java.io.IOException;

/**
 * 
 * Facade to a Memoria db
 * 
 * @author Sandro
 * 
 */
public final class Memoria {

  private static final Version fVersion = new Version(0, 0, 0);
  private static final int fFileLayoutVersion = 0;

  /**
   * @return The Version of the file-layout. Stays stable as long as possible.
   */
  public static int getFileLayoutVersion() {
    return fFileLayoutVersion;
  }

  /**
   * @return Memoria-Version.
   */
  public static Version getMemoriaVersion() {
    return fVersion;
  }

  /**
   * @param config
   * @return An ObjectStore backed with an in-memory file
   * @param config
   */
  public static IObjectStore open(CreateConfig config) {
    return open(config, new InMemoryFile());
  }

  public static IObjectStore open(CreateConfig config, File path) throws IOException {
    return open(config, new PhysicalFile(path));
  }
  
  public static IObjectStore open(CreateConfig config, IMemoriaFile file) {
    return new ObjectStore(Bootstrap.openOrCreate(config, file, new ObjectModeStrategy()));
  }

  public static IObjectStore open(CreateConfig config, String path) throws IOException {
    return open(config, new File(path));
  }
  
  public static IDataStore openIndataMode(CreateConfig config, String path) throws IOException {
    return openInDataMode(config, new File(path));
  }

  /**
   * @param config
   * @return An ObjectStore backed with an in-memory file
   * @param config
   */
  public static IDataStore openInDataMode(CreateConfig config) {
    return openInDataMode(config, new InMemoryFile());
  }
  
  public static IDataStore openInDataMode(CreateConfig config, File path) throws IOException {
    return openInDataMode(config, new PhysicalFile(path));
  }

  public static IDataStore openInDataMode(CreateConfig config, IMemoriaFile file) {
    return new DataStore(Bootstrap.openOrCreate(config, file, new DataModeStrategy()));
  }
  
  private Memoria() {}

}
