package org.lattice.spectrum_backend_final.beans;

public class LicenseStatus {

	private int statusId;
	private String status;
	
	public LicenseStatus() {
	}

	public LicenseStatus(int statusId, String status) {
		super();
		this.statusId = statusId;
		this.status = status;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
