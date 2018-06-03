package com.permoji.cache;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.permoji.api.trait.Trait;
import com.permoji.notifications.UserNotification;

import javax.annotation.Nonnull;

/**
 * Created by michael on 24/05/18.
 */

@Database(entities = {Trait.class, UserNotification.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class LocalDatabase extends RoomDatabase {

    private static LocalDatabase instance;

    public static LocalDatabase getInstance(final Context context)
    {
        if(instance == null) {
            instance =
                    Room.databaseBuilder(context.getApplicationContext(), LocalDatabase.class, "local_database")
//                            .addCallback(localDatabaseCallback)
                            .build();
        }
        return instance;
    }

    public abstract TraitDao traitDao();

    public abstract UserNotificationDao userNotificationDao();

    private static LocalDatabase.Callback localDatabaseCallback =
            new LocalDatabase.Callback() {
                @Override
                public void onOpen(@Nonnull SupportSQLiteDatabase db) {
                    super.onOpen(db);

                    new CleanDbAsync(instance).execute();
                }
            };
}
