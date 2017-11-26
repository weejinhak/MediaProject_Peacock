package com.peac.cock.peacock_project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.peac.cock.peacock_project.projectDto.UserDto;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by wee on 2017. 11. 9..
 */

public class MemberInfoActivitiy extends AppCompatActivity {

    private FirebaseAuth mAuth;
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
    private ImageButton join_ok_button;

    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_memberinfo);

        //firebase && database
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        //get button && EditText ID
        mName = findViewById(R.id.memberInfo_layout_editText_name);
        mBirthday = findViewById(R.id.memberInfo_layout_button_birthday);
        mGender = findViewById(R.id.memberInfo_layout_spinner_gender);
        mJob = findViewById(R.id.memberInfo_layout_editText_job);
        mMonthBudget = findViewById(R.id.memberInfo_layout_editText_monthBudget);
        join_ok_button = findViewById(R.id.memberInfo_layout_joinOk_imgButton);
        uid = mAuth.getCurrentUser().getUid();

        //before_intent_get
        intent = getIntent();
        final String email = mAuth.getCurrentUser().getEmail();

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mBirthday.setText(dateFormat.format(dateTime.getTime()).toString());

        mBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDate();
            }
        });


        //정보입력버튼 클릭.
        join_ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validation Check
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
                userDto.setGender(mGender.getSelectedItem().toString().replace("\\s",""));
                userDto.setJob(mJob.getText().toString());
                userDto.setBudget(mMonthBudget.getText().toString()+"0000");

                //uid에 맞는 정보 디비 입력
                database.getReference().child("users").child(uid).setValue(userDto);
                Toast.makeText(getApplicationContext(), "정보입력완료", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
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

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateTime.set(Calendar.YEAR, year);
            dateTime.set(Calendar.MONTH, month);
            dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateBirthdayText();
        }
    };

    private void updateDate() {
        new DatePickerDialog(this, d, dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void updateBirthdayText() {
        mBirthday.setText(dateFormat.format(dateTime.getTime()).toString());
    }
}
