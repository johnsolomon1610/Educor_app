package com.example.educor_app.Authentications;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.educor_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    public Button register;
    public TextView signin_button;
    EditText Name,Email,Password;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference("signup");

        signin_button=findViewById(R.id.sign_in_button);
        Name=findViewById(R.id.username);
        Email=findViewById(R.id.email);
        Password=findViewById(R.id.password);

        register=findViewById(R.id.Register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                checkConnection();
            }
        });

        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void checkConnection(){
        ConnectivityManager manager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork= manager.getActiveNetworkInfo();
        if (null==activeNetwork){
            Intent intent=new Intent(RegisterActivity.this,network_indicator.class);
            intent.putExtra("from","RegisterActivity");
            startActivity(intent);
            finish();
        }else{

            if(Name.length()==0) {
                Name.setError("Enter Name!");
            } else if(Email.length()==0) {
                Email.setError("Enter Email!");
            }else {
                //Firebase Authentication code starts
                String email =Email.getText().toString();
                String password=Password.getText().toString();
                String name=Name.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    register.setVisibility(View.INVISIBLE);
                                    final LottieAnimationView lottiePreloader=findViewById(R.id.preloader_button);

                                    lottiePreloader.setVisibility(View.VISIBLE);
                                    lottiePreloader.setSpeed(1);
                                    lottiePreloader.playAnimation();

                                    Thread timer = new Thread() {
                                        public void run() {
                                            try {
                                                sleep(3000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            } finally {
                                                Intent intent = new Intent(RegisterActivity.this,RegisterFormActivity.class);
                                                intent.putExtra("Name", name);
                                                intent.putExtra("Mail", email);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    };
                                    timer.start();

                                } else {
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                //code ends
            }
          /*  */
        }
    }
}