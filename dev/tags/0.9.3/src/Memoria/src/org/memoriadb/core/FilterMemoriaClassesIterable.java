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

package org.memoriadb.core;

import org.memoriadb.core.meta.IMemoriaClass;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Sandro
 */
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
