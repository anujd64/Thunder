package com.theflexproject.thunder.model.FanArt;

import java.util.ArrayList;



public class FanArtMovieResponseModel{
    public String name;
    public String tmdb_id;
    public String imdb_id;
    public ArrayList<Hdmovielogo> hdmovielogo;
    public ArrayList<Moviebackground> moviebackground;
    public ArrayList<Movieposter> movieposter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTmdb_id() {
        return tmdb_id;
    }

    public void setTmdb_id(String tmdb_id) {
        this.tmdb_id = tmdb_id;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public ArrayList<Hdmovielogo> getHdmovielogo() {
        return hdmovielogo;
    }

    public void setHdmovielogo(ArrayList<Hdmovielogo> hdmovielogo) {
        this.hdmovielogo = hdmovielogo;
    }

    public ArrayList<Moviebackground> getMoviebackground() {
        return moviebackground;
    }

    public void setMoviebackground(ArrayList<Moviebackground> moviebackground) {
        this.moviebackground = moviebackground;
    }

    public ArrayList<Movieposter> getMovieposter() {
        return movieposter;
    }

    public void setMovieposter(ArrayList<Movieposter> movieposter) {
        this.movieposter = movieposter;
    }

    public ArrayList<Hdmovieclearart> getHdmovieclearart() {
        return hdmovieclearart;
    }

    public void setHdmovieclearart(ArrayList<Hdmovieclearart> hdmovieclearart) {
        this.hdmovieclearart = hdmovieclearart;
    }

    public ArrayList<Movielogo> getMovielogo() {
        return movielogo;
    }

    public void setMovielogo(ArrayList<Movielogo> movielogo) {
        this.movielogo = movielogo;
    }

    public ArrayList<Moviethumb> getMoviethumb() {
        return moviethumb;
    }

    public void setMoviethumb(ArrayList<Moviethumb> moviethumb) {
        this.moviethumb = moviethumb;
    }

    public ArrayList<Moviedisc> getMoviedisc() {
        return moviedisc;
    }

    public void setMoviedisc(ArrayList<Moviedisc> moviedisc) {
        this.moviedisc = moviedisc;
    }

    public ArrayList<Moviebanner> getMoviebanner() {
        return moviebanner;
    }

    public void setMoviebanner(ArrayList<Moviebanner> moviebanner) {
        this.moviebanner = moviebanner;
    }

    public ArrayList<Movieart> getMovieart() {
        return movieart;
    }

    public void setMovieart(ArrayList<Movieart> movieart) {
        this.movieart = movieart;
    }

    public ArrayList<Hdmovieclearart> hdmovieclearart;
    public ArrayList<Movielogo> movielogo;
    public ArrayList<Moviethumb> moviethumb;
    public ArrayList<Moviedisc> moviedisc;
    public ArrayList<Moviebanner> moviebanner;
    public ArrayList<Movieart> movieart;

    @Override
    public String toString() {
        return "FanArtMovieResponseModel{" +
                "name='" + name + '\'' +
                ", tmdb_id='" + tmdb_id + '\'' +
                ", imdb_id='" + imdb_id + '\'' +
                ", hdmovielogo=" + hdmovielogo +
                ", moviebackground=" + moviebackground +
                ", movieposter=" + movieposter +
                ", hdmovieclearart=" + hdmovieclearart +
                ", movielogo=" + movielogo +
                ", moviethumb=" + moviethumb +
                ", moviedisc=" + moviedisc +
                ", moviebanner=" + moviebanner +
                ", movieart=" + movieart +
                '}';
    }
}

class Hdmovieclearart{
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

    @Override
    public String toString() {
        return "Hdmovieclearart{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", lang='" + lang + '\'' +
                ", likes='" + likes + '\'' +
                '}';
    }
}

class Movieart{
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

    @Override
    public String toString() {
        return "Movieart{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", lang='" + lang + '\'' +
                ", likes='" + likes + '\'' +
                '}';
    }
}

class Moviebackground{
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

    @Override
    public String toString() {
        return "Moviebackground{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", lang='" + lang + '\'' +
                ", likes='" + likes + '\'' +
                '}';
    }
}

class Moviebanner{
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

    @Override
    public String toString() {
        return "Moviebanner{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", lang='" + lang + '\'' +
                ", likes='" + likes + '\'' +
                '}';
    }
}

class Moviedisc{
    public String id;
    public String url;
    public String lang;
    public String likes;
    public String disc;
    public String disc_type;
}

class Movieposter{
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

    @Override
    public String toString() {
        return "Movieposter{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", lang='" + lang + '\'' +
                ", likes='" + likes + '\'' +
                '}';
    }
}

class Moviethumb{
    public String id;
    public String url;
    public String lang;
    public String likes;

    public void setId(String id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Moviethumb{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", lang='" + lang + '\'' +
                ", likes='" + likes + '\'' +
                '}';
    }
}



