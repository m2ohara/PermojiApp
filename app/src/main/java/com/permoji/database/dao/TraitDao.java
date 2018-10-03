//package com.permoji.database.dao;
//
//import android.arch.lifecycle.LiveData;
//import android.arch.persistence.room.Dao;
//import android.arch.persistence.room.Query;
//
//import com.permoji.api.trait.Trait;
//import com.permoji.database.BaseDAO;
//
//import java.util.List;
//
///**
// * Created by michael on 24/05/18.
// */
//
//@Dao
//public interface TraitDao extends BaseDAO<Trait> {
//
//    @Query("Select * from Trait")
//    LiveData<List<Trait>> getAllLive();
//
//    @Query("Select * from Trait")
//    List<Trait> getAll();
//
//    @Query("Delete from Trait")
//    void deleteAll();
//
//}
