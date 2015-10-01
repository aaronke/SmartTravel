package com.aaron.smarttravel.utilities;

public class SchoolZoneObject {
	private int id;
	private String school_type;
	private String school_name;
	private String address;
	private double longitude;
	private double latitude;
	private String sz_segments;
	private String grade_level;
	
	public SchoolZoneObject(){
		id=-1;
		school_type="unknown";
		school_name="unknown";
		address="unknown";
		longitude=-1;
		latitude=-1;
		sz_segments="unknown";
		grade_level="unknown";
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSchool_type() {
		return school_type;
	}

	public void setSchool_type(String school_type) {
		this.school_type = school_type;
	}

	public String getSchool_name() {
		return school_name;
	}

	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getSz_segments() {
		return sz_segments;
	}

	public void setSz_segments(String sz_segments) {
		this.sz_segments = sz_segments;
	}

	public String getGrade_level() {
		return grade_level;
	}

	public void setGrade_level(String grade_level) {
		this.grade_level = grade_level;
	}
	

}
