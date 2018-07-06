package com.permoji.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.text.emoji.EmojiCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.permoji.compatability.EmojiInitCallback;
import com.permoji.activity.TraitNotifierActivity;
import com.permoji.model.result.TraitResult;

import java.util.ArrayList;
import java.util.List;

import io.github.ctrlaltdel.aosp.ime.R;

/**
 * Created by michael on 15/06/18.
 */

public class TraitDefinitionListAdapter extends RecyclerView.Adapter<TraitDefinitionListAdapter.TraitDefinitionViewHolder> {

    class TraitDefinitionViewHolder extends RecyclerView.ViewHolder {
        private final TextView traitEmoji;
        private final TextView traitName;
        private final ImageView traitNotifierCount;
        private final View holderView;

        public TraitDefinitionViewHolder(View itemView) {
            super(itemView);
            traitEmoji = (TextView) itemView.findViewById(R.id.user_trait_emoji);
            traitName = (TextView) itemView.findViewById(R.id.user_trait_name);
            traitNotifierCount = itemView.findViewById(R.id.user_notifier_count);
            holderView = itemView;
        }
    }

    private ViewGroup viewGroup;
    private LayoutInflater inflater;
    private List<TraitResult> traitResultList;

    public TraitDefinitionListAdapter(Context context) { inflater = LayoutInflater.from(context); }

    @Override
    public TraitDefinitionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        viewGroup = parent;
        View itemView = inflater.inflate(R.layout.user_trait_list, parent, false);
        return new TraitDefinitionListAdapter.TraitDefinitionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TraitDefinitionViewHolder holder, int position) {

        final TraitResult traitResult = traitResultList.get(position);
        if(traitResult != null) {
            EmojiCompat.get().registerInitCallback(new EmojiInitCallback(holder.traitEmoji, new String(Character.toChars(traitResult.traitStatement.get(0).getCodePoint()))));
            holder.traitName.setText(traitResult.traitStatement.get(0).getTraitName());

//            ImageView imageView = (ImageView) inflater.inflate(R.layout.user_notifier_count_image, viewGroup, false);
            Integer amount = traitResult.traitNotifierFillerResultList.size();
            String resourceName = amount > 9 ? "num_10_plus" : "num_"+amount;
            holder.traitNotifierCount.setImageResource(holder.itemView.getContext().getResources().getIdentifier( resourceName, "drawable", holder.itemView.getContext().getPackageName()));

//            holder.traitNotifierCount.removeAllViews();
//            holder.traitNotifierCount = imageView;

            if(traitResult.isSelected) {
                setViewSelected(holder.holderView);
            }
            else {
                setViewUnselected(holder.holderView);
            }
        }
        else {
            holder.traitEmoji.setText("Loading.. ");
            holder.traitName.setText("Loading.. ");
        }


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
        }
    }

    @Override
    public int getItemCount() {
        if(traitResultList != null) {
            return  traitResultList.size();
        }
        else return 0;
    }

    public void setTraits(List<TraitResult> traitEntities) {
        this.traitResultList = traitEntities;
        notifyDataSetChanged();
    }

    public TraitResult getTraitResultByPosition(int position) {
        return  traitResultList.get(position);
    }

    public TraitResult getTraitResultById(int id) {
        for(TraitResult traitResult : traitResultList) {
            if(traitResult.getId() == id) {
                return traitResult;
            }
        }
        return null;
    }
}
