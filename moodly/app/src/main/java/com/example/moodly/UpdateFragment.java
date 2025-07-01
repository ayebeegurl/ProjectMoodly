package com.example.moodly;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.moodly.R;
import com.example.moodly.data.AddTaskAdapter;
import com.example.moodly.data.DatabaseHelper;
import com.example.moodly.data.Task;

import java.util.Calendar;

public class UpdateFragment extends DialogFragment {

    private AddTaskAdapter adapter;
    private EditText titleEditText, descriptionEditText, deadlineEditText, UpdateTaskTime;
    private Button updateButton ;
    private DatabaseHelper dbHelper;
    private Task task;
    private TextView dateEditText;
    private ImageButton closeButton;
    private RatingBar ratingBar;





    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getContext());
        long taskId = getArguments().getLong("taskId");
        task = dbHelper.getTaskById(taskId);
    }

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);

        titleEditText = view.findViewById(R.id.UpdateTaskTitle);
        descriptionEditText = view.findViewById(R.id.UpdateTaskDescription);
        dateEditText = view.findViewById(R.id.UpdateTaskDate);
        UpdateTaskTime = view.findViewById(R.id.UpdateTaskTime);
        deadlineEditText = view.findViewById(R.id.UpdateTaskDeadline);
        updateButton = view.findViewById(R.id.UpdateTask);
        closeButton = view.findViewById(R.id.editclose);
        ratingBar = view.findViewById(R.id.UpdateratingBar);

        // Pre-populate fields with task information
        titleEditText.setText(task.getTitle());
        dateEditText.setText(task.getTaskDate());
        UpdateTaskTime.setText(task.getTime());
        descriptionEditText.setText(task.getDescription());
        deadlineEditText.setText(task.getDeadline());
        ratingBar.setRating(task.getRating());

        updateButton.setOnClickListener(v -> updateTask());
        closeButton = view.findViewById(R.id.editclose);
        closeButton.setOnClickListener(v -> dismiss());

        deadlineEditText.setOnClickListener(v -> showDatePicker(deadlineEditText));
        UpdateTaskTime.setOnClickListener(v -> showTimePicker());



        return view;
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
                    UpdateTaskTime.setText(formattedTime);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private TaskUpdateListener taskUpdateListener;

    public interface TaskUpdateListener {
        void onTaskUpdated(Task updatedTask);
    }

    public static UpdateFragment newInstance(long taskId) {
        UpdateFragment fragment = new UpdateFragment();
        Bundle args = new Bundle();
        args.putLong("taskId", taskId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TaskUpdateListener) {
            taskUpdateListener = (TaskUpdateListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement TaskUpdateListener");
        }
    }



    private void updateTask() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String deadline = deadlineEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();

        if (!title.isEmpty() && !description.isEmpty() && !deadline.isEmpty()) {
            task.setTitle(title);
            task.setDescription(description);
            task.setTaskDate(date);
            task.setDeadline(deadline);
            dbHelper.updateTask(task);
            Toast.makeText(getContext(), "Task updated successfully", Toast.LENGTH_SHORT).show();
            if (taskUpdateListener != null) {
                taskUpdateListener.onTaskUpdated(task);
            }
            dismiss();
        } else {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Cancel any ongoing operations or listeners
    }

    @Override
    public void dismiss() {
        // Ensure any references to the parent activity are cleared
        taskUpdateListener = null;
        super.dismiss();
    }
}

