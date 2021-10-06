package com.example.educor_app.Assignment.Comments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.educor_app.Assignment.Comments.comments;
import com.example.educor_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportAbuse extends AppCompatActivity {

    EditText Report;
    Button Done;
    String name,student_name;
    DatabaseReference myRef,ref;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_abuse);

        user= FirebaseAuth.getInstance().getCurrentUser();

        Report=findViewById(R.id.report);
        Done=findViewById(R.id.done);

        Intent intent=getIntent();
        final String class_name=intent.getStringExtra("class_name");
        final String assignment_name=intent.getStringExtra("Assignment_name");

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String report=Report.getText().toString();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
                Date now = new Date();
                String date = formatter.format(now);

                ref= FirebaseDatabase.getInstance().getReference("signup").child(user.getUid());
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        student_name=snapshot.child("Userid").getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                myRef=FirebaseDatabase.getInstance().getReference("Assignment").child(class_name).child("Report").child(assignment_name)
                        .child(user.getUid());
                comments comment=new comments(report,date,student_name);

                myRef.setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(ReportAbuse.this,"Reported Successfully",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}