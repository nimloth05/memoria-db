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
 * Composite Implementierung des <tt>{@link IDisposable}</tt> Interfaces.
 * 
 * @author Sandro
 * 
 */
public final class Disposables implements IDisposable {

  /**
   * Liste mit den eingetragenen Disposables.
   */
  private final List<IDisposable> fDisposables = new ArrayList<IDisposable>();

  public <T extends IDisposable> T add(T disposable) {
    fDisposables.add(disposable);
    return disposable;
  }

  /**
   * Ruft auf allen enthaltenen Dispose-Objekten dipose auf. Danach wird die Liste geleert.
   * 
   */
  @Override
  public void dispose() {
    Iterator<IDisposable> iterator = fDisposables.iterator();
    while (iterator.hasNext()) {
      disposeObject(iterator.next());
    }
    fDisposables.clear();
  }

  private void disposeObject(IDisposable disposable) {
    disposable.dispose();
  }

}
