package org.memoriadb.core.file;

import java.io.*;

import org.memoriadb.block.Block;
import org.memoriadb.core.block.*;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.util.Constants;
import org.memoriadb.id.IObjectIdFactory;

/**
 * Reads the content of a {@link IMemoriaFile}.
 * 
 * This protocol must be strictly adhered to
 * 
 * 1. Create the FileReader 2. readHeader() - file is opened 3. readBlock 4. close()
 * 
 * @author msc
 */
public class FileReader {

  private enum State {
    created, headerRead, blockRead, closed
  };

  private final IMemoriaFile fFile;
  private State fState = State.created;
  private DataInputStream fStream;

  // the current position in the file
  private long fPosition = 0;
  private long fHeadRevision = Constants.INITIAL_HEAD_REVISION;
  private Header fHeader;

  public FileReader(IMemoriaFile file) {
    fFile = file;
  }

  /**
   * Reads all blocks and closes the file
   * 
   * @return Headrevision
   */
  public long readBlocks(IObjectIdFactory idFactory, IFileReaderHandler handler) throws IOException {
    checkState(State.headerRead, State.blockRead);

    BlockReader blockReader = new BlockReader();

    // read bootstrap-block. isLastWritten is false, because no recovery can be done if the bootstrap-block is corrupt.
    // readBlock(idFactory, handler, blockReader, false);
    // FIXME was ist mit dem bootstrap block?

    // read file header
    while (fStream.available() > 0) {
      Block block = new Block(fPosition);
      IBlockErrorHandler errorHandler = createErrorHandler(fPosition, block, handler);
      fPosition += blockReader.readBlock(fStream, block, idFactory, handler, errorHandler);
      fHeadRevision = Math.max(fHeadRevision, blockReader.getRevision());
    }

    fStream.close();
    fState = State.closed;

    return fHeadRevision;
  }

  public Header readHeader() throws IOException {
    checkState(State.created, State.headerRead);

    fStream = new DataInputStream(fFile.getInputStream());

    fHeader = HeaderHelper.getHeader(fStream);
    fPosition = fHeader.getHeaderSize();

    return fHeader;
  }

  private void checkState(State expectedState, State nextState) {
    if (fState != expectedState) throw new MemoriaException("wrong state. Expected " + expectedState + " but was " + fState);
    fState = nextState;
  }

  private IBlockErrorHandler createErrorHandler(long position, Block block, IFileReaderHandler handler) {
    // bootstrap block must never be corrupt!
    if(position == fHeader.getHeaderSize()) return new AlwaysThrowErrorHandler();
    if(!fHeader.getLastWrittenBlockInfo().isLastWritten(block)) return new AlwaysThrowErrorHandler();
    
    // the last written block is processed!
    
    if(fHeader.getLastWrittenBlockInfo().isAppend()) return new LastAppendedErrorHandler(handler, fFile);
    
    return new LastWrittenErrorHandler(handler);
  }
}
