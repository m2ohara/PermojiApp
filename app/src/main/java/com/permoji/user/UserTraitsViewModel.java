//package com.permoji.user;
//
//import android.app.Application;
//import android.arch.lifecycle.AndroidViewModel;
//import android.arch.lifecycle.LiveData;
//import android.support.annotation.NonNull;
//
//import com.permoji.api.trait.Trait;
//
//import java.util.List;
//
///**
// * Created by michael on 24/05/18.
// */
//
//public class UserTraitsViewModel extends AndroidViewModel {
//
//    private UserTraitsRepository userTraitsRepository;
//    private LiveData<List<Trait>> getUserTraits;
//
//    public UserTraitsViewModel(@NonNull Application application) {
//        super(application);
//
//        userTraitsRepository = new UserTraitsRepository(application);
//        getUserTraits = userTraitsRepository.getLiveTraitsByUserId(1);
//    }
//
//    public LiveData<List<Trait>> getGetUserTraits() {
//        return getUserTraits;
//    }
//
//}
