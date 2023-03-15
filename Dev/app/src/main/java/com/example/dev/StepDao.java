//package com.example.dev;
//
//import androidx.room.Dao;
//import androidx.room.Insert;
//import androidx.room.OnConflictStrategy;
//import androidx.room.Query;
//
//import java.util.List;
//
//@Dao
//public interface StepDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insert(Step step);
//
//    @Query("DELETE FROM StepTable")
//    void deleteALL();
//
//    @Query("SELECT * from StepTable")
//    List<Step> getStep();
//}
