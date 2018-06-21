package com.permoji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.text.emoji.EmojiCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.permoji.compatability.EmojiInitCallback;
import com.permoji.notifiers.TraitNotifierActivity;
import com.permoji.model.result.TraitResult;

import java.util.List;

import io.github.ctrlaltdel.aosp.ime.R;

/**
 * Created by michael on 15/06/18.
 */

public class TraitDefinitionListAdapter extends RecyclerView.Adapter<TraitDefinitionListAdapter.TraitDefinitionViewHolder> {

    class TraitDefinitionViewHolder extends RecyclerView.ViewHolder {
        private final TextView traitEmoji;
        private final TextView traitName;
        private final LinearLayout traitNotifierCount;

        public TraitDefinitionViewHolder(View itemView) {
            super(itemView);
            traitEmoji = (TextView) itemView.findViewById(R.id.user_trait_emoji);
            traitName = (TextView) itemView.findViewById(R.id.user_trait_name);
            traitNotifierCount = itemView.findViewById(R.id.user_notifier_count);
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

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent startUserActivity = new Intent(v.getContext(), TraitNotifierActivity.class);

                    startUserActivity.putExtra("TraitResult", traitResult);
                    if(traitResult.traitNotifierFillerResultList.size() > 0)
                        startUserActivity.putExtra("NotifierFillerEntityId", traitResult.traitNotifierFillerResultList.get(0).getId());

                    startUserActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(startUserActivity);
                }
            });

            ImageView imageView = (ImageView) inflater.inflate(R.layout.user_notifier_count_image, viewGroup, false);
            String amount = Integer.toString(traitResult.traitNotifierFillerResultList.size());
            String[] digits = amount.split("(?!^)");
            //TODO: account for amount > 10
            imageView.setImageResource(holder.itemView.getContext().getResources().getIdentifier("num_"+amount, "drawable", holder.itemView.getContext().getPackageName()));

            holder.traitNotifierCount.addView(imageView);
        }
        else {
            holder.traitEmoji.setText("Loading.. ");
            holder.traitName.setText("Loading.. ");
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
}
