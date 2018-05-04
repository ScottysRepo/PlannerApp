package com.example.sseethoff.plannerapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Spinner;

import com.example.sseethoff.plannerapp.database.PlanBaseHelper;
import com.example.sseethoff.plannerapp.database.PlanDbSchema;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CalendarActivity extends AppCompatActivity {

    private static final String TAG = "CalendarActivity";

    private TextView thedate;
    private Button btngocalendar;
    private Button btnAddActivity;
    private EditText etAct;
    private Spinner spinner;
    private TimePicker timePicker;
    private String text = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thedate = (TextView) findViewById(R.id.date);
        btngocalendar = (Button) findViewById(R.id.btngocalendar);
        btnAddActivity = (Button) findViewById(R.id.btnAddActivity);
        etAct = (EditText) findViewById(R.id.etAct);
        spinner = (Spinner) findViewById(R.id.spinner);
        timePicker = (TimePicker) findViewById(R.id.tp1);
        final Context context = getApplicationContext();
        final int duration = Toast.LENGTH_SHORT;

        //getting hours and minutes that user selects to use for event planning
        final int hour = timePicker.getCurrentHour();
        final int minute = timePicker.getCurrentMinute();

        Intent incoming = getIntent();
        final String date = incoming.getStringExtra("date");
        thedate.setText(date);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Work");
        categories.add("Shopping");
        categories.add("School");
        categories.add("Social");
        categories.add("Travel");
        categories.add("Other");

        // Creating adapter for spinner
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        etAct.setInputType(InputType.TYPE_CLASS_TEXT);

        final ArrayList<String> activities = new ArrayList<String>();
        final ArrayAdapter<String> activitiesListAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, activities);
        ListView listView = (ListView) findViewById(R.id.activitiesList);
        listView.setAdapter(activitiesListAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference fireDbRef = database.getReference(date.toString());
        final PlanBaseHelper mDbHelper = new PlanBaseHelper(getApplicationContext());

        try{
            // Read from the database
            final boolean shouldThrow;
            fireDbRef.addValueEventListener(new ValueEventListener() {
                boolean shouldThrow = false;
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    try{
                        activities.addAll(dataSnapshot.getValue(ArrayList.class));
                        activitiesListAdapter.notifyDataSetChanged();
                    } catch (Exception e){
                        shouldThrow = true;
                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
            if(shouldThrow = true){
                throw new Exception();
            }
        }catch(Exception e) {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            String query = "SELECT * FROM plans INNER JOIN dates ON dates._ID = plans._ID";
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndexOrThrow(PlanDbSchema.DateTable.Cols.DATE)).equals(date)) {
                    activities.add(cursor.getString(cursor.getColumnIndexOrThrow(PlanDbSchema.PlanTable.Cols.ACTIVITY)));
                }
            }
            activitiesListAdapter.notifyDataSetChanged();
            cursor.close();
        }

        btngocalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedItemText = (String) spinner.getItemAtPosition(spinner.getLastVisiblePosition());

                //
                text = Integer.toString(hour) + ":" + Integer.toString(minute);
                text += " > ";
                text += selectedItemText;
                text += " : ";
                text += etAct.getText();

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                //insert info into temp 'local' list
                activities.add(text);
                activitiesListAdapter.notifyDataSetChanged();
                //insert info into db
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(PlanDbSchema.PlanTable.Cols.ACTIVITY,text);
                db.insert(PlanDbSchema.PlanTable.NAME,null,values);
                ContentValues values1 = new ContentValues();
                values1.put(PlanDbSchema.DateTable.Cols.DATE,date);
                db.insert(PlanDbSchema.DateTable.NAME,null,values1);
                fireDbRef.setValue(activities);
            }
        });
    }
}