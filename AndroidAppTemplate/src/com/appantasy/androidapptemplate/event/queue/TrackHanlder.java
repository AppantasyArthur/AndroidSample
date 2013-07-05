package com.appantasy.androidapptemplate.event.queue;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.appantasy.androidapptemplate.event.lastchange.LastChangeDO;

public class TrackHanlder extends DefaultHandler  {

	 // this holds the data   
	  private ArrayList<TrackDO> _data;   
	  
	  /**  
	   * Returns the data object  
	   *  
	   * @return  
	   */   
	  public ArrayList<TrackDO> getData() {   
	    return _data;   
	  }   
	  
	  /**  
	   * This gets called when the xml document is first opened  
	   *  
	   * @throws SAXException  
	   */   
	  @Override   
	  public void startDocument() throws SAXException {
		  
		  // 掃描文件開始
	    _data = new ArrayList<TrackDO>();
	    
	  }   
	  
	  /**  
	   * Called when it's finished handling the document  
	   *  
	   * @throws SAXException  
	   */   
	  @Override   
	  public void endDocument() throws SAXException {   
	  
	  }   
	  
	  /**  
	   * This gets called at the start of an element. Here we're also setting the booleans to true if it's at that specific tag. (so we  
	   * know where we are)  
	   *  
	   * @param namespaceURI  
	   * @param localName  
	   * @param qName  
	   * @param atts  
	   * @throws SAXException  
	   */   
	  @Override   
	  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {   
	  
		  String id = atts.getValue("id");
		  String title = atts.getValue("title");
		  
		  if(localName.equalsIgnoreCase("TransportState")){
			  
			  TrackDO doTrack = new TrackDO();
			  doTrack.setId(id);
			  doTrack.setTitle(title);
			  _data.add(doTrack);
			  
		  }
	    
	  }   
	  
	  /**  
	   * Called at the end of the element. Setting the booleans to false, so we know that we've just left that tag.  
	   *  
	   * @param namespaceURI  
	   * @param localName  
	   * @param qName  
	   * @throws SAXException  
	   */   
	  @Override   
	  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {   
	    
	  }   
	  
	  /**  
	   * Calling when we're within an element. Here we're checking to see if there is any content in the tags that we're interested in  
	   * and populating it in the Config object.  
	   *  
	   * @param ch  
	   * @param start  
	   * @param length  
	   */   
	  @Override   
	  public void characters(char ch[], int start, int length) {   
	    
		  //String chars = new String(ch, start, length);   
		  //chars = chars.trim();   
	 
	  }
	
}
