package com.example.educor_app.Classroom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.educor_app.Authentications.LoginActivity;
import com.example.educor_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Classrooms extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref1,ref2,ref3,ref4;
    EditText t1,t2;
    Button b1,b2;
    DatabaseReference reference,ref,Ref;
    RecyclerView recyclerView;
    ArrayList<classroom_data> list;
    RecyclerAdapter_classroom adapter;
    public static String Name;
    FirebaseUser user;
    public  static int[] androidColors;
    public  static int randomAndroidColor;
    Window window;
    private ActionBar actionBar;
    public LottieAnimationView lottiePreloader;
    public TextView textview_class;
    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    private ImageView menu_button;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classrooms);

        if (Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }

        drawerLayout=findViewById(R.id.drawer_layout);
        menu_button=findViewById(R.id.menu_button);

        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView=findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id=item.getItemId();

                if (id == R.id.create) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Classrooms.this);
                    View view = getLayoutInflater().inflate(R.layout.activity_create_class, null);

                    t1 = view.findViewById(R.id.name);
                    b1 = view.findViewById(R.id.create);
                    builder.setView(view);
                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            assert user != null;
                            ref4 = FirebaseDatabase.getInstance().getReference("signup").child(user.getUid());
                            ref4.addValueEventListener(new ValueEventListener() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String qualification = Objects.requireNonNull(dataSnapshot.child("qualification").getValue()).toString();
                                    String create_name = Objects.requireNonNull(dataSnapshot.child("Username").getValue()).toString();

                                    if (qualification.equals("Teacher")) {

                                        final String class_name = t1.getText().toString();
                                        final String key = unique_key();

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        firebaseDatabase = FirebaseDatabase.getInstance();
                                        assert user != null;
                                        reference = firebaseDatabase.getReference("Class")
                                                .child("Create").child(class_name);

                                        final classroom_create info = new classroom_create(class_name, key, user.getUid(), create_name);

                                        reference.setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                Toast.makeText(Classrooms.this, "success",
                                                        Toast.LENGTH_SHORT).show();

                                                dialog.dismiss();
                                            }
                                        });

                                        list.clear();
                                    } else {
                                        Toast.makeText(Classrooms.this, "Teachers can only create the class", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }

                    });

                } else if (id == R.id.join) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Classrooms.this);
                    View mview = getLayoutInflater().inflate(R.layout.activity_join_class, null);

                    t2 = mview.findViewById(R.id.key);
                    b2 = mview.findViewById(R.id.join);
                    builder1.setView(mview);
                    final AlertDialog dialog1 = builder1.create();
                    dialog1.show();

                    b2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final String key = t2.getText().toString();

                            ref1 = FirebaseDatabase.getInstance().getReference("Class").child("Create");

                            ref1.addValueEventListener(new ValueEventListener() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    for (DataSnapshot df : snapshot.getChildren()) {

                                        final String Key = Objects.requireNonNull(df.child("Key").getValue()).toString();
                                        if (Key.equals(key)) {

                                            final String class_name = Objects.requireNonNull(df.child("Name").getValue()).toString();
                                            final String Name = Objects.requireNonNull(df.child("Name").getValue()).toString();
                                            final String create_name = Objects.requireNonNull(df.child("Create_Name").getValue()).toString();

                                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            assert user != null;

                                            //write into database reference

                                            Ref = FirebaseDatabase.getInstance().getReference().child("signup").child(user.getUid());
                                            Ref.addValueEventListener(new ValueEventListener() {
                                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    String join_name = Objects.requireNonNull(dataSnapshot.child("Username").getValue()).toString();

                                                    final classroom_join info = new classroom_join(join_name, Name, user.getUid(), Key, create_name);

                                                    ref = FirebaseDatabase.getInstance().getReference("Class")
                                                            .child("Join").child(user.getUid()).child(class_name);

                                                    ref.setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            Toast.makeText(Classrooms.this, "success",
                                                                    Toast.LENGTH_SHORT).show();

                                                            dialog1.dismiss();

                                                        }
                                                    });


                                                    list.clear();

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        } else {
                                            Toast.makeText(Classrooms.this, "Enter the correct class key", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }

                            });

                        }


                    });
                } else if (id == R.id.feedback) {
                    Toast.makeText(Classrooms.this, "FEEDBACK",
                            Toast.LENGTH_SHORT).show();
                } else if (id == R.id.Help) {
                    Toast.makeText(Classrooms.this, "HELP",
                            Toast.LENGTH_SHORT).show();
                } else if (id == R.id.signout) {
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();

                    Intent intent = new Intent(Classrooms.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

      //androidColors=getResources().getIntArray(R.array.androidcolors);
      //randomAndroidColor=androidColors[new Random().nextInt(androidColors.length)];
        // int[] color ={R.color.black,R.color.blue};

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        textview_class=findViewById(R.id.textview_class);


       // recyclerView.setBackgroundColor(color[0]);
        list=new ArrayList<>();
        if(list.isEmpty()){
            lottiePreloader=findViewById(R.id.class_button);
            textview_class.setVisibility(View.VISIBLE);

            lottiePreloader.setVisibility(View.VISIBLE);
            lottiePreloader.setSpeed(1);
            lottiePreloader.playAnimation();
        }

        user= FirebaseAuth.getInstance().getCurrentUser();

        //show the created class

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        ref2 = FirebaseDatabase.getInstance().getReference().child("Class")
                .child("Create");
        ref2.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot3)
            {


                    for (DataSnapshot dataSnapshot1 : dataSnapshot3.getChildren()) {

                        String userid= Objects.requireNonNull(dataSnapshot1.child("UserId").getValue()).toString();
                        if(userid.equals(user.getUid())) {
                            lottiePreloader.setVisibility(View.INVISIBLE);
                            textview_class.setVisibility(View.INVISIBLE);

                            classroom_data p = dataSnapshot1.getValue(classroom_data.class);
                            list.add(p);
                        }
                    }
                    adapter = new RecyclerAdapter_classroom(Classrooms.this, list);
                    recyclerView.setAdapter(adapter);


                }


               // recyclerView.setBackgroundColor(randomAndroidColor);


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //show the joined class

        ref3 = FirebaseDatabase.getInstance().getReference("Class").child("Join").child(user.getUid());
        ref3.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //list.clear();

                    for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {


                        String userid= Objects.requireNonNull(dataSnapshot2.child("Join_Userid").getValue()).toString();
                        if(userid.equals(user.getUid())) {
                            lottiePreloader.setVisibility(View.INVISIBLE);
                            textview_class.setVisibility(View.INVISIBLE);
                            classroom_data p = dataSnapshot2.getValue(classroom_data.class);
                            list.add(p);
                        }
                    }
                    adapter = new RecyclerAdapter_classroom(Classrooms.this, list);
                    recyclerView.setAdapter(adapter);
                }

               // recyclerView.setBackgroundColor(randomAndroidColor);


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



   /* @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            //create class

            case R.id.create:

                AlertDialog.Builder builder=new AlertDialog.Builder(Classrooms.this);
                View view=getLayoutInflater().inflate(R.layout.activity_create_class,null);

                t1=view.findViewById(R.id.name);
                b1=view.findViewById(R.id.create);
                builder.setView(view);
                final AlertDialog dialog=builder.create();
                dialog.show();

                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        assert user != null;
                        ref4=FirebaseDatabase.getInstance().getReference("signup").child(user.getUid());
                        ref4.addValueEventListener(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String qualification= Objects.requireNonNull(dataSnapshot.child("qualification").getValue()).toString();
                                String create_name= Objects.requireNonNull(dataSnapshot.child("Username").getValue()).toString();

                                if(qualification.equals("Teacher")){

                                    final String class_name = t1.getText().toString();
                                    final String key = unique_key();

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    firebaseDatabase = FirebaseDatabase.getInstance();
                                    assert user != null;
                                    reference = firebaseDatabase.getReference("Class")
                                            .child("Create").child(class_name);

                                    final classroom_create info = new classroom_create(class_name, key, user.getUid(),create_name);

                                    reference.setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            Toast.makeText(Classrooms.this, "success",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    dialog.dismiss();
                                    list.clear();
                                }
                                else{
                                    Toast.makeText(Classrooms.this,"Teachers can only create the class",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }

                });
                return true;
            // join class

            case R.id.join:

                AlertDialog.Builder builder1=new AlertDialog.Builder(Classrooms.this);
                View mview=getLayoutInflater().inflate(R.layout.activity_join_class,null);

                t2=mview.findViewById(R.id.key);
                b2=mview.findViewById(R.id.join);
                builder1.setView(mview);
                final AlertDialog dialog1=builder1.create();
                dialog1.show();

                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final String key = t2.getText().toString();

                        ref1=FirebaseDatabase.getInstance().getReference("Class").child("Create");

                        ref1.addValueEventListener(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot df : snapshot.getChildren()) {

                                    final String Key = Objects.requireNonNull(df.child("Key").getValue()).toString();
                                    if (Key.equals(key)) {

                                        final String class_name= Objects.requireNonNull(df.child("Name").getValue()).toString();
                                        final String Name = Objects.requireNonNull(df.child("Name").getValue()).toString();
                                        final String create_name = Objects.requireNonNull(df.child("Create_Name").getValue()).toString();

                                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        assert user != null;

                                        //write into database reference

                                        Ref = FirebaseDatabase.getInstance().getReference().child("signup").child(user.getUid());
                                        Ref.addValueEventListener(new ValueEventListener() {
                                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                String join_name = Objects.requireNonNull(dataSnapshot.child("Username").getValue()).toString();

                                                final classroom_join info = new classroom_join(join_name, Name, user.getUid(), Key, create_name);

                                                ref = FirebaseDatabase.getInstance().getReference("Class")
                                                        .child("Join").child(user.getUid()).child(class_name);

                                                ref.setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        Toast.makeText(Classrooms.this, "success",
                                                                Toast.LENGTH_SHORT).show();

                                                    }
                                                });

                                                dialog1.dismiss();
                                                list.clear();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                    else
                                    {
                                        Toast.makeText(Classrooms.this,"Enter the correct class key",Toast.LENGTH_SHORT).show();;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }

                        });
                    }
                });
                return true;
            case R.id.signout:
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();

                    Intent intent=new Intent(Classrooms.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    //unique key for create & join class

    public String unique_key(){

        int length=5,i;
        char [] charr="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        StringBuilder stringBuilder=new StringBuilder();
        Random random=new Random();
        for( i=0;i<length;i++){
            char c=charr[random.nextInt(charr.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

}