package com.cleanup.todoc.ui;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.injections.DI;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.SortMethod;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repository.Repository;

import java.util.Collections;
import java.util.List;

public class TaskViewModel extends ViewModel {

    //private Context context;

    private final Repository repository;

    LiveData<List<Task>> liveTasks;

    MutableLiveData<List<Task>> sortedTasks = new MutableLiveData<List<Task>>();

    public TaskViewModel(Repository repository) {
        this.repository = repository;
        liveTasks = getTaskList();
    }

    public void deleteTask(Task task){
        repository.deleteTask(task);
    }

    public void addTask(Task task){
        repository.addTask(task);
    }

    public LiveData<List<Task>> getTaskList(){
        return repository.getTaskList();
    }

    public void sortMethod(SortMethod sortMethod){
        if(liveTasks.getValue() != null) {
            List<Task> tasks = liveTasks.getValue();
            switch (sortMethod) {
                case ALPHABETICAL:
                    Collections.sort(tasks, new Task.TaskAZComparator());
                    break;
                case ALPHABETICAL_INVERTED:
                    Collections.sort(tasks, new Task.TaskZAComparator());
                    break;
                case RECENT_FIRST:
                    Collections.sort(tasks, new Task.TaskRecentComparator());
                    break;
                case OLD_FIRST:
                    Collections.sort(tasks, new Task.TaskOldComparator());
                    break;
            }
            sortedTasks.setValue(tasks);
        }
    }






}
