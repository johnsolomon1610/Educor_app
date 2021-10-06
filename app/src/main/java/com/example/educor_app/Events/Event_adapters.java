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

public class Event_adapters extends RecyclerView.Adapter< Event_adapters.MyViewHolders> {
    Context context;
    ArrayList<Event_rvs>Event_rvs ;
    public Event_adapters(Context c, ArrayList<Event_rvs> rcm){
        context=c;
        Event_rvs=rcm;
    }
    @NonNull
    @Override
    public MyViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolders(LayoutInflater.from(context).inflate(R.layout.cardview_event, parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolders holder, int position) {
        holder.Time.setText(Event_rvs.get(position).getTime());
        holder.Post.setText(Event_rvs.get(position).getPost());
        holder.User.setText(Event_rvs.get(position).getUser());
    }
    @Override
    public int getItemCount() {
        return Event_rvs.size();
    }
    class MyViewHolders extends  RecyclerView.ViewHolder{
        TextView Time, Post,User;
        public MyViewHolders(@NonNull View itemView) {
            super(itemView);
            Time=(TextView) itemView.findViewById(R.id.Time);
            Post=(TextView) itemView.findViewById(R.id.Post);
            User=(TextView) itemView.findViewById(R.id.User);
        }
    }
}
