package com.trainman.gifbrowser.application;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.trainman.gifbrowser.R;
import com.trainman.gifbrowser.gif.GifsBrowserMain;
import com.trainman.gifbrowser.supporting_files.NetworkModule;

import org.json.JSONObject;

public class SplashScreenActivity extends AppCompatActivity {

    private boolean isAnimOver = false, isResponseAvilable = false;
    private final String TAG = "SplashScreenActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);
        Animation topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        ImageView image = findViewById(R.id.appIcon);
        image.setAnimation(topAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isAnimOver = true;
                setupAppCycleFlow();
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isResponseAvilable){
            getData();
        }
    }

    private void setupAppCycleFlow(){
        if (isAnimOver && isResponseAvilable) {
            Intent intent = new Intent(SplashScreenActivity.this,GifsBrowserMain.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void getData(){
        String api = MyApplication.getInstance().tredingUrl;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, api, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                NetworkModule.getInstance().saveData(response);
                isResponseAvilable = true;
                setupAppCycleFlow();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError){
                    MyApplication.getInstance().showNoInternetScreen(SplashScreenActivity.this);
                    Log.d("CALLED","called this one");
                }else if (error instanceof TimeoutError){
                    Log.d("CALLED","called this two");
                    if (!MyApplication.getInstance().internetIsConnected()){
                        MyApplication.getInstance().showNoInternetScreen(SplashScreenActivity.this);
                    }else{
                        MyApplication.getInstance().showSomethingWentWrong(SplashScreenActivity.this);
                    }
                }else{
                    MyApplication.getInstance().showSomethingWentWrong(SplashScreenActivity.this);
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
}
