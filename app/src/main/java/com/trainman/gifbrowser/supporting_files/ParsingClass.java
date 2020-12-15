package com.trainman.gifbrowser.supporting_files;

import com.trainman.gifbrowser.models.GifImagesModel;
import com.trainman.gifbrowser.models.GifModel;
import com.trainman.gifbrowser.models.GifUserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class ParsingClass {

    private static ParsingClass instance;

    private ParsingClass(){}

    public static ParsingClass getInstance() {
        if (instance == null){
            instance = new ParsingClass();
        }
        return instance;
    }

    public GifModel parseGifModel(JSONObject jsonObject){
        try{
            GifModel model = new GifModel();
            model.setId(jsonObject.getString("id"));
            if (!jsonObject.isNull("type")){
                model.setType(jsonObject.getString("type"));
            }

            if (!jsonObject.isNull("username")){
                model.setUsername(jsonObject.getString("username"));
            }
            if (!jsonObject.isNull("title")){
                model.setTitle(jsonObject.getString("title"));
            }
            if (jsonObject.has("images")){
                JSONObject imagesObject = jsonObject.getJSONObject("images");
                Iterator<String> iterator = imagesObject.keys();
                while (iterator.hasNext()){
                    String key = iterator.next();
                    JSONObject jsonObject1 = imagesObject.getJSONObject(key);
                    if (key.equalsIgnoreCase("preview_gif")){
                        if (!jsonObject1.isNull("url")){
                            model.setUrl(jsonObject1.getString("url"));
                        }
                    }
                    if (key.equalsIgnoreCase("original")){
                        if (!jsonObject1.isNull("url")){
                            model.setOriginalUrl(jsonObject1.getString("url"));
                        }
                    }
                    GifImagesModel gifImagesModel = parseGifImageModel(jsonObject1,key);
                    if (gifImagesModel != null){
                        model.getImagesModels().add(gifImagesModel);
                    }
                }
            }
            if (jsonObject.has("user")){
                GifUserModel userModel = parseUserModel(jsonObject.getJSONObject("user"));
                if (userModel != null){
                    model.setUserModel(userModel);
                }
            }
            return model;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public GifImagesModel parseGifImageModel(JSONObject jsonObject, String type){
        try{
            GifImagesModel model = new GifImagesModel();
            model.setType(type);
            if (!jsonObject.isNull("height")){
                model.setHeight(jsonObject.getString("height"));
            }
            if (!jsonObject.isNull("width")){
                model.setWidth(jsonObject.getString("width"));
            }
            if (!jsonObject.isNull("url")){
                model.setUrl(jsonObject.getString("url"));
            }
            if (!jsonObject.isNull("webp")){
                model.setWebpUrl(jsonObject.getString("webp"));
            }
            return model;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public GifUserModel parseUserModel(JSONObject jsonObject){
        try {
            GifUserModel model = new GifUserModel();
            if (!jsonObject.isNull("avatar_url")){
                model.setImage(jsonObject.getString("avatar_url"));
            }
            if (!jsonObject.isNull("username")){
                model.setUsername(jsonObject.getString("username"));
            }
            if (!jsonObject.isNull("display_name")){
                model.setName(jsonObject.getString("display_name"));
            }
            if (!jsonObject.isNull("description")){
                model.setDescription(jsonObject.getString("description"));
            }
            return model;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

}
