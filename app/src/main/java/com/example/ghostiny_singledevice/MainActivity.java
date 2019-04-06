package com.example.ghostiny_singledevice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.ghostiny_singledevice.multi.MultiplayerActivity;
import com.example.ghostiny_singledevice.single.SingleNumberActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityChangeService.CommandBinder commandBinder;
    private Button btn_open;
    private boolean status=false;
    Intent intent1;

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

        intent1=new Intent(this,MusicServer.class);
//        startService(intent);
//        btn_open= (Button) findViewById(R.id.btn_open);
//        btn_open.setText("open ");
//        btn_open.setVisibility(View.INVISIBLE);
//        btn_open.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!status){
//                    btn_open.setText("close");
//                    startService(intent);//调用onCreate的方法
//                    status=true;
//                }else{
//                    btn_open.setText("open");
//                    stopService(intent);//调用onDestory方法
//                    status=false;
//                }
//            }
//        });

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
                startService(intent1);
            }
        });

        multiButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,MultiplayerActivity.class);
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
