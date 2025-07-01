package com.example.moodly;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.moodly.data.IAddTask;
import com.example.moodly.data.Task;

import java.util.Calendar;

public class AddTask extends DialogFragment {
    private IAddTask addTaskCallback;
    private EditText taskTitle;
    private EditText taskDesc;
    private EditText taskDate;
    private EditText taskDeadline;
    private EditText taskTime;
    private Button addTaskButton;
    private RatingBar ratingBar;
    private ImageButton closeBtn;

    public AddTask(IAddTask addTaskCallback) {
        super();
        this.addTaskCallback = addTaskCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_addtask, container, false); // Make sure this matches your layout file name
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        taskTitle = view.findViewById(R.id.addTaskTitle);
        taskDesc = view.findViewById(R.id.addTaskDescription);
        taskDate = view.findViewById(R.id.taskDate);
        taskDeadline = view.findViewById(R.id.taskDeadline);
        taskTime = view.findViewById(R.id.taskTime);
        addTaskButton = view.findViewById(R.id.addTask);
        ratingBar = view.findViewById(R.id.ratingBar);
        closeBtn = view.findViewById(R.id.close);

        closeBtn.setOnClickListener(v -> dismiss());
        taskDate.setOnClickListener(v -> showDatePicker(taskDate));
        taskDeadline.setOnClickListener(v -> showDatePicker(taskDeadline));
        taskTime.setOnClickListener(v -> showTimePicker());

        // Set click listener for add task button
        addTaskButton.setOnClickListener(v -> {
            // Get values from input fields
            long id = 0;
            long userId = 0;
            String title = taskTitle.getText().toString().trim();
            String description = taskDesc.getText().toString().trim();
            String date = taskDate.getText().toString().trim();
            String deadline = taskDeadline.getText().toString().trim();
            String time = taskTime.getText().toString().trim();
            float rating = ratingBar.getRating();
            String status = "ON PROGRESS";

            // Validate inputs
            if (date.isEmpty() || deadline.isEmpty() || time.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Create task with actual values
                Task task = new Task(id, userId, title, description, date, deadline, time, rating, status);
                this.addTaskCallback.onAddTask(task);
                Toast.makeText(getActivity(), "Task added successfully!", Toast.LENGTH_SHORT).show();
                dismiss();
            } catch (Exception e) {
                Log.e("Debug", e.getMessage());
                Toast.makeText(getActivity(), "Failed to add task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set the width and height of the dialog
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    private void showDatePicker(EditText targetEditText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format tanggal yang dipilih dan set ke EditText target
                    String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear; // Bulan dimulai dari 0
                    targetEditText.setText(formattedDate);
                }, year, month, day);

        datePickerDialog.show();
    }


    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                (view, selectedHour, selectedMinute) -> {
                    // Format the time and set it to the EditText
                    String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                    taskTime.setText(formattedTime);
                }, hour, minute, true);
        timePickerDialog.show();
    }
}



