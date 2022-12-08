package com.theflexproject.thunder.model.FanArt;

public class Clearlogo {
    public String id;
    public String url;
    public String lang;
    public String likes;

    @Override
    public String toString() {
        return "Clearlogo{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", lang='" + lang + '\'' +
                ", likes='" + likes + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }
}
