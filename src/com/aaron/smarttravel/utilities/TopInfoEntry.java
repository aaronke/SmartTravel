package com.aaron.smarttravel.utilities;

public class TopInfoEntry {
	public String location_name;
	public String hotspot_reason;
	public String warning_message;
	public int distance;
	
	public TopInfoEntry(){
		location_name="unknown";
		hotspot_reason="unknown";
		warning_message="unknown";
		distance=0;
	}

	public String getLocation_name() {
		return location_name;
	}

	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}

	public String getHotspot_reason() {
		return hotspot_reason;
	}

	public void setHotspot_reason(String hotspot_reason) {
		this.hotspot_reason = hotspot_reason;
	}

	public String getWarning_message() {
		return warning_message;
	}

	public void setWarning_message(String warning_message) {
		this.warning_message = warning_message;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	
}
