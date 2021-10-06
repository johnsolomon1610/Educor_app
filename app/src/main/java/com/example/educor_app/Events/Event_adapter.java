package com.example.educor_app.Events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.example.educor_app.R;

public class Event_adapter extends RecyclerView.Adapter< Event_adapter.MyViewHolder> {

    Context context;
    ArrayList<Event_rv> Event_rv;
    public  Event_adapter(Context c, ArrayList<Event_rv> rm){
        context=c;
        Event_rv=rm;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_event, parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.Time.setText(Event_rv.get(position).getTime());
        holder.Post.setText(Event_rv.get(position).getPost());
        holder.User.setText(Event_rv.get(position).getUser());
    }
    @Override
    public int getItemCount() {
        return Event_rv.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Time, Post,User;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Time=(TextView) itemView.findViewById(R.id.Time);
            Post=(TextView) itemView.findViewById(R.id.Post);
            User=(TextView) itemView.findViewById(R.id.Post);
        }
    }
}