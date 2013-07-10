package com.appantasy.androidapptemplate;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.teleal.cling.android.AndroidUpnpService;
import org.teleal.cling.android.AndroidUpnpServiceImpl;
import org.teleal.cling.controlpoint.ActionCallback;
import org.teleal.cling.controlpoint.SubscriptionCallback;
import org.teleal.cling.model.action.ActionArgumentValue;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.gena.CancelReason;
import org.teleal.cling.model.gena.GENASubscription;
import org.teleal.cling.model.message.UpnpResponse;
import org.teleal.cling.model.meta.Action;
import org.teleal.cling.model.meta.ActionArgument;
import org.teleal.cling.model.meta.ActionArgument.Direction;
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.state.StateVariableValue;
import org.teleal.cling.registry.DefaultRegistryListener;
import org.teleal.cling.registry.Registry;
import org.teleal.cling.registry.RegistryListener;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.appantasy.androidapptemplate.didllite.item.ItemDO;
import com.appantasy.androidapptemplate.didllite.item.ItemHandler;
import com.appantasy.androidapptemplate.event.group.GroupHandler;
import com.appantasy.androidapptemplate.event.group.GroupVO;
import com.appantasy.androidapptemplate.event.lastchange.LastChangeDO;
import com.appantasy.androidapptemplate.event.lastchange.LastChangeHandler;



public class MainActivity extends ListActivity {
	
	private static Logger log = Logger.getLogger(MainActivity.class.getName());
	
	// PROPERTIES
	
	private ArrayAdapter<DeviceDisplay> listAdapter;

    private AndroidUpnpService upnpService; // android's service
    
    private RegistryListener registryListener = new BrowseRegistryListener();
    private ServiceConnection serviceConnection = new ServiceConnection() {

    	@Override
        public void onServiceConnected(ComponentName className, IBinder service) { // 
            
    		upnpService = (AndroidUpnpService) service; // 一個協定、interface
            
            //UpnpService us = upnpService.get(); // 真正存取 upnp 的 service，非 Android Service

            // Refresh the list with all known devices
            listAdapter.clear();
            for (Device device : upnpService.getRegistry().getDevices()) {
                ((BrowseRegistryListener) registryListener).deviceAdded(device); // 加到 ListView
            }

            // Getting ready for future device advertisements
            upnpService.getRegistry().addListener(registryListener); // 呼叫 Service 裡的 Registry，加入 Listener (from Activity)

            // Search asynchronously for all devices
            upnpService.getControlPoint().search(); // 呼叫 Service 裡的 upnpservice 開始 search
        }

    	@Override
        public void onServiceDisconnected(ComponentName className) {
            upnpService = null;
        }

    };
   
