package com.theflexproject.thunder.model;

import java.util.Date;

public class File {

    public String id;
    public String name;
    public String mimeType;
    public Date modifiedTime;
    public String size;
    public String urlString;

    public String getUrlstring() {
        return urlString;
    }

    public void setUrlstring(String urlString) {
        this.urlString = urlString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    @Override
    public String toString() {
        return
//                "{" + "\"File\"" + ':' +
                "{" +
//                "\"id\"" + ':' + '\"' + id + '\"'  +
//                ", \"name\"" + ':' + '\"' + name + '\"'  +
                "\"mimeType\"" + ':' + '\"' + mimeType + '\"'  +
//                ", \"modifiedTime\"" + ':' + '\"' + modifiedTime + '\"'  +
                ", \"size\"" + ':' + '\"' + size + '\"'  +
                ", \"urlString\"" + ':' + '\"' + urlString + '\"'  +
                '}' ;
//                        +"}";
    }
}
