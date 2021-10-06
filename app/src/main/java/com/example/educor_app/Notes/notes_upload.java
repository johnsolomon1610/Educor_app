package com.example.educor_app.Notes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.MenuCompat;

import com.example.educor_app.R;
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

import static android.content.Intent.ACTION_VIEW;

public class notes_upload extends AppCompatActivity {

    TextView t1,t2;
    public static String name,key;
    public EditText editText;
    Button b1;
    private  TextView files_header;
    private TextView file_name;
    private DatabaseReference ref;
    private FirebaseDatabase database;
    public Button upload_button;
    private String date;
    public FirebaseUser user;
    StorageReference storageReference;
    DatabaseReference databaseReference,Ref;
    DatabaseReference databaseReferences;
    DatabaseReference databaseReferencess;
    DatabaseReference databaseReferencesss;
    ListView listView;
    public static String fileref;
    public static  String user_name;
    List<uploadPDF> uploadPDFS;
    public static Uri url;
    public static String[] file_names;

    private ActionBar actionBar;
    Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_upload);

        actionBar=getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#26A9E6")));

        this.setTitle("Notes");

        if (Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }

        t1=findViewById(R.id.class_name);
        t2=findViewById(R.id.key_name);

        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        key=intent.getStringExtra("Teacher");

        t1.setText(name);
        t2.setText(key);

        listView=(ListView)findViewById(R.id.files_list);
        listView.setNestedScrollingEnabled(false);

        files_header=findViewById(R.id.files_header);

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        uploadPDFS=new ArrayList<>();

        DatabaseReference userref= FirebaseDatabase.getInstance().getReference();
        DatabaseReference myref= userref.child("signup").child(user.getUid()).child("Username");
        myref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_name= (Objects.requireNonNull(dataSnapshot.getValue()).toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        viewAllFiles();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.activity_menu_file,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.upload) {

            AlertDialog.Builder builder = new AlertDialog.Builder(notes_upload.this);
            View view = getLayoutInflater().inflate(R.layout.activity_files_dialog, null);

            editText = view.findViewById(R.id.name);
            b1 = view.findViewById(R.id.upload_files);
            builder.setView(view);
            final AlertDialog dialog = builder.create();
            dialog.show();

            storageReference = FirebaseStorage.getInstance().getReference();
            databaseReference = FirebaseDatabase.getInstance().getReference("file_uploads").child("files").child(name);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectPDFFILE();
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
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

        if(requestCode ==1 && resultCode == RESULT_OK && data != null && data.getData() !=null){
            uploadPDFFile(data.getData());
        }
    }

    private  void uploadPDFFile(Uri data){

        final ProgressDialog progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference reference=storageReference.child("file_uploads/"+System.currentTimeMillis()+".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
                        Date now = new Date();
                        date = formatter.format(now);

                        Task<Uri> uri =taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete());
                        url= uri.getResult();

                        uploadPDF uploadPDF=new uploadPDF(editText.getText().toString(),url.toString(),user_name, date);
                        databaseReference.child(editText.getText().toString()).setValue(uploadPDF);

                        Toast.makeText(notes_upload.this,"File Uploaded",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();


                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded: "+(int)progress+"%");

            }
        });
    }

    private void  viewAllFiles(){
        file_names = new String[uploadPDFS.size()];
        for (int i=0;i<file_names.length;i++){
            file_names[i]=uploadPDFS.get(i).getName();
        }
        // fileref= Arrays.toString(file_names);
        databaseReferences=FirebaseDatabase.getInstance().getReference("file_uploads").child("files").child(name);
        databaseReferences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                uploadPDFS.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    uploadPDF uploadPDF=postSnapshot.getValue(uploadPDF.class);
                    uploadPDFS.add(uploadPDF);
                }

                String file_name[] = new String[uploadPDFS.size()];
                String users[]=new String[uploadPDFS.size()];
                String Time[]=new String[uploadPDFS.size()];

                files_header.setText(uploadPDFS.size() == 0 ? "No Files yet!" : "Files");

                for (int i=0;i<file_name.length;i++){
                    file_name[i]=uploadPDFS.get(i).getName();
                }

                for (int i=0;i<users.length;i++){
                    users[i]=uploadPDFS.get(i).getUser_name();
                }

                for (int i=0;i<Time.length;i++){
                    Time[i]=uploadPDFS.get(i).getDate();
                }

                MyAdapter adapter=new MyAdapter(notes_upload.this,file_name,users,Time);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    class  MyAdapter extends ArrayAdapter<String> {
        Context context;
        String rfile_name[];
        String rusers[];
        String rDate[];
        // String rUrl[];

        MyAdapter(Context c, String sfile_name[], String susers[], String sDate[]){
            super(c,R.layout.cardview_files,R.id.file_name,susers);

            this.context=c;
            this.rfile_name=sfile_name;
            this.rusers=susers;
            this.rDate=sDate;
            // this.rUrl=sUrl;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View cardview_files= layoutInflater.inflate(R.layout.cardview_files,parent,false);
            final TextView myusers=cardview_files.findViewById(R.id.users);
            final TextView myfile_name=cardview_files.findViewById(R.id.file_name);
            final TextView myDate=cardview_files.findViewById(R.id.Date);
            final EditText mycomments=cardview_files.findViewById(R.id.comments);
            ImageButton mysendbutton=cardview_files.findViewById(R.id.send_icon);
            final ImageButton mybutton=cardview_files.findViewById(R.id.more_icon_vertical);


            myfile_name.setText(rfile_name[position]);
            myusers.setText(rusers[position]);
            myDate.setText(rDate[position]);

            String[] Url =new String[uploadPDFS.size()];

            for (int i=0;i<Url.length;i++){
                Url[i]=uploadPDFS.get(i).getUrl();
            }

            final String filename=myfile_name.getText().toString();
            final String username=myusers.getText().toString();
            final String time=myDate.getText().toString();

            mysendbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ref = database.getReference("file_uploads").child("comments").push();
                    databaseReferences= FirebaseDatabase.getInstance().getReference("file_uploads").child("Comments").child(name).child(username).child(filename);
                    final String comments=mycomments.getText().toString();
                    comment_upload comment_upload=new comment_upload(comments,user_name,time);
                    databaseReferences.child(databaseReferences.push().getKey()).setValue(comment_upload);
                  /*  ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            final String comments=mycomments.getText().toString();
                            ref=database.getReference("file_uploads").child("Comments").child(username).child(filename).child(Objects.requireNonNull(ref.push().getKey()));
                            ref.child("Comment").setValue(comments);
                            ref.child("User").setValue(user_name);
                            ref.child("Time").setValue(time);

                            //ref.child("Time").setValue(strDate);
                           // ref.child("User").setValue(user_name);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/
                }
            });

            myfile_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    uploadPDF uploadPDFs=uploadPDFS.get(position);
                    // Uri uri;
                    //   uri = Uri.parse(Arrays.toString(Url));
                    //  Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                    //  startActivity(intent);
                    Uri uri=Uri.parse(uploadPDFs.getUrl());
                    Intent intent=new Intent(ACTION_VIEW,uri);
                    startActivity(intent);

                }
            });

            mybutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu=new PopupMenu(notes_upload.this,mybutton);
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu_files,popupMenu.getMenu());
                    MenuCompat.setGroupDividerEnabled(popupMenu.getMenu(),true);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.Delete:
                                    databaseReferencess= FirebaseDatabase.getInstance().getReference("file_uploads").child("Comments").child(name).child(username).child(filename);
                                    databaseReferencesss=FirebaseDatabase.getInstance().getReference("file_uploads").child("files").child(name).child(user.getUid()).child(myfile_name.getText().toString());
                                    databaseReferencess.removeValue();
                                    databaseReferencesss.removeValue();
                                    break;
                                case R.id.Comments:
                                    String file=myfile_name.getText().toString();
                                    String usernames=myusers.getText().toString();
                                    Intent intent=new Intent(notes_upload.this,comment_page.class);
                                    Bundle b=new Bundle();
                                    b.putString("Key",file);
                                    b.putString("Key1",usernames);
                                    b.putString("Key2",name);
                                    intent.putExtras(b);
                                    startActivity(intent);
                                    break;
                                default:
                                    return false;
                            }
                            return  true;
                        }
                    });
                    popupMenu.show();
                }
            });
            return cardview_files;
        }
    }
}