    private String xml =	"<Event xmlns=\"urn:schemas-upnp-org:metadata-1-0/AVT/\">" +
			"<InstanceID val=\"0\">" +
				"<TransportState val=\"STOPPED\"/>" +
				"<TransportStatus val=\"PREPARE\"/>" +
				"<CurrentTrack val=\"1\"/>" + 
				"<CurrentTrackURI val=\"http://192.168.1.11:9000/disk/DLNA-PNMP3-OP01-FLAGS01700000/music/O7I70/Open%20All%20Night.mp3\"/>" +
				"<CurrentTrackEmbeddedMetaData val=\"&lt;DIDL-Lite xmlns=&quot;urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/&quot; xmlns:dc=&quot;http://purl.org/dc/elements/1.1/&quot; xmlns:upnp=&quot;urn:schemas-upnp-org:metadata-1-0/upnp/&quot;&gt;&lt;item id=&quot;7I70&quot; parentID=&quot;7&quot; restricted=&quot;1&quot;&gt;     &lt;dc:title&gt;Open All Night&lt;/dc:title&gt;     &lt;dc:date&gt;2002-01-01&lt;/dc:date&gt;     &lt;upnp:artist&gt;Bon Jovi&lt;/upnp:artist&gt;     &lt;upnp:genre&gt;Rock&lt;/upnp:genre&gt;     &lt;upnp:album&gt;Bounce&lt;/upnp:album&gt;     &lt;upnp:originalTrackNumber&gt;12&lt;/upnp:originalTrackNumber&gt;     &lt;dc:creator&gt;Bon Jovi&lt;/dc:creator&gt;     &lt;upnp:albumArtURI dlna:profileID=&quot;JPEG_TN&quot;&gt;http://192.168.1.11:9000/cgi-bin/W160/H160/S267/L15463/Xjpeg-scale.desc/O7I70.jpg&lt;/upnp:albumArtURI&gt;     &lt;upnp:albumArtist&gt;Bon Jovi&lt;/upnp:albumArtist&gt;     &lt;pv:lastPlayedTime&gt;2013-07-01T17:14:12&lt;/pv:lastPlayedTime&gt;     &lt;pv:playcount&gt;1&lt;/pv:playcount&gt;     &lt;pv:modificationTime&gt;1338984350&lt;/pv:modificationTime&gt;     &lt;pv:addedTime&gt;1368092439&lt;/pv:addedTime&gt;     &lt;pv:lastUpdated&gt;1372670052&lt;/pv:lastUpdated&gt;     &lt;res resolution=&quot;&quot;  colorDepth=&quot;0&quot;  size=&quot;6265580&quot; duration=&quot;0:04:20&quot; protocolInfo=&quot;http-get:*:audio/mpeg:DLNA.ORG_PN=MP3;DLNA.ORG_OP=01;DLNA.ORG_FLAGS=01700000000000000000000000000000&quot;&gt;http://192.168.1.11:9000/disk/DLNA-PNMP3-OP01-FLAGS01700000/music/O7I70/Open%20All%20Night.mp3&lt;/res&gt;     &lt;upnp:class&gt;object.item.audioItem.musicTrack&lt;/upnp:class&gt;   &lt;/item&gt;&lt;/DIDL-Lite&gt;\"/>" +
				"<CurrentTrackMetaData val=\"&lt;DIDL-Lite xmlns=&quot;urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/&quot; xmlns:dc=&quot;http://purl.org/dc/elements/1.1/&quot; xmlns:upnp=&quot;urn:schemas-upnp-org:metadata-1-0/upnp/&quot;&gt;&lt;item id=&quot;7I70&quot; parentID=&quot;7&quot; restricted=&quot;1&quot;&gt;     &lt;dc:title&gt;Open All Night&lt;/dc:title&gt;     &lt;dc:date&gt;2002-01-01&lt;/dc:date&gt;     &lt;upnp:artist&gt;Bon Jovi&lt;/upnp:artist&gt;     &lt;upnp:genre&gt;Rock&lt;/upnp:genre&gt;     &lt;upnp:album&gt;Bounce&lt;/upnp:album&gt;     &lt;upnp:originalTrackNumber&gt;12&lt;/upnp:originalTrackNumber&gt;     &lt;dc:creator&gt;Bon Jovi&lt;/dc:creator&gt;     &lt;upnp:albumArtURI dlna:profileID=&quot;JPEG_TN&quot;&gt;http://192.168.1.11:9000/cgi-bin/W160/H160/S267/L15463/Xjpeg-scale.desc/O7I70.jpg&lt;/upnp:albumArtURI&gt;     &lt;upnp:albumArtist&gt;Bon Jovi&lt;/upnp:albumArtist&gt;     &lt;pv:lastPlayedTime&gt;2013-07-01T17:14:12&lt;/pv:lastPlayedTime&gt;     &lt;pv:playcount&gt;1&lt;/pv:playcount&gt;     &lt;pv:modificationTime&gt;1338984350&lt;/pv:modificationTime&gt;     &lt;pv:addedTime&gt;1368092439&lt;/pv:addedTime&gt;     &lt;pv:lastUpdated&gt;1372670052&lt;/pv:lastUpdated&gt;     &lt;res resolution=&quot;&quot;  colorDepth=&quot;0&quot;  size=&quot;6265580&quot; duration=&quot;0:04:20&quot; protocolInfo=&quot;http-get:*:audio/mpeg:DLNA.ORG_PN=MP3;DLNA.ORG_OP=01;DLNA.ORG_FLAGS=01700000000000000000000000000000&quot;&gt;http://192.168.1.11:9000/disk/DLNA-PNMP3-OP01-FLAGS01700000/music/O7I70/Open%20All%20Night.mp3&lt;/res&gt;     &lt;upnp:class&gt;object.item.audioItem.musicTrack&lt;/upnp:class&gt;   &lt;/item&gt;&lt;/DIDL-Lite&gt;\"/>" +
				"<CurrentTrackDuration val=\"0:04:20\"/>" +
				"<CurrentMediaDuration val=\"0:04:20\"/>" +
				"<NumberOfTracks val=\"1\"/>" +
				"<AVTransportURI val=\"http://192.168.1.11:9000/disk/DLNA-PNMP3-OP01-FLAGS01700000/music/O7I70/Open%20All%20Night.mp3\"/>" +
				"<AVTransportURIMetaData val=\"&lt;DIDL-Lite xmlns=&quot;urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/&quot; xmlns:dc=&quot;http://purl.org/dc/elements/1.1/&quot; xmlns:upnp=&quot;urn:schemas-upnp-org:metadata-1-0/upnp/&quot;&gt;&lt;item id=&quot;7I70&quot; parentID=&quot;7&quot; restricted=&quot;1&quot;&gt;     &lt;dc:title&gt;Open All Night&lt;/dc:title&gt;     &lt;dc:date&gt;2002-01-01&lt;/dc:date&gt;     &lt;upnp:artist&gt;Bon Jovi&lt;/upnp:artist&gt;     &lt;upnp:genre&gt;Rock&lt;/upnp:genre&gt;     &lt;upnp:album&gt;Bounce&lt;/upnp:album&gt;     &lt;upnp:originalTrackNumber&gt;12&lt;/upnp:originalTrackNumber&gt;     &lt;dc:creator&gt;Bon Jovi&lt;/dc:creator&gt;     &lt;upnp:albumArtURI dlna:profileID=&quot;JPEG_TN&quot;&gt;http://192.168.1.11:9000/cgi-bin/W160/H160/S267/L15463/Xjpeg-scale.desc/O7I70.jpg&lt;/upnp:albumArtURI&gt;     &lt;upnp:albumArtist&gt;Bon Jovi&lt;/upnp:albumArtist&gt;     &lt;pv:lastPlayedTime&gt;2013-07-01T17:14:12&lt;/pv:lastPlayedTime&gt;     &lt;pv:playcount&gt;1&lt;/pv:playcount&gt;     &lt;pv:modificationTime&gt;1338984350&lt;/pv:modificationTime&gt;     &lt;pv:addedTime&gt;1368092439&lt;/pv:addedTime&gt;     &lt;pv:lastUpdated&gt;1372670052&lt;/pv:lastUpdated&gt;     &lt;res resolution=&quot;&quot;  colorDepth=&quot;0&quot;  size=&quot;6265580&quot; duration=&quot;0:04:20&quot; protocolInfo=&quot;http-get:*:audio/mpeg:DLNA.ORG_PN=MP3;DLNA.ORG_OP=01;DLNA.ORG_FLAGS=01700000000000000000000000000000&quot;&gt;http://192.168.1.11:9000/disk/DLNA-PNMP3-OP01-FLAGS01700000/music/O7I70/Open%20All%20Night.mp3&lt;/res&gt;     &lt;upnp:class&gt;object.item.audioItem.musicTrack&lt;/upnp:class&gt;   &lt;/item&gt;&lt;/DIDL-Lite&gt;\"/>" +
				"<CurrentTransportActions val=\"Play,Stop,Pause,Seek,Next,Previous,X_DLNA_SeekTime,X_DLNA_SeekByte\"/>" +
			"</InstanceID>" +
		"</Event>";
    
