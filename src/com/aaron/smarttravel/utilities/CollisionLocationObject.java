package com.aaron.smarttravel.utilities;

public class CollisionLocationObject {
	
	private String location_name;
	private String roadway_portion;
	private double longitude;
	private double latitude;
	private String loc_code;
	
	public CollisionLocationObject(){
		location_name="unknown";
		roadway_portion="unknown";
		longitude=-1;
		latitude=-1;
		loc_code="unknown";
	}
	
	
	public String getLocation_name() {
		return location_name;
	}

	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}

	public String getRoadway_portion() {
		return roadway_portion;
	}

	public void setRoadway_portion(String roadway_portion) {
		this.roadway_portion = roadway_portion;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getLoc_code() {
		return loc_code;
	}

	public void setLoc_code(String loc_code) {
		this.loc_code = loc_code;
	}

	
}
