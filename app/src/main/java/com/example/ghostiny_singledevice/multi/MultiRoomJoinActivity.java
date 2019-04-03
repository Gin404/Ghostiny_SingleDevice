package com.example.ghostiny_singledevice.multi;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ghostiny_singledevice.ActivityChangeService;
import com.example.ghostiny_singledevice.R;

public class MultiRoomJoinActivity extends AppCompatActivity {

    private ActivityChangeService.CommandBinder commandBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            commandBinder = (ActivityChangeService.CommandBinder)service;
            ActivityChangeService myService = commandBinder.getService();

            myService.setInputCallBack(new ActivityChangeService.InputCallBack() {
                @Override
                public void input() {
                    Intent intent = new Intent(MultiRoomJoinActivity.this, MultiRoomOthersActivity.class);
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
        setContentView(R.layout.activity_multi_room_join);
        Intent startIntent = new Intent(this, ActivityChangeService.class);
        startService(startIntent);
        bindService(startIntent, serviceConnection, BIND_AUTO_CREATE);
    }
}
