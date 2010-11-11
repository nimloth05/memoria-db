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

package org.memoriadb.core.util.disposable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Sandro
 */
public final class ListenerList<T> implements Iterable<T> {

  private static class RemoveEntry<T> implements IDisposable {

    private T fListener;
    private List<T> fList;

    public RemoveEntry(List<T> list, T listener) {
      this.fListener = listener;
      this.fList = list;
    }

    @Override
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

  @Override
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