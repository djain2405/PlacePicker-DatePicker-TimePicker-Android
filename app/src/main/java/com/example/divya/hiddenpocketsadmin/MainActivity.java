package com.example.divya.hiddenpocketsadmin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class MainActivity extends FragmentActivity  {

    private int PLACE_PICKER_REQUEST = 1;
    Button pickplace;
    Button pickDate;
    Button pickTime;
    EditText title;
    EditText description;
    Button submit;
    TextView placeName;
    TextView addressText;
    Calendar myCalendar;
    private int hr;
    private int min;
    private int year;
    private int month;
    private int day;
    Place place;
    DatabaseReference database;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickplace = (Button) findViewById(R.id.PickPlace);
        pickDate = (Button) findViewById(R.id.dateButton);
        pickTime = (Button) findViewById(R.id.timeButton);
        submit = (Button) findViewById(R.id.submit);
        myCalendar = Calendar.getInstance();
        pickplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });
        hr = myCalendar.get(Calendar.HOUR_OF_DAY);
        min = myCalendar.get(Calendar.MINUTE);
        year = myCalendar.get(Calendar.YEAR);
        month = myCalendar.get(Calendar.MONTH);
        day = myCalendar.get(Calendar.DAY_OF_MONTH);
        placeName = (TextView) findViewById(R.id.placename);
        addressText = (TextView) findViewById(R.id.address);
        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        database = FirebaseDatabase.getInstance().getReference();

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });

        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timepicker();
            }
        });

        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                event = new Event();
                event.setTitle(title.getText().toString());
                event.setDesc(description.getText().toString());
                event.setLat(String.valueOf(place.getLatLng().latitude));
                event.setLongitude(String.valueOf(place.getLatLng().longitude));
                event.setYear(year);
                event.setMonth(month+1);
                event.setDay(day);
                event.setHour(hr);
                event.setMin(min);

                Toast.makeText(getApplicationContext(), "Event details are set", Toast.LENGTH_LONG);
                database.child("Event").push().setValue(event);


            }
        });


    }

    private void datePicker() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int yr,int monthOfYear, int dayOfMonth) {

                        year = yr;
                        month = monthOfYear;
                        day = dayOfMonth;
                        Toast.makeText(getApplicationContext(), "Date is " + year + " " + month + 1 + " " + day, Toast.LENGTH_LONG).show();

                    }
                }, year, month, day);
        datePickerDialog.show();

    }

    private void timepicker() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {

                        hr = hourOfDay;
                        min = minute;

                        Toast.makeText(getApplicationContext(), "Time is " + hr + ":" + min, Toast.LENGTH_LONG).show();
                    }
                }, hr, min, false);
        timePickerDialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(this, data);
                StringBuilder stBuilder = new StringBuilder();
                //String placename = String.format("%s", new String(place.getName().toString()));
                String placename = place.getName().toString();
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                String address = place.getAddress().toString();
                stBuilder.append("Name: ");
                stBuilder.append(placename);
                stBuilder.append("\n");
                stBuilder.append("lat: ");
                stBuilder.append(latitude);
                stBuilder.append("\n");
                stBuilder.append("Logitude: ");
                stBuilder.append(longitude);
                stBuilder.append("\n");
                stBuilder.append("Address: ");
                stBuilder.append(address);
                Toast.makeText(this, "Place details are " + stBuilder.toString(), Toast.LENGTH_SHORT).show();
                placeName.setText(placename);
                addressText.setText(address);
            }
        }
    }







}
