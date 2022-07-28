package com.cleanup.todoc.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.database.AppDatabase;
import com.cleanup.todoc.injections.DI;
import com.cleanup.todoc.injections.ViewModelFactory;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.SortMethod;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repository.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <p>Home activity of the application which is displayed when the user opens the app.</p>
 * <p>Displays the list of tasks.</p>
 *
 * @author Gaëtan HERFRAY
 */
public class MainActivity extends AppCompatActivity implements TasksAdapter.DeleteTaskListener {

    private Project[] allProjects;// = Project.getAllProjects();
    @NonNull
    private List<Task> tasks;// = new ArrayList<>();
    private TasksAdapter adapter;
    @NonNull
    private SortMethod sortMethod = SortMethod.NONE;
    @Nullable
    public AlertDialog dialog = null;
    @Nullable
    private EditText dialogEditText = null;
    @Nullable
    private Spinner dialogSpinner = null;
    // Suppress warning is safe because variable is initialized in onCreate
    @SuppressWarnings("NullableProblems")
    @NonNull
    private RecyclerView recyclerViewTasks;
    // Suppress warning is safe because variable is initialized in onCreate
    @SuppressWarnings("NullableProblems")
    @NonNull
    private TextView lblNoTasks;
    private TaskViewModel taskViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(TaskViewModel.class);

        recyclerViewTasks = findViewById(R.id.list_tasks); //recyclerView
        lblNoTasks = findViewById(R.id.lbl_no_task); //textView "no task"

        taskViewModel.liveProjects.observe(this, list -> {
            if(list == null){return;}
            allProjects = new Project[list.size()];
            list.toArray(allProjects);
        });

        taskViewModel.sortedTasks.observe(this, list -> {
            adapter = new TasksAdapter(list, this);
            updateTasks(list);
            recyclerViewTasks.setAdapter(adapter);
        });


        findViewById(R.id.fab_add_task).setOnClickListener(view -> showAddTaskDialog());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == SortMethod.findFilterById(id).getFilterId()){
            sortMethod = SortMethod.findFilterById(id);
        }

        taskViewModel.sortMethod(sortMethod); //on modifie une sortedlist observable

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeleteTask(Task task) {
        taskViewModel.deleteTask(task);
    }

    //Called when the user clicks on the positive button of the Create Task Dialog.
    private void onPositiveButtonClick(DialogInterface dialogInterface) {
        // If dialog is open
        if (dialogEditText != null && dialogSpinner != null) {
            // Get the name of the task
            String taskName = dialogEditText.getText().toString();

            // Get the selected project to be associated to the task
            Project taskProject = null;
            if (dialogSpinner.getSelectedItem() instanceof Project) {
                taskProject = (Project) dialogSpinner.getSelectedItem();
            }

            // If a name has not been set
            if (taskName.trim().isEmpty()) {
                dialogEditText.setError(getString(R.string.empty_task_name));
            }
            // If both project and name of the task have been set
            else if (taskProject != null) {
                // TODO: Replace this by id of persisted task
                long id = (long) (Math.random() * 50000);


                Task task = new Task(
                        taskProject.getId(),
                        taskName,
                        new Date().getTime()
                );

                taskViewModel.addTask(task);

                dialogInterface.dismiss();
            }
            // If name has been set, but project has not been set (this should never occur)
            else{
                dialogInterface.dismiss();
            }
        }
        // If dialog is aloready closed
        else {
            dialogInterface.dismiss();
        }
    }

    /**
     * Shows the Dialog for adding a Task
     */
    private void showAddTaskDialog() {
        final AlertDialog dialog = getAddTaskDialog();

        dialog.show();

        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);

        populateDialogSpinner();
    }

    /**
     * Updates the list of tasks in the UI
     */
    private void updateTasks(List<Task> tasks) {
        if (tasks.size() == 0) {
            lblNoTasks.setVisibility(View.VISIBLE);
            recyclerViewTasks.setVisibility(View.GONE);
        } else {
            lblNoTasks.setVisibility(View.GONE);
            recyclerViewTasks.setVisibility(View.VISIBLE);
            /*switch (sortMethod) {
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

             */
            adapter.updateTasks(tasks);
        }
    }

    /**
     * Returns the dialog allowing the user to create a new task.
     *
     * @return the dialog allowing the user to create a new task
     */
    @NonNull
    private AlertDialog getAddTaskDialog() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Dialog);

        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setView(R.layout.dialog_add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        alertBuilder.setOnDismissListener(dialogInterface -> {
            dialogEditText = null;
            dialogSpinner = null;
            dialog = null;
        });

        dialog = alertBuilder.create();

        // This instead of listener to positive button in order to avoid automatic dismiss
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        onPositiveButtonClick(dialog);
                    }
                });
            }
        });

        return dialog;
    }

    /**
     * Sets the data of the Spinner with projects to associate to a new task
     */
    private void populateDialogSpinner() {
        final ArrayAdapter<Project> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allProjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dialogSpinner != null) {
            dialogSpinner.setAdapter(adapter);
        }
    }

}
