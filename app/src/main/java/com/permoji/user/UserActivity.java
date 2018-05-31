package com.permoji.user;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.permoji.api.trait.Trait;
import com.permoji.broadcast.FacebookNotificationListenerService;
import com.permoji.model.UserTrait;
import com.permoji.notifications.Notification;
import com.permoji.notifications.NotificationListAdapter;
import com.permoji.notifications.NotificationViewModel;
import com.permoji.notifications.NotificationsRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import io.github.ctrlaltdel.aosp.ime.R;

public class UserActivity extends AppCompatActivity {

    private UserTraitsViewModel userTraitsViewModel;
    private UserTraitListAdapter userTraitListAdapter;
    private NotificationViewModel notificationViewModel;
    private NotificationListAdapter notificationListAdapter;

    private NotificationReceiver nReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        userTraitListAdapter = new UserTraitListAdapter(this);
        userTraitsViewModel = ViewModelProviders.of(this).get(UserTraitsViewModel.class);

        setUserTraitsObserver();
        setUserTraitsRecyclerView();

        notificationListAdapter = new NotificationListAdapter(this);
        notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);

        setNotificationsRecyclerView();
        setNotificationsObserver();

        registerFbReceiver();
    }

    private void setUserTraitsObserver() {

        final List<Notification> notifications = new NotificationsRepository(this).generateNotificationsList();

        userTraitsViewModel.getGetUserTraits().observe(this, new Observer<List<Trait>>() {
            @Override
            public void onChanged(@Nullable List<Trait> traits) {
                userTraitListAdapter.setTraits(traits);

                notificationListAdapter.setNotifications(notifications);
            }
        });
    }

    private void setUserTraitsRecyclerView() {
        RecyclerView view = findViewById(R.id.user_trait_recyclerView);
        view.setAdapter(userTraitListAdapter);
        view.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setNotificationsObserver() {

        notificationViewModel.getNotifications().observe(this, new Observer<List<Notification>>() {
            @Override
            public void onChanged(@Nullable List<Notification> notifications) {
                notificationListAdapter.setNotifications(notifications);
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
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nReceiver);
    }

    private void registerFbReceiver() {
        nReceiver = new NotificationReceiver();
        registerReceiver(nReceiver,new IntentFilter("io.github.ctrlaltdel.aosp.ime"));
    }


    class NotificationReceiver extends BroadcastReceiver {

        private UserTraitsRepository userTraitsRepository;

        @Override
        public void onReceive(Context context, Intent intent) {

            userTraitsRepository = new UserTraitsRepository(context);

            List<Trait> cachedTraits = userTraitsRepository.getTraitsByUserId(0).getValue();

            Bundle bundle = intent.getExtras();
            Log.d(this.getClass().getSimpleName(), "Received broadcast");
            if (bundle != null) {

                ArrayList<Integer> emojiCodepoints = bundle.getIntegerArrayList("emojiCodepoints");

                for(int codepoint : emojiCodepoints) {

                    writeTraitToCache(codepoint, cachedTraits);
                }
            }
        }

        private void writeTraitToCache(int traitCodepoint, List<Trait> cachedTraits) {
            Trait traitToWrite = null;

            //TODO: Implement actual voter images
            ArrayList<String> voters = new ArrayList<>(); voters.addAll(Arrays.asList("contactimage1", "contactimage2", "contactimage3",
                    "contactimage4", "contactimage5", "contactimage6","contactimage7", "contactimage8", "contactimage9", "contactimage10"));
            Random random = new Random();

            for(Trait cachedTrait : cachedTraits) {
                if(cachedTrait.getCodepoint() == traitCodepoint) {

                    //TODO: Implement actual voter image retrieval
                    ArrayList<String> v = cachedTrait.getVoucherImageNames();
                    v.add(voters.get(random.nextInt(10)));
                    cachedTrait.setVoucherImageNames(v);

                    cachedTrait.setAmount(cachedTrait.getAmount() + 1);

                    userTraitsRepository.update(cachedTrait);
                    return;
                }
            }


            traitToWrite = new Trait();
            traitToWrite.setCodepoint(traitCodepoint);

            //TODO: Implement actual trait generation
            traitToWrite.setDescription("New Emoji");
            ArrayList<String> v = new ArrayList<>(); v.add(voters.get(random.nextInt(10)));
            traitToWrite.setVoucherImageNames(v);

            userTraitsRepository.insert(traitToWrite);

        }
    }

}
