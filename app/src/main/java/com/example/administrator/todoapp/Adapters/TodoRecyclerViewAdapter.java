package com.example.administrator.todoapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.todoapp.DataBase.Model.Todo;
import com.example.administrator.todoapp.DataBase.TodoDataBase;
import com.example.administrator.todoapp.R;

import org.w3c.dom.Text;

import java.util.List;

public class TodoRecyclerViewAdapter
        extends RecyclerView.Adapter<TodoRecyclerViewAdapter.ViewHolder> {

    public static List<Todo> items ;
    private onItemClickListener updataTodoListener;


    public TodoRecyclerViewAdapter(List<Todo> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.todo_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Todo item = items.get(position);
        viewHolder.title.setText(item.getTitle());
        viewHolder.date.setText(item.getDate());

    }

    public void changeData(List<Todo> items){
        this.items =items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(items==null)return 0;
        return items.size();
    }


    public  Todo getData(int position) {
        return items.get(position);
    }


    public void deleteItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        //notifyItemRangeChanged(position, items.size());


    }

    public void restoreItem(Todo item, int position) {
        items.add(position, item);
        notifyItemInserted(position);
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,date;
        public ViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.title);
            date = view.findViewById(R.id.date);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (updataTodoListener != null && position != RecyclerView.NO_POSITION) {
                        updataTodoListener.onItemClick(items.get(position));
                    }
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(Todo todo);
    }

    public void setOnItemClickListener(onItemClickListener updataTodoListener) {
        this.updataTodoListener = updataTodoListener;

    }
}
