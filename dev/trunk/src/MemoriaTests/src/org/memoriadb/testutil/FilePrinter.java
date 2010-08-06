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

package org.memoriadb.testutil;

import org.memoriadb.block.Block;
import org.memoriadb.core.file.Header;
import org.memoriadb.core.file.IMemoriaFile;
import org.memoriadb.core.file.read.FileReader;
import org.memoriadb.core.file.read.HydratedObject;
import org.memoriadb.core.file.read.IFileReaderHandler;
import org.memoriadb.id.IObjectId;

import java.io.IOException;

public class FilePrinter {
  
  private static class PrintHandler implements IFileReaderHandler {
    
    @Override
    public void block(Block block) {
      System.out.println(block);      
    }

    @Override
    public void memoriaClass(HydratedObject metaClass, IObjectId id, int size) {
      System.out.println("  memoriaClassObject id " + id + " typeId " + metaClass.getTypeId());
    }

    @Override
    public void memoriaClassDeleted(IObjectId id) {
      System.out.println("  memoriaClassObject deleted id " + id);
    }

    @Override
    public void object(HydratedObject object, IObjectId id, int size) {
      System.out.println("  object id " + id + " typeId " + object.getTypeId());
    }

    @Override
    public void objectDeleted(IObjectId id) {
      System.out.println("  object deleted id " + id);
    }
    
  }
  
  public static void print(IMemoriaFile file) {
    try {
      internalPrint(file);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private static void internalPrint(IMemoriaFile file) throws IOException {
    FileReader reader = new FileReader(file);
    
    Header header = reader.readHeader();
    
    System.out.println("------------------------");

    reader.readBlocks(header.loadIdFactory(), header.getCompressor(), new PrintHandler());
  }
}
