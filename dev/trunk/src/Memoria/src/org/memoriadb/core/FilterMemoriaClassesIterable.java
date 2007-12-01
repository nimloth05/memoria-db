package org.memoriadb.core;

import java.util.*;

import org.memoriadb.core.meta.IMemoriaClass;

public class FilterMemoriaClassesIterable implements Iterable<Object> {
  
  
  private static class FilterMemoriaClassIterator implements Iterator<Object> {

    private final Iterator<Object> fIterator;
    private Object fNext;

    public FilterMemoriaClassIterator(Iterator<Object> iterator) {
      fIterator = iterator;
    }

    @Override
    public boolean hasNext() {
      while (fIterator.hasNext()) {
        fNext = fIterator.next();
        if (fNext instanceof IMemoriaClass) continue;
        return true;
      }
      fNext = null;
      return false;
    }

    @Override
    public Object next() {
      if (fNext == null) throw new NoSuchElementException();
      return fNext;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  private final Iterable<Object> fIterable;
  
  public FilterMemoriaClassesIterable(Iterable<Object> iterable) {
    fIterable = iterable;
  }

  @Override
  public Iterator<Object> iterator() {
    return new FilterMemoriaClassIterator(fIterable.iterator());
  }

}
