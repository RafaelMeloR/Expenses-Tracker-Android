package com.example.expensestracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    private EditText userEmailEdit_Text;
    private EditText userPasswordEdit_Text;
    private TextView alreadyAccountText_View;
    private Button userSignUpButton;
    private FirebaseAuth auth;


    public static Intent newIntent(Context packageContext)
    {
        Intent intent = new Intent(packageContext, SignUpActivity.class);
        return  intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Instantiate auth reference object
        auth=FirebaseAuth.getInstance();

        //Get View Email editTextTextEmailAddress
        userEmailEdit_Text = (EditText) findViewById(R.id.editTextTextEmailAddress_SignUp);

        //Get view Password editTextTextPassword
        userPasswordEdit_Text = (EditText) findViewById(R.id.editTextTextPassword_SignUp);

        //Get view alreadyAccountText_View
        alreadyAccountText_View = (TextView) findViewById(R.id.sigInTextView);
        alreadyAccountText_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Get The view SignUpButton
        userSignUpButton =(Button) findViewById(R.id.signUpButton);
        userSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_text = userEmailEdit_Text.getText().toString();
                String password_text = userPasswordEdit_Text.getText().toString();

                if(TextUtils.isEmpty(email_text) || TextUtils.isEmpty(password_text))
                {
                    Toast.makeText(SignUpActivity.this,"Empty credentials",Toast.LENGTH_SHORT).show();
                } else if (password_text.length()<6) {
                    Toast.makeText(SignUpActivity.this,"Password too short",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    register(email_text,password_text);
                }
            }
        });
    }
    private void register(String email, String password)
    {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignUpActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SignUpActivity.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                        }
                        else {
                            Toast.makeText(SignUpActivity.this,"Register failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}