package com.theflexproject.thunder.model.TVShowInfo;

public class Network {
    public int id;
    public String name;
    public String logo_path;
    public String origin_country;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo_path() {
        return logo_path;
    }

    public void setLogo_path(String logo_path) {
        this.logo_path = logo_path;
    }

    public String getOrigin_country() {
        return origin_country;
    }

    public void setOrigin_country(String origin_country) {
        this.origin_country = origin_country;
    }

    @Override
    public String toString() {
        return "Network{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", logo_path='" + logo_path + '\'' +
                ", origin_country='" + origin_country + '\'' +
                '}';
    }
}
