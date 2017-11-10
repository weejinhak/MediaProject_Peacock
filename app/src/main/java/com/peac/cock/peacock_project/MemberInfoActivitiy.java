package com.peac.cock.peacock_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.peac.cock.peacock_project.projectDto.UserDto;

/**
 * Created by wee on 2017. 11. 9..
 */

public class MemberInfoActivitiy extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private UserDto userDto =new UserDto();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberinfo);

        //firebase && database
        mAuth=FirebaseAuth.getInstance();

        //get button && EditText ID
        final EditText mName=findViewById(R.id.memberinfo_layout_editText_name);
        final EditText mBirthday=findViewById(R.id.memberinfo_layout_editText_birthday);
        final EditText mGender=findViewById(R.id.memberinfo_layout_editText_gender);
        final EditText mJob=findViewById(R.id.memberinfo_layout_editText_job);
        final EditText mMonthBudget=findViewById(R.id.memberinfo_layout_editText_monthBudget);
        ImageButton join_ok_button=findViewById(R.id.memberinfo_layout_joinOk_imgButton);

        join_ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDto.setEmail(mAuth.getCurrentUser().getEmail().toString());
                userDto.setBirthday(mBirthday.getText().toString());
                userDto.setName(mName.getText().toString());
                userDto.setGender(mGender.getText().toString());
                userDto.setJob(mJob.getText().toString());
                userDto.setBudget(mMonthBudget.getText().toString());
                Toast.makeText(getApplicationContext(),"정보입력완료",Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("userInfo", userDto);
                startActivity(intent);
                finish();
            }
        });

    }
}
