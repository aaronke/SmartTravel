package com.aaron.smarttravel.injection.event;

import com.aaron.smarttravel.utilities.TopInfoEntry;

public class UpdateInfoBoxEvent {
	
	private TopInfoEntry infoEntry;
	public UpdateInfoBoxEvent(TopInfoEntry infoEntry){
		this.infoEntry=infoEntry;
	}
	public TopInfoEntry getInfoEntry() {
		return infoEntry;
	}
	public void setInfoEntry(TopInfoEntry infoEntry) {
		this.infoEntry = infoEntry;
	}
	
	
}
