package com.example.firebaseexampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public final String TAG = "MainActivity";
    private String dateSelected = "No date chosen";
    private int dateMonth;
    private int dateDay;
    private int dateYear;
    FirebaseDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new FirebaseDatabaseHelper();

        //  Video to learn basic access to CalendarView Data
        //  https://www.youtube.com/watch?v=WNBE_3ZizaA

        CalendarView calendarView = findViewById(R.id.eventCalendarDate);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
                                                 @Override
                                                 public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                                                     dateSelected =  (month + 1) + "/" + day + "/" + year;
                                                     dateYear = year;
                                                     dateMonth = month + 1;
                                                     dateDay = day;
                                                     Log.i(TAG, "" + dateSelected);
                                                     closeKeyboard();
                                                 }
                                             }
        );
    }

    public void addEventButtonPressed(View v) {
        EditText eventNameET = (EditText) findViewById(R.id.eventName);
        String eventName = eventNameET.getText().toString();

        // verify there is a name and date
        if (eventName.length() == 0 ) {
            Toast.makeText(MainActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
        }
        else if (dateSelected.equals("No date chosen")) {
            Toast.makeText(MainActivity.this, "Please select Date", Toast.LENGTH_SHORT).show();
        }
        else {
           Log.i(TAG, "Trying to add: " + eventName + ", " + dateSelected);
           Event newEvent = new Event (eventName, dateSelected, dateYear, dateMonth, dateDay);
           eventNameET.setText("");
           dbHelper.addEvent(newEvent);
        }
    }

    /**
     * This method will be called to minimize the on screen keyboard in the Activity
     * When we get the current view, it is the view that has focus, which is the keyboard
     * Credit - Found by Ram Dixit, 2019
     *
     * Source:  https://www.youtube.com/watch?v=CW5Xekqfx3I
     */
    private void closeKeyboard() {
        View view = this.getCurrentFocus();     // view will refer to the keyboard
        if (view != null ){                     // if there is a view that has focus
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    // code to add to MainActivity.java


// add this method to the end
// call this method in the onClick for the show data method in the activity_main.xml show events button


    public void onRetrieve(View v){
                Intent intent = new Intent(MainActivity.this, DisplayEventsActivity.class);
                intent.putExtra("events", dbHelper.getEventsArrayList());
                startActivity(intent);
            }


            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("callError", "There has been an Error with database retrieval");
            }
}