    private String didlLite = "<DIDL-Lite xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\" xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\" xmlns:pv=\"http://www.pv.com/pvns/\" xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\">" +
    							"<item id=\"7I70\" parentID=\"7\" restricted=\"1\">" +
    							"<dc:title>Open All Night</dc:title>" +
    							"<dc:date>2002-01-01</dc:date>" +
    							"<upnp:artist>Bon Jovi</upnp:artist>" +
    							"<upnp:genre>Rock</upnp:genre>" +
    							"<upnp:album>Bounce</upnp:album>" +
    							"<upnp:originalTrackNumber>12</upnp:originalTrackNumber>" +
    							"<dc:creator>Bon Jovi</dc:creator>" +
    							"<upnp:albumArtURI dlna:profileID=\"JPEG_TN\">http://192.168.1.11:9000/cgi-bin/W160/H160/S267/L15463/Xjpeg-scale.desc/O7I70.jpg</upnp:albumArtURI>" +
    								"<upnp:albumArtist>Bon Jovi</upnp:albumArtist>" +
    								"<pv:lastPlayedTime>2013-07-01T17:14:12</pv:lastPlayedTime>" +
    								"<pv:playcount>1</pv:playcount>" +
    								"<pv:modificationTime>1338984350</pv:modificationTime>" +
    								"<pv:addedTime>1368092439</pv:addedTime>" +
    								"<pv:lastUpdated>1372670052</pv:lastUpdated>" +
    								"<res size=\"6265580\" duration=\"0:04:20\" protocolInfo=\"http-get:*:audio/mpeg:DLNA.ORG_PN=MP3;DLNA.ORG_OP=01;DLNA.ORG_FLAGS=01700000000000000000000000000000\">" +
    									"http://192.168.1.11:9000/disk/DLNA-PNMP3-OP01-FLAGS01700000/music/O7I70/Open%20All%20Night.mp3" +
    								"</res>" +
    							"<upnp:class>object.item.audioItem.musicTrack</upnp:class>" +
    							"</item>" +
    							"</DIDL-Lite>";
    
