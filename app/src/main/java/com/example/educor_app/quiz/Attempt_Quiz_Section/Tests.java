package com.example.educor_app.quiz.Attempt_Quiz_Section;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.educor_app.R;
import com.example.educor_app.quiz.Create_Quiz.Custom_quiz;
import com.example.educor_app.quiz.Model.Question;
import com.example.educor_app.quiz.Model.Test;
import com.example.educor_app.quiz.NotificationActivity.NotificationService;
import com.example.educor_app.quiz.Results_section.ResultsAdmin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class Tests extends AppCompatActivity {
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference myRef,databaseReference;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    private ListView listView;
    private TestAdapter testAdapter;
    private int lastPos = -1;
    String class_name;
    private ActionBar actionBar;
    Window window;

    ArrayList<Test> tests=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);

        actionBar=getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#26A9E6")));

        this.setTitle("Tests");

        if (Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }

        user= FirebaseAuth.getInstance().getCurrentUser();

        Intent intent=getIntent();
        class_name=intent.getStringExtra("name");

        avLoadingIndicatorView = findViewById(R.id.loader1);
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        avLoadingIndicatorView.show();

        database= FirebaseDatabase.getInstance();
        myRef=database.getReference();
        listView=findViewById(R.id.test_listview);
        testAdapter=new TestAdapter(Tests.this,tests);
        listView.setAdapter(testAdapter);
        getQues();

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_quiz,menu);
        final MenuItem test=menu.findItem(R.id.test);
        final MenuItem result=menu.findItem(R.id.result);

        databaseReference=FirebaseDatabase.getInstance().getReference("signup").child(user.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String qualification=snapshot.child("qualification").getValue().toString();
                    if(qualification.equals("Student")){
                        test.setVisible(false);
                        result.setVisible(false);
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.result:
                Intent intent = new Intent(Tests.this, ResultsAdmin.class);
                intent.putExtra("class_name",class_name);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            case R.id.test:
                Intent intent1 = new Intent(Tests.this, Custom_quiz.class);
                intent1.putExtra(" class_name",class_name);
                startActivity(intent1);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        stopService(new Intent(Tests.this, NotificationService.class));
    }

    public void getQues(){
        //addListenerForSingleValueEvent
        myRef.child("Quiz").child(class_name).child("tests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tests.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Test t=new Test();
                    t.setName(snapshot.getKey());
                    t.setTime(Long.parseLong(snapshot.child("Time").getValue().toString()));
                    ArrayList<Question> ques=new ArrayList<>();
                    for (DataSnapshot qSnap:snapshot.child("Questions").getChildren()){
                        ques.add(qSnap.getValue(Question.class));
                    }
                    t.setQuestions(ques);
                    tests.add(t);

                }
                testAdapter.dataList=tests;
                testAdapter.notifyDataSetChanged();
                avLoadingIndicatorView.setVisibility(View.GONE);
                avLoadingIndicatorView.hide();
                Log.e("The read success: " ,"su"+tests.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                avLoadingIndicatorView.setVisibility(View.GONE);
                avLoadingIndicatorView.hide();
                Log.e("The read failed: " ,databaseError.getMessage());
            }
        });
    }

    class TestAdapter extends ArrayAdapter<Test> implements Filterable {
        private Context mContext;
        ArrayList<Test> dataList;
        public TestAdapter( Context context,ArrayList<Test> list) {
            super(context, 0 , list);
            mContext = context;
            dataList = list;
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItem = convertView;
            if(listItem == null)
                listItem = LayoutInflater.from(mContext).inflate(R.layout.test_item,parent,false);

            ((ImageView)listItem.findViewById(R.id.item_imageView)).
                    setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.educor_logo));

            ((TextView)listItem.findViewById(R.id.item_textView))
                    .setText(dataList.get(position).getName()+" : "+dataList.get(position).getTime()+"Min");

            databaseReference=FirebaseDatabase.getInstance().getReference("signup").child(user.getUid());
            final View finalListItem = listItem;
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String qualification=snapshot.child("qualification").getValue().toString();
                        if(qualification.equals("Teacher")){

                            ((Button) finalListItem.findViewById(R.id.item_button)).setVisibility(View.INVISIBLE);

                        }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            (listItem.findViewById(R.id.item_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext, AttemptTest.class);
                    intent.putExtra("Questions",dataList.get(position));
                    intent.putExtra("TESTNAME",dataList.get(position).getName());
                    intent.putExtra("class_name",class_name);
                    startActivity(intent);
                }
            });

            Animation animation = AnimationUtils.loadAnimation(getContext(),
                    (position > lastPos) ? R.anim.up_from_bottom : R.anim.down_from_top);
            (listItem).startAnimation(animation);
            lastPos = position;

            return listItem;
        }
    }


}
