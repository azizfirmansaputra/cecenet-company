package com.cecenet.company.features.videos;

public class Videos {
    private String id, name, visible;

    public Videos(String id, String name, String visible) {
        this.id         = id;
        this.name       = name;
        this.visible    = visible;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getVisible() {
        return visible;
    }
}