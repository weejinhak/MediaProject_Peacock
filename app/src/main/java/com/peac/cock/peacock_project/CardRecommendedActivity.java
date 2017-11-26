package com.peac.cock.peacock_project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class CardRecommendedActivity extends AppCompatActivity {

    ImageButton card_join1;
    ImageButton card_detail1;
    ImageButton card_join2;
    ImageButton card_detail2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendcard);
        card_detail1 = (ImageButton) findViewById(R.id.card_detail1);
        card_join1 = (ImageButton) findViewById(R.id.card_join1);
        card_detail2 = (ImageButton) findViewById(R.id.card_detail2);
        card_join2 = (ImageButton) findViewById(R.id.card_join2);

        String KBurl1 = "https://card.kbcard.com/CXPRICAC0076.cms?mainCC=a&cooperationcode=09170";
        String KBurl2 = "https://card.kbcard.com/CXPRICAC0137.cms?cooperationcode=09170&solicitorcode=&issueStateType=&trid=";
        String SHurl1 = "https://www.shinhancard.com/conts/person/card_info/major/business/buy/1198422_12913.jsp";
        String SHurl2 = "https://www.shinhancard.com/hpp/HPPCARDN/HPPCrdPttA01.shc?EntryLoc=2514&tmEntryLoc=&empSeq=&targetID=auc4j23&datakey=&agcCd=";

        card_detail1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(KBurl1));
                startActivity(i);
            }
        });

        card_join1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(KBurl2));
                startActivity(i);
            }
        });
        card_detail2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(SHurl1));
                startActivity(i);
            }
        });
        card_join2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(SHurl2));
                startActivity(i);
            }
        });
    }

}
