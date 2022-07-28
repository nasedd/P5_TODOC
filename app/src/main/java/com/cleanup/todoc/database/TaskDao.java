package com.cleanup.todoc.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.model.Task;

import java.util.ArrayList;
import java.util.List;
@Dao
public interface TaskDao {

    // *** ADD *** //
    @Insert
    void addTask(Task ... task);

    // *** DELETE *** //
    @Delete
    void deleteTask(Task ... task);

    @Query("DELETE FROM Task")
    void deleteAllTask();

    // *** GET *** //
    @Query("SELECT * FROM Task")
    LiveData<List<Task>> getTaskList();

    // *** GET SORTED LIST BY OLD ***
    @Query("SELECT * FROM Task ORDER BY creationTimestamp DESC")
    LiveData<List<Task>> getTaskListOld();

    // *** GET ALPHABETICAL LIST ***
    @Query("SELECT * FROM Task ORDER BY name ASC")
    LiveData<List<Task>> getTaskListAZ();

    // *** GET ALPHABETICAL DESC LIST ***
    @Query("SELECT * FROM Task ORDER BY name DESC")
    LiveData<List<Task>> getTaskListZA();


}
