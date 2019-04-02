package com.example.ghostiny_singledevice;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MultiplayerActivity extends AppCompatActivity {


    private ActivityChangeService.CommandBinder commandBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            commandBinder = (ActivityChangeService.CommandBinder)service;
            ActivityChangeService myService = commandBinder.getService();

            myService.setCreateRoomCallBack(new ActivityChangeService.CreateRoomCallBack() {
                @Override
                public void createRoom() {
                    roomDialog();
                    /*Intent intent = new Intent(MultiplayerActivity.this, MultiNumberActivity.class);
                    startActivity(intent);*/
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
        Intent startIntent = new Intent(this, ActivityChangeService.class);
        startService(startIntent);
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
                                Intent intent = new Intent(MultiplayerActivity.this, MultiNumberActivity.class);
                                startActivity(intent);
                            }
                        }).show();
    }

}
