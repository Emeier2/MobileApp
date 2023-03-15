package com.example.dev;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserData table);

    @Delete
    void delete(List<UserData> data);

    @Query("SELECT * from profileTable WHERE ID = :id")
    List<UserData> getData( int id);
}
