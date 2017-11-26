package com.peac.cock.peacock_project;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peac.cock.peacock_project.projectDto.LedgerDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView nameTextView;
    private TextView emailTextView;

    private FirebaseAuth auth;

    private Intent intent;

    private FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        intent = new Intent();

        //fire base Auth && database
        auth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String uid = auth.getCurrentUser().getUid();

        //Toolbar setting
        Toolbar toolbar = findViewById(R.id.app_bar_layout_my_toolbar);
        setSupportActionBar(toolbar);

        // get ID
        ImageButton assetGoButton = findViewById(R.id.main_layout_asset_go_button);
        ImageButton analysisGoButton = findViewById(R.id.main_layout_analysis_go_button);
        ImageButton detailGoButton = findViewById(R.id.main_layout_breakdown_go_button);
        ImageButton settingGoButton = findViewById(R.id.main_layout_setting_go_button);

        fragmentTransaction = getFragmentManager().beginTransaction();

        detailGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   fragmentTransaction.replace(R.id.main_layout_content, new DetailTabActivity());
                intent.setClass(getApplicationContext(), DetailTabActivity.class);
                startActivity(intent);
            }
        });

        assetGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.main_layout_content, new AssetActivity());
            }
        });
        settingGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.replace(R.id.main_layout_content, new SettingActivity());
            }
        });
        analysisGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.replace(R.id.main_layout_content, new AnalysisActivity());
            }
        });
        fragmentTransaction.commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);

        nameTextView = view.findViewById(R.id.header_name_textView);
        emailTextView = view.findViewById(R.id.header_email_textView);

        nameTextView.setText(auth.getCurrentUser().getDisplayName());
        emailTextView.setText(auth.getCurrentUser().getEmail());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            auth.signOut();
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }/*else if(id == R.id.nav_setting){
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }*/
       /* if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout){
            auth.signOut();
            finish();
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
        }*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

   /* @Override
    public void onBackPressed() {
        if ( pressedTime == 0 ) {
            Toast.makeText(MainActivity.this, " 한 번 더 누르면 종료됩니다." , Toast.LENGTH_LONG).show();
            pressedTime = System.currentTimeMillis();
        }
        else {
            int seconds = (int) (System.currentTimeMillis() - pressedTime);

            if ( seconds > 2000 ) {
                Toast.makeText(MainActivity.this, " 한 번 더 누르면 종료됩니다." , Toast.LENGTH_LONG).show();
                pressedTime = 0 ;
            }
            else {
                super.onBackPressed();
//                finish(); // app 종료 시키기
            }
        }
    }*/

    /*public void callDetailData() {

        databaseReference = database.getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LedgerDto ledger;
                dataSnapshot.getValue();
                for(DataSnapshot fileSnapshot : dataSnapshot.child("users").child(uid).child("ledger").getChildren()) {
                    ledger = fileSnapshot.getValue(LedgerDto.class);

                    ledgers.add(ledger);

                    boolean check = true;

                    if(ledger.getInOut().equals("지출")) {
                        if(data != null) {
                            for (Map.Entry<String, String> d : data.entrySet()) {
                                if (d.getKey().equals(ledger.getCategory())) {
                                    d.setValue(String.valueOf(Integer.parseInt(d.getValue()) + (Integer.parseInt(ledger.getAmount()))));
                                    check = false;
                                    break;
                                }
                            }
                        }

                        if (check) {
                            data.put(ledger.getCategory(), ledger.getAmount());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }*/


}
