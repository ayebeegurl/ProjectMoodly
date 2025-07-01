package com.example.moodly.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MoodTaskTracker.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    private static final String TABLE_TASKS = "tasks";
    private static final String COLUMN_TASK_ID = "taskId";
    private static final String COLUMN_TASK_DATE = "date";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DEADLINE = "deadline";
    private static final String COLUMN_RATING= "rating";
    private static final String COLUMN_STATUS = "status";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);

        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ID + " INTEGER,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_TASK_DATE + " TEXT,"
                + COLUMN_DEADLINE + " TEXT,"
                + COLUMN_TIME + " TEXT,"
                + COLUMN_RATING + " REAL,"
                + COLUMN_STATUS + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        long id = db.insert(TABLE_USER, null, values);
        db.close();
        return id;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_EMAIL + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }


    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USER, new String[]{COLUMN_USERNAME, COLUMN_EMAIL},
                null, null, null, null, null);
    }



    // Task operations
    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, task.getUserId());
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        values.put(COLUMN_TASK_DATE, task.getTaskDate());
        values.put(COLUMN_DEADLINE, task.getDeadline());
        values.put(COLUMN_TIME, task.getTime());
        values.put(COLUMN_RATING, task.getRating());
        values.put(COLUMN_STATUS, task.getStatus());
        long id = db.insert(TABLE_TASKS, null, values);
        db.close();
        return id;
    }

    @SuppressLint("Range")
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, null, null, null, null, null, COLUMN_DEADLINE + " ASC");
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(
                        cursor.getLong(cursor.getColumnIndex(COLUMN_TASK_ID)),
                        cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TASK_DATE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DEADLINE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TIME)),
                        cursor.getFloat(cursor.getColumnIndex(COLUMN_RATING)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_STATUS))
                );
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tasks;
    }

    @SuppressLint("Range")
    public List<Task> getTasksForUser(long userId) {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, null, COLUMN_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, COLUMN_DEADLINE + " ASC");
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(
                        cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TASK_DATE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DEADLINE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TIME)),
                        cursor.getFloat(cursor.getColumnIndex(COLUMN_RATING)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_STATUS))
                );
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tasks;
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, task.getUserId());
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        values.put(COLUMN_TASK_DATE, task.getTaskDate());
        values.put(COLUMN_DEADLINE, task.getDeadline());
        values.put(COLUMN_TIME, task.getTime());
        values.put(COLUMN_RATING, task.getRating());
        values.put(COLUMN_STATUS, task.getStatus());
        int rowsAffected = db.update(TABLE_TASKS, values, COLUMN_TASK_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        return rowsAffected;
    }

    public void deleteTask(long taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
        db.close();
    }

    @SuppressLint("Range")
    public Task getTaskById(long taskId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, null, COLUMN_ID + "=?",
                new String[]{String.valueOf(taskId)}, null, null, null);

        Task task = null;
        if (cursor != null && cursor.moveToFirst()) {
            task = new Task(
                    cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TASK_DATE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DEADLINE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TIME)),
                    cursor.getFloat(cursor.getColumnIndex(COLUMN_RATING)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_STATUS))
            );
            cursor.close();
        }
        db.close();
        return task;
    }
}



