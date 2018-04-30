package com.example.sseethoff.plannerapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sseethoff.plannerapp.database.PlanDbSchema.PlanTable;
import com.example.sseethoff.plannerapp.database.PlanDbSchema.DateTable;

public class PlanBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "planBase.db";

    public PlanBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + PlanTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                PlanTable.Cols.UUID + ", " +
                PlanTable.Cols.ACTIVITY + ", " +
                PlanTable.Cols.LOCATION +
                ")"
        );
        db.execSQL("create table " + DateTable.NAME + "(" +
                " _id ineger primary key autoincrement, " +
                DateTable.Cols.DATE + ", " +
                DateTable.Cols.TIME + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}