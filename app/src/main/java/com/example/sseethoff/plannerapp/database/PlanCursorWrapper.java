package com.example.sseethoff.plannerapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.sseethoff.plannerapp.Plan;
import com.example.sseethoff.plannerapp.database.PlanDbSchema.PlanTable;
import com.example.sseethoff.plannerapp.database.PlanDbSchema.DateTable;

import java.util.Date;
import java.util.UUID;

public class PlanCursorWrapper extends CursorWrapper {

    public PlanCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Plan getPlan() {
        String uuidString = getString(getColumnIndex(PlanTable.Cols.UUID));
        String activity = getString(getColumnIndex(PlanTable.Cols.ACTIVITY));

        //String uuidString = getString(getColumnIndex(DateTable.Cols.UUID));
        long date = getLong(getColumnIndex(DateTable.Cols.DATE));

        Plan plan = new Plan(UUID.fromString(uuidString));
        plan.setActivity(activity);
        plan.setDate(new Date(date));

        return plan;
    }
}