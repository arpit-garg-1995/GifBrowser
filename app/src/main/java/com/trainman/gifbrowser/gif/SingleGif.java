package com.trainman.gifbrowser.gif;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.trainman.gifbrowser.R;
import com.trainman.gifbrowser.models.GifModel;
import com.trainman.gifbrowser.supporting_files.CommonValues;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class SingleGif extends AppCompatActivity {

    private GifModel model;
    private File gifFile;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_gif_layout);
        model = CommonValues.getInstance().getModel();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        final TextView gifName = findViewById(R.id.gif_name);
        gifName.setText(model.getTitle());
        final ImageView imageView = findViewById(R.id.image);
        Glide.with(this).asGif().load(model.getOriginalUrl()).placeholder(new ColorDrawable(Color.BLACK))
                .fallback(new ColorDrawable(Color.GRAY)).error(R.drawable.ic_baseline_error_24).into(imageView);
        Glide.with(this).asFile().load(model.getUrl()).apply(new RequestOptions()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(Target.SIZE_ORIGINAL)).into(new Target<File>() {
            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {

            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                Log.d("onLoadFailed","onLoadFailed");
            }

            @Override
            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                Log.d("onResourceReady","onResourceReady");
                gifFile = resource;
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
                Log.d("onLoadCleared","onLoadCleared");
            }

            @Override
            public void getSize(@NonNull SizeReadyCallback cb) {
                Log.d("getSize","getSize" + cb.toString());
            }

            @Override
            public void removeCallback(@NonNull SizeReadyCallback cb) {
                Log.d("removeCallback","removeCallback");
            }

            @Override
            public void setRequest(@Nullable Request request) {
                Log.d("setRequest","setRequest");
            }

            @Nullable
            @Override
            public Request getRequest() {
                return null;
            }

            @Override
            public void onStart() {
                Log.d("started","start");
            }

            @Override
            public void onStop() {
                Log.d("onStop","onStop");
            }

            @Override
            public void onDestroy() {
                Log.d("onDestroy","onDestroy");
            }
        });
        TextView textView = findViewById(R.id.line3);
        if (model.getUserModel() != null){
            textView.setVisibility(View.VISIBLE);
            TextView userName = findViewById(R.id.name);
            userName.setText(model.getUserModel().getName());
            ImageView userImage = findViewById(R.id.user_image);
            Glide.with(this).load(model.getUserModel().getImage()).placeholder(new ColorDrawable(Color.BLACK))
                    .fallback(new ColorDrawable(Color.GRAY)).error(R.drawable.ic_baseline_error_24).into(userImage);
        }else{
            textView.setVisibility(View.GONE);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }else if (item.getItemId() == R.id.download){
            checkFile();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    private void checkFile(){

        File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "gif" + model.getId() + ".gif");
        if (file.exists()){
            showError(getString(R.string.already_exist));
        }else{
            if (gifFile != null){
                saveLocally();
            }else{
                showError(getString(R.string.not_ready));
            }
        }
    }

    private void saveLocally(){
        try {
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "gif" + model.getId() + ".gif");
            FileOutputStream output = new FileOutputStream(file);
            FileInputStream input = new FileInputStream(gifFile);

            FileChannel inputChannel = input.getChannel();
            FileChannel outputChannel = output.getChannel();

            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            output.close();
            input.close();
            showError(getString(R.string.download_success));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showError(String string){
        Toast.makeText(SingleGif.this,string,Toast.LENGTH_SHORT).show();
    }
}
