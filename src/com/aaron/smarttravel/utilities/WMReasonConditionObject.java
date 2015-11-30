package com.aaron.smarttravel.utilities;

public class WMReasonConditionObject {

	private String warning_message;
	private Boolean weekday;
	private String reason;
	private Boolean weekend;
	private String end_time;
	private int reason_id;
	private String month;
	private String start_time;
	private Boolean scholl_day;
	private String category;
	
	public WMReasonConditionObject(){
		warning_message="unknown";
		weekday=true;
		reason="unknown";
		weekend=false;
		end_time="unknown";
		reason_id=1;
		month="unknown";
		start_time="unknown";
		scholl_day=true;
		category="unknown";
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getWarning_message() {
		return warning_message;
	}

	public void setWarning_message(String warning_message) {
		this.warning_message = warning_message;
	}

	public Boolean getWeekday() {
		return weekday;
	}

	public void setWeekday(Boolean weekday) {
		this.weekday = weekday;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Boolean getWeekend() {
		return weekend;
	}

	public void setWeekend(Boolean weekend) {
		this.weekend = weekend;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public int getReason_id() {
		return reason_id;
	}

	public void setReason_id(int reason_id) {
		this.reason_id = reason_id;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public Boolean getScholl_day() {
		return scholl_day;
	}

	public void setScholl_day(Boolean scholl_day) {
		this.scholl_day = scholl_day;
	}
	
	
}
