package com.cleanup.todoc.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.Date;
import java.util.concurrent.Executors;

@Database(entities = {Task.class, Project.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
    public abstract ProjectDao projectDao();

    private static volatile AppDatabase DATABASE; // = null if not instantiate
    public static AppDatabase instantiateAppDatabase(Context context){
        if(DATABASE == null){
            synchronized (AppDatabase.class){
                if(DATABASE == null){
                    DATABASE = Room.databaseBuilder(context,
                            AppDatabase.class, "database-name")
                            .addCallback(prepopulateDatabase())
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return DATABASE;
    }

    public static AppDatabase instantiateAppDatabaseInMemory(Context context){

        DATABASE = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .addCallback(prepopulateDatabase())
                .addMigrations(MIGRATION_1_2)
                .build();

        return DATABASE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Executors.newSingleThreadExecutor().execute(() -> DATABASE.projectDao().addProject(Project.getAllProjects()));
        }
    };

    private static Callback prepopulateDatabase() {
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                Executors.newSingleThreadExecutor().execute(() -> DATABASE.projectDao().addProject(Project.getAllProjects()));
                //Executors.newSingleThreadExecutor().execute(() -> DATABASE.projectDao().addProject(new Project(1L, "Projet Tartampion", 0xFFEADAD1)));
                //Executors.newSingleThreadExecutor().execute(() -> DATABASE.taskDao().addTask(new Task(1L,"test",new Date().getTime())));
            }
        };
    }


}
