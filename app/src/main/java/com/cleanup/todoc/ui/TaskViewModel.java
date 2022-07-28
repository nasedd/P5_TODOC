package com.cleanup.todoc.ui;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.injections.DI;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.SortMethod;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repository.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskViewModel extends ViewModel {

    //private Context context;

    private final Repository repository;

    MediatorLiveData<List<Task>> sortedTasks = new MediatorLiveData<List<Task>>();

    LiveData<List<Project>> liveProjects;

    public TaskViewModel(Repository repository) {
        this.repository = repository;
        liveProjects = getAllProjects();
        sortedTasks.addSource(getTaskList(), list -> sortedTasks.setValue(list));
    }


    public LiveData<List<Project>> getAllProjects(){return repository.getAllProjects();}

    public void deleteTask(Task task){
        repository.deleteTask(task);
    }

    public void addTask(Task task){
        repository.addTask(task);
    }

    public LiveData<List<Task>> getTaskList(){
        return repository.getTaskList();
    }
    public LiveData<List<Task>> getTaskListOld(){
        return repository.getTaskListOld();
    }
    public LiveData<List<Task>> getTaskListAZ(){
        return repository.getTaskListAZ();
    }
    public LiveData<List<Task>> getTaskListZA(){
        return repository.getTaskListZA();
    }


    public void sortMethod(SortMethod sortMethod){
        if(sortedTasks.getValue() != null) {
            //List<Task> tasks = sortedTasks.getValue();
            switch (sortMethod) {
                case ALPHABETICAL:
                    sortedTasks.addSource(getTaskListAZ(), list -> sortedTasks.setValue(list));
                    //sortedTasks.setValue(getTaskListAZ().getValue());
                    //Collections.sort(tasks, new Task.TaskAZComparator());
                    break;
                case ALPHABETICAL_INVERTED:
                    sortedTasks.addSource(getTaskListZA(), list -> sortedTasks.setValue(list));
                    //sortedTasks.setValue(getTaskListZA().getValue());
                    //Collections.sort(tasks, new Task.TaskZAComparator());
                    break;
                case RECENT_FIRST:
                    sortedTasks.addSource(getTaskList(), list -> sortedTasks.setValue(list));
                    //sortedTasks.setValue(getTaskList().getValue());
                    //Collections.sort(tasks, new Task.TaskRecentComparator());
                    break;
                case OLD_FIRST:
                    sortedTasks.addSource(getTaskListOld(), list -> sortedTasks.setValue(list));
                    //sortedTasks.setValue(getTaskListOld().getValue());
                    //Collections.sort(tasks, new Task.TaskOldComparator());
                    break;
            }
            //sortedTasks.setValue(tasks);
        }
    }






}
