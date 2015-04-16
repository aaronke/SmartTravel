package com.aaron.smarttravel.utilities;

public class NavDrawerItem {
	private String type_hotspot;
	private  String name_hotspot;
	private int count_collisions;
	
	public NavDrawerItem() {}
	
	public NavDrawerItem(String type, String name, int count ){
		
		this.type_hotspot=type;
		this.name_hotspot=name;
		this.count_collisions=count;
	}

	public String getType_hotspot() {
		return type_hotspot;
	}

	public void setType_hotspot(String type_hotspot) {
		this.type_hotspot = type_hotspot;
	}

	public String getName_hotspot() {
		return name_hotspot;
	}

	public void setName_hotspot(String name_hotspot) {
		this.name_hotspot = name_hotspot;
	}

	public int getCount_collisions() {
		return count_collisions;
	}

	public void setCount_collisions(int count_collisions) {
		this.count_collisions = count_collisions;
	}
	
	
}
