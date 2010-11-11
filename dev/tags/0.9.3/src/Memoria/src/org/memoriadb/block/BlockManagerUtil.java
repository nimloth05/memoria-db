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

package org.memoriadb.block;

/**
 * @author msc
 */
public final class BlockManagerUtil {

  private static final double K = 1d / Math.log10(2);
  private static final int LOG_2_OF_1024 = 10;

  private BlockManagerUtil() {}

  public static long getNextPowerOfTwo(final long i) {
    if (i < 0) throw new IllegalArgumentException("negative numbers are not allowed");
    return 2 * Long.highestOneBit(i - 1);
  }

  public static int getLog2(final long l) {
//    double result =  Math.log10(l) / Math.log10(2);
    double result = Math.ceil(K * Math.log10(l));
    return (int)result;
  }

  public static long getNextAlignedBlockSize(final long size) {
    if (size <= 1024) return getNextPowerOfTwo(size);
    long result = 1024 + 512;
    while (result < size) result += 512;
    return result;
  }

  public static int getIndexForAlignedBlockSize(final long size) {
    if (size <= 1024) return getLog2(size);
    long temp = size - 1024;
    long offsetIndex = temp / 512;
    return LOG_2_OF_1024 + (int)offsetIndex;
  }

}
