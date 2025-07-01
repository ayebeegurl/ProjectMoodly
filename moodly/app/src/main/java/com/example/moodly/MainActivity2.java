package com.example.moodly;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moodly.data.DatabaseHelper;
import com.example.moodly.data.IAddTask;
import com.example.moodly.data.IMoodChanged;
import com.example.moodly.data.MoodFragment;
import com.example.moodly.data.Task;
import com.example.moodly.data.User;
import com.example.moodly.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.moodly.databinding.ActivityMain2Binding;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements IAddTask, UpdateFragment.TaskUpdateListener, MoodFragment.OnMoodSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMain2Binding binding;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ArrayList<Task> taskList;
    private DatabaseHelper databaseHelper;
    private NavigationView navigationView;
    private long currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize taskList
        taskList = new ArrayList<>();

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Get the logged-in user ID from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        currentUserId = prefs.getLong("user_id", -1); // Keep it as long

        // Inflate the layout using View Binding
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the toolbar
        toolbar = binding.appBarMain.toolbar;
        setSupportActionBar(toolbar);
        setToolbarTitle("All Tasks"); // Set the toolbar title

        // Initialize the drawer layout
        drawerLayout = binding.drawerLayout;

        // Initialize ActionBarDrawerToggle
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState(); // Sync the toggle state
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable the home button

        // Set custom hamburger icon
        toggle.setHomeAsUpIndicator(R.drawable.baseline_menu_24);

        // Set up the Floating Action Button
        binding.appBarMain.fab.setOnClickListener(view -> {
            AddTask dialogFragment = new AddTask(this);
            dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
        });

        // Set up NavigationView
        navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        // Set up AppBarConfiguration
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_task, R.id.nav_logout) // Add other top-level destinations here
                .setOpenableLayout(drawerLayout)
                .build();

        // Show MoodFragment when the app starts or when triggered

            MoodFragment moodFragment = new MoodFragment();
            moodFragment.setMoodSelectedListener(this); // Set listener for mood selection
            moodFragment.show(getSupportFragmentManager(), "MoodFragment");
            Log.d("MainActivity2", "Moodfragment listener set");

        updateNavHeaderFromDatabase();
    }

    private void updateNavHeaderFromDatabase() {
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navEmail = headerView.findViewById(R.id.nav_email);


        Cursor cursor = databaseHelper.getAllUsers();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Profile Details", Toast.LENGTH_SHORT).show();
        } else {
            // Assuming we're using the first user's details
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL));

                navUsername.setText(username);
                navEmail.setText(email);

            }
        }
        cursor.close();
    }

    public void setToolbarTitle(String title) {
        toolbar.setTitle(title); // Set the title of the toolbar
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reset the toolbar title if needed
        setToolbarTitle("All Tasks");
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_task) {
            // Instead of creating a new fragment, find existing one or create if doesn't exist
            Fragment currentFragment = getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment_content_main);
            if (!(currentFragment instanceof HomeFragment)) {
                currentFragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main, currentFragment)
                        .commit();
            }
            setToolbarTitle("All Tasks");
        } else if (id == R.id.nav_mood) {
            MoodFragment dialogFragment = new MoodFragment();
            dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
        } else if (id == R.id.nav_logout) {
            logout();
            return true;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true; // Handle the toggle click
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddTask(Task task) {
        Log.i("TestDebug", "onAddTask");
        if (taskList == null) {
            taskList = new ArrayList<>();
        }
        taskList.add(task);

        // Add task to database
        databaseHelper.addTask(task);

        // Get the NavHostFragment first
        Fragment navHostFragment = getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);

        if (navHostFragment != null) {
            // Get the current fragment from NavHostFragment's child FragmentManager
            Fragment currentFragment = navHostFragment.getChildFragmentManager()
                    .getFragments().get(0);

            if (currentFragment instanceof HomeFragment) {
                ((HomeFragment) currentFragment).updateTaskList(task);
                Log.d("MainActivity2", "Updating HomeFragment with new task");
            } else {
                Log.e("MainActivity2", "Current fragment is not HomeFragment");
            }
        } else {
            Log.e("MainActivity2", "NavHostFragment not found");
        }
    }

    @Override
    public ArrayList<Task> getTaskList() {
        if (taskList == null || taskList.isEmpty()) {
            // If taskList is null or empty, fetch tasks from the database
            taskList = new ArrayList<>(databaseHelper.getAllTasks());
        }
        return this.taskList;
    }

    public void onTaskUpdated(Task updatedTask) {
        // Update the task in the database
        databaseHelper.updateTask(updatedTask);

        // Update the task in the taskList
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getId() == updatedTask.getId()) {
                taskList.set(i, updatedTask);
                break;
            }
        }
//diubah
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);
        if (currentFragment instanceof HomeFragment) {
            ((HomeFragment) currentFragment).updateTask(updatedTask);
        }
    }

    //

    @Override
    public void onMoodSelected(String mood) {
        Log.d("TaskMood", "Selected Mood: " + mood);

        // Retrieve the NavHostFragment
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);

        if (navHostFragment != null && navHostFragment instanceof NavHostFragment) {
            // Retrieve the active fragment within the NavHostFragment
            Fragment currentFragment = navHostFragment.getChildFragmentManager().getPrimaryNavigationFragment();

            if (currentFragment instanceof HomeFragment) {
                HomeFragment homeFragment = (HomeFragment) currentFragment;

                // Sort tasks based on the mood
                if ("happy".equals(mood)) {
                    Log.d("MainActivity2", "Sorting tasks: hardest to easiest (happy mood)");
                    homeFragment.sortTasksByDifficultyDescending();
                } else if ("sad".equals(mood)) {
                    Log.d("MainActivity2", "Sorting tasks: easiest to hardest (sad mood)");
                    homeFragment.sortTasksByDifficultyAscending();
                }
            } else {
                Log.e("MainActivity2", "Current fragment is not HomeFragment. Cannot sort tasks.");
                if (currentFragment != null) {
                    Log.d("MainActivity2", "Current fragment class: " + currentFragment.getClass().getSimpleName());
                } else {
                    Log.e("MainActivity2", "Current fragment is null.");
                }
            }
        } else {
            Log.e("MainActivity2", "NavHostFragment is null or not found.");
        }
    }


    //

    private void logout() {
        // Clear user session (e.g., SharedPreferences)
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Navigate to LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

