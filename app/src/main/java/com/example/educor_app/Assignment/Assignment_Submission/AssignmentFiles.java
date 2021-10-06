package com.example.educor_app.Assignment.Assignment_Submission;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.educor_app.R;
import com.example.educor_app.databinding.ActivityAssignmentFilesBinding;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AssignmentFiles extends AppCompatActivity {

    TextView t1,t2,t3,t4,t5,t6;
    Button b1,b2,b3;
    ListView listView;
    Uri url;
    DatabaseReference databaseReference,ref1,ref,ref2;
    StorageReference reference;
    FirebaseUser user;
    List<upload_answer> upload_answers;
    List<upload> uploads;
    String due,title,description,teacher_userid,class_name,Url;
    ActivityAssignmentFilesBinding binding;
    public TextView progressmark;
    public ProgressBar progressBar;
    public TextView score_level;
    TextView submission_header, grade_header,comment_header;
    View view_divider;
    private ActionBar actionBar;
    Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_files);

        actionBar=getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#26A9E6")));

        this.setTitle("Your Work");

        if (Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }



        binding=ActivityAssignmentFilesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user=FirebaseAuth.getInstance().getCurrentUser();

        submission_header=findViewById(R.id.Submisiion_header);
        grade_header=findViewById(R.id.grade_header);
        comment_header=findViewById(R.id.comment_header);
        view_divider=findViewById(R.id.view_divider);

        listView=findViewById(R.id.listview);
        t1=findViewById(R.id.due);
        t2=findViewById(R.id.titles);
        t3=findViewById(R.id.description);
        b1=findViewById(R.id.files);
        t4=findViewById(R.id.grade);
        b2=findViewById(R.id.submit);
        t5=findViewById(R.id.comments);
        progressmark=findViewById(R.id.tvcounter);
        progressBar=findViewById(R.id.progressbar);
        score_level=findViewById(R.id.score_level);

        upload_answers=new ArrayList<upload_answer>();
        uploads=new ArrayList<>();

        Intent intent=getIntent();
        due=intent.getStringExtra("due");
        title=intent.getStringExtra("title");
        description=intent.getStringExtra("description");
        teacher_userid=intent.getStringExtra("teacher_userid");
        class_name=intent.getStringExtra("class_name");
        Url= intent.getStringExtra("Url");

        t1.setText(due);
        t2.setText(title);
        t3.setText("Comment: "+description);

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Url));
                startActivity(intent);
            }
        });

        viewAllFiles();

        ref= FirebaseDatabase.getInstance().getReference("Assignment").child(class_name).child("submission").child(title)
                .child(user.getUid()).child("grade");
        ref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    String grade = Objects.requireNonNull(snapshot.getValue()).toString();
                    view_divider.setVisibility(View.VISIBLE);
                    grade_header.setVisibility(View.VISIBLE);
                    t4.setText(grade+"/100");

                    try{
                        int value= Integer.parseInt(grade);
                        progressBar.setVisibility(View.VISIBLE);
                        progressmark.setVisibility(View.VISIBLE);
                        score_level.setVisibility(View.VISIBLE);
                        if (value>50){
                            score_level.setText("GOOD");
                        }else if (value>80){
                            score_level.setText("EXCELLENT");
                        }else{
                            score_level.setText("WORST");
                        }
                        startAnimator(0,value);
                    }catch (NumberFormatException e){
                        progressBar.setVisibility(View.VISIBLE);
                        progressmark.setVisibility(View.VISIBLE);
                        progressmark.setText("0");
                        score_level.setVisibility(View.VISIBLE);
                        score_level.setText("BAD");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref2= FirebaseDatabase.getInstance().getReference("Assignment").child(class_name).child("submission").child(title)
                .child(user.getUid()).child("comments");
        ref2.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    String comments = Objects.requireNonNull(snapshot.getValue()).toString();
                    comment_header.setVisibility(View.VISIBLE);
                    t5.setText(comments);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // selectPDFFILE();
               // viewAllFiles();

                AlertDialog.Builder builder=new AlertDialog.Builder(AssignmentFiles.this);
                 view=getLayoutInflater().inflate(R.layout.activity_assignment_dialog,null);

                t6=view.findViewById(R.id.name);
                b3=view.findViewById(R.id.create);
                builder.setView(view);
                final AlertDialog dialog=builder.create();
                dialog.show();

                b3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectPDFFILE();
                        viewAllFiles();
                    }
                });

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                upload upload=uploads.get(i);


                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(upload.getUrl()));
                startActivity(intent);
            }
        });
    }

    //methods

    public void startAnimator(int start_no, int end_no){
        ValueAnimator animator=ValueAnimator.ofInt(start_no,end_no);
        animator.setDuration(5000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                binding.tvcounter.setText(valueAnimator.getAnimatedValue().toString()+"");
                binding.progressbar.setProgress(Integer.parseInt(valueAnimator.getAnimatedValue().toString()));
            }
        });

        animator.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding=null;
    }

    private void viewAllFiles(){

        ref= FirebaseDatabase.getInstance().getReference("Assignment").child(class_name).child("submission").child(title).child(user.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                uploads.clear();
                if(dataSnapshot.exists()) {
                    upload upload = dataSnapshot.getValue(upload.class);
                    uploads.add(upload);
                    submission_header.setVisibility(View.VISIBLE);


                    String[] Upload = new String[uploads.size()];

                    for (int i = 0; i < Upload.length; i++) {

                        Upload[i] = uploads.get(i).getPdf_NAME();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, Upload);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void selectPDFFILE(){
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select PDF File"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode!=RESULT_CANCELED) {
            if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
                uploadPDFFile(data.getData());
            }
        }
    }

    private  void uploadPDFFile(Uri data) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        reference = FirebaseStorage.getInstance().getReference().child("Assignment_uploads/" + System.currentTimeMillis() + ".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete()) ;
                        url = uri.getResult();

                        ref1=FirebaseDatabase.getInstance().getReference("signup").child(user.getUid());
                        ref1.addValueEventListener(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String user_name= Objects.requireNonNull(snapshot.child("Username").getValue()).toString();

                                assert url != null;
                                upload_answer Upload_answer = new upload_answer(title, url.toString(), user_name, user.getUid(),teacher_userid,class_name,t6.getText().toString());
                                databaseReference = FirebaseDatabase.getInstance().getReference("Assignment").child(class_name).child("submission").child(title).child(user.getUid());
                                databaseReference.setValue(Upload_answer).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(AssignmentFiles.this, " Uploaded", Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

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