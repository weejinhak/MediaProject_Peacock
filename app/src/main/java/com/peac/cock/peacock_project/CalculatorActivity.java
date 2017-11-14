package com.peac.cock.peacock_project;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {
    String state = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator2);
        state = "outgoing";

        final ImageButton incomingButton = findViewById(R.id.calculator_layout_incoming_button);
        final ImageButton outgoingButton = findViewById(R.id.calculator_layout_outgoing_button);
        final ImageButton transferButton = findViewById(R.id.calculator_layout_transfer_button);
        final ImageButton checkButton = findViewById(R.id.calculator_layout_check_button);
        final ImageButton backButton = findViewById(R.id.calculator_layout_back_button);
        final Button one = findViewById(R.id.calculator_layout_1_button);
        final Button two = findViewById(R.id.calculator_layout_2_button);
        final Button three = findViewById(R.id.calculator_layout_3_button);
        final Button four = findViewById(R.id.calculator_layout_4_button);
        final Button five = findViewById(R.id.calculator_layout_5_button);
        final Button six = findViewById(R.id.calculator_layout_6_button);
        final Button seven = findViewById(R.id.calculator_layout_7_button);
        final Button eight = findViewById(R.id.calculator_layout_8_button);
        final Button nine = findViewById(R.id.calculator_layout_9_button);
        final ImageButton eraseButton = findViewById(R.id.calculator_layout_erase_button);
        final ImageButton subtractButton = findViewById(R.id.calculator_layout_subtract_button);
        final ImageButton plusButton = findViewById(R.id.calculator_layout_plus_button);
        final ImageButton multipleButton = findViewById(R.id.calculator_layout_multiple_button);
        final ImageButton divideButton = findViewById(R.id.calculator_layout_divide_button);


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

        // 체크 버튼 클릭
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
         //       String amount = result.
                Intent intent = new Intent(getApplicationContext(), HandwritingActivity.class);
                startActivity(intent);
            }
        });


        // 뒤로가기 버튼 클릭 시 다이얼로그 창 뜨도록
        final AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alt_bld.setMessage("페이지에서 나가면 작성된 정보가 사라집니다. 그래도 나가시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = alt_bld.create();
                // Title for AlertDialog
                alert.setTitle("잠깐!");
                // Icon for AlertDialog
                alert.setIcon(R.drawable.calculator_layout_warning_icon);
                alert.show();

            }
        });

    }

    @Override
    public void onClick(View v) {
        TextView result = findViewById(R.id.calculator_layout_result_view);
        switch (v.getId()) {
            case R.id.calculator_layout_0_button:

                result.setText(result.getText().toString() + ((Button)v).getText());
        }
    }


    /*int temp_num = 0;

    var $result_show = document.getElementById('result_show');

    function cal(input) {
        if(input == '*' || input == '/' || input == '+' || input == '-') {
            if (result == '0') {
                result = temp_num;
            }
            result += input;
            temp_num = 0;
        } else {
            if(temp_num == '0') {
                temp_num = input;
                if(result == '0') {
                    result = input;
                } else {
                    result += input;
                }
            } else {
                if(result == '0') {
                    result = input;
                    temp_num = input;
                } else {
                    result += input;
                    temp_num += input;
                }
            }
            $result_show.innerHTML = temp_num;
        }

        console.log(result);
    }

    function removeAll() {
        result = '0';
        $result_show.innerHTML = result;
        console.log(result);
    }


    public void calculate(String result) {
        try {

            result = Math.round(eval(result)*100)/100;
            $result_show.innerHTML = result;
            console.log(result);
            temp_num = result;
            result = '0';
        }catch(e){
            removeAll();
        }
    }*/
}
