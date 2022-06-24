package com.cecenet.company.features.alert;

public class Alert {
    private String id, date, time, content, sound, animation, status;

    public Alert(String id, String date, String time, String content, String sound, String animation, String status) {
        this.id         = id;
        this.date       = date;
        this.time       = time;
        this.content    = content;
        this.sound      = sound;
        this.animation  = animation;
        this.status     = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getSound() {
        return sound;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public String getAnimation() {
        return animation;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}