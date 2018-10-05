package com.permoji.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.text.emoji.EmojiCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.permoji.builder.StatementBuilder;
import com.permoji.compatability.EmojiInitCallback;
import com.permoji.model.entity.TraitStatement;
import com.permoji.model.entity.User;
import com.permoji.model.result.TraitNotifierFillerResult;
import com.permoji.model.result.TraitResult;
import com.permoji.viewModel.TraitSelectedViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.github.ctrlaltdel.aosp.ime.R;

public class TraitSelectedActivity extends AppCompatActivity {

    private TraitResult traitResult;
    private TraitSelectedViewModel traitSelectedViewModel;
    private String userName = "Your friend";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trait_selected);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Express your personality");
        setSupportActionBar(toolbar);

        traitResult = (TraitResult)getIntent().getSerializableExtra("TraitResult");

        traitSelectedViewModel = ViewModelProviders.of(this).get(TraitSelectedViewModel.class);
        setUserName();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareView(findViewById(R.id.selected_trait));
            }
        });
    }

    private void setUserName() {
        traitSelectedViewModel.getLiveUsers.observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                if(users.size() > 0) {
                    userName = users.get(0).getName();
                }

                setView();
            }
        });
    }

    public void setView() {

        if(traitResult != null) {

            TraitStatement traitStatement = traitResult.traitStatement.get(0);
            TraitNotifierFillerResult traitNotifierFiller = traitResult.getSelectedTraitNotifierFillerResult().get(0);

            TextView userEmotionView = findViewById(R.id.user_emotion_text);
            userEmotionView.setText("Right now "+userName+" is "+traitStatement.getTraitName().toLowerCase());

            TextView emojiHeadText = findViewById(R.id.emoji_head_image);
            EmojiCompat.get().registerInitCallback(new EmojiInitCallback(emojiHeadText, new String(Character.toChars(traitStatement.getCodePoint()))));

            TextView statementTextView = findViewById(R.id.statement_text);
//            StatementViewBuilder.get().setExistingView(traitResult, statementText);
            new StatementBuilder().setStatement(statementTextView, traitResult, traitNotifierFiller);

            TextView notifierText = findViewById(R.id.notifier_text);
            notifierText.setText(traitStatement.getHeading().replace("<name>", traitNotifierFiller.notifier.get(0).getName()));

            ImageView frameView = findViewById(R.id.notifier_frame_image);
            File imageFile =  null;
            try {
                imageFile = new File(traitNotifierFiller.notifier.get(0).getImagePath());
            }
            catch (Exception e) {
                //TODO: add default image
            }
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            frameView.setBackground(new BitmapDrawable(getResources(), imageBitmap));
            frameView.setImageResource(R.drawable.notifier_frame_2);


        }

    }

    private void shareView(View view) {
        Bitmap bm = screenShot(view);
        File file = saveBitmap(bm, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "trait_image.png");
        Log.i(this.getClass().getSimpleName(), "filepath: "+file.getAbsolutePath());
        Uri uri = Uri.fromFile(new File(file.getAbsolutePath()));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "share via"));
    }

    private Bitmap screenShot(View view) {
        view.findViewById(R.id.brand_text).setVisibility(View.VISIBLE);
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        view.findViewById(R.id.brand_text).setVisibility(View.INVISIBLE);
        return bitmap;
    }

    private static File saveBitmap(Bitmap bm, String fileName){
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Permoji/Screenshots";
        File dir = new File(path);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

}
