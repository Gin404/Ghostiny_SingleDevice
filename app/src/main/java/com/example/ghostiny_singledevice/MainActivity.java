package com.example.ghostiny_singledevice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.ghostiny_singledevice.multi.MultiNickNameActivity;
import com.example.ghostiny_singledevice.single.SingleNumberActivity;

/**
 * 游戏主界面
 */
public class MainActivity extends AppCompatActivity {

    ImageButton  instructionButton,settingButton,singleButton,multiButton;
    private ActivityChangeService.CommandBinder commandBinder;
    //private Button btn_open;
    //private boolean status=false;
    Intent intent1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO: 25/02/2019

        instructionButton=(ImageButton)findViewById(R.id.icon_home_instruction);
        settingButton=(ImageButton)findViewById(R.id.icon_home_setting);
        singleButton=(ImageButton)findViewById(R.id.icon_home_SingleGame);
        multiButton=(ImageButton)findViewById(R.id.icon_home_multiplayer);
        intent1=new Intent(this,MusicServer.class);

        instructionButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,InstructionActivity.class);
                startActivity(intent);
            }
        });


       settingButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });

        singleButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this, SingleNumberActivity.class);
                startActivity(intent);
                startService(intent1);
            }
        });

        multiButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this, MultiNickNameActivity.class);
                startActivity(intent);
                startService(intent1);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this,MusicServer.class);
        stopService(intent);
    }


}
