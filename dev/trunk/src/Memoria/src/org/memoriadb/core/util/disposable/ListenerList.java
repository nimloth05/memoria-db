package org.memoriadb.core.util.disposable;

import java.util.*;

public final class ListenerList<T> implements Iterable<T> {

  private static class RemoveEntry<T> implements IDisposable {

    private T fListener;
    private List<T> fList;

    public RemoveEntry(List<T> list, T listener) {
      this.fListener = listener;
      this.fList = list;
    }

    public void dispose() {
      fList.remove(fListener);

      fList = null;
      fListener = null;
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
   * @return A copy of the listener-list. Very important to allow modifications during notifications.
   */
  private List<T> getListeners() {
    return new ArrayList<T>(fListeners);
  }

}