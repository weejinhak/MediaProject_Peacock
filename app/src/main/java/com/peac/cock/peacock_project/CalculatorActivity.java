package com.peac.cock.peacock_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {
    private String state;
    private TextView result;
    private ImageButton checkButton;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        intent = getIntent();
        if(intent.getStringExtra("state") == null) {
            state = "outgoing";
        } else {
            state = intent.getStringExtra("state");
        }

        if(intent.getStringExtra("amount") != null) {
            result.setText(intent.getStringExtra("amount"));
        }

        final ImageButton incomingButton = findViewById(R.id.calculator_layout_incoming_button);
        final ImageButton outgoingButton = findViewById(R.id.calculator_layout_outgoing_button);
        final ImageButton transferButton = findViewById(R.id.calculator_layout_transfer_button);

        checkButton = findViewById(R.id.calculator_layout_check_button);
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

        result = findViewById(R.id.calculator_layout_result_view);

        one.setOnClickListener(numberClickListener);
        two.setOnClickListener(numberClickListener);
        three.setOnClickListener(numberClickListener);
        four.setOnClickListener(numberClickListener);
        five.setOnClickListener(numberClickListener);
        six.setOnClickListener(numberClickListener);
        seven.setOnClickListener(numberClickListener);
        eight.setOnClickListener(numberClickListener);
        nine.setOnClickListener(numberClickListener);

        eraseButton.setOnClickListener(this);
        subtractButton.setOnClickListener(this);
        plusButton.setOnClickListener(this);
        multipleButton.setOnClickListener(this);
        divideButton.setOnClickListener(this);
        checkButton.setOnClickListener(this);
        backButton.setOnClickListener(this);


        // 초기 상태

        if(state.equals("outgoing")) {
            outgoingButton.setBackgroundResource(R.drawable.handwriting_layout_active_outgoing_button);
            incomingButton.setBackgroundResource(R.drawable.handwriting_layout_incoming_button);
            transferButton.setBackgroundResource(R.drawable.handwriting_layout_transfer_button);
        } else if(state.equals("incoming")) {
            incomingButton.setBackgroundResource(R.drawable.handwriting_layout_active_incoming_button);
            outgoingButton.setBackgroundResource(R.drawable.handwriting_layout_outgoing_button);
            transferButton.setBackgroundResource(R.drawable.handwriting_layout_transfer_button);
        } else {
            transferButton.setBackgroundResource(R.drawable.handwriting_layout_active_transfer_button);
            incomingButton.setBackgroundResource(R.drawable.handwriting_layout_incoming_button);
            outgoingButton.setBackgroundResource(R.drawable.handwriting_layout_outgoing_button);
        }



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
    }


    // 숫자 버튼 누를 때
    View.OnClickListener numberClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(result.getText().toString().equals("0")) {
                result.setText("");
                checkButton.setBackgroundResource(R.drawable.handwriting_layout_active_check_button);
            }
            result.setText(result.getText().toString() + ((Button) view).getText().toString());
        }
    };

    // 나머지 이미지 버튼 누를 때
    @Override
    public void onClick(View v) {
        final TextView result = findViewById(R.id.calculator_layout_result_view);
        switch (v.getId()) {
            case R.id.calculator_layout_erase_button:
                if(!result.getText().toString().equals("0")) {
                    result.setText(result.getText().toString().substring(0, result.length()-1));
                    if(result.getText().toString().equals("")) {
                        result.setText("0");
                        checkButton.setBackgroundResource(R.drawable.handwriting_layout_check_button);
                    }
                }
                break;
            case R.id.calculator_layout_subtract_button:
                if(!result.getText().toString().equals("0")
                        && !result.getText().toString().substring(result.length()-1, result.length()).equals("-")
                        && !result.getText().toString().substring(result.length()-1, result.length()).equals("+")
                        && !result.getText().toString().substring(result.length()-1, result.length()).equals("x")
                        && !result.getText().toString().substring(result.length()-1, result.length()).equals("÷")) {
                    result.setText(result.getText().toString() + "-");
                }
                break;
            case R.id.calculator_layout_plus_button:
                if(!result.getText().toString().equals("0")
                        && !result.getText().toString().substring(result.length()-1, result.length()).equals("-")
                        && !result.getText().toString().substring(result.length()-1, result.length()).equals("+")
                        && !result.getText().toString().substring(result.length()-1, result.length()).equals("x")
                        && !result.getText().toString().substring(result.length()-1, result.length()).equals("÷")) {
                    result.setText(result.getText().toString() + "+");
                }
                break;
            case R.id.calculator_layout_multiple_button:
                if(!result.getText().toString().equals("0")
                        && !result.getText().toString().substring(result.length()-1, result.length()).equals("-")
                        && !result.getText().toString().substring(result.length()-1, result.length()).equals("+")
                        && !result.getText().toString().substring(result.length()-1, result.length()).equals("x")
                        && !result.getText().toString().substring(result.length()-1, result.length()).equals("÷")) {
                    result.setText(result.getText().toString() + "x");
                }
                break;
            case R.id.calculator_layout_divide_button:
                if(!result.getText().toString().equals("0")
                        && !result.getText().toString().substring(result.length()-1, result.length()).equals("-")
                        && !result.getText().toString().substring(result.length()-1, result.length()).equals("+")
                        && !result.getText().toString().substring(result.length()-1, result.length()).equals("x")
                        && !result.getText().toString().substring(result.length()-1, result.length()).equals("÷")) {
                    result.setText(result.getText().toString() + "÷");
                }
                break;
            case R.id.calculator_layout_check_button:
                intent = new Intent(getApplicationContext(), HandwritingActivity.class);
                intent.putExtra("amount", result.getText().toString());
                intent.putExtra("state", state);
                startActivity(intent);
                break;
            case R.id.calculator_layout_back_button:
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
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
                alert.setTitle("잠깐!");
                alert.setIcon(R.drawable.calculator_layout_warning_icon);
                alert.show();
                break;
        }
    }
}
