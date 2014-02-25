/*
 * Copyright 2010 memoria db project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package org.memoriadb.core.file;

import java.util.zip.*;

import org.memoriadb.core.util.ZipUtil;

/**
 * @author Sandro
 */
public final class ZipCompressor implements ICompressor {
  
  private static boolean REUSE_FLATERS = true;
  
  private Inflater fInflater = null;
  private Deflater fDeflater = null;

  public ZipCompressor() {
    if (REUSE_FLATERS) {
      fInflater = new Inflater();
      fDeflater = new Deflater();
    }
  }
  
  @Override
  public byte[] compress(byte[] input) {
    Deflater deflater = getDeflater();
    try {
      return ZipUtil.compress(input, deflater);
    } finally {
      if (!REUSE_FLATERS) {
        deflater.end();
      }
    }
  }
  
  @Override
  public byte[] decompress(byte[] input) {
    Inflater inflater = getInflater();
    try {
      return ZipUtil.decompress(input, inflater);
    } finally {
      if (!REUSE_FLATERS) {
        inflater.end();
      }
    }
  }
  
  private Deflater getDeflater() {
    if (REUSE_FLATERS) {
      fDeflater.reset();
      return fDeflater;
    }
    return new Deflater();
  }

  private Inflater getInflater() {
    if (REUSE_FLATERS) {
      fInflater.reset();
      return fInflater;
    }
    return new Inflater();
  }

}
