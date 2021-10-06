package com.example.educor_app.Authentications;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.educor_app.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.*;


public class SendOTP extends AppCompatActivity {

    EditText editTextPhone;
    Button buttonContinue;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    public static  String Phone;
    public ImageView send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_o_t_p);

        editTextPhone = findViewById(R.id.inputMobile);
        buttonContinue = findViewById(R.id.getOTP);
        send=findViewById(R.id.send);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("signup").child(user.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   // Phone = (Objects.<Object>requireNonNull(dataSnapshot.child("MobileNumber").getValue()).toString());

                    if(dataSnapshot.exists()) {

                        Phone = (Objects.requireNonNull(dataSnapshot.child("MobileNumber").getValue()).toString());
                    }else {
                        Toast.makeText(SendOTP.this,"Data doesnt exists",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkConnection();
               /* */
            }
        });
    }
    public void checkConnection(){
        ConnectivityManager manager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork= manager.getActiveNetworkInfo();
        if (null==activeNetwork){
            Intent intent=new Intent(SendOTP.this,network_indicator.class);
            intent.putExtra("from","SendOTP");
            startActivity(intent);
            finish();
        }else{
            String code = "+91";
            String number = editTextPhone.getText().toString().trim();

            if (number.isEmpty() || number.length() < 10) {
                editTextPhone.setError("Valid number is required");
                editTextPhone.requestFocus();
                return;
            }
            String phoneNumber = code + number;

            if (Phone.equals(number)){


                final LottieAnimationView lottiePreloader=findViewById(R.id.lottie_send);

                send.setVisibility(View.INVISIBLE);
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
                            Intent intent = new Intent(SendOTP.this, VerifyOTP.class);
                            intent.putExtra("phoneNumber", phoneNumber);
                            startActivity(intent);
                            finish();
                        }
                    }
                };
                timer.start();

               /* */
            }else{
                Toast.makeText(SendOTP.this,"Mobile number you have entered currently doesn't match with your registered mobile number!",Toast.LENGTH_LONG).show();
            }
        }
    }
}