package com.example.carparkdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {



    private EditText emailt;
    private EditText passwordt;
    private Button logint;
    private TextView failt;
    private int counter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//
        emailt = (EditText)findViewById(R.id.email);
        passwordt = (EditText)findViewById(R.id.password);
        logint = (Button)findViewById(R.id.btnLogin);
        failt = (TextView) findViewById(R.id.failView);
        failt.setEnabled(false);
        logint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { validate(emailt.getText().toString(), passwordt.getText().toString());
            }
        });
    }

    private void validate(String userName, String userPassword)
    {
        if((userName.equals("admin")) && (userPassword.equals("password")))
        {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else
        {
            failt.setText("Login Failed");
        }

    }
}
