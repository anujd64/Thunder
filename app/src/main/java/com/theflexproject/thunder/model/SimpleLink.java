package com.theflexproject.thunder.model;

public class SimpleLink{
    public String status;
    public String url;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "SimpleLink{" +
                "status='" + status + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}