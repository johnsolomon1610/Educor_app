package com.example.educor_app.Assignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.educor_app.Assignment.Assignment_Submission.upload;
import com.example.educor_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AssignmentForm extends AppCompatActivity {

    EditText Title,Due_time,Description;
    Button Upload;
    public static Uri url;
     String date,user_name,class_name;
    FirebaseUser user;
    FirebaseDatabase database;
    StorageReference storageReference,reference;
    DatabaseReference databaseReference,userref,myref,ref;
    List<upload> uploads;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_form);

        Title=findViewById(R.id.title);
        Due_time=findViewById(R.id.duetime);
        Description=findViewById(R.id.description);
        Upload=findViewById(R.id.upload);
        listView=findViewById(R.id.listview);

        uploads=new ArrayList<>();

        Intent intent=getIntent();
        class_name=intent.getStringExtra("class_name");

        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select PDF File"),1);
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED) {
            if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                storageReference = FirebaseStorage.getInstance().getReference();
                reference = storageReference.child("Assignment_uploads/" + System.currentTimeMillis() + ".pdf");
                reference.putFile(data.getData())
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
                                Date now = new Date();
                                date = formatter.format(now);


                                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uri.isComplete()) ;
                                url = uri.getResult();


                                userref= FirebaseDatabase.getInstance().getReference();
                                myref= userref.child("signup").child(user.getUid()).child("Username");
                                myref.addValueEventListener(new ValueEventListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        user_name= (Objects.requireNonNull(dataSnapshot.getValue()).toString());

                                        uploadPDF upload_assignment = new uploadPDF(Title.getText().toString(), url.toString(), user_name,date,Due_time.getText().toString(),
                                                Description.getText().toString(), user.getUid(),class_name);

                                        databaseReference = FirebaseDatabase.getInstance().getReference("Assignment")
                                                .child(class_name).child("Assignment").child(Title.getText().toString());
                                        databaseReference.setValue(upload_assignment).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                Toast.makeText(AssignmentForm.this, "Assignment Uploaded", Toast.LENGTH_LONG).show();
                                                progressDialog.dismiss();

                                            }
                                        });
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded: " + (int) progress + "%");


                    }
                });
            }
        }
    }
}