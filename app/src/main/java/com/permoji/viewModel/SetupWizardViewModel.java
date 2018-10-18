package com.permoji.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.MutableBoolean;

import com.permoji.database.LocalDatabase;
import com.permoji.model.entity.User;
import com.permoji.repository.UserRepository;

import java.util.List;

/**
 * Created by michael on 03/10/18.
 */

public class SetupWizardViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    public LiveData<List<User>> user;
    public MutableBoolean isUserNameInputted;

    public SetupWizardViewModel(@NonNull Application application) {
        super(application);

        userRepository = new UserRepository(LocalDatabase.getInstance(application).userDao());

        user = userRepository.get();

        isUserNameInputted = new MutableBoolean(false);

    }


}
