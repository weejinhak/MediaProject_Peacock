package com.peac.cock.peacock_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dahye on 2017-11-13.
 */

public class AssetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);

        Button assetAddButton = findViewById(R.id.asset_layout_button_assetAdd_button);
        ImageButton assetGoButton = findViewById(R.id.asset_layout_asset_go_button);
        ImageButton breakDownGoButton = findViewById(R.id.asset_layout_breakdown_go_button);
        ImageButton analysisGoButton = findViewById(R.id.asset_layout_analysis_go_button);
        ImageButton settingGoButton = findViewById(R.id.asset_layout_setting_go_button);

        //하단 페이지 넘기기 클릭 이벤트
        assetGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "여기가 자산 페이지야!", Toast.LENGTH_SHORT).show();

            }
        });
        breakDownGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(intent);
            }
        });
        analysisGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DetailAnalysisActivity.class);
                startActivity(intent);
            }
        });
        settingGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        //asset Add call method event
        assetAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

    }

    void show() {
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("현금");
        ListItems.add("카드");

        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("자산 유형 선택");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                String selectedText = items[pos].toString();
                Toast.makeText(getApplicationContext(), selectedText, Toast.LENGTH_SHORT).show();
                if (selectedText.equals("현금")) {
                    Intent intent = new Intent(getApplicationContext(), AddCashActivity.class);
                    startActivity(intent);
                } else if (selectedText.equals("카드")) {
                    Intent intent = new Intent(getApplicationContext(), AddCardActivity.class);
                    startActivity(intent);
                }
            }
        });
        builder.show();

    }
}
