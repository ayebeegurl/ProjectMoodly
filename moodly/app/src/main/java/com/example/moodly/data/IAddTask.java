package com.example.moodly.data;

import java.util.ArrayList;

public interface IAddTask {
    void onAddTask(Task task);
    ArrayList<Task> getTaskList();
}