    private String embMeta = "<DIDL-Lite xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\"><item id=\"7I70\" parentID=\"7\" restricted=\"1\"> <dc:title>Open All Night</dc:title> <dc:date>2002-01-01</dc:date> <upnp:artist>Bon Jovi</upnp:artist> <upnp:genre>Rock</upnp:genre> <upnp:album>Bounce</upnp:album> <upnp:originalTrackNumber>12</upnp:originalTrackNumber> <dc:creator>Bon Jovi</dc:creator> <upnp:albumArtURI dlna:profileID=\"JPEG_TN\">http://192.168.1.11:9000/cgi-bin/W160/H160/S267/L15463/Xjpeg-scale.desc/O7I70.jpg</upnp:albumArtURI> <upnp:albumArtist>Bon Jovi</upnp:albumArtist> <pv:lastPlayedTime>2013-07-01T17:14:12</pv:lastPlayedTime> <pv:playcount>1</pv:playcount> <pv:modificationTime>1338984350</pv:modificationTime> <pv:addedTime>1368092439</pv:addedTime> <pv:lastUpdated>1372670052</pv:lastUpdated> <res resolution=\"\" colorDepth=\"0\" size=\"6265580\" duration=\"0:04:20\" protocolInfo=\"http-get:*:audio/mpeg:DLNA.ORG_PN=MP3;DLNA.ORG_OP=01;DLNA.ORG_FLAGS=01700000000000000000000000000000\">http://192.168.1.11:9000/disk/DLNA-PNMP3-OP01-FLAGS01700000/music/O7I70/Open%20All%20Night.mp3</res> <upnp:class>object.item.audioItem.musicTrack</upnp:class> </item></DIDL-Lite>";
    
