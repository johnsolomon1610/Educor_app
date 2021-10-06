package com.example.educor_app.Authentications;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.educor_app.Events.Event_page;
import com.example.educor_app.Notifications.Notification;
import com.example.educor_app.R;
import com.example.educor_app.chats.ui.activities.UserListingActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    private TextView t1,t2,t3,t4,t5,t6;
    private CircleImageView circle;
    private ImageView coverpic;
    private static final int PICK_IMAGE = 1;
    private static final String TAG ="ProfileActivity";
    public Uri imageUri;
    public Uri resultUri;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef,databaseReference,profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        circle =findViewById(R.id.circle);
        coverpic=findViewById(R.id.coverpic);
        t1=findViewById(R.id.name);
        t3=findViewById(R.id.email2);
        t4=findViewById(R.id.phone_number);
        t5=findViewById(R.id.institution);
        t6=findViewById(R.id.qualification);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationview);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        String Classname = getIntent().getStringExtra("name");
        String key = getIntent().getStringExtra("key");
        String Teacher=getIntent().getStringExtra("Teacher");

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        return true;
                    case R.id.homepage:
                        Intent intent=new Intent(ProfileActivity.this,Dashboard.class);
                        intent.putExtra("name",Classname);
                        intent.putExtra("Teacher",Teacher);
                        intent.putExtra("key",key);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.chat:
                        Intent lntent=new Intent(ProfileActivity.this, UserListingActivity.class);
                        lntent.putExtra("name",Classname);
                        lntent.putExtra("Teacher", Teacher);
                        lntent.putExtra("key", key);
                        startActivity(lntent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.Schedule:
                        Intent lntents=new Intent(ProfileActivity.this, Event_page.class);
                        lntents.putExtra("name",Classname);
                        lntents.putExtra("Teacher", Teacher);
                        lntents.putExtra("key", key);
                        startActivity(lntents);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.notification:
                        Intent lntentss=new Intent(ProfileActivity.this, Notification.class);
                        lntentss.putExtra("key", key);
                        lntentss.putExtra("name",Classname);
                        lntentss.putExtra("Teacher", Teacher);
                        startActivity(lntentss);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mAuth = FirebaseAuth.getInstance();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .into(circle);

            }
        }

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .into(coverpic);

            }
        }

        databaseReference= FirebaseDatabase.getInstance().getReference().child("signup").child(user.getUid());
        profile=FirebaseDatabase.getInstance().getReference().child("profile").child("images");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String Name= Objects.requireNonNull(dataSnapshot.child("Username").getValue()).toString();
                String Email2= Objects.requireNonNull(dataSnapshot.child("Email").getValue()).toString();
                String Phone_number= Objects.requireNonNull(dataSnapshot.child("MobileNumber").getValue()).toString();
                String Institution= Objects.requireNonNull(dataSnapshot.child("institution").getValue()).toString();
                String Qualification= Objects.requireNonNull(dataSnapshot.child("qualification").getValue()).toString();

                t1.setText(Name);
                t3.setText(Email2);
                t4.setText(Phone_number);
                t5.setText(Institution);
                t6.setText(Qualification);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery, "select picture"), PICK_IMAGE);

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode  == RESULT_OK && data != null) {

            imageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            resultUri = result.getUri();

            if(resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    circle.setImageBitmap(bitmap);
                    handleUpload(bitmap);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void handleUpload(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final StorageReference reference = FirebaseStorage.getInstance().getReference()

                .child("profileImages")
                .child( ""+ ".jpeg");

        reference.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getDownloadUrl(reference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ",e.getCause() );
                    }
                });
    }

    private void getDownloadUrl(StorageReference reference) {
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "onSuccess: " + uri);
                        setUserProfileUrl(uri);
                    }
                });
    }

    private void setUserProfileUrl(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ProfileActivity.this, "Updated succesfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, "Profile image failed...", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void Signout(View v){
        PopupMenu popupMenu=new PopupMenu(this,v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.profile_menu);
        popupMenu.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("remember", "false");
        editor.apply();

        Intent intent=new Intent(ProfileActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
}