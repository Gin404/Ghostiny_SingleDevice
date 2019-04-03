package com.example.ghostiny_singledevice.multi;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ghostiny_singledevice.ActivityChangeService;
import com.example.ghostiny_singledevice.MainActivity;
import com.example.ghostiny_singledevice.R;

public class MultiRoomOwnerActivity extends AppCompatActivity {

    Button startbtn;

    private ActivityChangeService.CommandBinder commandBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            commandBinder = (ActivityChangeService.CommandBinder)service;
            ActivityChangeService myService = commandBinder.getService();

            myService.setLeaveRoomCallBack(new ActivityChangeService.LeaveRoomCallBack() {
                @Override
                public void leaveRoom() {
                    Intent intent=new Intent(MultiRoomOwnerActivity.this, MultiplayerActivity.class);
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
        setContentView(R.layout.activity_multi_room_owner);
        Intent startIntent = new Intent(this, ActivityChangeService.class);
        startService(startIntent);
        bindService(startIntent, serviceConnection, BIND_AUTO_CREATE);

        startbtn=(Button)findViewById(R.id.multistartbtn);
        startbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MultiRoomOwnerActivity.this,MultiGameActivity.class);
                startActivity(intent);
            }
        });

    }
}
