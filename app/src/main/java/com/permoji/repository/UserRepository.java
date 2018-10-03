package com.permoji.repository;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

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
        return userDao.get();
    }

    public void Insert(User user) {
        new InsertAsync(userDao).execute(user);
    }

    class InsertAsync extends AsyncTask<User, Void, Void> {

        private UserDao userDao;

        public InsertAsync(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {

            userDao.insert(users[0]);

            return null;
        }
    }
}
