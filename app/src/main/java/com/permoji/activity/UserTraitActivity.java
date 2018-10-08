package com.permoji.activity;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.FontRequestEmojiCompatConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.provider.FontRequest;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.permoji.adapter.TraitDefinitionListAdapter;
import com.permoji.builder.TraitDefinitionBuilder;
import com.permoji.builder.TraitItemClickBuilder;
import com.permoji.clickListener.TraitItemClickListener;
import com.permoji.database.tasks.InsertDefaultTraitAsync;
import com.permoji.model.entity.TraitStatement;
import com.permoji.model.result.TraitResult;
import com.permoji.viewModel.TraitViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.ctrlaltdel.aosp.ime.R;

public class UserTraitActivity extends AppCompatActivity {

    TraitViewModel traitViewModel;
    TraitDefinitionListAdapter traitDefinitionListAdapter;
    RecyclerView traitRecyclerView;
    TraitItemClickBuilder traitItemClickBuilder;
    private final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_trait);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        traitViewModel = ViewModelProviders.of(this).get(TraitViewModel.class);
        traitDefinitionListAdapter = new TraitDefinitionListAdapter(this);

        traitItemClickBuilder = new TraitItemClickBuilder(this, toolbar, traitDefinitionListAdapter);

        setObserver();
        setRecyclerView();

        initializeDeviceEmojiSupport();

        grantExternalStorageWritePermission();
    }


    private void initializeDeviceEmojiSupport() {
        FontRequest fontRequest = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.com_google_android_gms_fonts_certs);
        EmojiCompat.Config config = new FontRequestEmojiCompatConfig(this, fontRequest)
                .setReplaceAll(true)
                .registerInitCallback(new EmojiCompat.InitCallback() {
                    @Override
                    public void onInitialized() {
                        Log.i(this.getClass().getSimpleName(), "EmojiCompat initialized");
                    }

                    @Override
                    public void onFailed(@Nullable Throwable throwable) {
                        Log.e(this.getClass().getSimpleName(), "EmojiCompat initialization failed", throwable);
                    }
                });
        EmojiCompat.init(config);
    }

    private void setObserver() {
        traitViewModel.getLiveTraitEntities().observe(this, new Observer<List<TraitResult>>() {
            @Override
            public void onChanged(@Nullable List<TraitResult> traitEntities) {
                if(traitEntities.size() < 2) {
                    setTraitMessage(traitEntities);
                }
                traitDefinitionListAdapter.setTraits(traitEntities);
            }
        });
    }

    private void setTraitMessage(List<TraitResult> traitEntities) {

        TraitResult message = new TraitResult();
        TraitStatement statement = new TraitStatement();
        statement.setTraitName("Add emojis using Permoji Keyboard for more traits");
        message.traitStatement = Arrays.asList(statement);

        traitEntities.add(message);

    }

    private void setRecyclerView() {
        traitRecyclerView = findViewById(R.id.user_trait_definition_recyclerView);
        traitRecyclerView.setAdapter(traitDefinitionListAdapter);
        traitRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        traitItemClickBuilder.setRecyclerOnClick(traitRecyclerView);
    }

    private void grantExternalStorageWritePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            //request for the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)   {

                    // permission was granted, yay! Do the
                    // write to external strage operations here

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                        /*You can forcefully again ask for the permissions to the user here but it is a bad practice*/
                }
                return;
            }
        }
    }

}
