package com.permoji.user;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.permoji.api.trait.Trait;
import com.permoji.cache.Images;
import com.permoji.model.UserTrait;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

import io.github.ctrlaltdel.aosp.ime.R;

import static com.permoji.cache.Images.IMAGE_FILTER;

/**
 * Created by michael on 22/05/18.
 */

public class UserTraitAdapter extends ArrayAdapter<UserTrait> {

    private List<UserTrait> traits;

    public UserTraitAdapter(@NonNull Context context, int resource, @NonNull List<UserTrait> traits) {
        super(context, resource, traits);

        this.traits = traits;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.user_trait_list, null);

            UserTrait trait = traits.get(position);

            TextView traitEmoji = (TextView) convertView.findViewById(R.id.user_trait_emoji);
            traitEmoji.setText(new String(Character.toChars(trait.getTrait().getCodepoint())));

            TextView traitName = (TextView) convertView.findViewById(R.id.user_trait_name);
            traitName.setText(trait.getTrait().getDescription());

            LinearLayout traitVoters = (LinearLayout) convertView.findViewById(R.id.user_trait_voters);
//            traitVoters.addView(traitVoters, trait.getTrait().getAmount());

        }

        return convertView;
    }

    private void getVoterImages(LinearLayout traitVotersLayout, int voteAmount) {
        //TODO: get voters from cache
        Images.setContactImageDirectory(getContext());

        File file = getContext().getDir("Images", Context.MODE_PRIVATE);

        int count = 0;
        for (final File f : file.listFiles(IMAGE_FILTER)) {

            if(count < voteAmount) {

                Drawable drawable = Drawable.createFromPath(f.getAbsolutePath());
                ImageView view = new ImageView(getContext());
                view.setImageDrawable(drawable);

                traitVotersLayout.addView(view);

                count++;
            }
        }
    }
}
