package com.example.ghostiny_singledevice.multi;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ghostiny_singledevice.ActivityChangeService;
import com.example.ghostiny_singledevice.R;

public class MultiRoomJoinActivity extends AppCompatActivity {

    Button join;
    EditText roonnumbers;
    ActivityChangeService myService;
    private ActivityChangeService.CommandBinder commandBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            commandBinder = (ActivityChangeService.CommandBinder)service;
            myService = commandBinder.getService();

            myService.setJoinRoomCallBack(new ActivityChangeService.JoinRoomCallBack() {
                @Override
                public void joinRoom() {
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

        join=(Button)findViewById(R.id.join);
        roonnumbers =findViewById(R.id.roomnum);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String romnums = roonnumbers.getText().toString();
                myService.getCommandTask().send(romnums);

            }
        });

        Intent startIntent = new Intent(this, ActivityChangeService.class);
        startService(startIntent);
        bindService(startIntent, serviceConnection, BIND_AUTO_CREATE);
    }
}
