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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {


    private EditText register_user_email,register_user_password;
    private Button create_account_button;
    private TextView alreadyhaveaccount,forget_password;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();


        initializemethod();
        alreadyhaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToLoginActivity();
            }
        });

        create_account_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();

            }
        });

    }

    private void createNewAccount() {

        String email = register_user_email.getText().toString().trim();
        String password = register_user_password.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter Email ID...", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter Password...", Toast.LENGTH_SHORT).show();
        }else{

            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait while we create an acoount for you.....");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            
                            if(task.isSuccessful()){
                                //sendUserToLoginActivity();


                                String currentUserId = mAuth.getCurrentUser().getUid();
                                reference.child("Users").child(currentUserId).setValue("");
                                sendUserToMainActivity();
                                Toast.makeText(RegisterActivity.this, "Account Created Succesfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }else{
                                String message = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Account Creation Failed.. "+message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }

                        }
                    });
        }
    }

    private void sendUserToLoginActivity() {

        Intent loginactivity = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(loginactivity);
    }

    private void sendUserToMainActivity() {

        Intent mainactivity = new Intent(RegisterActivity.this,MainActivity.class);
        mainactivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(mainactivity);
        finish();
    }

    private void initializemethod() {

        register_user_email = findViewById(R.id.register_email);
        register_user_password = findViewById(R.id.register_password);
        create_account_button = findViewById(R.id.register_button);

        alreadyhaveaccount = findViewById(R.id.already_registered);
        forget_password = findViewById(R.id.foregt_password);

        loadingBar = new ProgressDialog(this);




    }
}
