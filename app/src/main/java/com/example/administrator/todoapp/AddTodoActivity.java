package com.example.administrator.todoapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
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

    protected EditText title;
    protected EditText content;
    protected TextView date;
    protected Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        initView();


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add) {
            String sTitle = title.getText().toString();
            String sContent = content.getText().toString();
            String sDate = date.getText().toString();

            //todo : Validation

            Todo newItem = new Todo(sTitle,sContent,sDate);
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
        }else if(view.getId()==R.id.date){
            Calendar calendar=Calendar.getInstance();
            int m=calendar.get(Calendar.MINUTE);
            int h=calendar.get(Calendar.HOUR_OF_DAY);
            TimePickerDialog dialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view,
                                      int hours, int minutes) {
                    hour=hours;
                    minute=minutes;
                    date.setText(hours+":"+minute);
                }
            },h,m,false);
            dialog.show();
        }
    }

    private void addAlarmForTodo() {
        Intent alarmIntent= new Intent(activity,TodoAlarmReciever.class);
        alarmIntent.putExtra("title",title.getText().toString());
        alarmIntent.putExtra("content",content.getText().toString());

        PendingIntent pendingIntent= PendingIntent.getBroadcast(activity,
                (int) System.currentTimeMillis(),
                alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager =(AlarmManager)
                getSystemService(ALARM_SERVICE);

        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);

        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);


    }

    int hour,minute;

    private void initView() {
        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
        date =  findViewById(R.id.date);
        date.setOnClickListener(this);
        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(AddTodoActivity.this);
    }
}
