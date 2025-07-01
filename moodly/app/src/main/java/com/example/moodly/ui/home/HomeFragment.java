package com.example.moodly.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodly.MainActivity2;
import com.example.moodly.data.AddTaskAdapter;
import com.example.moodly.data.MoodFragment;
import com.example.moodly.data.Task;
import com.example.moodly.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeFragment extends Fragment implements MoodFragment.OnMoodSelectedListener {

    private RecyclerView recyclerView;
    private AddTaskAdapter adapter;
    private ArrayList<Task> taskList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); // Retain the fragment instance
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (adapter == null) {
            // Initialize adapter with the task list
            if (getActivity() instanceof MainActivity2) {
                ArrayList<Task> mainActivityTasks = ((MainActivity2) getActivity()).getTaskList();
                if (mainActivityTasks != null) {
                    taskList.addAll(mainActivityTasks);
                }
            }
            adapter = new AddTaskAdapter(getActivity(), taskList);
        }

        recyclerView.setAdapter(adapter);

        return root;
    }

    //
    // Sort tasks by difficulty (highest to lowest)
    public void sortTasksByDifficultyDescending() {
        Log.d("HomeFragment", "Before sorting (Descending): " + taskList);
        Collections.sort(taskList, (task1, task2) -> Float.compare(task2.getRating(), task1.getRating())); // Hardest to easiest
        Log.d("HomeFragment", "After sorting (Descending): " + taskList);

        // Update adapter dataset and notify changes
        if (adapter != null) {
            adapter.setTasks(taskList); // Pass the updated taskList to the adapter
            Log.d("HomeFragment", "UI updated after descending sort.");
        }
    }

    public void sortTasksByDifficultyAscending() {
        Log.d("HomeFragment", "Before sorting (Ascending): " + taskList);
        Collections.sort(taskList, (task1, task2) -> Float.compare(task1.getRating(), task2.getRating())); // Easiest to hardest
        Log.d("HomeFragment", "After sorting (Ascending): " + taskList);

        // Update adapter dataset and notify changes
        if (adapter != null) {
            adapter.setTasks(taskList); // Pass the updated taskList to the adapter
            Log.d("HomeFragment", "UI updated after ascending sort.");
        }
    }

    //


    public void updateTaskList(Task newTask) {
        if (adapter != null) {
            adapter.addTask(newTask);
            recyclerView.scrollToPosition(adapter.getItemCount() - 1); // Scroll to new item
            Log.d("HomeFragment", "Task added to adapter: " + newTask.getTitle());
        } else {
            Log.e("HomeFragment", "Adapter is null");
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }


        public void updateTask(Task updatedTask) {
        adapter.updateTask(updatedTask);
        }

        //
        @Override
        public void onMoodSelected(String mood) {
            Log.d("HomeFragment", "Mood selected: " + mood);
            if (mood == null) {
                Log.e("HomeFragment", "Received null mood");
                return;
            }

            if ("happy".equals(mood)) {
                Log.d("HomeFragment", "Sorting tasks in descending order");
                sortTasksByDifficultyDescending(); // Sort tasks from hardest to easiest
            } else if ("sad".equals(mood)) {
                Log.d("HomeFragment", "Sorting tasks in ascending order");
                sortTasksByDifficultyAscending(); // Sort tasks from easiest to hardest
            }
            if (adapter != null) {
                adapter.setTasks(taskList);
                adapter.notifyDataSetChanged();
                Log.d("HomeFragment", "Adapter notified of data changes. UI should refresh.");
            } else {
                Log.e("HomeFragment", "Adapter is null. Cannot notify UI of changes.");
            }
        }

    //

}
