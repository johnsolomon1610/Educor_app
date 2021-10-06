package com.example.educor_app.Assignment.Assignment_Submission;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.educor_app.R;

import java.util.ArrayList;

public class RecyclerAdapter_submission extends RecyclerView.Adapter<RecyclerAdapter_submission.MyViewHolder> {

    Context context;
    ArrayList<assignment_submission> data1;

    public RecyclerAdapter_submission(Context c , ArrayList<assignment_submission> p)
    {
        context = c;
        data1= p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_submission, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(data1.get(position).getStudent_Username());
    }

    @Override
    public int getItemCount() {
        return data1.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView name;
        public MyViewHolder(View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.submission);

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {

            int position=getAdapterPosition();
            Intent intent=new Intent(context, AssignmentGrade.class);
            intent.putExtra("student_name",data1.get(position).getStudent_Username());
            intent.putExtra("Assignment_name",data1.get(position).getTitle());
            intent.putExtra("url",data1.get(position).getUrl());
            intent.putExtra("student_userid",data1.get(position).getStudent_Userid());
            intent.putExtra("class_name",data1.get(position).getClass_name());
            intent.putExtra("pdf_NAME",data1.get(position).getPdf_NAME());
            context.startActivity(intent);

        }
    }

}
