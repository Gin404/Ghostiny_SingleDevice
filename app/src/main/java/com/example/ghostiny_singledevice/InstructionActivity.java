package com.example.ghostiny_singledevice;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class InstructionActivity extends AppCompatActivity {

    Button singleButton1,multiButton1;
    ImageButton back1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        singleButton1=(Button)findViewById(R.id.icon_instruction_SingleGame);
        multiButton1=(Button)findViewById(R.id.icon_instruction_Multiplayer);
        back1=(ImageButton) findViewById(R.id.imagebutton2);


        singleButton1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(InstructionActivity.this,SingleGameoverActivity.class);
                startActivity(intent);
            }
        });

        multiButton1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(InstructionActivity.this,MultiplayerActivity.class);
                startActivity(intent);
            }
        });

        back1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(InstructionActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
