package com.trainman.gifbrowser.supporting_files;

import com.trainman.gifbrowser.models.GifModel;

import java.util.ArrayList;

public class CommonValues {

    private static CommonValues instance;

    private CommonValues(){}

    public static CommonValues getInstance() {
        if (instance == null){
            instance = new CommonValues();
        }
        return instance;
    }

    private GifModel model;

    public GifModel getModel() {
        return model;
    }

    public void setModel(GifModel model) {
        this.model = model;
    }

    private ArrayList<GifModel> models = new ArrayList<>();

    public ArrayList<GifModel> getModels() {
        return models;
    }
}
