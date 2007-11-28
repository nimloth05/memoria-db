package org.memoriadb.id;

import java.io.*;

/**
 * This Interface represents an id for a object.
 * 
 * ATTENTION: The implementor must implement <tt>equals()</tt> and <tt>hashCode()</tt>
 * @author sandro
 *
 */
public interface IObjectId {

  public String asString();
  
  public boolean equals(Object object);
  
  public int hashCode();
  
  public void writeTo(DataOutput output) throws IOException;

}
