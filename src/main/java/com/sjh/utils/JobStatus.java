package com.sjh.utils;

public enum JobStatus {

    RUNNING("RUNNING"),
    STOP("STOP");

    private String status;

    JobStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
