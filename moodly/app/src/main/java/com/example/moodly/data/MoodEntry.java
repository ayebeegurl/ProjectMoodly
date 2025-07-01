package com.example.moodly.data;

public class MoodEntry {
    private int id;
    private int userId;
    private String mood;
    private int energyLevel;
    private String date;
    private String time;

    public MoodEntry() {
    }

    public MoodEntry(int userId, String mood, int energyLevel, String date, String time) {
        this.userId = userId;
        this.mood = mood;
        this.energyLevel = energyLevel;
        this.date = date;
        this.time = time;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

