package com.permoji.user;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.FontRequestEmojiCompatConfig;
import android.support.v4.provider.FontRequest;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.permoji.api.trait.Trait;
import com.permoji.broadcast.NotificationReceiver;
import com.permoji.notifications.UserNotification;
import com.permoji.notifications.NotificationListAdapter;
import com.permoji.notifications.NotificationViewModel;

import java.util.List;

import io.github.ctrlaltdel.aosp.ime.R;

public class UserActivity extends AppCompatActivity {

//    private UserTraitsViewModel userTraitsViewModel;
    private UserTraitListAdapter userTraitListAdapter;
    private NotificationViewModel notificationViewModel;
    private NotificationListAdapter notificationListAdapter;

    private NotificationReceiver nReceiver;

    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        userTraitListAdapter = new UserTraitListAdapter(this);
//        userTraitsViewModel = ViewModelProviders.of(this).get(UserTraitsViewModel.class);
//        setUserTraitsObserver();
        setUserTraitsRecyclerView();

        notificationListAdapter = new NotificationListAdapter(this);
        notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
        setNotificationsRecyclerView();
        setNotificationsObserver();

        initializeDeviceEmojiSupport();
    }

    private void initializeDeviceEmojiSupport() {
        FontRequest fontRequest = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.com_google_android_gms_fonts_certs);
        EmojiCompat.Config config = new FontRequestEmojiCompatConfig(this, fontRequest)
                .setReplaceAll(true)
                .registerInitCallback(new EmojiCompat.InitCallback() {
                    @Override
                    public void onInitialized() {
                        Log.i(TAG, "EmojiCompat initialized");
                    }

                    @Override
                    public void onFailed(@Nullable Throwable throwable) {
                        Log.e(TAG, "EmojiCompat initialization failed", throwable);
                    }
                });
        EmojiCompat.init(config);
    }

//    private void setUserTraitsObserver() {
//
//        userTraitsViewModel.getGetUserTraits().observe(this, new Observer<List<Trait>>() {
//            @Override
//            public void onChanged(@Nullable List<Trait> traits) {
//                userTraitListAdapter.setTraits(traits);
//            }
//        });
//    }

    private void setUserTraitsRecyclerView() {
        RecyclerView view = findViewById(R.id.user_trait_recyclerView);
        view.setAdapter(userTraitListAdapter);
        view.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setNotificationsObserver() {

        notificationViewModel.getNotifications().observe(this, new Observer<List<UserNotification>>() {
            @Override
            public void onChanged(@Nullable List<UserNotification> userNotifications) {
                notificationListAdapter.setUserNotifications(userNotifications);
            }
        });
    }

    private void setNotificationsRecyclerView() {
        RecyclerView view = findViewById(R.id.user_notification_recyclerView);
        view.setAdapter(notificationListAdapter);
        view.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_trait_activity:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected  void onResume() {
        super.onResume();
//        registerNotificationReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(nReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void registerNotificationReceiver() {
        nReceiver = new NotificationReceiver();
        registerReceiver(nReceiver,new IntentFilter("io.github.ctrlaltdel.aosp.ime"));
    }

}
