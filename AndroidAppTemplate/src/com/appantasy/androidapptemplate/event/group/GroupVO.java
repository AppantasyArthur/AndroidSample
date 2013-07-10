package com.appantasy.androidapptemplate.event.group;

import java.util.ArrayList;

public class GroupVO {

	private boolean isMaster = false;
	private boolean isSlave = false;
	private boolean isStandalone = false;
	
	private String udn;
	private String name;
	private GROUP_ROLE role;
	
	private boolean alive = false; // for group member
	private String icon = ""; // for group member
	
	private Group group = new Group();
	
	public boolean isMaster() {
		return isMaster;
	}

	public void setMaster(boolean isMaster) {
		this.isMaster = isMaster;
	}

	public boolean isSlave() {
		return isSlave;
	}

	public void setSlave(boolean isSlave) {
		this.isSlave = isSlave;
	}

	public boolean isStandalone() {
		return isStandalone;
	}

	public void setStandalone(boolean isStandalone) {
		this.isStandalone = isStandalone;
	}

	public String getUdn() {
		return udn;
	}

	public void setUdn(String udn) {
		this.udn = udn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GROUP_ROLE getRole() {
		return role;
	}

	public void setRole(String role) {
		
		if(role.equalsIgnoreCase("DEVICEROLETYPE_E_MASTER")){
			isMaster = true;
			this.role = GROUP_ROLE.MASTER; 
		} else if(role.equalsIgnoreCase("DEVICEROLETYPE_E_SLAVE")){
			isSlave = true;
			this.role = GROUP_ROLE.SLAVE; 
		} else if(role.equalsIgnoreCase("DEVICEROLETYPE_E_STANDALONE")){
			isStandalone = true;
			this.role = GROUP_ROLE.STANDALONE; 
		}
		
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public static enum GROUP_ROLE{
		MASTER,
		SLAVE,
		STANDALONE
	};
	
	public class Group{
		
		private String name; // = "";
		private ArrayList<GroupVO> members; // = new ArrayList<GroupVO>();
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public ArrayList<GroupVO> getMembers() {
			return members;
		}
		public void setMembers(ArrayList<GroupVO> members) {
			this.members = members;
		}
		
		public Group(){
			name = "";
			members = new ArrayList<GroupVO>();
		}
		
	}
	
}
