package com.peac.cock.peacock_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class EmailJoinActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private EditText join_editTextEmail;
    private EditText join_editTextPassword;
    private EditText join_editTextPasswordOk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        join_editTextEmail = findViewById(R.id.join_layout_text_email);
        join_editTextPassword = findViewById(R.id.join_layout_text_password);
        join_editTextPasswordOk = findViewById(R.id.join_layout_text_passwordOk);
        ImageButton joinOkButton = findViewById(R.id.join_layout_imageButton_joinOkNext_button);

        joinOkButton.setOnClickListener(v -> {
            if (join_editTextEmail.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "Email을 입력하세요!", Toast.LENGTH_SHORT).show();
                join_editTextEmail.requestFocus();
                return;
            }
            if (join_editTextPassword.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "Password를 입력하세요!", Toast.LENGTH_SHORT).show();
                join_editTextPassword.requestFocus();
                return;
            }
            if (join_editTextPasswordOk.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "Password를 입력하세요!", Toast.LENGTH_SHORT).show();
                join_editTextPasswordOk.requestFocus();
                return;
            }
            if (!join_editTextPassword.getText().toString().equals(join_editTextPasswordOk.getText().toString())) {
                Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다!!", Toast.LENGTH_SHORT).show();
                join_editTextPassword.setText("");
                join_editTextPasswordOk.setText("");
                join_editTextPassword.requestFocus();
                return;
            }

            createUser(join_editTextEmail.getText().toString(), join_editTextPassword.getText().toString());
        });

    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(EmailJoinActivity.this, "회원인증실패!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EmailJoinActivity.this, "회원인증성공", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MemberInfoActivity.class);
                        intent.putExtra("userEmail",join_editTextEmail.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                });
    }

}
