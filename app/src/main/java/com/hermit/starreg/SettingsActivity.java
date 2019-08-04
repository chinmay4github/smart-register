package com.hermit.starreg;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {


    private Button updateAccountSettings;
    private EditText username, userstatus;
    private CircleImageView userprofileimage;
    private String currentuserUID;
    private FirebaseAuth mAuth;
    private DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        currentuserUID = mAuth.getCurrentUser().getUid();
        mref = FirebaseDatabase.getInstance().getReference();


        initializeids();

        updateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSettings();

            }
        });

        retrieveUserInfo();

        username.setVisibility(View.INVISIBLE);


    }


    private void UpdateSettings() {

        String setusername = username.getText().toString();
        String setuserstatus = userstatus.getText().toString();

        if (TextUtils.isEmpty(setusername)) {
            Toast.makeText(this, "Please Write Your UserName", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(setuserstatus)) {
            Toast.makeText(this, "Please Write Your UserStatus..", Toast.LENGTH_SHORT).show();


        } else {
            HashMap<String, String> profilemap = new HashMap<>();
            profilemap.put("uid", currentuserUID);
            profilemap.put("name", setusername);
            profilemap.put("status", setuserstatus);

            mref.child("Users").child(currentuserUID).setValue(profilemap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        SendUsertoMainActivity();
                        Toast.makeText(SettingsActivity.this, "Profile Updated Successfully..", Toast.LENGTH_SHORT).show();
                    } else {
                        String error_mmessage = task.getException().toString();
                        Toast.makeText(SettingsActivity.this, "Error " + error_mmessage, Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }

    private void SendUsertoMainActivity() {

        Intent sendtomainactivity = new Intent(SettingsActivity.this, MainActivity.class);
        sendtomainactivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(sendtomainactivity);

    }

    private void initializeids() {

        username = findViewById(R.id.user_name);
        userstatus = findViewById(R.id.user_status);
        updateAccountSettings = findViewById(R.id.update_profile);
        userprofileimage = findViewById(R.id.profile_image);
    }

    private void retrieveUserInfo() {

        mref.child("Users").child(currentuserUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists() && dataSnapshot.hasChild("name") && dataSnapshot.hasChild("image") ){

                    String retrieveusername = dataSnapshot.child("name").getValue().toString();
                    String retrieveuserStatus = dataSnapshot.child("status").getValue().toString();
                    String retrieveProfileImage = dataSnapshot.child("image").getValue().toString();

                    username.setText(retrieveusername);
                    userstatus.setText(retrieveuserStatus);



                }else if(dataSnapshot.exists() && dataSnapshot.hasChild("name")){
                    String retrieveusername = dataSnapshot.child("name").getValue().toString();
                    String retrieveuserStatus = dataSnapshot.child("status").getValue().toString();

                    username.setText(retrieveusername);
                    userstatus.setText(retrieveuserStatus);

                }else{
                    Toast.makeText(SettingsActivity.this, "Please Update Your Profile Information", Toast.LENGTH_SHORT).show();
                    username.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
