package org.memoriadb.core.listener;

import org.memoriadb.core.block.Block;
import org.memoriadb.util.disposable.IDisposable;

public interface IMemoriaListeners {
  public IDisposable add(IWriteListener listener);
  
  public void triggerAfterAppend(Block block);
  public void triggerAfterWrite(Block block);
  
  public void triggerBeforeAppend(Block block);
  public void triggerBeforeWrite(Block block);

  
}
