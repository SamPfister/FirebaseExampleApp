package com.example.firebaseexampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditEventActivity extends AppCompatActivity {
    private FirebaseDatabaseHelper dbHelper;
    private EditText eventNameET;
    private EditText eventDateET;
    private String keyToUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        dbHelper = new FirebaseDatabaseHelper();
        Intent intent = getIntent();
        Event event = intent.getParcelableExtra("event");

        String eventNameToUpdate = event.getEventName();
        String eventDateToUpdate = event.getEventDate();
        keyToUpdate = event.getKey();

        eventNameET = (EditText)findViewById(R.id.eventName);
        eventDateET = (EditText)findViewById(R.id.eventDate);

        eventNameET.setText(eventNameToUpdate);
        eventDateET.setText(eventDateToUpdate);
    }



    public void updateEventData(View v) {
        String newName = eventNameET.getText().toString();
        String newDate = eventDateET.getText().toString();

        // error checking to ensure date is of the form 01/17/1979 etc.
        if (newDate.length() != 10)
            Toast.makeText(EditEventActivity.this, "Please enter date as MM/DD/YYYY", Toast.LENGTH_SHORT).show();
        else if (newName.length() == 0)
            Toast.makeText(EditEventActivity.this, "Please enter a name for the event", Toast.LENGTH_SHORT).show();
        else
        {
            // prevents the app from crashing if user doesn't use correct date format
            try{
                int month = Integer.parseInt(newDate.substring(0, 2));
                int day =  Integer.parseInt(newDate.substring(3, 5));
                int year =  Integer.parseInt(newDate.substring(6));

                if (month > 0 && month < 13 && day > 0 && day < 32 )
                    dbHelper.updateEvent(keyToUpdate, newName, newDate, month, day, year);
                else
                    Toast.makeText(EditEventActivity.this, "Please enter a valid month/day", Toast.LENGTH_SHORT).show();

            }
            catch (Exception e) {
                Toast.makeText(EditEventActivity.this, "Please enter date as MM/DD/YYYY", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void deleteEventData(View v) {
        dbHelper.deleteEvent(keyToUpdate);
        onHome(v);              // reloads opening screen
    }

    public void onHome(View v){
        Intent intent = new Intent(EditEventActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onRetrieve(View v){

        Intent intent = new Intent(EditEventActivity.this, DisplayEventsActivity.class);
        intent.putExtra("events", dbHelper.getEventsArrayList());
        startActivity(intent);
    }
}
