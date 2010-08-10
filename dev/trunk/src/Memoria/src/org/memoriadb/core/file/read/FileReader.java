/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb.core.file.read;

import java.io.IOException;

import org.memoriadb.block.Block;
import org.memoriadb.core.block.*;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.*;
import org.memoriadb.core.util.Constants;
import org.memoriadb.core.util.io.MemoriaDataInputStream;
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
  }

  private final IMemoriaFile fFile;
  private State fState = State.created;
  private MemoriaDataInputStream fStream;

  // the current position in the file
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
  public long readBlocks(IObjectIdFactory idFactory, ICompressor compressor, IFileReaderHandler handler) throws IOException {
    checkState(State.headerRead, State.blockRead);

    BlockReader blockReader = new BlockReader(compressor);

    while (fStream.available() > 0) {
      Block block = new Block(fStream.getReadBytes());
      IBlockErrorHandler errorHandler = createErrorHandler(fStream.getReadBytes(), block, handler);
      long revision = blockReader.readBlock(fStream, block, idFactory, handler, errorHandler);
      fHeadRevision = Math.max(fHeadRevision, revision);
    }

    fStream.close();
    fState = State.closed;

    return fHeadRevision;
  }

  public Header readHeader() throws IOException {
    checkState(State.created, State.headerRead);

    fStream = new MemoriaDataInputStream(fFile.getInputStream());

    fHeader = HeaderHelper.getHeader(fStream);
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
