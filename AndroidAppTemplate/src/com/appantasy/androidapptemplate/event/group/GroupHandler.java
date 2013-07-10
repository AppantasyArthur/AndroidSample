package com.appantasy.androidapptemplate.event.group;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GroupHandler extends DefaultHandler {
	
	// this holds the data   
	  private GroupVO _data;   
	  
	  /**  
	   * Returns the data object  
	   *  
	   * @return  
	   */   
	  public GroupVO getData() {   
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
	    _data = new GroupVO();
	    
	  }   
	  
	  /**  
	   * Called when it's finished handling the document  
	   *  
	   * @throws SAXException  
	   */   
	  @Override   
	  public void endDocument() throws SAXException {   
	  
	  }
	  
	  private boolean inUDN = false;
	  private boolean inName = false;
	  private boolean inRole = false;
	  
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
	  
		  //String val = atts.getValue("val");
		  //Log.d("startElement", localName + " : " + val);
		 
		  if(localName.equalsIgnoreCase("Device_udn")){
			  inUDN = true;
		  } else if(localName.equalsIgnoreCase("Device_name")){
			  inName = true;
		  } else if(localName.equalsIgnoreCase("Device_role")){
			  inRole = true;
		  } else if(localName.equalsIgnoreCase("GroupMap")){
			  
			  String groupname = atts.getValue("groupname");
			  _data.getGroup().setName(groupname);
			  
		  } else if(localName.equalsIgnoreCase("GroupMember")){
			  
			  String UDN = atts.getValue("UDN");
			  String name = atts.getValue("name");
			  String role = atts.getValue("role");
			  String alive = atts.getValue("alive");
			  String icon = atts.getValue("icon");
			  
			  //Group g = new Group();
			  
			  GroupVO g = new GroupVO();
			  g.setUdn(UDN);
			  g.setName(name);
			  g.setRole(role);
			  
			  if(alive.equalsIgnoreCase("1"))
				  g.setAlive(true);
			  else
				  g.setAlive(false);
			  
			  g.setIcon(icon);
			  
			  _data.getGroup().getMembers().add(g);
			  
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
		  
		  if(inUDN){
			  _data.setUdn(chars);
			  inUDN = false;
		  } else if(inName){
			  _data.setName(chars);
			  inName = false;
		  } else if(inRole){
			  _data.setRole(chars);
			  inRole = false;
		  }
	 
	  }

}
