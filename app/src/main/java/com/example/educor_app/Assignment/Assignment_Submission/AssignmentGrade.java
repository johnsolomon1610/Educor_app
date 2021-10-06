package com.example.educor_app.Assignment.Assignment_Submission;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.educor_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AssignmentGrade extends AppCompatActivity {

    TextView student_name,assignment_name;
    ListView listView;
    DatabaseReference ref,ref1,ref2;
    EditText comments,grade;
    Button submit;
    List<upload> uploads;
    String student__name,Assignment__name,url,Student_userid,Class_name,pdf_NAME;
    private ActionBar actionBar;
    Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_grade);


        actionBar=getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#26A9E6")));

        this.setTitle("Grade");

        if (Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }


        student_name=findViewById(R.id.student_name);
        assignment_name=findViewById(R.id.title);
        comments=findViewById(R.id.comments);
        grade=findViewById(R.id.grade);
        submit=findViewById(R.id.submit);
        listView=findViewById(R.id.listview);

        Intent intent =getIntent();
        student__name=intent.getStringExtra("student_name");
        Assignment__name=intent.getStringExtra("Assignment_name");
        url=intent.getStringExtra("url");
        Student_userid=intent.getStringExtra("student_userid");
        Class_name=intent.getStringExtra("class_name");
        pdf_NAME=intent.getStringExtra("pdf_NAME");


        student_name.setText(student__name);
        assignment_name.setText(Assignment__name);

        uploads=new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                upload upload=uploads.get(i);

                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(upload.getUrl()));
                startActivity(intent);
            }
        });

        viewAllFiles();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Comments=comments.getText().toString();
                String Grade=grade.getText().toString();

                ref1= FirebaseDatabase.getInstance().getReference("Assignment").child(Class_name).child("submission").child(Assignment__name)
                        .child(Student_userid).child("comments");
                ref1.setValue(Comments);

                ref1= FirebaseDatabase.getInstance().getReference("Assignment").child(Class_name).child("submission").child(Assignment__name)
                        .child(Student_userid).child("grade");
                ref1.setValue(Grade);

                Toast.makeText(AssignmentGrade.this,"graded successfully",Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void viewAllFiles(){

        ref= FirebaseDatabase.getInstance().getReference("Assignment").child(Class_name).child("submission").child(Assignment__name).child(Student_userid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                uploads.clear();

                    upload upload=dataSnapshot.getValue(upload.class);
                    uploads.add(upload);

                String [] Upload=new String[uploads.size()];

                for(int i=0;i<Upload.length;i++){

                    Upload[i]=uploads.get(i).getPdf_NAME();
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,Upload);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}