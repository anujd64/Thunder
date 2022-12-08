package com.theflexproject.thunder.model;

public class ExternalIds{
    public int id;
    public String imdb_id;
    public Object freebase_mid;
    public Object freebase_id;
    public int tvdb_id;
    public Object tvrage_id;
    public Object facebook_id;
    public String instagram_id;
    public String twitter_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public Object getFreebase_mid() {
        return freebase_mid;
    }

    public void setFreebase_mid(Object freebase_mid) {
        this.freebase_mid = freebase_mid;
    }

    public Object getFreebase_id() {
        return freebase_id;
    }

    public void setFreebase_id(Object freebase_id) {
        this.freebase_id = freebase_id;
    }

    public int getTvdb_id() {
        return tvdb_id;
    }

    public void setTvdb_id(int tvdb_id) {
        this.tvdb_id = tvdb_id;
    }

    public Object getTvrage_id() {
        return tvrage_id;
    }

    public void setTvrage_id(Object tvrage_id) {
        this.tvrage_id = tvrage_id;
    }

    public Object getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(Object facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getInstagram_id() {
        return instagram_id;
    }

    public void setInstagram_id(String instagram_id) {
        this.instagram_id = instagram_id;
    }

    public String getTwitter_id() {
        return twitter_id;
    }

    public void setTwitter_id(String twitter_id) {
        this.twitter_id = twitter_id;
    }

    @Override
    public String toString() {
        return "ExternalIds{" +
                "id=" + id +
                ", imdb_id='" + imdb_id + '\'' +
                ", freebase_mid=" + freebase_mid +
                ", freebase_id=" + freebase_id +
                ", tvdb_id=" + tvdb_id +
                ", tvrage_id=" + tvrage_id +
                ", facebook_id=" + facebook_id +
                ", instagram_id='" + instagram_id + '\'' +
                ", twitter_id='" + twitter_id + '\'' +
                '}';
    }
}

