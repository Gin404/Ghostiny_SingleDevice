package com.example.ghostiny_singledevice.single;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ghostiny_singledevice.MainActivity;
import com.example.ghostiny_singledevice.R;

/**
 * 单人人数选择
 */
public class SingleNumberActivity extends AppCompatActivity {
    private Button three, four, five, six, seven, eight,
            nine, ten, eleven, twelve;
    private Button start;

    private TextView intro;

    private int num = 0;

    private RadioGroup r1, r2, r3, r4;

    ImageView back2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_number);

        three = (RadioButton) findViewById(R.id.icon_number3_s);
        four = (RadioButton) findViewById(R.id.icon_number4_s);
        five = (RadioButton) findViewById(R.id.icon_number5_s);
        six = (RadioButton) findViewById(R.id.icon_number6_s);
        seven = (RadioButton) findViewById(R.id.icon_number7_s);
        eight = (RadioButton) findViewById(R.id.icon_number8_s);
        nine = (RadioButton) findViewById(R.id.icon_number9_s);
        ten = (RadioButton) findViewById(R.id.icon_number10_s);
        eleven = (RadioButton) findViewById(R.id.icon_number11_s);
        //twelve = (RadioButton) findViewById(R.id.icon_number12_s);

        start = (Button) findViewById(R.id.start_s);
        intro = (TextView) findViewById ( R.id.textView_title5_s );

        r1 = (RadioGroup)findViewById(R.id.radioGroup1_s);
        r2 = (RadioGroup)findViewById(R.id.radioGroup2_s);
        r3 = (RadioGroup)findViewById(R.id.radioGroup3_s);
        r4 = (RadioGroup)findViewById(R.id.radioGroup4_s);


        AssetManager mgr=getAssets();        //設置字體
        Typeface typeface=Typeface.createFromAsset(mgr,"font/TM.ttf");
        three.setTypeface(typeface);
        four.setTypeface(typeface);
        five.setTypeface(typeface);
        six.setTypeface(typeface);
        seven.setTypeface(typeface);
        eight.setTypeface(typeface);
        nine.setTypeface(typeface);
        ten.setTypeface(typeface);
        eleven.setTypeface(typeface);
        //twelve.setTypeface(typeface);
        start.setTypeface ( typeface );
        intro.setTypeface ( typeface );


        back2=(ImageView)findViewById(R.id.back2);
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent (SingleNumberActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num == 0){
                    Toast.makeText(getApplicationContext(), "请选择人数",Toast.LENGTH_SHORT).show();
                    return;
                }
                //人数传递给颜色选择activity
                Intent intent = new Intent(SingleNumberActivity.this, GameActivity.class);
                intent.putExtra("num", num);
                startActivity(intent);
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 3;
                keepR1();
                Log.d("checkbox", "3");
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 4;
                keepR1();
                Log.d("checkbox", "4");
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 5;
                keepR1();
                Log.d("checkbox", "5");
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 6;
                keepR2();
                Log.d("checkbox", "6");
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
                Log.d("checkbox", "11");
            }
        });

        /*twelve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 12;
                keepR4();
            }
        });*/
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
