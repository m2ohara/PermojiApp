package com.permoji.user;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.permoji.api.trait.Trait;

import java.util.List;

import io.github.ctrlaltdel.aosp.ime.R;

public class UserActivity extends AppCompatActivity {

    private UserTraitsViewModel userTraitsViewModel;
    private UserTraitListAdapter userTraitListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        userTraitListAdapter = new UserTraitListAdapter(this);
        userTraitsViewModel = ViewModelProviders.of(this).get(UserTraitsViewModel.class);

        setUserTraitsObserver();

        setUserTraitsRecyclerView();

    }

    private void setUserTraitsObserver() {
        userTraitsViewModel.getGetUserTraits().observe(this, new Observer<List<Trait>>() {
            @Override
            public void onChanged(@Nullable List<Trait> traits) {
                userTraitListAdapter.setTraits(traits);
            }
        });
    }

    private void setUserTraitsRecyclerView() {
        RecyclerView view = findViewById(R.id.user_trait_recyclerView);
        view.setAdapter(userTraitListAdapter);
        view.setLayoutManager(new LinearLayoutManager(this));
    }

}