    private String errMeta = "<Event xmlns=\"urn:schemas-upnp-org:metadata-1-0/AVT/\"><InstanceID val=\"0\"><RecordStorageMedium val=\"NOT_IMPLEMENTED\"/><CurrentPlayMode val=\"NORMAL\"/><TransportState val=\"STOPPED\"/><RecordMediumWriteStatus val=\"NOT_IMPLEMENTED\"/><CurrentRecordQualityMode val=\"NOT_IMPLEMENTED\"/><PlaybackStorageMedium val=\"NOT_IMPLEMENTED\"/><NumberOfTracks val=\"1\"/><CurrentMediaDuration val=\"0:00:00\"/><CurrentTrackURI val=\"\"/><NextAVTransportURIMetaData val=\"NOT_IMPLEMENTED\"/><PossiblePlaybackStorageMedia val=\"NOT_IMPLEMENTED\"/><PossibleRecordStorageMedia val=\"NOT_IMPLEMENTED\"/><AVTransportURI val=\"http://opml.radiotime.com/Tune.ashx?id=s90625&partnerId=iq4GbQ5&serial=e069954d826b\"/><NextAVTransportURI val=\"NOT_IMPLEMENTED\"/><TransportStatus val=\"OK\"/><CurrentTrackMetaData val=\"\"/><AVTransportURIMetaData val=\"&lt;DIDL-Lite xmlns:dc=&quot;http://purl.org/dc/elements/1.1/&quot; xmlns:upnp=&quot;urn:schemas-upnp-org:metadata-1-0/upnp/&quot; xmlns:dlna=&quot;urn:schemas-dlna-org:metadata-1-0/&quot; xmlns=&quot;urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/&quot;&gt;&lt;item id=&quot;Local Radio-Asia FM 92.3 | (Top 40/Pop)&quot; parentID=&quot;Local Radio&quot; restricted=&quot;0&quot; dlna:dlnaManaged=&quot;00000004&quot;&gt;&lt;dc:title&gt;Asia FM 92.3 | (Top 40/Pop)&lt;/dc:title&gt;&lt;upnp:class&gt;object.item.audioItem.musicTrack&lt;/upnp:class&gt;&lt;res protocolInfo=&quot;http-get:*:audio/x-mpegurl:*&quot; bitrate=&quot;64&quot;&gt;http://opml.radiotime.com/Tune.ashx?id=s90625&amp;partnerId=iq4GbQ5&amp;serial=e069954d826b&lt;/res&gt;&lt;upnp:albumArtURI dlna:profileID=&quot;JPEG_TN&quot;&gt;http://d1i6vahw24eb07.cloudfront.net/s3000q.png&lt;/upnp:albumArtURI&gt;&lt;/item&gt;&lt;/DIDL-Lite&gt;\"/><CurrentTrackDuration val=\"0:00:00\"/><PossibleRecordQualityModes val=\"NOT_IMPLEMENTED\"/><CurrentTransportActions val=\"Play,Seek,X_DLNA_SeekTime\"/><CurrentTrack val=\"1\"/></InstanceID></Event>";
    
    private String testErrMeta = "<Event xmlns=\"urn:schemas-upnp-org:metadata-1-0/AVT/\">"+
    		"<InstanceID val=\"0\">"+
        "<RecordStorageMedium val=\"NOT_IMPLEMENTED\"/>"+
        "<CurrentPlayMode val=\"NORMAL\"/>"+
        "<TransportState val=\"STOPPED\"/>"+
        "<RecordMediumWriteStatus val=\"NOT_IMPLEMENTED\"/>"+
        "<CurrentRecordQualityMode val=\"NOT_IMPLEMENTED\"/>"+
        "<PlaybackStorageMedium val=\"NOT_IMPLEMENTED\"/>"+
        "<NumberOfTracks val=\"1\"/>"+
        "<CurrentMediaDuration val=\"0:00:00\"/>"+
        "<CurrentTrackURI val=\"\"/>"+
        "<NextAVTransportURIMetaData val=\"NOT_IMPLEMENTED\"/>"+
        "<PossiblePlaybackStorageMedia val=\"NOT_IMPLEMENTED\"/>"+
        "<PossibleRecordStorageMedia val=\"NOT_IMPLEMENTED\"/>"+
        "<AVTransportURI val=\"http://opml.radiotime.com/Tune.ashx?id=s90625&partnerId=iq4GbQ5&serial=e069954d826b\"/>"+
        "<NextAVTransportURI val=\"NOT_IMPLEMENTED\"/>"+
        "<TransportStatus val=\"OK\"/>"+
        "<CurrentTrackMetaData val=\"\"/>"+
        "<AVTransportURIMetaData val=\"&lt;DIDL-Lite xmlns:dc=&quot;http://purl.org/dc/elements/1.1/&quot; xmlns:upnp=&quot;urn:schemas-upnp-org:metadata-1-0/upnp/&quot; xmlns:dlna=&quot;urn:schemas-dlna-org:metadata-1-0/&quot; xmlns=&quot;urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/&quot;&gt;&lt;item id=&quot;Local Radio-Asia FM 92.3 | (Top 40/Pop)&quot; parentID=&quot;Local Radio&quot; restricted=&quot;0&quot; dlna:dlnaManaged=&quot;00000004&quot;&gt;&lt;dc:title&gt;Asia FM 92.3 | (Top 40/Pop)&lt;/dc:title&gt;&lt;upnp:class&gt;object.item.audioItem.musicTrack&lt;/upnp:class&gt;&lt;res protocolInfo=&quot;http-get:*:audio/x-mpegurl:*&quot; bitrate=&quot;64&quot;&gt;http://opml.radiotime.com/Tune.ashx?id=s90625&amp;partnerId=iq4GbQ5&amp;serial=e069954d826b&lt;/res&gt;&lt;upnp:albumArtURI dlna:profileID=&quot;JPEG_TN&quot;&gt;http://d1i6vahw24eb07.cloudfront.net/s3000q.png&lt;/upnp:albumArtURI&gt;&lt;/item&gt;&lt;/DIDL-Lite&gt;\"/>"+
        "<CurrentTrackDuration val=\"0:00:00\"/>"+
        "<PossibleRecordQualityModes val=\"NOT_IMPLEMENTED\"/>"+
        "<CurrentTransportActions val=\"Play,Seek,X_DLNA_SeekTime\"/>"+
        "<CurrentTrack val=\"1\"/>"+
	"</InstanceID>"+
"</Event>";
    
