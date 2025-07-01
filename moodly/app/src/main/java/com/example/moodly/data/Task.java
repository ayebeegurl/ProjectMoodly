package com.example.moodly.data;

public class Task {
    private long id;
    private long userId;
    private String title;
    private String description;
    private String taskDate;
    private String deadline;
    private String time;
    private float rating;
    private String status;

    // Constructor for creating a new task (without id)
    public Task(long userId, String title, String description, String taskDate, String deadline, String time, float rating, String status) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.taskDate = taskDate;
        this.deadline = deadline;
        this.time = time;
        this.rating = rating;
        this.status = status;
    }

    // Constructor for retrieving a task from the database (with id)
    public Task(long id, long userId, String title, String description, String taskDate, String deadline, String time, float rating, String status) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.taskDate = taskDate;
        this.deadline = deadline;
        this.time = time;
        this.rating = rating;
        this.status = status;
    }


    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

