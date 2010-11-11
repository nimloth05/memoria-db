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

package org.memoriadb.core.listener;

import org.memoriadb.block.Block;
import org.memoriadb.core.util.disposable.IDisposable;
import org.memoriadb.core.util.disposable.ListenerList;

/**
 * @author Sandro
 */
public class MemoriaListeners implements IMemoriaListeners {

  private final ListenerList<IWriteListener> fWriteListeners = new ListenerList<IWriteListener>();
  
  @Override
  public IDisposable add(IWriteListener listener) {
    return fWriteListeners.add(listener);
  }

  @Override
  public void triggerAfterAppend(Block block) {
    for(IWriteListener listener: fWriteListeners)listener.afterAppend(block);
  }

  @Override
  public void triggerAfterWrite(Block block) {
    for(IWriteListener listener: fWriteListeners)listener.afterWrite(block);
  }

  @Override
  public void triggerBeforeAppend(Block block) {
    for(IWriteListener listener: fWriteListeners)listener.beforeAppend(block);
  }

  @Override
  public void triggerBeforeWrite(Block block) {
    for(IWriteListener listener: fWriteListeners)listener.beforeWrite(block);
  }

}
