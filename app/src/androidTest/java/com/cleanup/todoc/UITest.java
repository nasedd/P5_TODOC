package com.cleanup.todoc;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.cleanup.todoc.TestUtils.withRecyclerView;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

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

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UITest {

    private AppDatabase appDatabase;
    Repository repository = DI.getRepository(ApplicationProvider.getApplicationContext());

    @Rule
    //public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);
    //public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);
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

    @Test
    public void sortTasks2() {
        //ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);
        /*
        mActivityRule.getActivity();
        activityRule.getScenario().onActivity(activity -> {

        });
         */
        ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);
        MainActivity mainActivity = mActivityRule.getActivity();


        //Delete all tasks
        repository.deleteAllTasks();

        //Add tasks
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("aaa Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("zzz Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("hhh Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));

        // Sort alphabetical
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));

        // Sort alphabetical inverted
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical_invert)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));

        // Sort old first
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_oldest_first)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));

        // Sort recent first
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_recent_first)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));

        //Delete all tasks
        repository.deleteAllTasks();

    }
}
