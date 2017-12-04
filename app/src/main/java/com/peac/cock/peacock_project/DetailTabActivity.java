package com.peac.cock.peacock_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.peac.cock.peacock_project.projectAdapter.BackPressCloseHandler;

public class DetailTabActivity extends AppCompatActivity {

    protected Intent intent;
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tab_detail);

        backPressCloseHandler = new BackPressCloseHandler(this);

        intent = new Intent();

        final SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        final ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        final FloatingActionButton fab = findViewById(R.id.main_layout_plus_button);
        final ImageButton assetGoButton = findViewById(R.id.main_layout_asset_go_button);
        final ImageButton analysisGoButton = findViewById(R.id.main_layout_analysis_go_button);
        final ImageButton detailGoButton = findViewById(R.id.main_layout_breakdown_go_button);
        final ImageButton settingGoButton = findViewById(R.id.main_layout_setting_go_button);

        fab.setOnClickListener(view -> {
            intent.setClass(getApplicationContext(), CalculatorActivity.class);
            startActivity(intent);
        });

        detailGoButton.setOnClickListener(view -> {
            intent.setClass(getApplicationContext(), DetailTabActivity.class);
            startActivity(intent);
        });

        assetGoButton.setOnClickListener(view -> {
            intent.setClass(getApplicationContext(), AssetActivity.class);
            startActivity(intent);
        });
        settingGoButton.setOnClickListener(view -> {
            intent.setClass(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
        });
        analysisGoButton.setOnClickListener(view -> {
            intent.setClass(getApplicationContext(), AnalysisActivity.class);
            startActivity(intent);
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Tab1_content();
                case 1:
                    return new Tab2_content();

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle (int position) {
            switch (position) {
                case 0:
                    return "리스트";
                case 1:
                    return "달력";

            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}