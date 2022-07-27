package com.cleanup.todoc;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.cleanup.todoc.database.AppDatabase;
import com.cleanup.todoc.injections.DI;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repository.Repository;
import com.cleanup.todoc.ui.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.cleanup.todoc.TestUtils.withRecyclerView;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @author Gaëtan HERFRAY
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class DaoTest {

    private AppDatabase appDatabase;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();// permet d'executer les taches de manière synchrone

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
    }
    @After
    public void closeDb() throws IOException {
        appDatabase.close();
    }


    @Test
    public void insertAndGetTask() throws InterruptedException {

        this.appDatabase.projectDao().addProject(new Project(1L, "Projet Tartampion", 0xFFEADAD1));
        this.appDatabase.taskDao().addTask(new Task(1,"Task 1",new Date().getTime()));
        this.appDatabase.taskDao().addTask(new Task(1,"Task 2",new Date().getTime()));
        this.appDatabase.taskDao().addTask(new Task(1,"Task 3",new Date().getTime()));

        List<Task> tasks = LiveDataTestUtil.getValue(this.appDatabase.taskDao().getTaskList());
        assertEquals(3, tasks.size());
    }

    @Test
    public void insertAndDeleteTask() throws InterruptedException {
        // Adding demo project & demo task. Next, get the task added & delete it.
        this.appDatabase.projectDao().addProject(new Project(1L, "Projet Tartampion", 0xFFEADAD1));
        this.appDatabase.taskDao().addTask(new Task(1,"Task 1",new Date().getTime()));
        List<Task> tasks = LiveDataTestUtil.getValue(this.appDatabase.taskDao().getTaskList());
        this.appDatabase.taskDao().deleteTask(tasks.get(0));

        tasks = LiveDataTestUtil.getValue(this.appDatabase.taskDao().getTaskList());
        assertEquals(0,tasks.size());
        assertTrue(tasks.isEmpty());


    }


}
