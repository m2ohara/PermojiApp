package com.permoji.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.permoji.notifiers.StatementBuilder;
import com.permoji.model.result.TraitNotifierFillerResult;
import com.permoji.model.result.TraitResult;

import java.io.File;
import java.util.List;

import io.github.ctrlaltdel.aosp.ime.R;

/**
 * Created by michael on 18/06/18.
 */

public class TraitNotifierListAdapter extends RecyclerView.Adapter<TraitNotifierListAdapter.TraitNotifierViewHolder> {

    class TraitNotifierViewHolder extends RecyclerView.ViewHolder {

        private final ImageView notifierImage;
        private final TextView heading;
        private final TextView content;
        private final ImageButton imageButton;

        public TraitNotifierViewHolder(View itemView) {
            super(itemView);
            this.notifierImage = itemView.findViewById(R.id.list_item_notifier_image);
            this.heading = itemView.findViewById(R.id.list_item_heading);
            this.content = itemView.findViewById(R.id.list_item_content);
            this.imageButton = itemView.findViewById(R.id.list_item_button);
        }
    }

    private Resources resources;
    private LayoutInflater inflater;
    private TraitResult traitResult;
    private List<TraitNotifierFillerResult> traitNotifierFillerResultList;

    public TraitNotifierListAdapter(Context context, TraitResult traitResult) {
        this.traitResult = traitResult;
        inflater = LayoutInflater.from(context);
        resources = context.getResources();
    }

    @Override
    public TraitNotifierViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.trait_notifier_list_item, parent, false);
        return new TraitNotifierListAdapter.TraitNotifierViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TraitNotifierViewHolder holder, int position) {

        TraitNotifierFillerResult traitNotifierFillerResult = traitNotifierFillerResultList.get(position);

        File imageFile = new File(traitNotifierFillerResult.notifier.get(0).getImagePath());
        Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        holder.notifierImage.setImageBitmap(imageBitmap);

        holder.heading.setText(traitResult.traitStatement.get(0).getHeading().replace("<name>", traitNotifierFillerResult.notifier.get(0).getName() ));
        holder.imageButton.setImageDrawable(setDrawableSize(R.drawable.tick_icon, 80, 80));

        StatementBuilder.get().buildStatement(traitResult, holder.content, traitNotifierFillerResult);

    }

    private Drawable setDrawableSize(int id, int width, int height) {
        Drawable dr = resources.getDrawable(id);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Drawable d = new BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, width, height, true));
        return d;
    }

    @Override
    public int getItemCount() {
        if(traitNotifierFillerResultList != null) {
            return traitNotifierFillerResultList.size();
        }
        return 0;
    }

    public void setTraitNotifierFillerResultList(List<TraitNotifierFillerResult> traitNotifierFillerResultList) {
        this.traitNotifierFillerResultList = traitNotifierFillerResultList;
    }
}
