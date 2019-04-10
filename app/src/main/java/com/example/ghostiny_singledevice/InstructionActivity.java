package com.example.ghostiny_singledevice;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ghostiny_singledevice.multi.MultiplayerActivity;
import com.example.ghostiny_singledevice.single.SingleNumberActivity;

public class InstructionActivity extends AppCompatActivity {


    ImageButton back1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        AssetManager mgr=getAssets();  //设置字体
        Typeface typeface=Typeface.createFromAsset(mgr,"font/TM.ttf");
        TextView test1=(TextView)findViewById(R.id.textView2);
        test1.setTypeface(typeface);
        TextView test2=(TextView)findViewById(R.id.textView3);
        test2.setTypeface(typeface);
        TextView test3=(TextView)findViewById(R.id.textView4);
        test3.setTypeface(typeface);
        TextView test4=(TextView)findViewById(R.id.textView_instruction);
        test4.setTypeface(typeface);
        TextView test5=(TextView)findViewById(R.id.textView5);
        test5.setTypeface(typeface);



        back1=(ImageButton) findViewById(R.id.imagebutton2);




        back1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(InstructionActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
