package com.example.expensestracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {


    private EditText userEmailEdit_Text;
    private EditText userPasswordEdit_Text;
    private TextView notAccountText_View;
    private Button userLoginButton;
    private FirebaseAuth auth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instantiate auth reference object
        auth=FirebaseAuth.getInstance();

        //Get View Email editTextTextEmailAddress
        userEmailEdit_Text = (EditText) findViewById(R.id.editTextTextEmailAddress);

        //Get view Password editTextTextPassword
        userPasswordEdit_Text = (EditText) findViewById(R.id.editTextTextPassword);

        //Get view Login userLoginButton
        userLoginButton = (Button) findViewById(R.id.loginButton);
        userLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emai_txt = userEmailEdit_Text.getText().toString();
                String password_txt = userPasswordEdit_Text.getText().toString();

                login(emai_txt,password_txt);
            }
        });

        //Get view NotAccount signUpTextView
        notAccountText_View = (TextView) findViewById(R.id.signUpTextView);
        notAccountText_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =SignUpActivity.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });

    }

    private void login(String email, String password)
    {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this,"Login Successfully",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,HomeActivity.class));
                }
                else {
                    Toast.makeText(MainActivity.this,"Login failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}