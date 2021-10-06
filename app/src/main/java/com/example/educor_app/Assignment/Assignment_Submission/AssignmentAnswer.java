package com.example.educor_app.Assignment.Assignment_Submission;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AssignmentAnswer extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView t1,t2;
    ArrayList<assignment_submission> list;
    FirebaseUser user;
    DatabaseReference ref1,ref2;
    RecyclerAdapter_submission adapter;
    public  TextView Attachment;
    private ActionBar actionBar;
    Window window;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_answer);

        actionBar=getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#26A9E6")));

        this.setTitle("Submissions");

        if (Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        t1=findViewById(R.id.title);
        t2=findViewById(R.id.due);
        Attachment=findViewById(R.id.attachments);


        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        String due=intent.getStringExtra("due");
        String class_name=intent.getStringExtra("class_name");

        t1.setText(title);
        t2.setText("Due :"+due);

        list=new ArrayList<>();
        user= FirebaseAuth.getInstance().getCurrentUser();

        ref1= FirebaseDatabase.getInstance().getReference("Assignment").child(class_name).child("submission").child(title);
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                for(DataSnapshot df:snapshot.getChildren()){
                    assignment_submission p = df.getValue(assignment_submission.class);
                    list.add(p);
                }
                Attachment.setText(list.size() == 0 ? "No Submissions yet!" : "Submissions");
                adapter = new RecyclerAdapter_submission(AssignmentAnswer.this, list);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}