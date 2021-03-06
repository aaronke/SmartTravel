package com.aaron.smarttravel.utilities;

import com.google.android.gms.maps.model.LatLng;

public class BottomInfoItem {
	
	private String location_name;
	private String reason;
	private String direction;
	private int Total;
	private LatLng locationLatLng;
	private Boolean SchoolDay;
	private String type;
	
	public BottomInfoItem(){}

	public int getTotal() {
		return Total;
	}

	public Boolean getSchoolDay() {
		return SchoolDay;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSchoolDay(Boolean schoolDay) {
		SchoolDay = schoolDay;
	}

	public LatLng getLocationLatLng() {
		return locationLatLng;
	}

	public void setLocationLatLng(LatLng locationLatLng) {
		this.locationLatLng = locationLatLng;
	}

	public void setTotal(int total) {
		Total = total;
	}

	public String getLocation_name() {
		return location_name;
	}

	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
	
}
