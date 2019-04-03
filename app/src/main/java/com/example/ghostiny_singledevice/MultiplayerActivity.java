package com.example.ghostiny_singledevice;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MultiplayerActivity extends AppCompatActivity {

    Button create_room;
    ActivityChangeService myService;
    private ActivityChangeService.CommandBinder commandBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            commandBinder = (ActivityChangeService.CommandBinder)service;
            myService = commandBinder.getService();

            myService.setCreateRoomCallBack(new ActivityChangeService.CreateRoomCallBack() {
                @Override
                public void createRoom() {
                    roomDialog();
                    /*Intent intent = new Intent(MultiplayerActivity.this, MultiRoomJoinActivity.class);
                    startActivity(intent);*/
                }
            });

            myService.setJoinRoomCallBack(new ActivityChangeService.JoinRoomCallBack() {
                @Override
                public void joinRoom() {
                    Intent intent = new Intent(MultiplayerActivity.this, MultiRoomOwnerActivity.class);
                    startActivity(intent);

                }
            });


            myService.setJoinInputCallBack(new ActivityChangeService.JoinInputCallBack() {
                @Override
                public void joinInput() {
                    Intent intent = new Intent(MultiplayerActivity.this, MultiRoomJoinActivity.class);
                    startActivity(intent);

                }
            });

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);


        create_room=(Button)findViewById(R.id.icon_create_room);
        create_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myService.getCommandTask().send("createRoom");
            }
        });

        Intent startIntent = new Intent(this, ActivityChangeService.class);
        //startService(startIntent);
        bindService(startIntent, serviceConnection, BIND_AUTO_CREATE);


    }

    public void roomDialog() {
        new AlertDialog.Builder(MultiplayerActivity.this)
                .setTitle("Your room number")
                .setMessage("1234")
                .setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {
                                /*Intent intent = new Intent(MultiplayerActivity.this,MultiRoomJoinActivity.class);
                                startActivity(intent);*/
                            }
                        }).show();
    }
}
