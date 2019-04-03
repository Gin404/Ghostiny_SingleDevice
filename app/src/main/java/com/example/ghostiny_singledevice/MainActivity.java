package com.example.ghostiny_singledevice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.ghostiny_singledevice.multi.MultiplayerActivity;
import com.example.ghostiny_singledevice.single.SingleNumberActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityChangeService.CommandBinder commandBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            commandBinder = (ActivityChangeService.CommandBinder)service;
            ActivityChangeService myService = commandBinder.getService();

            myService.setStartCallBack(new ActivityChangeService.StartCallBack() {
                @Override
                public void skipToGame() {
                    Intent intent = new Intent(MainActivity.this, MultiplayerActivity.class);
                    startActivity(intent);
                }
            });

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    ImageButton  instructionButton,settingButton,singleButton,multiButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO: 25/02/2019

        instructionButton=(ImageButton)findViewById(R.id.icon_home_instruction);
        settingButton=(ImageButton)findViewById(R.id.icon_home_setting);
        singleButton=(ImageButton)findViewById(R.id.icon_home_SingleGame);
        multiButton=(ImageButton)findViewById(R.id.icon_home_multiplayer);

        Intent startIntent = new Intent(this, ActivityChangeService.class);
        startService(startIntent);
        bindService(startIntent, serviceConnection, BIND_AUTO_CREATE);

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
            }
        });

        multiButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,MultiplayerActivity.class);
                startActivity(intent);
            }
        });


    }

}
