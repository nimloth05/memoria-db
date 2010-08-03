package org.memoriadb.block.maintenancefree;

import java.util.Comparator;

public class BlockBucketComparator implements Comparator<BlockBucket> {

  @Override
  public int compare(BlockBucket bucket1, BlockBucket bucket2) {
    return (bucket1.getSize() < bucket2.getSize() ? -1 : (bucket1.getSize() == bucket2.getSize() ? 0 : 1));
  }

}
