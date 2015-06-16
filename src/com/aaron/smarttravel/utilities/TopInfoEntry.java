package com.aaron.smarttravel.utilities;

public class TopInfoEntry {
	public String location_name;
	public String hotspot_reason;
	public String warning_message;
	public int distance;
	private int reason_id;
	
	public TopInfoEntry(){
		location_name="unknown";
		hotspot_reason="unknown";
		warning_message="unknown";
		distance=0;
		reason_id=-1;
	}

	public int getReason_id() {
		return reason_id;
	}

	public void setReason_id(int reason_id) {
		this.reason_id = reason_id;
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