    private String _group = "<?xml version=\"1.0\"?> "+
"<e:propertyset xmlns:e=\"urn:schemas-upnp-org:event-1-0\">"+
    "<e:property>"+
     "   <Device_udn>uuid:60f87458-e7e7-4b2d-b3c1-4ed9a831e62a</Device_udn>"+
        
    "</e:property>"+
    "<e:property>"+
     "   <Device_name>AGS</Device_name>"+
            
    "</e:property>"+
    "<e:property>"+
     "   <Device_role>DEVICEROLETYPE_E_MASTER</Device_role>"+
     
    "</e:property>"+
    "<e:property>"+
     "   <GroupMap groupname=\"AGS + AGS777\">"+
      "      <GroupMember UDN=\"uuid:60f87458-e7e7-4b2d-b3c1-4ed9a831e62a\" name=\"AGS\" role=\"DEVICEROLETYPE_E_MASTER\" alive=\"1\" icon=\"icon1\"></GroupMember>"+
      "      <GroupMember UDN=\"uuid:10a4890b-a255-4871-8c17-225d85366b17\" name=\"AGS777\" role=\"DEVICEROLETYPE_E_SLAVE\" alive=\"0\" icon=\"icon1\"></GroupMember>"+
            
       " </GroupMap>"+
     
    "</e:property>"+
   
"</e:propertyset> ";
    
    // METHODS
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		GroupVO data = _parseGroupInfo(_group); 
		System.out.println(data);
		
		//ItemDO data = _parseDidleListItem(embMeta); 
		//System.out.println(data);
		//LastChangeDO data = _parseLastChangeEvent(testErrMeta); 
		//if(data != null)
		//	Log.d(this.toString(), data.toString());
		
		//ServerRunner.run(DebugServer.class);
		
