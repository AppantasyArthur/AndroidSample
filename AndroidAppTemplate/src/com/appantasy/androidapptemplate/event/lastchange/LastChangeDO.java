package com.appantasy.androidapptemplate.event.lastchange;

public class LastChangeDO {

	public static String TRANSPORT_STATE = "TransportState";
	public static String CURRENT_TRACK_DURATION = "CurrentTrackDuration";
	public static String RELATIVE_TIME_POSITION = "RelativeTimePosition";
	
	public enum E_TRANSPORT_STATE{
		
		STOPPED("STOPPED"),
		PLAYING("PLAYING"),
		PAUSED("PAUSED");
		
		private String event_value;
		private E_TRANSPORT_STATE(String event_value){
			this.event_value = event_value;
		}
		
		public String getEventValue(){
			return event_value;
		}
		
	};
	
	private String transportState;
	private String currentTrackDuration;
	private String relativeTimePosition;
	
	private String currentTrackURI;
	private String currentTrackEmbeddedMetaData;
	
	
	public String getCurrentTrackURI() {
		return currentTrackURI;
	}

	public void setCurrentTrackURI(String currentTrackURI) {
		this.currentTrackURI = currentTrackURI;
	}

	public String getCurrentTrackEmbeddedMetaData() {
		return currentTrackEmbeddedMetaData;
	}

	public void setCurrentTrackEmbeddedMetaData(String currentTrackEmbeddedMetaData) {
		this.currentTrackEmbeddedMetaData = currentTrackEmbeddedMetaData;
	}

	public String getTransportState() {
		return transportState;
	}

	public void setTransportState(String transportState) {
		this.transportState = transportState;
	}

	public String getCurrentTrackDuration() {
		return currentTrackDuration;
	}

	public void setCurrentTrackDuration(String currentTrackDuration) {
		this.currentTrackDuration = currentTrackDuration;
	}

	public String getRelativeTimePosition() {
		return relativeTimePosition;
	}

	public void setRelativeTimePosition(String relativeTimePosition) {
		this.relativeTimePosition = relativeTimePosition;
	}

	// I know this could be an int, but this is just to show you how it works   
	public String sectionId;   
	public String section;   
	public String area;
	
	@Override
	public String toString() {
		
		StringBuffer ret = new StringBuffer();
		
		return ret.toString();
		
	} 
	
	
	
}
