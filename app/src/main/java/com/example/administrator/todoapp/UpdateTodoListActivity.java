package com.example.administrator.todoapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class UpdateTodoListActivity extends BaseActivity implements View.OnClickListener {


    protected Button update;
    protected EditText titleUpdate;
    protected EditText contentUpdate;
    protected TextView timeUpdate;
    protected TextView dateUpdate;
    String sTitle, sContent, sDate, sTime;
    int idUpdate;

    int hour, minutes ,day, mounth, years;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_update_todo_list);
        getSupportActionBar().setTitle(getIntent().getStringExtra("EXTRA_TITLE"));
        initView();

        idUpdate = getIntent().getIntExtra("EXTRA_ID", 0);
        titleUpdate.setText(getIntent().getStringExtra("EXTRA_TITLE"));
        contentUpdate.setText(getIntent().getStringExtra("EXTRA_CONTENT"));
        dateUpdate.setText(getIntent().getStringExtra("EXTRA_DATA"));
        timeUpdate.setText(getIntent().getStringExtra("EXTRA_TIME"));

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.update) {
            //Todo todo = new Todo()
            sTitle = titleUpdate.getText().toString().trim();
            sContent = contentUpdate.getText().toString().trim();
            sDate = dateUpdate.getText().toString().trim();
            sTime = timeUpdate.getText().toString().trim();

            final Todo updateItem = new Todo(idUpdate, sTitle, sContent, sDate ,sTime);
            TodoDataBase.getInstance(activity)
                    .todoDao()
                    .updateTodo(updateItem);
            addAlarmForTodo();
            showConfirmationMessage(R.string.success
                    , R.string.todo_update_successfully,
                    R.string.ok
                    , new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog,
                                            @NonNull DialogAction which) {
                            dialog.dismiss();
                            Intent intent = new Intent();
                            intent.putExtra("EXTRA_ID", updateItem.getId());
                            intent.putExtra("EXTRA_TITLE", updateItem.getTitle());
                            intent.putExtra("EXTRA_CONTENT", updateItem.getContent());
                            intent.putExtra("EXTRA_DATA", updateItem.getDate());
                            intent.putExtra("EXTRA_TIME",updateItem.getTime());
                            finish();
                        }
                    }).setCancelable(false);
        }
         else if (view.getId() == R.id.timeUpdate) {
           ShowTimePickerDialogStart();

        } else if (view.getId() == R.id.dateUpdate) {
            ShowDatePickerDialogStart();
         }

    }

    private void addAlarmForTodo() {
        Intent alarmIntent = new Intent(activity, TodoAlarmReciever.class);
        alarmIntent.putExtra("title", titleUpdate.getText().toString());
        alarmIntent.putExtra("content", contentUpdate.getText().toString());

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
                        if (dateUpdate.getText().toString().isEmpty()) {
                            dateUpdate.setError("Not Valid");
                        } else {
                            mounth = monthOfYear;
                            day = dayOfMonth;
                            years =  year;
                            dateUpdate.setText(dayOfMonth + "-" + (monthOfYear+1) + "-" + year);

                        }
                    }
                }, years, mounth, day);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

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
                        if (timeUpdate.getText().toString().isEmpty()) {
                            timeUpdate.setError("Not Valid");
                        } else {
                            hour = hourOfDay;
                            minutes = minute;
                            timeUpdate.setText(hourOfDay + ":" + minute);
                        }

                    }
                }, hour, minutes, false);

        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

    private void initView() {

        update = findViewById(R.id.update);
        update.setOnClickListener(UpdateTodoListActivity.this);
        titleUpdate = (EditText) findViewById(R.id.titleUpdate);
        contentUpdate = (EditText) findViewById(R.id.contentUpdate);
        timeUpdate = (TextView) findViewById(R.id.timeUpdate);
        timeUpdate.setOnClickListener(UpdateTodoListActivity.this);
        dateUpdate = (TextView) findViewById(R.id.dateUpdate);
        dateUpdate.setOnClickListener(UpdateTodoListActivity.this);
    }
}
