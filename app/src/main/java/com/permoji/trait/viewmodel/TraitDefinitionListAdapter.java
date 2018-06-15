package com.permoji.trait.viewmodel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.permoji.trait.data.TraitDefinition;

import java.util.List;

import io.github.ctrlaltdel.aosp.ime.R;

/**
 * Created by michael on 15/06/18.
 */

public class TraitDefinitionListAdapter extends RecyclerView.Adapter {

    class TraitDefinitionViewHolder extends RecyclerView.ViewHolder {
        private final TextView traitEmoji;
        private final TextView traitName;
        private final LinearLayout traitContacts;

        public TraitDefinitionViewHolder(View itemView) {
            super(itemView);
            traitEmoji = (TextView) itemView.findViewById(R.id.user_trait_emoji);
            traitName = (TextView) itemView.findViewById(R.id.user_trait_name);
            traitContacts = itemView.findViewById(R.id.user_trait_voters);
        }
    }

    private LayoutInflater inflater;
    private List<TraitDefinition> traitDefinitionList;

    TraitDefinitionListAdapter(Context context) { inflater = LayoutInflater.from(context); }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.user_trait_list, parent, false);
        return new TraitDefinitionListAdapter.TraitDefinitionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        if(traitDefinitionList != null) {
            return  traitDefinitionList.size();
        }
        else return 0;
    }

    void setTraits(List<TraitDefinition> userTraits) {
        this.traitDefinitionList = userTraits;
        notifyDataSetChanged();
    }
}
