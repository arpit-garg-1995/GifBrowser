package com.trainman.gifbrowser.gif;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trainman.gifbrowser.R;

import java.io.File;
import java.util.ArrayList;

public class DownlaodedGIfs extends AppCompatActivity {

    private ArrayList<String> gifs = new ArrayList<>();
    private GifsGridAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gifs_browser_main_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new GifsGridAdapter(this,null,gifs,true);
        GridLayoutManager manager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        FrameLayout progressView = findViewById(R.id.progress_view);
        progressView.setVisibility(View.GONE);
        checkForGifs();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    private void checkForGifs(){
        File directory =  getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (directory != null){
            File[] files = directory.listFiles();
            if (files != null){
                for (File file : files) {
                    gifs.add(file.getAbsolutePath());
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
}
