package org.memoriadb.id;

/**
 * An ObjectId which is an integral type (int long, etc.) should implement this interface.
 * @author nienor
 *
 */
public interface IIntegralId {
  
  public long getValue();

}
