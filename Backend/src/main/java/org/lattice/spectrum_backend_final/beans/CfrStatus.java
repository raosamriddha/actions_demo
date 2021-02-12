package org.lattice.spectrum_backend_final.beans;

public class CfrStatus {
    private int statusId;
    private int status;

    public CfrStatus() {
    }

    public CfrStatus(int statusId, int status) {
        this.statusId = statusId;
        this.status = status;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getStatus() {
        return status;
    }


    public void setStatus(int status) {
        this.status = status;
    }

}
