package com.example.educor_app.Classroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.educor_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Classroom_ReportActivity extends AppCompatActivity {

    EditText Report;
    Button Done;
    String name,report;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom__report);

        Report=findViewById(R.id.report);
        Done=findViewById(R.id.done);

        Intent intent=getIntent();
        name=intent.getStringExtra("name");

        final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                report=Report.getText().toString();

                assert user != null;
                myRef= FirebaseDatabase.getInstance().getReference("Class").child("Report").child(name)
                        .child(user.getUid());
                myRef.setValue(report);

                Toast.makeText(Classroom_ReportActivity.this,"Reported Successfully",Toast.LENGTH_SHORT).show();
            }
        });
    }
}