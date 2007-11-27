package org.memoriadb.core.listener;

import org.memoriadb.block.Block;
import org.memoriadb.util.disposable.*;

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
