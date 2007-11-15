//*****************************************************************************
//
//  N E T S T A L   M A S C H I N E N   A G      8 7 5 2   N A E F E L S
//
//-----------------------------------------------------------------------------
//
//  Version    :  $Rev$
//
//*****************************************************************************

package org.memoriadb.util.disposable;

import java.util.*;

public final class ListenerList<T> implements Iterable<T> {
  
  private static class RemoveEntry<T> implements IDisposable {
     
   private T listener;
   private List<T> list;
   
   public RemoveEntry(List<T> list, T listener) {
      this.listener = listener;
      this.list = list;
   }

   public void dispose() {
      list.remove(listener);
      
      list = null;
      listener = null;
   }
     
  }
  
  private final List<T> fListeners = new ArrayList<T>();
  
  public IDisposable add(final T listener) {
    fListeners.add(listener);
    return new RemoveEntry<T>(fListeners, listener);
  }
  
  public boolean isEmpty() {
    return fListeners.isEmpty();
  }

  public Iterator<T> iterator() {
    return getListeners().iterator();
  }

  public void reset() {
    fListeners.clear();
  }

  public int size() {
    return fListeners.size();
  }

  @Override
  public String toString() {
    return fListeners.toString();
  }
  
  /**
   * @return A copy of the listener-list.
   * Very important to allow modifications during notifications. 
   */
  private List<T> getListeners() {
    return new ArrayList<T>(fListeners);
  }
  
  
}