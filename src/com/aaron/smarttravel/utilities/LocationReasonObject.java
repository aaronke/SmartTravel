package com.aaron.smarttravel.utilities;

public class LocationReasonObject {
	private int total;
	private int warning_priority;
	private int reason_id;
	private String travel_direction;
	private String loc_code;
	
	public LocationReasonObject(){
		total=-1;
		warning_priority=-1;
		reason_id=-1;
		travel_direction="unknown";
		loc_code="unknown";
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getWarning_priority() {
		return warning_priority;
	}

	public void setWarning_priority(int warning_priority) {
		this.warning_priority = warning_priority;
	}

	public int getReason_id() {
		return reason_id;
	}

	public void setReason_id(int reason_id) {
		this.reason_id = reason_id;
	}

	public String getTravel_direction() {
		return travel_direction;
	}

	public void setTravel_direction(String travel_direction) {
		this.travel_direction = travel_direction;
	}

	public String getLoc_code() {
		return loc_code;
	}

	public void setLoc_code(String loc_code) {
		this.loc_code = loc_code;
	}

	
}
