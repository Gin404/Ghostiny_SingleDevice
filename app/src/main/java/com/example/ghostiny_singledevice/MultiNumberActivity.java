package com.example.ghostiny_singledevice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MultiNumberActivity extends AppCompatActivity {
    private Button three, four, five, six, seven, eight,
                    nine, ten, eleven, twelve;
    private Button start;

    private int num = 0;

    private RadioGroup r1, r2, r3, r4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_number);

        three = (Button) findViewById(R.id.icon_number3);
        four = (Button) findViewById(R.id.icon_number4);
        five = (Button) findViewById(R.id.icon_number5);
        six = (Button) findViewById(R.id.icon_number6);
        seven = (Button) findViewById(R.id.icon_number7);
        eight = (Button) findViewById(R.id.icon_number8);
        nine = (Button) findViewById(R.id.icon_number9);
        ten = (Button) findViewById(R.id.icon_number10);
        eleven = (Button) findViewById(R.id.icon_number11);
        twelve = (Button) findViewById(R.id.icon_number12);

        start = (Button) findViewById(R.id.start);

        r1 = (RadioGroup)findViewById(R.id.radioGroup1);
        r2 = (RadioGroup)findViewById(R.id.radioGroup2);
        r3 = (RadioGroup)findViewById(R.id.radioGroup3);
        r4 = (RadioGroup)findViewById(R.id.radioGroup4);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num == 0){
                    Toast.makeText(getApplicationContext(), "请选择人数",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(MultiNumberActivity.this, GameActivity.class);
                intent.putExtra("num", num);
                startActivity(intent);
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 3;
                keepR1();
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 4;
                keepR1();
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 5;
                keepR1();
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 6;
                keepR2();
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 7;
                keepR2();
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 8;
                keepR2();
            }
        });

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 9;
                keepR3();
            }
        });

        ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 10;
                keepR3();
            }
        });

        eleven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 11;
                keepR3();
            }
        });

        twelve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 12;
                keepR4();
            }
        });
    }

    private void keepR1(){
        r2.clearCheck();
        r3.clearCheck();
        r4.clearCheck();
    }

    private void keepR2(){
        r1.clearCheck();
        r3.clearCheck();
        r4.clearCheck();
    }

    private void keepR3(){
        r1.clearCheck();
        r2.clearCheck();
        r4.clearCheck();
    }

    private void keepR4(){
        r1.clearCheck();
        r2.clearCheck();
        r3.clearCheck();
    }
}
