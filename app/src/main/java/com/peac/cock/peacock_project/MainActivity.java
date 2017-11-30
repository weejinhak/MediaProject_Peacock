package com.peac.cock.peacock_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.view.KeyEvent;
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
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peac.cock.peacock_project.projectAdapter.BackPressCloseHandler;
import com.peac.cock.peacock_project.projectDto.LedgerDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView nameTextView;
    private TextView emailTextView;
    private FirebaseAuth auth;
    private Intent intent;
    private WebView mWebView;
    private BackPressCloseHandler backPressCloseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        backPressCloseHandler = new BackPressCloseHandler(this);

        intent = new Intent();

        //fire base Auth && database
        auth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        //Toolbar setting
        Toolbar toolbar = findViewById(R.id.app_bar_layout_my_toolbar);
        setSupportActionBar(toolbar);

        // get ID
        ImageButton assetGoButton = findViewById(R.id.main_layout_asset_go_button);
        ImageButton analysisGoButton = findViewById(R.id.main_layout_analysis_go_button);
        ImageButton detailGoButton = findViewById(R.id.main_layout_breakdown_go_button);
        ImageButton settingGoButton = findViewById(R.id.main_layout_setting_go_button);

        //webView
        mWebView = findViewById(R.id.main_webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("http://www.podbbang.com/ch/14295");
        mWebView.setWebViewClient(new WebViewClientClass());
        mWebView.setVerticalScrollBarEnabled(true);

        detailGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.setClass(getApplicationContext(), DetailTabActivity.class);
                startActivity(intent);
                finish();
            }
        });

        assetGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.setClass(getApplicationContext(), AssetActivity.class);
                startActivity(intent);
                finish();
            }
        });
        settingGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });
        analysisGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getApplicationContext(), AnalysisActivity.class);
                startActivity(intent);
                finish();
            }
        });

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
            finish();
        }/*else if(id == R.id.nav_setting){
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }*/
        //navigation 수정하기
        if (id == R.id.nav_asset) {
            Intent intent = new Intent(this, AssetActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_list) {
            Intent intent = new Intent(this, DetailTabActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_analysis) {
            Intent intent = new Intent(this, AnalysisActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_recommed) {
            Intent intent = new Intent(this, RecommendationActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_logout) {
            auth.signOut();
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

}
