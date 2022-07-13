package com.cleanup.todoc.injections;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cleanup.todoc.repository.Repository;
import com.cleanup.todoc.ui.TaskViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Repository repository;
    private static ViewModelFactory factory;

    public static ViewModelFactory getInstance(Context context){
        if(factory == null){
            synchronized (ViewModelFactory.class){
                if (factory == null){
                    factory = new ViewModelFactory(context);
                }
            }
        }
        return factory;
    }

    public ViewModelFactory(Context context) {
        this.repository = DI.getRepository(context);
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TaskViewModel.class)){
            return (T) new TaskViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
