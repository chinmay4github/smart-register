package com.hermit.starreg;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference mref = database.getReference("message");

    private FirebaseUser currentuser;

    private FirebaseAuth mAuth;
    private DatabaseReference mref;

    private Button submit;
    private EditText email,password;

    private ViewPager viewPager;

    private TabLayout tabLayout;

    private Toolbar mtoolbar;
    private TabAccesorAdapter tabAccesorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser();
        mref = FirebaseDatabase.getInstance().getReference();

        mtoolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("StarReg");


        viewPager = findViewById(R.id.main_tabs_pager);
        tabAccesorAdapter = new TabAccesorAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAccesorAdapter);

        tabLayout = findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(viewPager);


    }






    @Override
    protected void onStart() {
        super.onStart();

        if(currentuser==null){

            SendUserToLoginActivity();

        }
        else{

            VerifyUserExistance();

        }
    }

    private void VerifyUserExistance() {

        String currentUserID = mAuth.getCurrentUser().getUid();
        mref.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("name").exists()){

                    Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();

                }else{
                    SendUserToSettingsActivity();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId()==R.id.main_logout_option){
            mAuth.signOut();
            SendUserToLoginActivity();

        }
        if(item.getItemId()==R.id.main_settings_option){

            SendUserToSettingsActivity();


        }
        if(item.getItemId()==R.id.main_find_friends_option){

        }

        if(item.getItemId()==R.id.main_create_groups_option){

            RequestNewGroup();
        }

        return true;
    }

    private void RequestNewGroup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.AlertDialog);
        builder.setTitle("Enter Group Name :");
        final EditText groupNameField = new EditText(MainActivity.this);
        groupNameField.setHint("e.g Chinmay Groups");
        builder.setView(groupNameField);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String groupName = groupNameField.getText().toString();
                if(TextUtils.isEmpty(groupName)){
                    Toast.makeText(MainActivity.this, "Please Enter Your group", Toast.LENGTH_SHORT).show();
                }else{

                    CreateNewGroup(groupName);

                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.show();
    }

    private void CreateNewGroup(final String groupName) {
        mref.child("Groups").child(groupName).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, ""+groupName+" is created...Successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    private void SendUserToLoginActivity() {

        Intent loginintent = new Intent(MainActivity.this,LoginActivity.class);
        loginintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginintent);
        finish();

    }


    private void SendUserToSettingsActivity() {

        Intent settingsintent = new Intent(MainActivity.this,SettingsActivity.class);
        settingsintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingsintent);
        finish();

    }
}
