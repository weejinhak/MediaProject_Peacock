package com.peac.cock.peacock_project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

public class CardRecommendedActivity extends AppCompatActivity {

    private String KBurl1;
    private String KBurl2;
    private String SHurl1;
    private String SHurl2;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendcard);

        intent = new Intent();

        final ImageButton card_detail1 = findViewById(R.id.card_detail1);
        final ImageButton card_join1 = findViewById(R.id.card_join1);
        final ImageButton card_detail2 = findViewById(R.id.card_detail2);
        final ImageButton card_join2 = findViewById(R.id.card_join2);

        KBurl1 = "https://card.kbcard.com/CXPRICAC0076.cms?mainCC=a&cooperationcode=09170";
        KBurl2 = "https://card.kbcard.com/CXPRICAC0137.cms?cooperationcode=09170&solicitorcode=&issueStateType=&trid=";
        SHurl1 = "https://www.shinhancard.com/conts/person/card_info/major/business/buy/1198422_12913.jsp";
        SHurl2 = "https://www.shinhancard.com/hpp/HPPCARDN/HPPCrdPttA01.shc?EntryLoc=2514&tmEntryLoc=&empSeq=&targetID=auc4j23&datakey=&agcCd=";

        card_detail1.setOnClickListener(view -> {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(KBurl1));
            startActivity(intent);
        });

        card_join1.setOnClickListener(view -> {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(KBurl2));
            startActivity(intent);
        });
        card_detail2.setOnClickListener(view -> {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(SHurl1));
            startActivity(intent);
        });
        card_join2.setOnClickListener(view -> {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(SHurl2));
            startActivity(intent);
        });
    }

}
