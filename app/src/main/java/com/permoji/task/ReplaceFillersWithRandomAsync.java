package com.permoji.task;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;

import com.permoji.model.entity.NotifierFiller;
import com.permoji.model.entity.TraitFiller;
import com.permoji.model.result.NotifierFillerResult;
import com.permoji.model.result.TraitNotifierFillerResult;
import com.permoji.repository.NotifierFillerRepository;
import com.permoji.repository.TraitAdjustPropertiesRepository;
import com.permoji.repository.TraitFillerRepository;

import java.util.List;
import java.util.Random;

/**
 * Created by michael on 22/06/18.
 */

public class ReplaceFillersWithRandomAsync extends AsyncTask<TraitNotifierFillerResult, Void, Integer> {

    private NotifierFillerRepository notifierFillerRepository;
    private TraitFillerRepository traitFillerRepository;
    private TraitAdjustPropertiesRepository traitAdjustPropertiesRepository;
    private RecyclerView.Adapter listAdapter;


    public ReplaceFillersWithRandomAsync(Context context, RecyclerView.Adapter listAdapter) {
        notifierFillerRepository = new NotifierFillerRepository(context);
        traitFillerRepository = new TraitFillerRepository(context);
        traitAdjustPropertiesRepository = new TraitAdjustPropertiesRepository(context);
        this.listAdapter = listAdapter;

    }
    @Override
    protected Integer doInBackground(TraitNotifierFillerResult... traitNotifierFillerResults) {

        //Get fillers and remove existing
        List<NotifierFillerResult> currentNotifierFillerResults = traitNotifierFillerResults[0].notifierFillerResultList;
        List<TraitFiller> traitFillerList = traitFillerRepository.getAll();
        for(NotifierFillerResult traitFiller : currentNotifierFillerResults) {
            traitFillerList.remove(traitFiller.traitFiller.get(0));
        }

        //Build new entities and insert
        Random random = new Random();
        for(int count = 0; count < currentNotifierFillerResults.size(); count++) {
            TraitFiller traitFiller = traitFillerList.remove(random.nextInt(traitFillerList.size()));

            NotifierFiller notifierFiller = new NotifierFiller();
            notifierFiller.setId(currentNotifierFillerResults.get(count).getId());
            notifierFiller.setTraitNotifierFillerId(currentNotifierFillerResults.get(count).getTraitNotifierFillerId());
            notifierFiller.setFillerId(traitFiller.getId());

            notifierFillerRepository.update(notifierFiller);
        }

        traitAdjustPropertiesRepository.decreaseCount(1);

        return 1;
    }

    @Override
    protected void onPostExecute(Integer result) {
        if(result == 1) {
            listAdapter.notifyDataSetChanged();
        }
    }
}
