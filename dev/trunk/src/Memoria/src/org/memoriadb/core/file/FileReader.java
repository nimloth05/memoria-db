package org.memoriadb.core.file;

import java.io.*;

import org.memoriadb.block.Block;
import org.memoriadb.core.block.BlockReader;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.util.Constants;
import org.memoriadb.id.IObjectIdFactory;

/**
 * Reads the content of a {@link IMemoriaFile}.
 * 
 * This protocol must be strictly adhered to
 * 
 * 1. Create the FileReader 
 * 2. readHeader() - file is opened
 * 3. readBlock
 * 4. close()
 * 
 * @author msc
 */
public class FileReader {
  
  private enum State{created, headerRead, blockRead, closed};

  private final IMemoriaFile fFile;
  private State fState = State.created;
  private DataInputStream fStream;
  
  // the current position in the file
  private long fPosition = 0;
  private long fHeadRevision = Constants.INITIAL_HEAD_REVISION;

  public FileReader(IMemoriaFile file) {
    fFile = file;
  }

  /**
   * Reads all blocks and closes the file
   */
  public long readBlocks(IObjectIdFactory idFactory, IFileReaderHandler handler)  throws IOException {
    checkState(State.headerRead, State.blockRead);
    
    // read file header
    while (fStream.available() > 0) {
      BlockReader blockReader = new BlockReader();
      Block block = new Block(fPosition);
      fPosition += blockReader.readBlock(fStream, block, idFactory, handler);
      fHeadRevision = Math.max(fHeadRevision, blockReader.getRevision());
    }
    
    fStream.close();
    fState = State.closed;
    
    return fHeadRevision;
  }

  public Header readHeader() throws IOException {
    checkState(State.created, State.headerRead);
    
    // FIXME sollte hier ein Buffered Reader instantiiert werden? msc
    fStream = new DataInputStream(fFile.getInputStream());
    
    Header result = HeaderHelper.getHeader(fStream);
    fPosition = result.getHeaderSize();
    
    return result;
  }

  private void checkState(State expectedState, State nextState) {
    if(fState != expectedState) throw new MemoriaException("wrong state. Expected " + expectedState +" but was " + fState);
    fState = nextState;
  }


}
