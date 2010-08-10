package org.memoriadb.core.util.io;

import java.io.*;

public interface IDataInput extends DataInput {
  
  /**
   * @return the number of remaining bytes avaiable 
   * TOOD rename to remaining?
   */
  public int available() throws IOException;

  /**
   * Skips over and discards <code>n</code> bytes of data from this input
   * The <code>skip</code> method may, for a variety of reasons, end
   * up skipping over some smaller number of bytes, possibly <code>0</code>.
   * This may result from any of a number of conditions; reaching end of file
   * before <code>n</code> bytes have been skipped is only one possibility.
   * The actual number of bytes skipped is returned.  If <code>n</code> is
   * negative, no bytes are skipped.
   *
   * @param      n   the number of bytes to be skipped.
   * @return     the actual number of bytes skipped.
   * @exception  IOException  if the stream does not support seek,
   *       or if some other I/O error occurs.
   */
  public long skip(long byteCount) throws IOException;

}
