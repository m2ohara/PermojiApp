package com.permoji.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.text.emoji.EmojiCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.permoji.adapter.TraitNotifierListAdapter;
import com.permoji.compatability.EmojiInitCallback;
import com.permoji.model.entity.TraitAdjustProperties;
import com.permoji.model.result.TraitNotifierFillerResult;
import com.permoji.model.result.TraitResult;
import com.permoji.viewModel.NotifierFillerViewModel;
import com.permoji.viewModel.TraitAdjustViewModel;

import java.util.List;

import io.github.ctrlaltdel.aosp.ime.R;

public class TraitNotifierActivity extends AppCompatActivity {

    private TraitResult traitResult;
    private NotifierFillerViewModel notifierFillerViewModel;
    private TraitNotifierListAdapter traitNotifierListAdapter;
    private TraitAdjustViewModel traitAdjustViewModel;
    private FloatingActionButton adjustTraitButton;

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
        setNotifierFillerListObserver();


        traitAdjustViewModel = ViewModelProviders.of(this).get(TraitAdjustViewModel.class);

        adjustTraitButton = (FloatingActionButton) findViewById(R.id.reset_fillers_button);


    }

    private void setTraitAdjustObserver() {
        final Context context = this;
        traitAdjustViewModel.getTraitAdjustProperties().observe(this, new Observer<List<TraitAdjustProperties>>() {
            @Override
            public void onChanged(@Nullable List<TraitAdjustProperties> traitAdjustProperties) {
                if(traitAdjustProperties.get(0).getCount() == 0) {
                    adjustTraitButton.hide();
                }
                else {
                    adjustTraitButton.setImageResource( context.getResources().getIdentifier(
                            "num_"+traitAdjustProperties.get(0).getCount(), "drawable", context.getPackageName()));
                }
            }
        });
    }

    private void setNotifierFillerListObserver() {
        notifierFillerViewModel.getLiveNotifierFillersByTraitDefinitionId(traitResult.getId()).observe(this, new Observer<List<TraitNotifierFillerResult>>() {
            @Override
            public void onChanged(@Nullable List<TraitNotifierFillerResult> notifierFillerEntities) {
                traitNotifierListAdapter.setTraitNotifierFillerResultList(notifierFillerEntities);
            }
        });
    }

    private void setRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.notifier_recyclerView);

        traitNotifierListAdapter = new TraitNotifierListAdapter(this, traitResult, adjustTraitButton);
        recyclerView.setAdapter(traitNotifierListAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        //TODO: Enable smooth scroll
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
