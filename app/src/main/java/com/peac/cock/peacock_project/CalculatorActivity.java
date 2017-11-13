package com.peac.cock.peacock_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class CalculatorActivity extends AppCompatActivity {
    String state = "";
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator2);
        state = "outgoing";

        final ImageButton incomingButton = findViewById(R.id.calculator_layout_incoming_button);
        final ImageButton outgoingButton = findViewById(R.id.calculator_layout_outgoing_button);
        final ImageButton transferButton = findViewById(R.id.calculator_layout_transfer_button);
        final ImageButton checkButton = findViewById(R.id.calculator_layout_check_button);


        outgoingButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    outgoingButton.setBackgroundResource(R.drawable.handwriting_layout_active_outgoing_button);
                    incomingButton.setBackgroundResource(R.drawable.handwriting_layout_incoming_button);
                    transferButton.setBackgroundResource(R.drawable.handwriting_layout_transfer_button);
                    state = "outgoing";
                }
                return false;
            }
        });

        incomingButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    incomingButton.setBackgroundResource(R.drawable.handwriting_layout_active_incoming_button);
                    outgoingButton.setBackgroundResource(R.drawable.handwriting_layout_outgoing_button);
                    transferButton.setBackgroundResource(R.drawable.handwriting_layout_transfer_button);
                    state = "incoming";
                }
                return false;
            }
        });

        transferButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    transferButton.setBackgroundResource(R.drawable.handwriting_layout_active_transfer_button);
                    incomingButton.setBackgroundResource(R.drawable.handwriting_layout_incoming_button);
                    outgoingButton.setBackgroundResource(R.drawable.handwriting_layout_outgoing_button);
                    state = "transfer";
                }
                return false;
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HandwritingActivity.class);
                startActivity(intent);
            }
        });
    }
}
