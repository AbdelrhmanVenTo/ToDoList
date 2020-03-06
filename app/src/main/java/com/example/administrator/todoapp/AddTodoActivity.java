package com.example.administrator.todoapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

            //todo : Validation

            Todo newItem = new Todo(sTitle , sContent , sDate , sTime);
            TodoDataBase.getInstance(activity)
                    .todoDao()
                    .insertTodo(newItem);
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

        } else if (view.getId() == R.id.timeAdd) {
            Calendar calendar = Calendar.getInstance();
            int m = calendar.get(Calendar.MINUTE);
            int h = calendar.get(Calendar.HOUR_OF_DAY);
            TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view,
                                      int hours, int minutes) {
                    hour = hours;
                    minute = minutes;
                    timeAdd.setText(hours + ":" + minute);
                }
            }, h, m, false);
            dialog.show();

        } else if (view.getId() == R.id.dateAdd) {
            Calendar calendar = Calendar.getInstance();
            int monthsDatePiker = calendar.get(Calendar.MONTH);
            int daysDatePiker = calendar.get(Calendar.DAY_OF_MONTH);
            int yearsDatePiker = calendar.get(Calendar.YEAR);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            dateAdd.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, yearsDatePiker, monthsDatePiker, daysDatePiker);
            datePickerDialog.show();

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
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.MONTH, mounth);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.YEAR, year);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


    }

    int hour, minute ,day, mounth, year;

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
