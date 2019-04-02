package com.example.ghostiny_singledevice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 多人人数选择
 */
public class MultiNumberActivity extends AppCompatActivity {




    private Button three, four, five, six, seven, eight, nine, ten, eleven, twelve;
    private Button start;

    private int num = 0;

    private RadioGroup r1, r2, r3, r4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_number);




        three = (Button) findViewById(R.id.icon_number3_m);
        four = (Button) findViewById(R.id.icon_number4_m);
        five = (Button) findViewById(R.id.icon_number5_m);
        six = (Button) findViewById(R.id.icon_number6_m);
        seven = (Button) findViewById(R.id.icon_number7_m);
        eight = (Button) findViewById(R.id.icon_number8_m);
        nine = (Button) findViewById(R.id.icon_number9_m);
        ten = (Button) findViewById(R.id.icon_number10_m);
        eleven = (Button) findViewById(R.id.icon_number11_m);
        twelve = (Button) findViewById(R.id.icon_number12_m);

        start = (Button) findViewById(R.id.start);

        r1 = (RadioGroup)findViewById(R.id.radioGroup1_m);
        r2 = (RadioGroup)findViewById(R.id.radioGroup2_m);
        r3 = (RadioGroup)findViewById(R.id.radioGroup3_m);
        r4 = (RadioGroup)findViewById(R.id.radioGroup4_m);

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
