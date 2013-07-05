package com.appantasy.androidapptemplate.didllite.item;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class ItemHandler extends DefaultHandler {

	// this holds the data   
		  private ItemDO _data;   
		  
		  /**  
		   * Returns the data object  
		   *  
		   * @return  
		   */   
		  public ItemDO getData() {   
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
		    _data = new ItemDO();
		    
		  }   
		  
		  /**  
		   * Called when it's finished handling the document  
		   *  
		   * @throws SAXException  
		   */   
		  @Override   
		  public void endDocument() throws SAXException {   
		  
		  }   
		  
		  private boolean inTitle; // = false;
		  private boolean inArtist; // = false;
		  private boolean inGenre;
		  private boolean inAlbum;
		  private boolean inAlbumURI;
		  
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
		  
			  String val = atts.getValue("val");
			  Log.d("startElement", localName + " : " + val);
			  
			  inTitle = false;
			  inArtist = false;
			  inGenre = false;
			  inAlbum = false;
			  inAlbumURI = false;
			  
			  if(localName.equalsIgnoreCase("title")){
				  inTitle = true;
				  //_data.setTransportState(val);
			  } else if(localName.equalsIgnoreCase("artist")){
				  inArtist = true;
				  //_data.setCurrentTrackURI(val);
			  } else if(localName.equalsIgnoreCase("genre")){
				  inGenre = true;
				  //_data.setCurrentTrackEmbeddedMetaData(val);
			  } else if(localName.equalsIgnoreCase("album")){
				  inAlbum = true;
				  //_data.setCurrentTrackDuration(val);
			  } else if(localName.equalsIgnoreCase("albumArtURI")){
				  inAlbumURI = true;
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
		    
			  String chars = new String(ch, start, length);   
			  chars = chars.trim();  
			  
			  if (inTitle){
				  _data.setTitle(chars);
				  inTitle = false;
			  }
			  else if (inArtist){
				  _data.setArtist(chars);
				  inArtist = false;
			  }
			  else if (inGenre){
				  _data.setGenre(chars);
				  inGenre = false;
			  }
			  else if (inAlbum){
				  _data.setAlbum(chars);
				  inAlbum = false;
			  }
			  else if (inAlbumURI){
				  _data.setAlbumURI(chars);
				  inAlbumURI = false;
			  }
		 
		  }
	
}
