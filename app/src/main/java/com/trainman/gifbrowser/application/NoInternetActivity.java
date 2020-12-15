package com.trainman.gifbrowser.application;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.trainman.gifbrowser.R;
import com.trainman.gifbrowser.supporting_files.GeneralInterface;

public class NoInternetActivity extends AppCompatActivity implements GeneralInterface {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_internet_activity_layout);
        MyApplication.getInstance().setNoInternetActivityListener(this);
        MyApplication.getInstance().setShowingNoInternetScreen(true);
    }

    @Override
    public void onBackPressed() {
        MyApplication.getInstance().setShowingNoInternetScreen(false);
        super.onBackPressed();

    }

    @Override
    public void doSomething(String rescase, Object doObject) {
        if (rescase.equalsIgnoreCase("available")){
            MyApplication.getInstance().setShowingNoInternetScreen(false);
            MyApplication.getInstance().setNoInternetActivityListener(null);
            finish();
        }
    }

    @Override
    protected void onPause() {
        MyApplication.getInstance().setNoInternetActivityListener(null);
        super.onPause();
    }

    @Override
    protected void onResume() {
        MyApplication.getInstance().setNoInternetActivityListener(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        MyApplication.getInstance().setShowingNoInternetScreen(false);
        super.onDestroy();
    }
}

