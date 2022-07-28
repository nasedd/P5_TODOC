package com.cleanup.todoc.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cleanup.todoc.model.Project;

import java.util.List;

@Dao
public interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addProject(Project ... project);

    @Query("SELECT * FROM Project")
    LiveData<List<Project>> getAllProjects();
    //LiveData<Project[]> getAllProjects();
    //Project[] getAllProjects();

}
