package com.example.administrator.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.todoapp.Adapters.TodoRecyclerViewAdapter;
import com.example.administrator.todoapp.Base.BaseActivity;
import com.example.administrator.todoapp.DataBase.Model.Todo;
import com.example.administrator.todoapp.DataBase.TodoDataBase;

import java.util.ArrayList;
import java.util.List;

public class TodoListActivity extends BaseActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TodoRecyclerViewAdapter adapter;
    ConstraintLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        layoutManager= new LinearLayoutManager(activity);
        adapter = new TodoRecyclerViewAdapter(null);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(activity,AddTodoActivity.class);
                startActivity(intent);
            }
        });



        adapter.setOnItemClickListener(new TodoRecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Todo todo) {
                Intent intent = new Intent(activity,UpdateTodoListActivity.class);
                intent.putExtra("EXTRA_ID",todo.getId());
                intent.putExtra("EXTRA_TITLE",todo.getTitle());
                intent.putExtra("EXTRA_CONTENT",todo.getContent());
                intent.putExtra("EXTRA_DATA",todo.getDate());
                intent.putExtra("EXTRA_TIME",todo.getTime());
                startActivity(intent);
            }
        });

        enableSwipeToDeleteAndUndo();


    }


    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final Todo mTodo = adapter.getData(position);

                adapter.deleteItem(position);
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        adapter.restoreItem(mTodo, position);
                        recyclerView.scrollToPosition(position);
                    }
                });snackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        if (event == DISMISS_EVENT_TIMEOUT) {
                            // TODO delete from the database
                            TodoDataBase.getInstance(activity).todoDao().deleteTodo(mTodo);
                        }
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }



    @Override
    protected void onStart() {
        super.onStart();

        List<Todo> allTodos=TodoDataBase.getInstance(activity)
                .todoDao()
                .getAllTodo();
        adapter.changeData(allTodos);

        getIntent().getStringExtra("EXTRA_ID");
        getIntent().getStringExtra("EXTRA_TITLE");
        getIntent().getStringExtra("EXTRA_CONTENT");
        getIntent().getStringExtra("EXTRA_DATA");
        getIntent().getStringExtra("EXTRA_TIME");

        enableSwipeToDeleteAndUndo();
        adapter.notifyDataSetChanged();
/*
        adapter = new TodoRecyclerViewAdapter(allTodos);
        recyclerView.setAdapter(adapter);
*/
    }

}
