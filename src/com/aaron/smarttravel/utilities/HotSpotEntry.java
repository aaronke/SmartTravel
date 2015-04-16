package com.aaron.smarttravel.utilities;

import com.google.android.gms.maps.model.LatLng;

public class HotSpotEntry {
	
	private String name;
	private int collision_count;
	private int fatal_count;
	private int injury_count;
	private int PDO_value;
	private int rank;
	private LatLng latLng;
	
	public HotSpotEntry() {
		// TODO Auto-generated constructor stub
		this.name="UnKnown";
		this.collision_count=0;
		this.fatal_count=0;
		this.injury_count=0;
		this.PDO_value=0;
		this.rank=0;
		this.latLng=null;
	}
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCollision_count() {
		return collision_count;
	}
	public void setCollision_count(int collision_count) {
		this.collision_count = collision_count;
	}
	public int getFatal_count() {
		return fatal_count;
	}
	public void setFatal_count(int fatal_count) {
		this.fatal_count = fatal_count;
	}
	public int getInjury_count() {
		return injury_count;
	}
	public void setInjury_count(int injury_count) {
		this.injury_count = injury_count;
	}
	public int getPDO_value() {
		return PDO_value;
	}
	public void setPDO_value(int pDO_value) {
		PDO_value = pDO_value;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public LatLng getLatLng() {
		return latLng;
	}
	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}

	
}
