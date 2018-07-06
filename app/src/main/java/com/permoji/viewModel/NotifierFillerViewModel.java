package com.permoji.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.permoji.model.result.TraitNotifierFillerResult;
import com.permoji.repository.TraitDefinitionRepository;
import com.permoji.repository.TraitNotifierFillerRepository;

import java.util.List;

/**
 * Created by michael on 18/06/18.
 */

public class NotifierFillerViewModel extends AndroidViewModel {

    private TraitNotifierFillerRepository traitNotifierFillerRepository;

    public NotifierFillerViewModel(@NonNull Application application) {
        super(application);

        traitNotifierFillerRepository = new TraitNotifierFillerRepository(application);
    }

    public LiveData<List<TraitNotifierFillerResult>> getLiveNotifierFillersByTraitDefinitionId(int id) {
        return traitNotifierFillerRepository.getLiveByTraitDefinitionId(id);
    }
}
