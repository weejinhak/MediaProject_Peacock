package com.peac.cock.peacock_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class RecommendationActivity extends AppCompatActivity {

    ImageButton best_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        best_button = (ImageButton) findViewById(R.id.best_one);

        best_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(), CardRecommendedActivity.class);
                startActivity(intent);
            }
        });

    }
}
