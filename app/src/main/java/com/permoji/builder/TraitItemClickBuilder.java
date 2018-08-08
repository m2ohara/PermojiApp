package com.permoji.builder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.permoji.activity.TraitNotifierActivity;
import com.permoji.adapter.TraitDefinitionListAdapter;
import com.permoji.clickListener.TraitItemClickListener;
import com.permoji.model.entity.Trait;
import com.permoji.model.result.TraitResult;
import com.permoji.repository.TraitDefinitionRepository;

import java.util.ArrayList;
import java.util.List;

import io.github.ctrlaltdel.aosp.ime.R;

/**
 * Created by michael on 05/07/18.
 */

public class TraitItemClickBuilder {

    private boolean longClicked = false;
    private List<TraitResult> traitResultsToRemove = new ArrayList<>();
    private Toolbar toolbar;
    private TraitDefinitionListAdapter traitDefinitionListAdapter;
    private TraitDefinitionRepository traitDefinitionRepository;

    public TraitItemClickBuilder(Context context, Toolbar toolbar, TraitDefinitionListAdapter traitDefinitionListAdapter) {
        this.toolbar = toolbar;
        this.traitDefinitionListAdapter = traitDefinitionListAdapter;
        this.traitDefinitionRepository = new TraitDefinitionRepository(context);
    }


    public void setRecyclerOnClick(final RecyclerView view) {

        view.addOnItemTouchListener(new TraitItemClickListener(view.getContext(), view, new TraitItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TraitResult traitResult = traitDefinitionListAdapter.getTraitResultByPosition(position);

                if(!longClicked) {
                    Intent startUserActivity = new Intent(view.getContext(), TraitNotifierActivity.class);
                    startUserActivity.putExtra("TraitResult", traitResult);
                    if (traitResult.traitNotifierFillerResultList.size() > 0)
                        startUserActivity.putExtra("NotifierFillerEntityId", traitResult.traitNotifierFillerResultList.get(0).getId());

                    startUserActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(startUserActivity);
                }
                else {
                    if(!traitResult.isSelected) {
                        setViewSelected(view);
                        traitResultsToRemove.add(traitResult);
                        traitResult.isSelected = true;
                    }
                    else {
                        setViewUnselected(view);
                        traitResultsToRemove.remove(traitResult);
                        if(traitResultsToRemove.size() == 0) {
                            cancelToolbarEdit();
                        }
                        traitResult.isSelected = false;
                    }
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
                TraitResult traitResult = traitDefinitionListAdapter.getTraitResultByPosition(position);

                longClicked = true;
                setViewSelected(view);
                traitResultsToRemove.add(traitResult);
                traitDefinitionListAdapter.getTraitResultByPosition(position).isSelected = true;
            }
        }));
    }

    private void setViewUnselected(final View v) {
        if(v != null) {
            View selected = v.findViewById(R.id.selected_image);
            selected.setVisibility(View.INVISIBLE);
            v.setBackgroundResource(R.color.user_trait_layout);
        }
    }

    private void setViewSelected(final View v) {
        if(v != null) {
            View selected = v.findViewById(R.id.selected_image);
            selected.setVisibility(View.VISIBLE);
            v.setBackgroundResource(R.color.spacebar_text_color_holo);
            showToolbarEdit();
        }
    }

    private void showToolbarEdit() {
        if(toolbar != null) {
            toolbar.setTitle("");
            View editView = toolbar.findViewById(R.id.edit_layout);
            editView.setVisibility(View.VISIBLE);

            View removeView = toolbar.findViewById(R.id.remove_selected);
            removeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeSelectedTraits();
                }
            });

            View cancelView = toolbar.findViewById(R.id.cancel);
            cancelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelToolbarEdit();
                }
            });
        }
    }

    private void cancelToolbarEdit() {
        if(toolbar != null) {
            longClicked = false;
            View editView = toolbar.findViewById(R.id.edit_layout);
            editView.setVisibility(View.GONE);
            toolbar.setTitle("TODAY YOU ARE...");

            for(TraitResult traitResult : traitResultsToRemove) {
                traitDefinitionListAdapter.getTraitResultById(traitResult.getId()).isSelected = false;
            }

            traitResultsToRemove.clear();
            traitDefinitionListAdapter.notifyDataSetChanged();
        }
    }

    private void removeSelectedTraits() {

        for(TraitResult traitResult : traitResultsToRemove) {

            Trait trait = new Trait();
            trait.setId(traitResult.getId());
            trait.setDateCreated(traitResult.getDateCreated());
            trait.setSelectedFillerId(traitResult.getSelectedFillerId());
            trait.setStatementId(traitResult.getStatementId());

            traitDefinitionRepository.removeAsync(trait);
        }

        cancelToolbarEdit();
    }
}
