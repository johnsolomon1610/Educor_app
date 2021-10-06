package com.example.educor_app.Assignment.Comments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.educor_app.R;

import java.util.ArrayList;

public class RecyclerAdapter_comments extends RecyclerView.Adapter<RecyclerAdapter_comments.MyViewHolder> {

    Context context;
    ArrayList<comments> data;

    public RecyclerAdapter_comments(Context c , ArrayList<comments> p)
    {
        context = c;
        data= p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_comments, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.comment.setText(data.get(position).getComments());
        holder.student_name.setText(data.get(position).getStudent_name());
        holder.date.setText(data.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView comment,student_name,date;
        CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);

            comment = itemView.findViewById(R.id.comments);
            student_name = itemView.findViewById(R.id.name);
            date=itemView.findViewById(R.id.Date);
            cardView = itemView.findViewById(R.id.cardview1);

        }
    }
}
