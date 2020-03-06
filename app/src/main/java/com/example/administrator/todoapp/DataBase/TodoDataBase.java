package com.example.administrator.todoapp.DataBase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.administrator.todoapp.DataBase.Dao.TodoDao;
import com.example.administrator.todoapp.DataBase.Model.Todo;

@Database(entities = {Todo.class},version = 2,exportSchema = false)
public abstract class TodoDataBase extends RoomDatabase {
    private static TodoDataBase myTodoDataBase;
    private static final String DBName="TodoListDataBase";
    public abstract TodoDao todoDao();

    public static TodoDataBase getInstance(Context context){

        if (myTodoDataBase==null){//create object
            myTodoDataBase =
                    Room.databaseBuilder(context,TodoDataBase.class,DBName)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return myTodoDataBase;


    }
}
