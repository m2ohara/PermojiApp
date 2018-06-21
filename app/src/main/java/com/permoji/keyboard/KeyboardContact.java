package com.permoji.keyboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.permoji.trait.UserTraitActivity;
import com.permoji.user.UserActivity;

import io.github.ctrlaltdel.aosp.ime.R;
import io.github.ctrlaltdel.aosp.ime.keyboard.Key;

/**
 * Created by michael on 18/05/18.
 */

public class KeyboardContact {

    private static KeyboardContact instance;

    private FrameLayout contactFrameLayout;

    private boolean isEmojiPresent = false;
    private String emoji = null;

    private KeyboardContact() {};

    public static KeyboardContact get() {
        if(instance == null) {
            instance = new KeyboardContact();
        }
        return instance;
    }

    public void setContactFrameLayout (final FrameLayout contactFrameLayout, int themeId, final Context context) {
        this.contactFrameLayout = contactFrameLayout;

        if(themeId == 4)
            contactFrameLayout.setForeground(contactFrameLayout.getContext().getResources().getDrawable( R.drawable.contact_circle_border_lxx_dark ));

        contactFrameLayout.setOnClickListener(new View.OnClickListener() {

            private String NOTIFICATION = "io.github.ctrlaltdel.aosp.ime";

            @Override
            public void onClick(View view) {
                if(isEmojiPresent) {
                    broadcastEmoji(view);
                }
                else {
                    startUserActivity();
                }


            }

            private void startUserActivity() {
                Intent startUserActivity = new Intent(context, UserTraitActivity.class);
                startUserActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(startUserActivity);
            }

            private void broadcastEmoji(View view) {
                ((FrameLayout)view).removeAllViews();
                isEmojiPresent = false;
                if(emoji != null) {
                    broadcastNotification(emoji.codePointAt(0));
                }
            }

            private void broadcastNotification(int emojiCodepoint) {
                Intent intent = new Intent(NOTIFICATION);
                Bundle extra = new Bundle();
                extra.putInt("emojiCodepoint", emojiCodepoint);
                extra.putString("broadcastType", "keyboardBroadcast");
                intent.putExtras(extra);
                Log.i(this.getClass().getSimpleName(),"Broadcasting intent " + intent.getAction() + " for keyboard input "+emojiCodepoint );

                context.sendBroadcast(intent);
            }
        });


    }

    public void setEmojiToContactFrame(Key key, Context context) {
        contactFrameLayout.removeAllViews();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.emoji_contact_layout, contactFrameLayout, false);

        TextView emojiView = (TextView) view.findViewById(R.id.emoji_holder);
        emojiView.setText(key.getLabel());
        emoji = key.getLabel();

        Drawable drawable = context.getResources().getDrawable( R.drawable.addicon );
        ImageView addIcon = (ImageView) view.findViewById(R.id.add_icon);
        addIcon.setImageDrawable(drawable);

        contactFrameLayout.addView(view);
        isEmojiPresent = true;
    }
}
