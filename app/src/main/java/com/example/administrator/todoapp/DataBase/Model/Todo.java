package com.example.administrator.todoapp.DataBase.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Todo {

    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo
    String title;
    @ColumnInfo
    String content;
    @ColumnInfo
    String date;
    @ColumnInfo
    String time;


    public Todo(){

    }

    @Ignore
    public Todo(String title, String content, String date ,String time) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
    }


    public Todo(int id, String title, String content, String date, String time) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
