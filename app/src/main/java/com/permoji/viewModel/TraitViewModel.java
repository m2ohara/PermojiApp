package com.permoji.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.permoji.model.result.TraitResult;
import com.permoji.repository.TraitDefinitionRepository;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created by michael on 16/06/18.
 */

public class TraitViewModel extends AndroidViewModel {

    private TraitDefinitionRepository traitDefinitionRepository;
    private LiveData<List<TraitResult>> liveTraitEntities;

    public TraitViewModel(@Nonnull Application application) {
        super(application);
        this.traitDefinitionRepository = new TraitDefinitionRepository(application.getApplicationContext());
        this.liveTraitEntities = traitDefinitionRepository.getLiveTraitEntities();
    }

    public LiveData<List<TraitResult>> getLiveTraitEntities() {
        return liveTraitEntities;
    }
}
