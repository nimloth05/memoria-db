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

//*****************************************************************************
//
//  N E T S T A L   M A S C H I N E N   A G      8 7 5 2   N A E F E L S
//
//-----------------------------------------------------------------------------
//
//  Version    :  $Rev$
//
//*****************************************************************************

package org.memoriadb.core.util.collection;

import org.memoriadb.core.exception.MemoriaException;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Iteriert flach über eine Liste von Iterators.
 * Achtung: Iterable.iterator darf nur einmal aufgerufen werden!
 */
public class CompoundIterator<T> implements Iterator<T>, Iterable<T> {

   private Iterable<Iterator<T>> iters = new ArrayList<Iterator<T>>();

   @SuppressWarnings("unchecked")
   public CompoundIterator(Iterable<?>... iterables) {
      ArrayList<Iterator<T>> iterList = new ArrayList<Iterator<T>>();
      for (Iterable<?> iterable : iterables) {
         iterList.add((Iterator<T>)iterable.iterator());
      }
      iters = iterList;
   }
   
   public CompoundIterator(Iterable<Iterator<T>> iterators) {
      iters = iterators;
   }

   /**
    * Query-function
    */
   @Override
   public boolean hasNext() {
      for (Iterator<T> iter : iters) {
         if (iter.hasNext()) return true;
      }
      return false;
   }

   @Override
   public Iterator<T> iterator() {
      return this;
   }

   /**
    * @return The next element or throws, if no element is left
    */
   @Override
   public T next() {
      Iterator<Iterator<T>> iterators = iters.iterator();
      while (iterators.hasNext()) {
         Iterator<T> current = iterators.next();
         if (current.hasNext()) return current.next();
         iterators.remove();
      }
      throw new MemoriaException("No more elements");
   }

   @Override
   public void remove() {
      throw new UnsupportedOperationException();
   }
   
   @Override
   public String toString() {
      ArrayList<Object> list = new ArrayList<Object>();
      while(hasNext()){
         list.add(next());
      }
      return list.toString();
   }

}
