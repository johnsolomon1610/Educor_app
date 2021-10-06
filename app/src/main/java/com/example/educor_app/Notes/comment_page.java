package com.example.educor_app.Notes;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.educor_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class comment_page extends AppCompatActivity {

    private FirebaseDatabase database;
    DatabaseReference databaseReferences;
    ListView listView;
    List<comment_upload> uploadPDFSSS;
    public FirebaseUser user;
    public static  String user_name;
    public static  String Comment;
    public static  String date_time;
    public static  String User;
    public static String filename;
    public static String classname;
    private String date;
    public TextView comments_header;

    private ActionBar actionBar;
    Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_page);

        actionBar=getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#26A9E6")));

        this.setTitle("Comments");

        if (Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }

        listView=(ListView)findViewById(R.id.comments_list);
        comments_header=findViewById(R.id.comments_header);

        uploadPDFSSS=new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        DatabaseReference userref= FirebaseDatabase.getInstance().getReference();
        // DatabaseReference myref= userref.child("signup").child(user.getUid()).child("Name");
        userref.child("signup").child(user.getUid()).child("Username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_name= dataSnapshot.getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Bundle b=getIntent().getExtras();
        if(b!=null){
            filename=b.getString("Key");
            User=b.getString("Key1");
            classname=b.getString("Key2");
        }

        viewAllFiles();
    }

    private void  viewAllFiles(){
        databaseReferences=FirebaseDatabase.getInstance().getReference("file_uploads").child("Comments").child(classname).child(User).child(filename);
        databaseReferences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                uploadPDFSSS.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    comment_upload comment_upload=postSnapshot.getValue(comment_upload.class);
                    uploadPDFSSS.add(comment_upload);
                }

                String comment[] = new String[uploadPDFSSS.size()];
                String users[]=new String[uploadPDFSSS.size()];
                String Time[]=new String[uploadPDFSSS.size()];

                comments_header.setText(uploadPDFSSS.size() == 0 ? "No Comments!" : "Comments");

                for (int i=0;i<comment.length;i++){
                    comment[i]=uploadPDFSSS.get(i).getComment();
                }

                for (int i=0;i<users.length;i++){
                    users[i]=uploadPDFSSS.get(i).getUser_name();
                }

                for (int i=0;i<Time.length;i++){
                    Time[i]=uploadPDFSSS.get(i).getDate();
                }

                MyAdapter adapter=new MyAdapter(comment_page.this,comment,users,Time);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    class  MyAdapter extends ArrayAdapter<String> {
        Context context;
        String rcomment[];
        String rusers[];
        String rDate[];
        // String rUrl[];

        MyAdapter(Context c, String scomment[], String susers[], String sDate[]) {
            super(c, R.layout.cardview_comments, R.id.comment, susers);

            this.context = c;
            this.rcomment = scomment;
            this.rusers = susers;
            this.rDate = sDate;
            // this.rUrl=sUrl;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View cardview_comments = layoutInflater.inflate(R.layout.cardview_comments, parent, false);
            final TextView myusers = cardview_comments.findViewById(R.id.users);
            final TextView mycomment = cardview_comments.findViewById(R.id.comment);
            final TextView myDate = cardview_comments.findViewById(R.id.Date);


            mycomment.setText(rcomment[position]);
            myusers.setText(rusers[position]);
            myDate.setText(rDate[position]);

            return cardview_comments;
        }
    }
}