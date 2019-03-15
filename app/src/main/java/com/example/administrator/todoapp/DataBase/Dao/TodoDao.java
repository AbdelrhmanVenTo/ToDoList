package com.example.administrator.todoapp.DataBase.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.administrator.todoapp.DataBase.Model.Todo;

import java.util.List;

@Dao
public interface TodoDao {
    @Insert
    void insertTodo(Todo todo);

    @Update
    void updateTodo(Todo todo);

    @Delete
    void deleteTodo(Todo todo);

    @Query("select * from todo;")
    List<Todo> getAllTodo();

    @Query("select * from todo where id =:id")
    Todo getTodoById(int id);

    @Query("select * from todo where title like :subtitle ")
    List<Todo> searchTodoByTitle(String subtitle);


}
