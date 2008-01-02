package org.memoriadb.core.util.disposable;

import java.util.*;

/**
 * Composite Implementierung des <tt>{@link IDisposable}</tt> Interfaces.
 * 
 * @author sandro
 * 
 */
public class Disposables implements IDisposable {

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
