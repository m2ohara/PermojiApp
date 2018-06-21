package com.permoji.trait;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.FontRequestEmojiCompatConfig;
import android.support.v4.provider.FontRequest;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.permoji.adapter.TraitDefinitionListAdapter;
import com.permoji.model.result.TraitResult;
import com.permoji.viewModel.TraitViewModel;

import java.util.List;

import io.github.ctrlaltdel.aosp.ime.R;

public class UserTraitActivity extends AppCompatActivity {

    TraitViewModel traitViewModel;
    TraitDefinitionListAdapter traitDefinitionListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_trait);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("TODAY YOU ARE...");
        setSupportActionBar(toolbar);

        traitViewModel = ViewModelProviders.of(this).get(TraitViewModel.class);
        traitDefinitionListAdapter = new TraitDefinitionListAdapter(this);

        setObserver();
        setRecyclerView();

        initializeDeviceEmojiSupport();
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
                traitDefinitionListAdapter.setTraits(traitEntities);
            }
        });
    }

    private void setRecyclerView() {
        RecyclerView view = findViewById(R.id.user_trait_definition_recyclerView);
        view.setAdapter(traitDefinitionListAdapter);
        view.setLayoutManager(new LinearLayoutManager(this));
    }

}
