package org.memoriadb;

/**
 * Objects can implement this interface to be notified about relevant stages in their LiveCycle from
 * a memoria-perspective.
 *  
 * @author msc
 */
public interface ILifeCycle {
  
  /**
   * The objectgraph is completely restored. The whole memoria-API can be used inside this function. 
   */
  public void postReconstitute();
  
}
