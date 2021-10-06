package com.example.educor_app.Assignment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import java.util.Objects;

public class AssignmentUpload extends AppCompatActivity {

    TextView t1,t2;
    String name,key;
    public Button upload_button;
    public FirebaseUser user;
    DatabaseReference databaseReference,ref4;
    RecyclerAdapter_assignment adapter;

    RecyclerView recyclerView;
    ArrayList<assignment_data> list;

    private ActionBar actionBar;
    Window window;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_upload);

        actionBar=getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#26A9E6")));

        this.setTitle("Assignments");

        if (Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        t1=findViewById(R.id.class_name);
        t2=findViewById(R.id.key_name);
        upload_button=findViewById(R.id.assignment);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(true);

        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        key=intent.getStringExtra("Teacher");

        t1.setText(name);
        t2.setText(key);

        list = new ArrayList<>();

        user=FirebaseAuth.getInstance().getCurrentUser();

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ref4=FirebaseDatabase.getInstance().getReference("signup").child(user.getUid());
                ref4.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String qualification = Objects.requireNonNull(dataSnapshot.child("qualification").getValue()).toString();

                        if (qualification.equals("Teacher")) {

                            Intent intent=new Intent(AssignmentUpload.this, AssignmentForm.class);
                            intent.putExtra("class_name",name);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(AssignmentUpload.this,"Students cannot post the assignment",Toast.LENGTH_SHORT).show();;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        user= FirebaseAuth.getInstance().getCurrentUser();

        databaseReference=FirebaseDatabase.getInstance().getReference("Assignment").child(name).child("Assignment");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                    for (DataSnapshot df : snapshot.getChildren()) {
                        if(df.exists()) {
                            assignment_data p = df.getValue(assignment_data.class);
                            list.add(p);
                        }
                    }
                    adapter = new RecyclerAdapter_assignment(AssignmentUpload.this, list);
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
