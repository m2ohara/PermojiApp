package com.permoji.api.trait;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by michael on 22/05/18.
 */

public interface TraitApiInterface {

    @Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=2419200")
    @GET("api/UserExpression")
    Call<List<Trait>> getAll();

    @POST("api/UserExpression")
    Call<Trait> create(@Body Trait expression);

    @PUT("api/UserExpression")
    Call<Trait> update(@Body Trait expression);
}