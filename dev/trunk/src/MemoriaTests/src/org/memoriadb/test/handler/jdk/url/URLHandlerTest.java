package org.memoriadb.test.handler.jdk.url;

import java.net.*;
import java.util.*;

import org.memoriadb.handler.field.FieldbasedDataObject;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.ObjectReferencer;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class URLHandlerTest extends AbstractMemoriaTest {
  
  private static final String URL = "file:/";

  public void test_save_url_dataMode() throws MalformedURLException {
    IObjectId refId = saveURL();
    reopenDataMode();
    
    FieldbasedDataObject l1_ref = fDataStore.get(refId);
    assertEquals(URL,  l1_ref.get(ObjectReferencer.FIELD_NAME).toString());
  }
  
  public void test_save_url_field() throws MalformedURLException {
    IObjectId refId = saveURL();
    
    reopen();
    
    ObjectReferencer l1_ref = fObjectStore.get(refId);
    assertEquals(URL, l1_ref.getObject().toString());
  }

  public void test_save_url_in_array() throws MalformedURLException {
    URL[] urlArray = new URL[] {new URL(URL)};  
    
    IObjectId arrayId = fObjectStore.saveAll(urlArray);
    reopen();
    
    assertEquals(URL, fObjectStore.<URL[]>get(arrayId)[0].toString());
  }
  
  public void test_save_url_in_list() throws MalformedURLException {
    List<URL> urlList = new ArrayList<URL>();
    urlList.add(new URL(URL));
    
    IObjectId listId = fObjectStore.saveAll(urlList);
    reopen();
    
    assertEquals(URL, fObjectStore.<List<URL>>get(listId).get(0).toString());
  }
  
  private IObjectId saveURL() throws MalformedURLException {
    ObjectReferencer ref = new ObjectReferencer();
    
    ref.setObject(new URL(URL));
    IObjectId refId = fObjectStore.saveAll(ref);
    return refId;
  }

}
