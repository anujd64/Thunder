package com.theflexproject.thunder.model.FanArt;

import java.util.ArrayList;

 class Characterart{
    public String id;
    public String url;
    public String lang;
    public String likes;
}

 class Clearart{
    public String id;
    public String url;
    public String lang;
    public String likes;
}

class Hdclearart{
    public String id;
    public String url;
    public String lang;
    public String likes;
}

public class FanArtTvResponseModel{
    public String name;
    public String thetvdb_id;
    public ArrayList<Clearlogo> clearlogo;
    public ArrayList<Hdtvlogo> hdtvlogo;
    public ArrayList<Clearart> clearart;
    public ArrayList<Showbackground> showbackground;
    public ArrayList<Tvthumb> tvthumb;
    public ArrayList<Seasonposter> seasonposter;
    public ArrayList<Seasonthumb> seasonthumb;
    public ArrayList<Hdclearart> hdclearart;
    public ArrayList<Tvbanner> tvbanner;
    public ArrayList<Characterart> characterart;
    public ArrayList<Tvposter> tvposter;
    public ArrayList<Seasonbanner> seasonbanner;

    @Override
    public String toString() {
        return "FanArtTvResponseModel{" +
                "name='" + name + '\'' +
                ", thetvdb_id='" + thetvdb_id + '\'' +
                ", clearlogo=" + clearlogo +
                ", hdtvlogo=" + hdtvlogo +
                ", clearart=" + clearart +
                ", showbackground=" + showbackground +
                ", tvthumb=" + tvthumb +
                ", seasonposter=" + seasonposter +
                ", seasonthumb=" + seasonthumb +
                ", hdclearart=" + hdclearart +
                ", tvbanner=" + tvbanner +
                ", characterart=" + characterart +
                ", tvposter=" + tvposter +
                ", seasonbanner=" + seasonbanner +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThetvdb_id() {
        return thetvdb_id;
    }

    public void setThetvdb_id(String thetvdb_id) {
        this.thetvdb_id = thetvdb_id;
    }

    public ArrayList<Clearlogo> getClearlogo() {
        return clearlogo;
    }

    public void setClearlogo(ArrayList<Clearlogo> clearlogo) {
        this.clearlogo = clearlogo;
    }

    public ArrayList<Hdtvlogo> getHdtvlogo() {
        return hdtvlogo;
    }

    public void setHdtvlogo(ArrayList<Hdtvlogo> hdtvlogo) {
        this.hdtvlogo = hdtvlogo;
    }

    public ArrayList<Clearart> getClearart() {
        return clearart;
    }

    public void setClearart(ArrayList<Clearart> clearart) {
        this.clearart = clearart;
    }

    public ArrayList<Showbackground> getShowbackground() {
        return showbackground;
    }

    public void setShowbackground(ArrayList<Showbackground> showbackground) {
        this.showbackground = showbackground;
    }

    public ArrayList<Tvthumb> getTvthumb() {
        return tvthumb;
    }

    public void setTvthumb(ArrayList<Tvthumb> tvthumb) {
        this.tvthumb = tvthumb;
    }

    public ArrayList<Seasonposter> getSeasonposter() {
        return seasonposter;
    }

    public void setSeasonposter(ArrayList<Seasonposter> seasonposter) {
        this.seasonposter = seasonposter;
    }

    public ArrayList<Seasonthumb> getSeasonthumb() {
        return seasonthumb;
    }

    public void setSeasonthumb(ArrayList<Seasonthumb> seasonthumb) {
        this.seasonthumb = seasonthumb;
    }

    public ArrayList<Hdclearart> getHdclearart() {
        return hdclearart;
    }

    public void setHdclearart(ArrayList<Hdclearart> hdclearart) {
        this.hdclearart = hdclearart;
    }

    public ArrayList<Tvbanner> getTvbanner() {
        return tvbanner;
    }

    public void setTvbanner(ArrayList<Tvbanner> tvbanner) {
        this.tvbanner = tvbanner;
    }

    public ArrayList<Characterart> getCharacterart() {
        return characterart;
    }

    public void setCharacterart(ArrayList<Characterart> characterart) {
        this.characterart = characterart;
    }

    public ArrayList<Tvposter> getTvposter() {
        return tvposter;
    }

    public void setTvposter(ArrayList<Tvposter> tvposter) {
        this.tvposter = tvposter;
    }

    public ArrayList<Seasonbanner> getSeasonbanner() {
        return seasonbanner;
    }

    public void setSeasonbanner(ArrayList<Seasonbanner> seasonbanner) {
        this.seasonbanner = seasonbanner;
    }
}

 class Seasonbanner{
    public String id;
    public String url;
    public String lang;
    public String likes;
    public String season;
}

  class Seasonposter{
    public String id;
    public String url;
    public String lang;
    public String likes;
}

  class Seasonthumb{
    public String id;
    public String url;
    public String lang;
    public String likes;
    public String season;
}

  class Showbackground{
    public String id;
    public String url;
    public String lang;
    public String likes;
    public String season;
}

  class Tvbanner{
    public String id;
    public String url;
    public String lang;
    public String likes;
}

 class Tvposter{
    public String id;
    public String url;
    public String lang;
    public String likes;
}

class Tvthumb{
    public String id;
    public String url;
    public String lang;
    public String likes;

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


