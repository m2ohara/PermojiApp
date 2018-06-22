package com.permoji.cache;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.permoji.api.trait.Trait;
import com.permoji.cache.dao.NotifierDao;
import com.permoji.cache.dao.NotifierFillerDao;
import com.permoji.cache.dao.TraitAdjustPropertiesDao;
import com.permoji.cache.dao.TraitDao;
import com.permoji.cache.dao.TraitDefinitionDao;
import com.permoji.cache.dao.TraitFillerDao;
import com.permoji.cache.dao.TraitNotifierFillerDao;
import com.permoji.cache.dao.TraitStatementDao;
import com.permoji.cache.dao.UserNotificationDao;
import com.permoji.cache.tasks.CleanDbAsync;
import com.permoji.cache.tasks.LoadTraitsFromFileAsync;
import com.permoji.cache.tasks.PopulatePropertiesAsync;
import com.permoji.model.entity.NotifierFiller;
import com.permoji.model.entity.TraitAdjustProperties;
import com.permoji.notifications.UserNotification;
import com.permoji.model.entity.Notifier;
import com.permoji.model.entity.TraitDefinition;
import com.permoji.model.entity.TraitFiller;
import com.permoji.model.entity.TraitNotifierFiller;
import com.permoji.model.entity.TraitStatement;

import javax.annotation.Nonnull;

/**
 * Created by michael on 24/05/18.
 */

@Database(entities =
        {
                Trait.class, UserNotification.class,
                TraitDefinition.class, TraitStatement.class,
                TraitNotifierFiller.class, NotifierFiller.class, TraitFiller.class, Notifier.class,
                TraitAdjustProperties.class
        }, version = 1)
@TypeConverters({Converters.class})
public abstract class LocalDatabase extends RoomDatabase {

    private static LocalDatabase instance;
    private static Resources resources;
    private static String packageName;

    public static LocalDatabase getInstance(final Context context)
    {
        if(instance == null) {

            resources = context.getResources();
            packageName = context.getPackageName();


            //TODO: Add clean job on release
            instance =
                    Room.databaseBuilder(context.getApplicationContext(), LocalDatabase.class, "local_database")
//                            .addCallback(cleanDBCallback)
                            .addCallback(loadFilesToDBCallback)
                            .addCallback(populatePropertiesCallback)
                            .build();

        }
        return instance;
    }


    public abstract TraitDao traitDao();
    public abstract UserNotificationDao userNotificationDao();

    public abstract TraitDefinitionDao traitDefinitionDao();
    public abstract TraitStatementDao traitStatementDao();
    public abstract TraitFillerDao traitFillerDao();
    public abstract NotifierFillerDao notifierFillerDao();
    public abstract NotifierDao notifierDao();
    public abstract TraitNotifierFillerDao traitNotifierFillerDao();
    public abstract TraitAdjustPropertiesDao traitAdjustPropertiesDao();

    private static LocalDatabase.Callback cleanDBCallback =
            new LocalDatabase.Callback() {
                @Override
                public void onOpen(@Nonnull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new CleanDbAsync(instance).execute();
                }
            };

    private static LocalDatabase.Callback loadFilesToDBCallback =
            new LocalDatabase.Callback() {
                @Override
                public void onCreate(@Nonnull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    new LoadTraitsFromFileAsync(instance, resources, packageName).execute();
                }
            };

    private  static LocalDatabase.Callback populatePropertiesCallback =
            new LocalDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    new PopulatePropertiesAsync(instance).execute();
                }
            };
}
