package com.sunlin.playcat.domain;

/**
 * Created by sunlin on 2017/7/7.
 */

public class MapsGoogle {
    private String error_message;
    private MapsGoogleResult[] results;
    private String status;

    public void setStatus(String status) {
        this.status = status;
    }
    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
    public void setResults(MapsGoogleResult[] results) {
        this.results = results;
    }

    public MapsGoogleResult[] getResults() {
        return results;
    }

    public String getError_message() {
        return error_message;
    }

    public String getStatus() {
        return status;
    }
}
