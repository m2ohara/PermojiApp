package com.permoji.notifiers;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.text.emoji.EmojiCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.permoji.adapter.TraitNotifierListAdapter;
import com.permoji.compatability.EmojiInitCallback;
import com.permoji.model.result.TraitNotifierFillerResult;
import com.permoji.model.result.TraitResult;
import com.permoji.viewModel.NotifierFillerViewModel;

import java.util.List;

import io.github.ctrlaltdel.aosp.ime.R;

public class TraitNotifierActivity extends AppCompatActivity {

    private TraitResult traitResult;
    private NotifierFillerViewModel notifierFillerViewModel;
    private TraitNotifierListAdapter traitNotifierListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trait_notifier);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("WHY YOU FEELING THIS WAY?");
        setSupportActionBar(toolbar);

        traitResult = (TraitResult)getIntent().getSerializableExtra("TraitResult");

        setActivity();

        notifierFillerViewModel = ViewModelProviders.of(this).get(NotifierFillerViewModel.class);
        setRecyclerView();
        setObserver();

    }

    private void setObserver() {
        notifierFillerViewModel.getLiveNotifierFillersByTraitDefinitionId(traitResult.getId()).observe(this, new Observer<List<TraitNotifierFillerResult>>() {
            @Override
            public void onChanged(@Nullable List<TraitNotifierFillerResult> notifierFillerEntities) {
                traitNotifierListAdapter.setTraitNotifierFillerResultList(notifierFillerEntities);
            }
        });
    }

    private void setRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.notifier_recyclerView);

        traitNotifierListAdapter = new TraitNotifierListAdapter(this, traitResult);
        recyclerView.setAdapter(traitNotifierListAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        manager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(manager);

    }

    private void setActivity() {

        TextView textView = findViewById(R.id.notifier_trait_emoji);
        EmojiCompat.get().registerInitCallback(new EmojiInitCallback(textView, new String(Character.toChars(traitResult.traitStatement.get(0).getCodePoint()))));

        TextView name = findViewById(R.id.notifier_trait_name);
        name.setText(traitResult.traitStatement.get(0).getTraitName());

    }

}
