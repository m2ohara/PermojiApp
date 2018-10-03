package com.permoji.repository;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.permoji.database.dao.UserDao;
import com.permoji.model.entity.User;

import java.util.List;

/**
 * Created by michael on 01/10/18.
 */

public class UserRepository {

    private UserDao userDao;

    public UserRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    public LiveData<List<User>> get() {
        return userDao.getLive();
    }

    public void insertOrUpdateSingle(User user) {
        new InsertOrUpdateSingleAsync(userDao).execute(user);
    }

    class InsertOrUpdateSingleAsync extends AsyncTask<User, Void, Void> {

        private UserDao userDao;

        public InsertOrUpdateSingleAsync(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {

            List<User> userList = userDao.get();
            if(userList.size() > 0) {
                Log.i(this.getClass().getSimpleName(), "Updating existing user");
                User user = userList.get(0);
                user.setName(users[0].getName());
                userDao.update(user);
            }
            else {
                Log.i(this.getClass().getSimpleName(), "Creating new user");
                userDao.insert(users[0]);
            }

            return null;
        }
    }
}
