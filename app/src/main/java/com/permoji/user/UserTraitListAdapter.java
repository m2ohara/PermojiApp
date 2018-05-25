package com.permoji.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.permoji.api.trait.Trait;

import java.util.List;

import io.github.ctrlaltdel.aosp.ime.R;

/**
 * Created by michael on 24/05/18.
 */

public class UserTraitListAdapter extends RecyclerView.Adapter<UserTraitListAdapter.UserTraitViewHolder> {

    class UserTraitViewHolder extends RecyclerView.ViewHolder {
        private final TextView traitEmoji;
        private final TextView traitName;

        private UserTraitViewHolder(View itemView) {
            super(itemView);
            traitEmoji = (TextView) itemView.findViewById(R.id.user_trait_emoji);
            traitName = (TextView) itemView.findViewById(R.id.user_trait_name);

        }
    }

    private final LayoutInflater mInflater;
    private List<Trait> userTraits;

    UserTraitListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public UserTraitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.user_trait_list, parent, false);
        return new UserTraitViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserTraitViewHolder holder, int position) {

        Trait trait = userTraits.get(position);
        if(trait != null) {
            holder.traitEmoji.setText(new String(Character.toChars(trait.getCodepoint())));
            holder.traitName.setText(trait.getDescription());
        }
        else {
            holder.traitEmoji.setText("NA");
            holder.traitName.setText("Not available");
        }
    }

    @Override
    public int getItemCount() {
        if(userTraits != null) {
            return  userTraits.size();
        }
        else return 0;
    }

    void setTraits(List<Trait> userTraits) {
        this.userTraits = userTraits;
        notifyDataSetChanged();
    }


}
