package com.permoji.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.permoji.database.LocalDatabase;
import com.permoji.model.entity.User;
import com.permoji.repository.UserRepository;

import java.util.List;

/**
 * Created by michael on 03/10/18.
 */

public class TraitSelectedViewModel extends AndroidViewModel {

    public LiveData<List<User>> getLiveUsers;
    private UserRepository userRepository;

    public TraitSelectedViewModel(@NonNull Application application) {
        super(application);

        userRepository = new UserRepository(LocalDatabase.getInstance(application).userDao());

        getLiveUsers = userRepository.get();

    }


}
