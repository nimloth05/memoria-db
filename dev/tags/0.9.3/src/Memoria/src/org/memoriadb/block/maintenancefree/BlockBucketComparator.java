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

package org.memoriadb.block.maintenancefree;

import java.util.Comparator;

/**
 * @author msc
 */
public class BlockBucketComparator implements Comparator<BlockBucket> {

  @Override
  public int compare(BlockBucket bucket1, BlockBucket bucket2) {
    return (bucket1.getSize() < bucket2.getSize() ? -1 : (bucket1.getSize() == bucket2.getSize() ? 0 : 1));
  }

}
