package org.lattice.spectrum_backend_final.beans;

public class NotesHistory {

	private int notesId;
	private String notes;
    private String postedBy;
    private String createdOn;

	public NotesHistory() {
	}

	public NotesHistory(int notesId, String notes, String postedBy, String createdOn) {
		super();
		this.notesId = notesId;
		this.notes = notes;
		this.postedBy = postedBy;
		this.createdOn = createdOn;
	}

	public int getNotesId() {
		return notesId;
	}

	public void setNotesId(int notesId) {
		this.notesId = notesId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

}
