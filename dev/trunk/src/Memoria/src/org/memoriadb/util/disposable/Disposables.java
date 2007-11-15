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
   private final List<IDisposable> fDisposables = new LinkedList<IDisposable>();

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
