package com.example.administrator.todoapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.administrator.todoapp.Base.BaseActivity;
import com.example.administrator.todoapp.DataBase.Model.Todo;
import com.example.administrator.todoapp.DataBase.TodoDataBase;

import java.util.Calendar;

public class AddTodoActivity extends BaseActivity implements View.OnClickListener {

    protected Button add;
    protected EditText titleAdd;
    protected EditText contentAdd;
    protected TextView timeAdd;
    protected TextView dateAdd;
    int hour, minutes ,day, mounth, years;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        initView();


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add) {
            String sTitle = titleAdd.getText().toString();
            String sContent = contentAdd.getText().toString();
            String sDate = dateAdd.getText().toString();
            String sTime = timeAdd.getText().toString();

            if (TextUtils.isEmpty(sTitle)) {
                titleAdd.setError("Not Valid");
            }else if (TextUtils.isEmpty(sContent)){
                contentAdd.setError("Not Valid");
            }else if (TextUtils.isEmpty(timeAdd.getText().toString())){
                timeAdd.setError("Not Valid");
            }else if (TextUtils.isEmpty(dateAdd.getText().toString())){
                dateAdd.setError("Not Valid");
            }else {
                //todo : Validation

                Todo newItem = new Todo(sTitle , sContent , sDate , sTime);
                TodoDataBase.getInstance(activity)
                        .todoDao()
                        .insertTodo(newItem);
                Log.e("TAG", "onClick: "+newItem.getDate()+"-"+newItem.getTime());
                addAlarmForTodo();
                showConfirmationMessage(R.string.success
                        , R.string.todo_added_successfully,
                        R.string.ok
                        , new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog,
                                                @NonNull DialogAction which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).setCancelable(false);
            }

        } else if (view.getId() == R.id.timeAdd) {
          ShowTimePickerDialogStart();

        } else if (view.getId() == R.id.dateAdd) {
            ShowDatePickerDialogStart();
        }
    }

    private void addAlarmForTodo() {
        Intent alarmIntent = new Intent(activity, TodoAlarmReciever.class);
        alarmIntent.putExtra("title", titleAdd.getText().toString());
        alarmIntent.putExtra("content", contentAdd.getText().toString());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity,
                (int) System.currentTimeMillis(),
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)
                getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        Log.e("TAG", "addAlarmForTodo: hour"+ hour);
        calendar.set(Calendar.MINUTE, minutes);
        Log.e("TAG", "addAlarmForTodo: minute"+ minutes);
        calendar.set(Calendar.MONTH, mounth);
        Log.e("TAG", "addAlarmForTodo: mounth"+ mounth);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        Log.e("TAG", "addAlarmForTodo: day"+ day);
        calendar.set(Calendar.YEAR, years);
        Log.e("TAG", "addAlarmForTodo: year"+ years);

        Log.e("TAG", "addAlarmForTodo:"+ calendar.getTimeInMillis());




        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);


    }

    private void ShowTimePickerDialogStart() {
        // Get Current Time
        Calendar calendar  = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(activity, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if (timeAdd.getText().toString().isEmpty()) {
                            timeAdd.setError("Not Valid");
                        } else {
                            hour = hourOfDay;
                            minutes = minute;
                            timeAdd.setText(hourOfDay + ":" + minute);
                        }

                    }
                }, hour, minutes, false);

        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }


    private void ShowDatePickerDialogStart() {
        // Get Current Date
        Calendar calendar  = Calendar.getInstance();
        years = calendar.get(Calendar.YEAR);
        mounth = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if (dateAdd.getText().toString().isEmpty()) {
                            dateAdd.setError("Not Valid");
                        } else {
                            mounth = monthOfYear;
                            day = dayOfMonth;
                            years =  year;
                            dateAdd.setText(dayOfMonth + "-" + (monthOfYear+1) + "-" + year);

                        }
                    }
                }, years, mounth, day);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }



    private void initView() {
        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(AddTodoActivity.this);
        titleAdd = (EditText) findViewById(R.id.titleAdd);
        contentAdd = (EditText) findViewById(R.id.contentAdd);
        timeAdd = (TextView) findViewById(R.id.timeAdd);
        timeAdd.setOnClickListener(AddTodoActivity.this);
        dateAdd = (TextView) findViewById(R.id.dateAdd);
        dateAdd.setOnClickListener(AddTodoActivity.this);
    }
}
