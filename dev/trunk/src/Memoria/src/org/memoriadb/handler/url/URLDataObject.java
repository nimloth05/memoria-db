package org.memoriadb.handler.url;

import java.net.URL;

import org.memoriadb.handler.IDataObject;
import org.memoriadb.id.IObjectId;

public class URLDataObject implements IDataObject {
  
  private final IObjectId fClassId;
  private URL fURL;
  
  public URLDataObject(IObjectId classId, URL url) {
    fClassId = classId;
    fURL = url;
  }

  @Override
  public IObjectId getMemoriaClassId() {
    return fClassId;
  }
  
  public URL getURL() {
    return fURL;
  }
  
  public void setURL(URL url) {
    fURL = url;
  }
  
  @Override
  public String toString() {
    return fURL.toString();
  }

}
