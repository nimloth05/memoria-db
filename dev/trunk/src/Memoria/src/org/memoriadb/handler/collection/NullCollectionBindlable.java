package org.memoriadb.handler.collection;

import java.util.Collection;

import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.handler.IBindable;

public class NullCollectionBindlable implements IBindable {

  private final Collection<Object> fCollection;

  public NullCollectionBindlable(Collection<Object> collection) {
    fCollection = collection;
  }

  @Override
  public void bind(IReaderContext context) throws Exception {
    fCollection.add(null);
  }

}
