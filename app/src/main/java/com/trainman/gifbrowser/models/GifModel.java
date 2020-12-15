package com.trainman.gifbrowser.models;

import java.util.ArrayList;

public class GifModel {

    private String type, id, url, username, title, originalUrl;
    private ArrayList<GifImagesModel> imagesModels = new ArrayList<>();
    private GifUserModel userModel;

    public GifUserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(GifUserModel userModel) {
        this.userModel = userModel;
    }

    public ArrayList<GifImagesModel> getImagesModels() {
        return imagesModels;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
}
