package com.permoji.api.trait;

import android.content.Context;

import com.permoji.api.BaseApiClient;
import com.permoji.api.BaseCallback;

import java.util.List;

/**
 * Created by michael on 22/05/18.
 */

public class TraitApiClient extends BaseApiClient<Trait> {

    private TraitApiInterface apiCall;

    public TraitApiClient(Context context) {
        super(context);

        apiCall = getRetrofitCaller().create(TraitApiInterface.class);
    }


    public void getAll(BaseCallback<List<Trait>> callback ) {

        executeGet(callback, apiCall.getAll());
    }

    public void create(BaseCallback<Trait> callback, Trait trait) {

        executeSend(callback, apiCall.create(trait));

    }

    public void update(BaseCallback<Trait> callback, Trait trait) {

        executeSend(callback, apiCall.update(trait));
    }
}