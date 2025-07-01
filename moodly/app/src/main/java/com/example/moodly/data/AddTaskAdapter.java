package com.example.moodly.data;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodly.R;
import com.example.moodly.UpdateFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddTaskAdapter extends RecyclerView.Adapter<AddTaskAdapter.MyViewHolder> {

    private List<Task> tasks;
    private Context context;
    private DatabaseHelper dbHelper;

    public AddTaskAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
        this.dbHelper = new DatabaseHelper(context);
    }


    public void addTask(Task task) {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        tasks.add(task);
        Log.d("AddTaskAdapter", "Task added, new size: " + tasks.size());
        notifyItemInserted(tasks.size() - 1);
        notifyDataSetChanged();
    }

    //
    // Add this method to update the task list
    public void setTasks(ArrayList<Task> updatedTasks) {
        this.tasks = updatedTasks;
        notifyDataSetChanged(); // Notify adapter of changes
    }
    //


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task, parent, false);
        return new MyViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Task task = tasks.get(position);
        if (task != null) {
            holder.title.setText(task.getTitle());
            holder.description.setText(task.getDescription());


            String deadline = task.getDeadline();
            Log.i("AddTaskAdapter", "Binding task at position " + position + ": " + task.getTitle() + ", Deadline: " + deadline);

            if (deadline != null && deadline.contains("/")) {
                String[] dateParts = deadline.split("/");
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                    Date date = inputFormat.parse(deadline);

                    // Extract and set the day, date, and month name
                    SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH); // Full day name
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.ENGLISH); // Day of the month
                    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH); // Full month name

                    holder.day.setText(dayFormat.format(date)); // e.g., "Monday"
                    holder.date.setText(dateFormat.format(date)); // e.g., "30"
                    holder.month.setText(monthFormat.format(date)); // e.g. // Set the day of the week in the 'day' TextView
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Show PopupMenu when the options icon is clicked
            holder.options.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(context, holder.options);
                popupMenu.inflate(R.menu.taskmenu); // Ensure this menu exists in res/menu/task_options_menu.xml

                // Handle menu item clicks
                popupMenu.setOnMenuItemClickListener(item -> {
                    int itemId = item.getItemId();

                    if (itemId == R.id.menuDelete) {
                        deleteTask(task, position);
                        return true;

                    } else if (itemId == R.id.menuUpdate) {
                        showUpdateDialog(task.getId());
                        return true;

                    } else if (itemId == R.id.menuComplete) {
                        completeTask(task, position);
                        return true;
                    }

                    return false;
                });

                popupMenu.show();
            });

            // Update the status text view
            if (task.getStatus() != null) {
                holder.status.setText(task.getStatus().toUpperCase());
            }
        }
    }



    private void completeTask(Task task, int position) {
        task.setStatus("Completed");
        dbHelper.updateTask(task);
        tasks.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, tasks.size());
    }



    private void deleteTask(Task task, int position) {
        dbHelper.deleteTask(task.getId());
        tasks.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, tasks.size());
    }

    public void updateTask(Task updatedTask) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == updatedTask.getId()) {
                tasks.set(i, updatedTask); // Update the task in the list
                notifyItemChanged(i); // Notify that the item has changed
                break;
            }
        }
    }




    private void showUpdateDialog(long taskId) {
        UpdateFragment dialogFragment = UpdateFragment.newInstance(taskId);
        dialogFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "UpdateTaskDialog");
    }


    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title, description, day, date, month, status;
        ImageView options;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            day = itemView.findViewById(R.id.day);
            date = itemView.findViewById(R.id.date);
            month = itemView.findViewById(R.id.month);
            options = itemView.findViewById(R.id.options);
            status = itemView.findViewById(R.id.status);

        }
    }



}