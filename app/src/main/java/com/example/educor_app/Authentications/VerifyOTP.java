package com.example.educor_app.Authentications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.educor_app.Classroom.Classrooms;
import com.example.educor_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {

    private EditText inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6;
    private String verificationId;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    public static String otp;
    public TextView Resend_OTP;
    ProgressBar progressBar;
    public static Button buttonVerify;
    public ImageView lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);

        progressBar = findViewById(R.id.progressBar);
        buttonVerify = findViewById(R.id.buttonVerify);
        lock=findViewById(R.id.lock);

         Resend_OTP=findViewById(R.id.textResendOTP);

        inputCode1 = findViewById(R.id.inputCode1);
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);
        inputCode5 = findViewById(R.id.inputCode5);
        inputCode6 = findViewById(R.id.inputCode6);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        String phoneNumber = getIntent().getStringExtra("phoneNumber");

        sendVerificationCode(phoneNumber);
        // save phone number
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("USER_PREF",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("phoneNumber", phoneNumber);
        editor.apply();

        TextView textMobile = findViewById(R.id.textMobile);
        textMobile.setText(phoneNumber);

        setupOTPInputs();

        Resend_OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode(phoneNumber);
                setupOTPInputs();
            }
        });

        //verificationId=getIntent().getStringExtra("verificationId").toString();
        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkConnection();
              /*  if (inputCode1.getText().toString().isEmpty() ||
                        inputCode2.getText().toString().isEmpty() ||
                        inputCode3.getText().toString().isEmpty() ||
                        inputCode4.getText().toString().isEmpty() ||
                        inputCode5.getText().toString().isEmpty() ||
                        inputCode6.getText().toString().isEmpty()) {
                    Toast.makeText(VerifyOTP.this, "Please enter valid code", Toast.LENGTH_SHORT).show();
                    return;
                }

                otp = inputCode1.getText().toString() +
                        inputCode2.getText().toString() +
                        inputCode3.getText().toString() +
                        inputCode4.getText().toString() +
                        inputCode5.getText().toString() +
                        inputCode6.getText().toString();

                verifyCode(otp); */
            }
        });

    }



    private void verifyCode(String otp) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);

            lock.setVisibility(View.INVISIBLE);
            final LottieAnimationView lottiePreloader=findViewById(R.id.lottie_verify);

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
                        Intent intent = new Intent(VerifyOTP.this, Classrooms.class);
                        startActivity(intent);
                        finish();
                    }
                }
            };
            timer.start();

        } catch (Exception e) {
            Toast.makeText(VerifyOTP.this, "Invalid OTP", Toast.LENGTH_LONG).show();
        }


       // signInWithCredential(credential);
       /* if (otp.equals(code)){
            Intent intent = new Intent(VerifyOTP.this, Dashboard.class);
            startActivity(intent);
        }else {
            Toast.makeText(VerifyOTP.this, "Invalid OTP", Toast.LENGTH_LONG).show();
        }*/
    }


    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(VerifyOTP.this, Classrooms.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);

                        } else {
                            Toast.makeText(VerifyOTP.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }


    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                VerifyOTP.this,
                mCallBack
        );

        progressBar.setVisibility(View.GONE);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            otp = phoneAuthCredential.getSmsCode();
            if (otp != null) {
                //editText.setText(code);
                verifyCode(otp);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyOTP.this, e.getMessage(), Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
    };
    private void setupOTPInputs(){
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if (!s.toString().trim().isEmpty()){
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if (!s.toString().trim().isEmpty()){
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if (!s.toString().trim().isEmpty()){
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if (!s.toString().trim().isEmpty()){
                    inputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if (!s.toString().trim().isEmpty()){
                    inputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void checkConnection(){
        ConnectivityManager manager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork= manager.getActiveNetworkInfo();
        if (null==activeNetwork){
            Intent intent=new Intent(VerifyOTP.this,network_indicator.class);
            intent.putExtra("from","VerifyOTP");
            startActivity(intent);
            finish();
        }else {
            if (inputCode1.getText().toString().isEmpty() ||
                    inputCode2.getText().toString().isEmpty() ||
                    inputCode3.getText().toString().isEmpty() ||
                    inputCode4.getText().toString().isEmpty() ||
                    inputCode5.getText().toString().isEmpty() ||
                    inputCode6.getText().toString().isEmpty()) {
                Toast.makeText(VerifyOTP.this, "Please enter valid code", Toast.LENGTH_SHORT).show();
                return;
            }

          /*  */

            otp = inputCode1.getText().toString() +
                    inputCode2.getText().toString() +
                    inputCode3.getText().toString() +
                    inputCode4.getText().toString() +
                    inputCode5.getText().toString() +
                    inputCode6.getText().toString();

            verifyCode(otp);
        }
    }
}