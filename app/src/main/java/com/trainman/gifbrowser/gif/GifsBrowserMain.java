package com.trainman.gifbrowser.gif;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.trainman.gifbrowser.R;
import com.trainman.gifbrowser.application.MyApplication;
import com.trainman.gifbrowser.application.SplashScreenActivity;
import com.trainman.gifbrowser.models.GifModel;
import com.trainman.gifbrowser.supporting_files.CommonValues;
import com.trainman.gifbrowser.supporting_files.GeneralInterface;
import com.trainman.gifbrowser.supporting_files.NetworkModule;
import com.trainman.gifbrowser.supporting_files.ParsingClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GifsBrowserMain extends AppCompatActivity implements GeneralInterface {

    private final String TAG = "GifsBrowserMain";
    private FrameLayout progressView;
    private ArrayList<GifModel> models;
    private GifsGridAdapter adapter;
    int i = 9;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gifs_browser_main_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.loading);
        }
        models = CommonValues.getInstance().getModels();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new GifsGridAdapter(this,models,null,false);
        GridLayoutManager manager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        progressView = findViewById(R.id.progress_view);
        if (models.size() > 0){
            progressView.setVisibility(View.GONE);
        }else{
            progressView.setVisibility(View.VISIBLE);
        }
        int offset = 25;
        for(int i = 1; i < 10; i++){
            offset = offset * i;
            String api = MyApplication.getInstance().tredingUrl + "&offset=" + offset;
            getData(api);
        }
    }

    @Override
    protected void onStart() {
        NetworkModule.getInstance().setListener(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        NetworkModule.getInstance().setListener(null);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.downloads){
            Intent intent = new Intent(GifsBrowserMain.this,DownlaodedGIfs.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    private void getData(String api){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, api, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                NetworkModule.getInstance().saveData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError){
                    MyApplication.getInstance().showNoInternetScreen(GifsBrowserMain.this);
                }else if (error instanceof TimeoutError){
                    if (!MyApplication.getInstance().internetIsConnected()){
                        MyApplication.getInstance().showNoInternetScreen(GifsBrowserMain.this);
                    }else{
                        MyApplication.getInstance().showSomethingWentWrong(GifsBrowserMain.this);
                    }
                }else{
                    MyApplication.getInstance().showSomethingWentWrong(GifsBrowserMain.this);
                }
            }
        });
        NetworkModule.getInstance().addRequestWithRetry(request,false,TAG);
    }

    @Override
    protected void onDestroy() {
        NetworkModule.getInstance().cancelAll(TAG);
        super.onDestroy();
    }

    @Override
    public void doSomething(String resCase, Object object) {
        if (resCase.equalsIgnoreCase("refresh")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressView.setVisibility(View.GONE);
                    i = i-1;
                    if (models.size() > 0 && models.size() <=25){
                        adapter.notifyDataSetChanged();
                    }else if (i < 1){
                        adapter.notifyDataSetChanged();
                        if (getSupportActionBar() != null){
                            getSupportActionBar().setTitle(R.string.app_name);
                        }
                    }
                }
            });
        }
    }
}
