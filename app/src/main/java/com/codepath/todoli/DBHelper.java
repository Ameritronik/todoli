package com.codepath.todoli;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

/**
 * Created by hkanekal
 */
public class DBHelper extends SQLiteOpenHelper {

    /* Set up table of task and attributes */
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String TASKLIST_TABLE_NAME = "taskTable";
    public static final String TASKLIST_COLUMN_ID = "id";
    public static final String TASKLIST_COLUMN_TNAME = "taskName";
    public static final String TASKLIST_COLUMN_TNOTES = "taskNotes";
    public static final String TASKLIST_COLUMN_TPRIORITY = "taskPriority";
    public static final String TASKLIST_COLUMN_TSTATUS = "taskStatus";
    public static final String TASKLIST_COLUMN_TDUE = "taskDueDate";
    //String TAG = "ToDoLi: "; // need TAG for debugging
    // Constructor for context
    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    // Create  the table of tasks
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TASKLIST_TABLE_NAME + "("+
                        TASKLIST_COLUMN_ID+" integer primary key,"+
                        TASKLIST_COLUMN_TNAME+" text,"+
                        TASKLIST_COLUMN_TNOTES+" text,"+
                        TASKLIST_COLUMN_TPRIORITY+" text,"+
                        TASKLIST_COLUMN_TSTATUS+" text,"+
                        TASKLIST_COLUMN_TDUE+ " text"+")";
        db.execSQL(CREATE_TABLE);
    }

    // Create onUpgrade required for SQLiteOpenHelper
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS taskTable");
        onCreate(db);
    }
    /*
     * Begin methods for accessing data base from other activities
     * Names of methods are self explanatory below
     */
    public boolean insertTask(String taskName, String taskNotes, String taskPriority, String taskStatus, String taskDueDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASKLIST_COLUMN_TNAME, taskName);
        contentValues.put(TASKLIST_COLUMN_TNOTES, taskNotes);
        contentValues.put(TASKLIST_COLUMN_TPRIORITY, taskPriority);
        contentValues.put(TASKLIST_COLUMN_TSTATUS, taskStatus);
        contentValues.put(TASKLIST_COLUMN_TDUE, taskDueDate);
        db.insert(TASKLIST_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getTask(String task) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TASKLIST_TABLE_NAME,
                new String[] {TASKLIST_COLUMN_ID, TASKLIST_COLUMN_TNAME,
                        TASKLIST_COLUMN_TNOTES, TASKLIST_COLUMN_TPRIORITY,
                        TASKLIST_COLUMN_TSTATUS, TASKLIST_COLUMN_TDUE},
                TASKLIST_COLUMN_TNAME + " = '" + task + "'", null,null,null,null);
    }


    public int getTotalRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int totRows = (int) DatabaseUtils.queryNumEntries(db, TASKLIST_TABLE_NAME);
        return totRows;
    }

    public boolean updateTasks(String task, String taskName, String taskNotes, String taskPriority, String taskStatus, String taskDueDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASKLIST_COLUMN_TNAME, taskName);
        contentValues.put(TASKLIST_COLUMN_TNOTES, taskNotes);
        contentValues.put(TASKLIST_COLUMN_TPRIORITY, taskPriority);
        contentValues.put(TASKLIST_COLUMN_TSTATUS, taskStatus);
        contentValues.put(TASKLIST_COLUMN_TDUE, taskDueDate);
        db.update(TASKLIST_TABLE_NAME, contentValues, TASKLIST_COLUMN_TNAME + " = ?", new String[]{task});
        return true;
    }

    /*
     * Set up Array accesses from the db
     */
    public Integer deleteTask(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        int dtr = 0;
        dtr = db.delete(TASKLIST_TABLE_NAME, TASKLIST_COLUMN_TNAME + " = '" + task + "'", null);
        db.execSQL("VACUUM"); // keep db compact
        return dtr;
    }

    public ArrayList<String> getAllTasks() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from taskTable", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(TASKLIST_COLUMN_TNAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAlltPriorities() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from taskTable", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(TASKLIST_COLUMN_TPRIORITY)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAlltDue() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from taskTable", null );

        res.moveToFirst();
        while (res.isAfterLast() == false) {
                array_list.add(res.getString(res.getColumnIndex(TASKLIST_COLUMN_TDUE)));
                res.moveToNext();
        }
        return array_list;

    }

    public ArrayList<String> getAlltStatus() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from taskTable", null );

        res.moveToFirst();
        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(TASKLIST_COLUMN_TSTATUS)));
            res.moveToNext();
        }
        return array_list;

    }
}