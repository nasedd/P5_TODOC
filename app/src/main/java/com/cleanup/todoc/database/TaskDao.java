package com.cleanup.todoc.database;

import androidx.lifecycle.LiveData;
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

    // *** GET *** //
    @Query("SELECT * FROM Task")
    LiveData<List<Task>> getTaskList();

    @Query("DELETE FROM Task")
    void deleteAllTask();


}
