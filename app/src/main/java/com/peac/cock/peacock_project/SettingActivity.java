package com.peac.cock.peacock_project;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by wee on 2017. 11. 14..
 */

public class SettingActivity extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private String userEmail;

    private TextView categoryGoTextView;
    private TextView emailTextView;
    private TextView userNameTextView;

    public SettingActivity() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_setting, null);

        categoryGoTextView = view.findViewById(R.id.setting_layout_textView_categoryadd);
        emailTextView = view.findViewById(R.id.setting_layout_textView_userEmail);
        userNameTextView = view.findViewById(R.id.setting_layout_textView_userName);


        //get DataBase
        DatabaseReference databaseReference = mDatabase.getReference();
        //database get email && add ArrayList
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getValue();
                String name= dataSnapshot.child("users").child(mAuth.getCurrentUser().getUid()).child("name").getValue(String.class);
                emailTextView.setText(mAuth.getCurrentUser().getEmail());
                userNameTextView.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        categoryGoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity().getApplicationContext(),CategoryActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth=FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance();

    }
}
