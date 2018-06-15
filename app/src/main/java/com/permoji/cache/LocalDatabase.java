package com.permoji.cache;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.content.res.Resources;

import com.permoji.api.trait.Trait;
import com.permoji.notifications.UserNotification;
import com.permoji.trait.data.Notifier;
import com.permoji.trait.data.TraitDefinition;
import com.permoji.trait.data.TraitFiller;
import com.permoji.trait.data.TraitNotifierFiller;
import com.permoji.trait.data.TraitStatement;

import javax.annotation.Nonnull;

/**
 * Created by michael on 24/05/18.
 */

@Database(entities =
        {
                Trait.class, UserNotification.class,
                TraitDefinition.class, TraitStatement.class,
                TraitNotifierFiller.class, TraitFiller.class, Notifier.class
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

            instance =
                    Room.databaseBuilder(context.getApplicationContext(), LocalDatabase.class, "local_database")
//                            .addCallback(cleanDBCallback)
                            .addCallback(loadFilesToDBCallback)
                            .build();

        }
        return instance;
    }


    public abstract TraitDao traitDao();
    public abstract UserNotificationDao userNotificationDao();

    public abstract TraitDefinitionDao traitDefinitionDao();
    public abstract TraitStatementDao traitStatementDao();
    public abstract TraitFillerDao traitFillerDao();
    public abstract NotifierDao notifierDao();
    public abstract TraitNotifierFillerDao traitNotifierFillerDao();

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
                public void onOpen(@Nonnull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new LoadTraitsFromFileAsync(instance, resources, packageName).execute();
                }
            };
}
