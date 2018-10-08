package com.permoji.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.text.emoji.EmojiCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
        private final ImageView traitNotifierIcon;
        private final View holderView;

        public TraitDefinitionViewHolder(View itemView) {
            super(itemView);
            traitEmoji = (TextView) itemView.findViewById(R.id.user_trait_emoji);
            traitName = (TextView) itemView.findViewById(R.id.user_trait_name);
            traitNotifierCount = itemView.findViewById(R.id.user_notifier_count);
            traitNotifierIcon = itemView.findViewById(R.id.notifier_icon);
            holderView = itemView;
        }
    }

    private LayoutInflater inflater;
    private List<TraitResult> traitResultList;

    public TraitDefinitionListAdapter(Context context) { inflater = LayoutInflater.from(context); }

    @Override
    public TraitDefinitionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.user_trait_list, parent, false);
        return new TraitDefinitionListAdapter.TraitDefinitionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TraitDefinitionViewHolder holder, int position) {

        final TraitResult traitResult = traitResultList.get(position);
        if(traitResult != null) {
            if (traitResult.getStatementId() != null && traitResult.getStatementId() != 0) {
                setTrait(holder, traitResult);
            } else {
                setMessage(holder, traitResult);
            }
        }
        else {
            holder.traitEmoji.setText("Loading.. ");
            holder.traitName.setText("Loading.. ");
        }
    }

    private void setTrait(TraitDefinitionViewHolder holder, final TraitResult traitResult) {
        EmojiCompat.get().registerInitCallback(new EmojiInitCallback(holder.traitEmoji, new String(Character.toChars(traitResult.traitStatement.get(0).getCodePoint()))));
        holder.traitName.setText(traitResult.traitStatement.get(0).getTraitName());

        Integer amount = traitResult.traitNotifierFillerResultList.size();
        String resourceName = amount > 9 ? "num_10_plus" : "num_"+amount;
        holder.traitNotifierCount.setImageResource(holder.itemView.getContext().getResources().getIdentifier( resourceName, "drawable", holder.itemView.getContext().getPackageName()));

        if(traitResult.isSelected) {
            setViewSelected(holder.holderView);
        }
        else {
            setViewUnselected(holder.holderView);
        }
    }

    private void setMessage(TraitDefinitionViewHolder holder, final TraitResult traitResult) {
        holder.traitName.setText(traitResult.traitStatement.get(0).getTraitName());
        holder.traitName.setTextColor(Color.LTGRAY);
        holder.traitNotifierIcon.setImageResource(R.drawable.keyboard_icon);
//        holder.traitNotifierIcon.setPadding(0, 40,0,0);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                400
        );
        params.setMargins(0, 0, 0, 0);
        holder.traitNotifierIcon.setLayoutParams(params);
        holder.traitNotifierIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        holder.traitNotifierIcon.setScaleX(0.6f);
        holder.traitNotifierIcon.setScaleY(0.6f);
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
        if (traitResultList.get(position).getStatementId() != null) {
            return traitResultList.get(position);
        }
        else {
            return null;
        }
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
