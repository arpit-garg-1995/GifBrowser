package com.trainman.gifbrowser.supporting_files;

import android.os.AsyncTask;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.trainman.gifbrowser.application.MyApplication;
import com.trainman.gifbrowser.models.GifModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkModule {

    private static NetworkModule instance;

    private NetworkModule(){}

    public static NetworkModule getInstance() {
        if (instance == null){
            instance = new NetworkModule();
        }
        return instance;
    }

    private GeneralInterface listener;

    public void setListener(GeneralInterface listener) {
        this.listener = listener;
    }



    public void addRequestWithRetry(JsonObjectRequest request, boolean shouldCache, String tag){
        request.setShouldCache(shouldCache);
        request.setTag(tag);
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().getRequestQueue().add(request);
    }

    public void cancelAll(String TAG){
        MyApplication.getInstance().getRequestQueue().cancelAll(TAG);
    }

    public void saveData(final JSONObject response){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    if (response.has("meta")){
                        JSONObject metaObj = response.getJSONObject("meta");
                        if (!metaObj.isNull("status") && metaObj.getInt("status") == 200){
                            JSONArray jsonArray = response.getJSONArray("data");
                            for(int i = 0; i < jsonArray.length();i++){
                                GifModel model = ParsingClass.getInstance().parseGifModel(jsonArray.getJSONObject(i));
                                if (model != null){
                                    CommonValues.getInstance().getModels().add(model);
                                }
                            }
                            if (listener != null){
                                listener.doSomething("refresh",null);
                            }
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
