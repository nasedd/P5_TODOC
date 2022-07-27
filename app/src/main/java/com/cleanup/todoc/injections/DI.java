package com.cleanup.todoc.injections;

import android.content.Context;
import com.cleanup.todoc.database.AppDatabase;
import com.cleanup.todoc.repository.Repository;

public class DI {

    public static Repository repository = null;

    public static boolean databaseForTest = false;

    public static void setDatabaseForTest(boolean databaseForTest) {
        DI.databaseForTest = databaseForTest;
    }

    public static Repository getRepository(Context context){
        if(repository == null){
            if (databaseForTest){
                AppDatabase db = AppDatabase.instantiateAppDatabaseInMemory(context);
                repository = new Repository(db);
            }else {
                AppDatabase db = AppDatabase.instantiateAppDatabase(context);
                repository = new Repository(db);
            }

        }

        return repository;
    }
}
