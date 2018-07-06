package com.permoji.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.text.emoji.EmojiCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.permoji.LinearLayoutManagerWithSmoothScroller;
import com.permoji.adapter.TraitNotifierListAdapter;
import com.permoji.compatability.EmojiInitCallback;
import com.permoji.model.entity.TraitAdjustProperties;
import com.permoji.model.entity.TraitNotifierFiller;
import com.permoji.model.result.TraitNotifierFillerResult;
import com.permoji.model.result.TraitResult;
import com.permoji.repository.TraitNotifierFillerRepository;
import com.permoji.task.ReplaceFillersWithRandomAsync;
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
    private RecyclerView notifierRecyclerView;

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
        adjustTraitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = ((LinearLayoutManager)notifierRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                if(position != -1) {
                    TraitNotifierFillerResult traitNotifierFillerResult = ((TraitNotifierListAdapter) notifierRecyclerView.getAdapter()).getItemAt(position);

                    new ReplaceFillersWithRandomAsync(view.getContext(), traitNotifierListAdapter).execute(traitNotifierFillerResult);

                }
            }
        });

        setTraitAdjustObserver();
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
                    adjustTraitButton.show();
                    Integer amount = traitAdjustProperties.get(0).getCount();
                    adjustTraitButton.setImageResource( context.getResources().getIdentifier(
                            "white_num_"+ (amount > 9 ? "10_plus" : amount), "drawable", context.getPackageName()));
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

        notifierRecyclerView = findViewById(R.id.notifier_recyclerView);

        LinearLayoutManager manager = new LinearLayoutManagerWithSmoothScroller(this);
        notifierRecyclerView.setLayoutManager(manager);

        manager.setSmoothScrollbarEnabled(true);

        traitNotifierListAdapter = new TraitNotifierListAdapter(this, traitResult);
        notifierRecyclerView.setAdapter(traitNotifierListAdapter);

    }

    private void setActivity() {

        TextView textView = findViewById(R.id.notifier_trait_emoji);
        EmojiCompat.get().registerInitCallback(new EmojiInitCallback(textView, new String(Character.toChars(traitResult.traitStatement.get(0).getCodePoint()))));

        TextView name = findViewById(R.id.notifier_trait_name);
        name.setText(traitResult.traitStatement.get(0).getTraitName());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        traitNotifierListAdapter.clear();
        notifierRecyclerView.clearOnChildAttachStateChangeListeners();
    }
}