        listAdapter =
            new ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1
            );
        
        //listAdapter.
        setListAdapter(listAdapter);
		
        //綁定 service
        getApplicationContext().bindService(
            new Intent(this, AndroidUpnpServiceImpl.class), // intent service :  AndroidUpnpServiceImpl，android service
            serviceConnection, // action after connect/disconnect service
            Context.BIND_AUTO_CREATE
        );
		
		
	}
	
	private GroupVO _parseGroupInfo(String groupInfo) {
		
		GroupVO data = new GroupVO();
		
		try {   
			  
		    SAXParserFactory spf = SAXParserFactory.newInstance();   
		    SAXParser sp = spf.newSAXParser();   
		    XMLReader xr = sp.getXMLReader();   
		  
		    GroupHandler dataHandler = new GroupHandler();   
		    xr.setContentHandler(dataHandler);   
		  
		    if(true){
		    	
		    	xr.parse(new InputSource(new StringReader(groupInfo))); 
			    data = dataHandler.getData();  
		    	
		    } 
		    
		  } catch(ParserConfigurationException pce) {   
		    Log.e("SAX XML", "sax parse error", pce);   
		  } catch(SAXException se) {   
		    Log.e("SAX XML", "sax error", se);   
		  } catch(IOException ioe) {   
		    Log.e("SAX XML", "sax parse io error", ioe);   
		  } catch(Exception e) {
			  e.printStackTrace();
		  }  
		
		return data;
		
	}

	private ItemDO _parseDidleListItem(String metadata) {
		
		ItemDO data = new ItemDO();
		
		try {   
			  
		    SAXParserFactory spf = SAXParserFactory.newInstance();   
		    SAXParser sp = spf.newSAXParser();   
		    XMLReader xr = sp.getXMLReader();   
		  
		    ItemHandler dataHandler = new ItemHandler();   
		    xr.setContentHandler(dataHandler);   
		  
		    if(true){
		    			
		    	//  dlna:profileID=\"JPEG_TN\"
		    	//String dlna = ;
		    	metadata = metadata.replace(" dlna:profileID=\"JPEG_TN\"", "");
		    	metadata = metadata.replace("pv:", "");
		    	xr.parse(new InputSource(new StringReader(metadata))); 
			    data = dataHandler.getData();  
		    	
		    } 
		    
		  } catch(ParserConfigurationException pce) {   
		    Log.e("SAX XML", "sax parse error", pce);   
		  } catch(SAXException se) {   
		    Log.e("SAX XML", "sax error", se);   
		  } catch(IOException ioe) {   
		    Log.e("SAX XML", "sax parse io error", ioe);   
		  } catch(Exception e) {
			  e.printStackTrace();
		  }  
		
		return data;
		
	}

	private LastChangeDO _parseLastChangeEvent(String xml) {   
		
		LastChangeDO data = null;   
		  
		  // sax stuff   
		  try {   
			  
		    SAXParserFactory spf = SAXParserFactory.newInstance();   
		    SAXParser sp = spf.newSAXParser();   
		    XMLReader xr = sp.getXMLReader();   
		  
		    LastChangeHandler dataHandler = new LastChangeHandler();   
		    xr.setContentHandler(dataHandler);   
		  
		    if(true){
		    			
		    	xml = xml.replace("&", "&amp;");
		    	xr.parse(new InputSource(new StringReader(xml))); 
			    data = dataHandler.getData();  
		    	
		    } 
		    
		  } catch(ParserConfigurationException pce) {   
		    Log.e("SAX XML", "sax parse error", pce);   
		  } catch(SAXException se) {   
		    Log.e("SAX XML", "sax error", se);   
		  } catch(IOException ioe) {   
		    Log.e("SAX XML", "sax parse io error", ioe);   
		  } catch(Exception e) {
			  e.printStackTrace();
		  }  
		  
		  return data;   
	
	}
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
        if (upnpService != null) {
            upnpService.getRegistry().removeListener(registryListener);
        }
        getApplicationContext().unbindService(serviceConnection);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, 0, 0, R.string.search_lan)
	        .setIcon(android.R.drawable.ic_menu_search);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.getItemId() == 0 && upnpService != null) {
	        upnpService.getRegistry().removeAllRemoteDevices();
	        upnpService.getControlPoint().search();
	    }
	    return false;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		DeviceDisplay dd = (DeviceDisplay)getListView().getItemAtPosition(position);
		Device d = dd.getDevice();
		log.info("click " + d.getType().getType());
		
		// Monitor
		Service serviceSwitchPower = null;
		Service[] deviceServices = d.getServices();
		for(int i = 0;i < deviceServices.length;i++){
			if(deviceServices[i].getServiceType().toString().endsWith("urn:schemas-upnp-org:service:SwitchPower:1")){
				serviceSwitchPower = deviceServices[i];
			}
		}
		SwitchSubscriptionCallback sc = new SwitchSubscriptionCallback(serviceSwitchPower == null ? deviceServices[0] : serviceSwitchPower);
		upnpService.getControlPoint().execute(sc);
		//upnpService.getControlPoint().
		
		// Invoke
		Action getTarget = serviceSwitchPower.getAction("GetTarget");
		
		Action setTarget = serviceSwitchPower.getAction("Play");
		ActionArgumentValue[] values = new ActionArgumentValue[3];
		
		values[0] = new ActionArgumentValue(new ActionArgument("NewTargetValue", "Target", Direction.IN), true);
		
		ActionInvocation ai = new ActionInvocation(getTarget);
		
		//ActionInvocation ai = new ActionInvocation(setTarget, values);
		
		SwitchActionCallback sac = new SwitchActionCallback(ai);
		upnpService.getControlPoint().execute(sac);
		
		super.onListItemClick(l, v, position, id);
		
	}
	
	class SwitchActionCallback extends ActionCallback{

		protected SwitchActionCallback(ActionInvocation actionInvocation) {
			super(actionInvocation);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void failure(ActionInvocation arg0, UpnpResponse arg1,
				String arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void success(ActionInvocation invocation) {
		
			log.info("Action : " + invocation.getAction().getName() + ", Value : " + invocation.getOutput());
			
		}
		
	}

	class SwitchSubscriptionCallback extends SubscriptionCallback{

		protected SwitchSubscriptionCallback(Service service) {
			super(service);
			
		}

		@Override
		protected void ended(GENASubscription arg0, CancelReason arg1,
				UpnpResponse arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void established(GENASubscription arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void eventReceived(GENASubscription subscription) {
			
			for (Map.Entry<String, StateVariableValue> entry : ((Map<String, StateVariableValue>) subscription.getCurrentValues()).entrySet()) {
            
				log.info("switch event : " + entry.getKey() + ", value : " + entry.getValue());
				
			}
			
		}

		@Override
		protected void eventsMissed(GENASubscription arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void failed(GENASubscription arg0, UpnpResponse arg1,
				Exception arg2, String arg3) {
			// TODO Auto-generated method stub
			
		}
		
	}


	class BrowseRegistryListener extends DefaultRegistryListener {

		@Override
	    public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
	        //deviceAdded(device);
	    }

	    @Override
	    public void remoteDeviceDiscoveryFailed(Registry registry, final RemoteDevice device, final Exception ex) {
	        runOnUiThread(new Runnable() {
	            public void run() {
	                /*Toast.makeText(
	                        BrowseActivity.this,
	                        "Discovery failed of '" + device.getDisplayString() + "': " +
	                                (ex != null ? ex.toString() : "Couldn't retrieve device/service descriptors"),
	                        Toast.LENGTH_LONG
	                ).show();*/
	            }
	        });
	        deviceRemoved(device);
	    }

	    @Override
	    public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
	        deviceAdded(device);
	        System.out.println("remote found : " + device.getType().getDisplayString());
	    }

	    @Override
	    public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
	        deviceRemoved(device);
	    }

	    @Override
	    public void localDeviceAdded(Registry registry, LocalDevice device) {
	        //deviceAdded(device);
	    	System.out.println("local found : " + device.getType().getDisplayString());
	    }

	    @Override
	    public void localDeviceRemoved(Registry registry, LocalDevice device) {
	        deviceRemoved(device);
	    }

	    public void deviceAdded(final Device device) {
	        runOnUiThread(new Runnable() {
	            public void run() {
	                DeviceDisplay d = new DeviceDisplay(device);
	                int position = listAdapter.getPosition(d);
	                if (position >= 0) {
	                    // Device already in the list, re-set new value at same position
	                    listAdapter.remove(d);
	                    listAdapter.insert(d, position);
	                } else {
	                    listAdapter.add(d);
	                }
	            }
	        });
	    }

	    public void deviceRemoved(final Device device) {
	        runOnUiThread(new Runnable() {
	            public void run() {
	                listAdapter.remove(new DeviceDisplay(device));
	            }
	        });
	    }
		
	}
	
}
