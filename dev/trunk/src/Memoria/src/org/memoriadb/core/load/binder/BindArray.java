package org.memoriadb.core.load.binder;

import org.memoriadb.core.handler.array.IArray;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.*;

public class BindArray implements IBindable {
  
  private final IObjectId fId;
  private final IArray fArray;
  private final int fIndex;

  public BindArray(IArray array, int index, IObjectId objectId) {
    fIndex = index;
    fArray = array;
    fId = objectId;
  }

  @Override
  public void bind(IReaderContext context) throws Exception {
      Object obj = context.isNullReference(fId)? null: context.getObjectById(fId);
      fArray.set(fIndex, obj);
  }

}
