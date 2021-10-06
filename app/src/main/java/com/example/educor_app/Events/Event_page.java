package com.example.educor_app.Events;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.educor_app.Authentications.Dashboard;
import com.example.educor_app.Authentications.ProfileActivity;
import com.example.educor_app.Notifications.Notification;
import com.example.educor_app.R;
import com.example.educor_app.chats.ui.activities.UserListingActivity;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Event_page extends AppCompatActivity implements DatePickerListener {

    private String strDate;
    private String childdate;
    public TextView invisibletext;
    FirebaseUser user;
    RecyclerView recyclerView;
    private FirebaseDatabase database;
    DatabaseReference reference, Ref,notification_ref,message_ref;

    FirebaseAuth firebaseAuth;
    private DatabaseReference ref;
    private Button post_button;
    private EditText events;
    ArrayList<Event_rv> list;
    ArrayList<Event_rvs> lists;
    Event_adapter adapter;
    Event_adapters calendar_adapter;
    public TextView Event_header;
    public long selectedDate;
    private String calendar_date;
    private String datesetter;
    private String datesetters;
    public static String Classname;

    Window window;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_page);

        if (Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationview);
        bottomNavigationView.setSelectedItemId(R.id.Schedule);

        String Classnames = getIntent().getStringExtra("name");
        String key = getIntent().getStringExtra("key");
        String Teacher=getIntent().getStringExtra("Teacher");

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        Intent intent=new Intent(Event_page.this, ProfileActivity.class);
                        intent.putExtra("name",Classnames);
                        intent.putExtra("Teacher",Teacher);
                        intent.putExtra("key",key);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.homepage:
                        Intent lntent=new Intent(Event_page.this, Dashboard.class);
                        lntent.putExtra("name",Classnames);
                        lntent.putExtra("Teacher", Teacher);
                        lntent.putExtra("key", key);
                        startActivity(lntent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.chat:
                        Intent lntents=new Intent(Event_page.this, UserListingActivity.class);
                        lntents.putExtra("name",Classnames);
                        lntents.putExtra("Teacher", Teacher);
                        lntents.putExtra("key", key);
                        startActivity(lntents);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.Schedule:
                        return true;
                    case R.id.notification:
                        Intent lntentss=new Intent(Event_page.this, Notification.class);
                        lntentss.putExtra("key", key);
                        lntentss.putExtra("name",Classnames);
                        lntentss.putExtra("Teacher", Teacher);
                        startActivity(lntentss);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });


        Classname = getIntent().getStringExtra("name");

      /*  BottomNavigationView bottomNavigationView = findViewById(R.id.navigationview);
        bottomNavigationView.setSelectedItemId(R.id.Schedule);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.homepage:
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.chat:
                        startActivity(new Intent(getApplicationContext(), chat.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.Schedule:
                        return true;
                    case R.id.notification:
                        startActivity(new Intent(getApplicationContext(), Notification.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });*/


        post_button = findViewById(R.id.post_button);
        events = findViewById(R.id.events);
        invisibletext = findViewById(R.id.invisibletext);
        Event_header = findViewById(R.id.Event_header);
        recyclerView = (RecyclerView) findViewById(R.id.myRecycler);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        list = new ArrayList<Event_rv>();
        lists = new ArrayList<Event_rvs>();

        HorizontalPicker picker = (HorizontalPicker) findViewById(R.id.calenderview);
        picker.setListener(this)
                .setDays(120)
                .setOffset(7)
                .setDateSelectedColor(Color.DKGRAY)
                .setDateSelectedTextColor(Color.WHITE)
                .setMonthAndYearTextColor(Color.DKGRAY)
                .setTodayButtonTextColor(getResources().getColor(R.color.blue))
                .setTodayDateTextColor(getResources().getColor(R.color.blue))
                .setTodayDateBackgroundColor(Color.GRAY)
                .setUnselectedDayTextColor(Color.DKGRAY)
                .setDayOfWeekTextColor(Color.DKGRAY)
                .setUnselectedDayTextColor(getResources().getColor(R.color.white))
                .showTodayButton(false)
                .init();
        picker.setBackgroundColor(Color.LTGRAY);
        picker.setDate(new DateTime());


        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
        DateFormat dateFormats = new SimpleDateFormat("yyyy_MM_dd");
        Date date = new Date();
        selectedDate = picker.getDays();
        calendar_date = dateFormats.format(selectedDate);

        strDate = dateFormat.format(date);
        childdate = dateFormats.format(date);
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference userref = FirebaseDatabase.getInstance().getReference();
                DatabaseReference myref = userref.child("signup").child(user.getUid()).child("Username");
                myref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String user_name = dataSnapshot.getValue().toString();

                        ref = database.getReference("Events").child(Classname).child(childdate).push();
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String posted_msg = events.getText().toString();

                                ref.child("Post").setValue(posted_msg);
                                ref.child("Time").setValue(strDate);
                                ref.child("User").setValue(user_name);

                                //notification content

                                String message=user_name+"  uploaded an event";

                                NotificationCompat.Builder builder=new NotificationCompat.Builder(Event_page.this)
                                        .setSmallIcon(R.drawable.ic_baseline_message_24)
                                        .setContentTitle("Educor")
                                        .setContentText(message)
                                        .setAutoCancel(true);

                                Intent intent1=new Intent(Event_page.this,Notification.class);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                PendingIntent pendingIntent=PendingIntent.getActivity(Event_page.this,0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                                builder.setContentIntent(pendingIntent);

                                NotificationManager notificationManager=(NotificationManager)getSystemService(
                                        Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(0,builder.build());

                                Ref=FirebaseDatabase.getInstance().getReference("signup");
                                Ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for(DataSnapshot d1:snapshot.getChildren()){
                                            String User_id= Objects.requireNonNull(d1.child("Userid").getValue()).toString();

                                            notification_ref=FirebaseDatabase.getInstance().getReference("Class")
                                                    .child("Join").child(User_id);
                                            notification_ref.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for(DataSnapshot snapshot1:snapshot.getChildren()){

                                                                String class_name= Objects.requireNonNull(snapshot1.child("Name").getValue()).toString();
                                                                if(class_name.equals(Classnames)){
                                                                   message_ref= FirebaseDatabase.getInstance().getReference("Notification").
                                                                            child(User_id).push();
                                                                   message_ref.addValueEventListener(new ValueEventListener() {
                                                                       @Override
                                                                       public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                           message_ref.child("messsage").setValue(posted_msg);
                                                                     
                                                                       }

                                                                       @Override
                                                                       public void onCancelled(@NonNull DatabaseError error) {

                                                                       }
                                                                   });
                                                                }
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                        }
                                                    });
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

        });
        reference = FirebaseDatabase.getInstance().getReference().child("Events").child(Classname).child(calendar_date);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                events.setVisibility(View.VISIBLE);
                post_button.setVisibility(View.VISIBLE);
                invisibletext.setVisibility(View.INVISIBLE);
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Event_rv rm = dataSnapshot1.getValue(Event_rv.class);
                    list.add(rm);
                }
                Event_header.setText(list.size() == 0 ? "No Events yet!" : "Events");
                adapter = new Event_adapter(Event_page.this, list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Event_page.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

      /*  calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy_M_d");
                try {
                    Date selected = dateFormat.parse(i + "_" + (i1 + 1) + "_" + i2);
                    dateFormat = new SimpleDateFormat("yyyy_MM_dd");
                    datesetter = dateFormat.format(selected);

                    String curdate=new SimpleDateFormat("yyyy_MM_dd", Locale.getDefault()).format(new Date());

                    if(!datesetter.equals(curdate)){
                        events.setVisibility(View.INVISIBLE);
                        post_button.setVisibility(View.INVISIBLE);
                        invisibletext.setVisibility(View.VISIBLE);
                    }else {
                        events.setVisibility(View.VISIBLE);
                        post_button.setVisibility(View.VISIBLE);
                        invisibletext.setVisibility(View.INVISIBLE);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                reference = FirebaseDatabase.getInstance().getReference().child("Events").child(Classname).child(datesetter);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        lists.clear();

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Event_rvs rcm = dataSnapshot1.getValue(Event_rvs.class);
                            lists.add(rcm);
                        }
                        Event_header.setText(lists.size() == 0 ? "No Events Yet!" : "Events" );
                        calendar_adapter = new Event_adapters(Event_page.this, lists);
                        recyclerView.setAdapter(calendar_adapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Event_page.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });*/

    }

    @Override
    public void onDateSelected(org.joda.time.DateTime dateSelected) {
        // Toast.makeText(this,dateSelected.toString(),Toast.LENGTH_SHORT).show();

       DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
       DateFormat inputFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX",Locale.US);
       datesetters=dateSelected.toString();
        Date dates= null;
        try {
            dates = inputFormat.parse(datesetters);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        datesetter=dateFormat.format(dates);
        String curdate = new SimpleDateFormat("yyyy_MM_dd", Locale.getDefault()).format(new Date());

        if (!datesetter.equals(curdate)) {
            events.setVisibility(View.INVISIBLE);
            post_button.setVisibility(View.INVISIBLE);
            invisibletext.setVisibility(View.VISIBLE);
        } else {
            events.setVisibility(View.VISIBLE);
            post_button.setVisibility(View.VISIBLE);
            invisibletext.setVisibility(View.INVISIBLE);
        }

        reference = FirebaseDatabase.getInstance().getReference().child("Events").child(Classname).child(datesetter);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lists.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Event_rvs rcm = dataSnapshot1.getValue(Event_rvs.class);
                    lists.add(rcm);
                }
                Event_header.setText(lists.size() == 0 ? "No Events Yet!" : "Events");
                calendar_adapter = new Event_adapters(Event_page.this, lists);
                recyclerView.setAdapter(calendar_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Event_page.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}