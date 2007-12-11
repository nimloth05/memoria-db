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

import java.util.*;

import org.memoriadb.core.exception.MemoriaException;


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
   public boolean hasNext() {
      for (Iterator<T> iter : iters) {
         if (iter.hasNext()) return true;
      }
      return false;
   }

   public Iterator<T> iterator() {
      return this;
   }

   /**
    * @return The next element or throws, if no element is left
    */
   public T next() {
      Iterator<Iterator<T>> iterators = iters.iterator();
      while (iterators.hasNext()) {
         Iterator<T> current = iterators.next();
         if (current.hasNext()) return current.next();
         iterators.remove();
      }
      throw new MemoriaException("No more elements");
   }

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
