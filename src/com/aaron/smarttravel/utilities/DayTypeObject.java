package com.aaron.smarttravel.utilities;

public class DayTypeObject {
	private Boolean weekday;
	private Boolean weekend;
	private String date;
	private Boolean school_day;
	
	public DayTypeObject(){
		weekend=false;
		weekend=true;
		date="unknown";
		school_day=true;
	}

	public Boolean getWeekday() {
		return weekday;
	}

	public void setWeekday(Boolean weekday) {
		this.weekday = weekday;
	}

	public Boolean getWeekend() {
		return weekend;
	}

	public void setWeekend(Boolean weekend) {
		this.weekend = weekend;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Boolean getSchool_day() {
		return school_day;
	}

	public void setSchool_day(Boolean school_day) {
		this.school_day = school_day;
	}

	
}
