package com.hermit.starreg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


   // private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private Button loginbutton,phoneloginbutton;
    private EditText useremail,userpassword;
    private TextView neednewaccountlink,forgetpasswordlink;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();

       // currentUser = mAuth.getCurrentUser();


        initializemethod();

        neednewaccountlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendUserToRegisterActivity();
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                allowUsertoLogin();


            }
        });
    }

    private void allowUsertoLogin() {

        String email = useremail.getText().toString();
        String password = userpassword.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter Email ID...", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter Password...", Toast.LENGTH_SHORT).show();
        }else{


            loadingBar.setTitle("Signing In");

            loadingBar.setMessage("Please Wait...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        sendUserToMainActivity();
                        Toast.makeText(LoginActivity.this, "Logged In Successfull.....", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }else{

                        String errormessage = task.getException().toString();

                        Toast.makeText(LoginActivity.this, "Error ..."+errormessage, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                }
            });

        }
    }



    private void initializemethod() {

        loginbutton = findViewById(R.id.login);
        phoneloginbutton = findViewById(R.id.phone_number_insert);

        useremail = findViewById(R.id.email);
        userpassword = findViewById(R.id.password);
        neednewaccountlink = findViewById(R.id.register);
        forgetpasswordlink = findViewById(R.id.foregt_password);

        loadingBar = new ProgressDialog(this);



    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        if(currentUser!=null){
//            SendUserToMainActivity();
//
//        }
//    }

    private void sendUserToMainActivity() {

        Intent mainactivity = new Intent(LoginActivity.this,MainActivity.class);
        mainactivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(mainactivity);
        finish();
    }

    private void SendUserToRegisterActivity() {

        Intent registerintent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerintent);
    }

}
