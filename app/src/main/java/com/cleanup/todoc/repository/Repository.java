package com.cleanup.todoc.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.AppDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class Repository {
    private AppDatabase appDatabase;

    public Repository(AppDatabase appDatabase){
        this.appDatabase = appDatabase;
    }

    public void deleteTask(Task task){
        Thread thread1 = new Thread(){
            public void run(){  appDatabase.taskDao().deleteTask(task); }
        };
        thread1.start();

    }

    public void addTask(Task task){
        Thread thread2 = new Thread(){
            public void run(){ appDatabase.taskDao().addTask(task);}
        };
        thread2.start();
    }

    public LiveData<List<Task>> getTaskList(){
        //Log.d("qqqqMainActivity", appDatabase.toString());
        //Log.d("qqqqMainActivity", appDatabase.taskDao().toString());
        return appDatabase.taskDao().getTaskList();
    }

    public void addProject(Project project){
        Thread thread3 = new Thread(){
            public void run(){ appDatabase.projectDao().addProject(project);}
        };
        thread3.start();
    }
}
