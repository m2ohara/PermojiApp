package com.permoji.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.permoji.model.entity.TraitAdjustProperties;
import com.permoji.repository.TraitAdjustPropertiesRepository;

import java.util.List;

/**
 * Created by michael on 21/06/18.
 */

public class TraitAdjustViewModel extends AndroidViewModel {

    private TraitAdjustPropertiesRepository traitAdjustPropertiesRepository;

    public TraitAdjustViewModel(@NonNull Application application) {
        super(application);

        traitAdjustPropertiesRepository = new TraitAdjustPropertiesRepository(application);
    }

    public LiveData<List<TraitAdjustProperties>> getTraitAdjustProperties() {
        return traitAdjustPropertiesRepository.getLiveTraitAdjustProperties();
    }
}
