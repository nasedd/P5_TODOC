package com.cleanup.todoc.injections;

import android.content.Context;
import com.cleanup.todoc.database.AppDatabase;
import com.cleanup.todoc.repository.Repository;

public class DI {

    public static Repository repository = null;

    public static Repository getRepository(Context context){
        if(repository == null){
            AppDatabase db = AppDatabase.instantiateAppDatabase(context);
            repository = new Repository(db);
        }

        return repository;
    }
}
