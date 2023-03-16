package com.zwk.trash_detective.Bean;

public class D_SqlTrashData {
    int id;
    int sortId;
    int star;
    String name;
    String description;
    String url;
    String root;

    public D_SqlTrashData(int id, int sortId, String name, String root, String description, String url, int star){
        this.id = id;
        this.sortId = sortId;
        this.name = name;
        this.description = description;
        this.url = url;
        this.root = root;
        this.star = star;
    }

    public int getId() {
        return id;
    }

    public int getSortId() {
        return sortId;
    }

    public String getName() {
        return name;
    }

    public int getStar() {return star;}

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getRoot() {
        return root;
    }

    public void setStar(int star) {
        this.star = star;
    }
}
