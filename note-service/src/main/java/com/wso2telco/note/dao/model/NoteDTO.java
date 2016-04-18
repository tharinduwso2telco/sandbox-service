package com.wso2telco.note.dao.model;

public class NoteDTO {

	private String noteDid; 
	private String clientRef;
	private String note; 
	private String reslutionTypeDid;
	private String description;
	private String noteTypeDid;
	
	public String getNoteDid() {
		return noteDid;
	}
	public void setNoteDid(String noteDid) {
		this.noteDid = noteDid;
	}
	public String getClientRef() {
		return clientRef;
	}
	public void setClientRef(String clientRef) {
		this.clientRef = clientRef;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getReslutionTypeDid() {
		return reslutionTypeDid;
	}
	public void setReslutionTypeDid(String reslutionTypeDid) {
		this.reslutionTypeDid = reslutionTypeDid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNoteTypeDid() {
		return noteTypeDid;
	}
	public void setNoteTypeDid(String noteTypeDid) {
		this.noteTypeDid = noteTypeDid;
	}
}
