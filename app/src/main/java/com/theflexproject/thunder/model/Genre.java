package com.theflexproject.thunder.model;

public class Genre {
    public int id;
    public String name;

    @Override
    public String toString() {
        return
//                "{" + "\"Genre\"" + ':' +
                "{" +
                "\"id\"" + ':' + '\"' + id + '\"'  +
                ", \"name\"" + ':' + '\"' + name + '\"'  +
                '}';
//                 +"}";
    }

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
}
