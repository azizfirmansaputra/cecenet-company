package com.cecenet.company.features.runningtext;

public class RunningText {
    private String id, content, visible;

    public RunningText(String id, String content, String visible) {
        this.id         = id;
        this.content    = content;
        this.visible    = visible;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent(){
        return content;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getVisible() {
        return visible;
    }
}