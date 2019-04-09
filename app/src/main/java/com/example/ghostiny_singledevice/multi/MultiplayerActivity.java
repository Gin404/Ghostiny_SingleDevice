package com.example.ghostiny_singledevice.multi;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ghostiny_singledevice.ActivityChangeService;
import com.example.ghostiny_singledevice.MainActivity;
import com.example.ghostiny_singledevice.R;

public class MultiplayerActivity extends AppCompatActivity {


    ImageButton create_room,join_room;
    ActivityChangeService myService;
    TextView create_tv, join_tv;
    private ActivityChangeService.CommandBinder commandBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            commandBinder = (ActivityChangeService.CommandBinder)service;
            myService = commandBinder.getService();

/*
            myService.setCreateRoomCallBack(new ActivityChangeService.CreateRoomCallBack() {
                @Override
                public void createRoom() {
                    //roomDialog();

                    Intent intent = new Intent(MultiplayerActivity.this, MultiRoomOwnerActivity.class);
                    startActivity(intent);
                }
            });
*/

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);


        create_room=(ImageButton)findViewById(R.id.icon_create_room);
        create_room.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        myService.getCommandTask().send("createRoom");
               /* Intent intent=new Intent(MultiplayerActivity.this,MultiRoomOwnerActivity.class);
                startActivity(intent);*/
        }
        });

        join_room=(ImageButton)findViewById(R.id.icon_join_room);
        join_room.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent intent=new Intent(MultiplayerActivity.this,MultiRoomJoinActivity.class);
        startActivity(intent);
        }
        });

        Intent startIntent = new Intent(this, ActivityChangeService.class);
        startService(startIntent);
        bindService(startIntent, serviceConnection, BIND_AUTO_CREATE);

        //设置字体
        create_tv = (TextView) findViewById (R.id.TVcreate );
        join_tv = (TextView) findViewById ( R.id.TVjoin );
        AssetManager mgr=getAssets();
        Typeface typeface=Typeface.createFromAsset(mgr,"font/TM.ttf");
        create_tv.setTypeface(typeface);
        join_tv.setTypeface ( typeface );
        }

        }
