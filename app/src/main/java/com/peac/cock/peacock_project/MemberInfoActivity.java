package com.peac.cock.peacock_project;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.peac.cock.peacock_project.projectDto.UserDto;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MemberInfoActivity extends AppCompatActivity {

    private UserDto userDto = new UserDto();
    private FirebaseDatabase database;
    private String uid;

    private Calendar dateTime = Calendar.getInstance();
    private SimpleDateFormat dateFormat;

    private EditText mName;
    private Button mBirthday;
    private Spinner mGender;
    private EditText mJob;
    private EditText mMonthBudget;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_memberinfo);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        mName = findViewById(R.id.memberInfo_layout_editText_name);
        mBirthday = findViewById(R.id.memberInfo_layout_button_birthday);
        mGender = findViewById(R.id.memberInfo_layout_spinner_gender);
        mJob = findViewById(R.id.memberInfo_layout_editText_job);
        mMonthBudget = findViewById(R.id.memberInfo_layout_editText_monthBudget);
        final ImageButton join_ok_button = findViewById(R.id.memberInfo_layout_joinOk_imgButton);
        uid = mAuth.getCurrentUser().getUid();

        final String email = mAuth.getCurrentUser().getEmail();

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mBirthday.setText(dateFormat.format(dateTime.getTime()));

        mBirthday.setOnClickListener(view -> updateDate());

        mGender.setSelection(0);

        join_ok_button.setOnClickListener(v -> {
            if (mName.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "이름을 입력하세요!", Toast.LENGTH_SHORT).show();
                mName.requestFocus();
                return;
            }
            if (mBirthday.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "생일을 입력하세요!", Toast.LENGTH_SHORT).show();
                mBirthday.requestFocus();
                return;
            }
            if (mGender.getSelectedItem().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "성별을 입력하세요!", Toast.LENGTH_SHORT).show();
                mGender.requestFocus();
                return;
            }
            if (mJob.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "직업을 입력하세요!", Toast.LENGTH_SHORT).show();
                mJob.requestFocus();
                return;
            }
            if (mMonthBudget.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), "월예산을 입력하세요!", Toast.LENGTH_SHORT).show();
                mMonthBudget.requestFocus();
                return;
            }

            userDto.setEmail(email);
            userDto.setBirthday(mBirthday.getText().toString());
            userDto.setName(mName.getText().toString());
            userDto.setGender(mGender.getSelectedItem().toString().replaceAll("\\p{Space}",""));
            userDto.setJob(mJob.getText().toString());
            userDto.setBudget(mMonthBudget.getText().toString()+"0000");

            database.getReference().child("users").child(uid).setValue(userDto);
            Toast.makeText(getApplicationContext(), "정보입력완료", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });

        mGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mGender.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });



    }

    DatePickerDialog.OnDateSetListener d = (view, year, month, dayOfMonth) -> {
        dateTime.set(Calendar.YEAR, year);
        dateTime.set(Calendar.MONTH, month);
        dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateBirthdayText();
    };

    private void updateDate() {
        new DatePickerDialog(this, d, dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void updateBirthdayText() {
        mBirthday.setText(dateFormat.format(dateTime.getTime()));
    }
}
