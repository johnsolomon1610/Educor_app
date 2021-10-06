package com.example.educor_app.Assignment.Comments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.educor_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AssignmentComments extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference reference,ref;
    String class_name,assignment_name;
    ArrayList<comments> list;
    RecyclerAdapter_comments adapter;
    public TextView comments_header;
    private ActionBar actionBar;
    Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_comments);



        actionBar=getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#26A9E6")));

        this.setTitle("Comments");
        if (Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }

        comments_header=findViewById(R.id.comments_header);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent=getIntent();
        class_name=intent.getStringExtra("class_name");
        assignment_name=intent.getStringExtra("title");

        list = new ArrayList<>();

        reference= FirebaseDatabase.getInstance().getReference("Assignment").child(class_name).child("comments")
                .child(assignment_name);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                for(DataSnapshot df:snapshot.getChildren()) {
                    comments p = df.getValue(comments.class);
                    list.add(p);
                }
                comments_header.setText(list.size() == 0 ? "No Comments yet!" : "Comments");
                adapter = new RecyclerAdapter_comments(AssignmentComments.this, list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

      /*  ref= FirebaseDatabase.getInstance().getReference("Assignment").child(class_name).child("Report")
                .child(assignment_name);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                for(DataSnapshot df:snapshot.getChildren()) {
                    comments p = df.getValue(comments.class);
                    list.add(p);
                }
                adapter = new RecyclerAdapter_comments(AssignmentComments.this, list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

    }
}