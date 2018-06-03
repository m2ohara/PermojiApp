package com.permoji.compatability;

import android.support.text.emoji.EmojiCompat;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by michael on 03/06/18.
 */

public class EmojiInitCallback extends EmojiCompat.InitCallback {

    private final WeakReference<TextView> textView;
    private final String text;

    public EmojiInitCallback(TextView textView, String text) {
        this.textView = new WeakReference<>(textView);
        this.text = text;
    }

    @Override
    public void onInitialized() {
        final TextView regularTextView = textView.get();
        if (regularTextView != null) {
            final EmojiCompat compat = EmojiCompat.get();
            regularTextView.setText(
                    compat.process(text));
        }
    }

}