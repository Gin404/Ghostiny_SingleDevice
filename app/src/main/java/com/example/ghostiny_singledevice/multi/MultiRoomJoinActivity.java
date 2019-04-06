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
import android.widget.Toast;

import com.example.ghostiny_singledevice.ActivityChangeService;
import com.example.ghostiny_singledevice.R;

public class MultiRoomJoinActivity extends AppCompatActivity {

    private Button join;
    private EditText roomId;
    private String rId;
    private ActivityChangeService myService;
    private ActivityChangeService.CommandBinder commandBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            commandBinder = (ActivityChangeService.CommandBinder)service;
            myService = commandBinder.getService();


            myService.setJoinRoomCallBack(new ActivityChangeService.JoinRoomCallBack() {
                @Override
                public void joinRoom(int capacity) {
                    Intent intent = new Intent(MultiRoomJoinActivity.this, MultiRoomOthersActivity.class);
                    intent.putExtra("capacity", capacity);
                    intent.putExtra("roomId", rId);
                    startActivity(intent);
                }
            });

            myService.setJoinRefuseCallBack(new ActivityChangeService.JoinRefuseCallBack() {
                @Override
                public void joinRefuse() {
                    Toast.makeText(MultiRoomJoinActivity.this, "Room full or in game", Toast.LENGTH_SHORT).show();
                }
            });

            myService.setRoomNExistCallBack(new ActivityChangeService.RoomNExistCallBack() {
                @Override
                public void nExist() {
                    Toast.makeText(MultiRoomJoinActivity.this, "Room not exist", Toast.LENGTH_SHORT).show();
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

        join=(Button)findViewById(R.id.join_btn);
        roomId =findViewById(R.id.room_id);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rId = roomId.getText().toString();
                myService.getCommandTask().send("-command join" + rId);
            }
        });

    }
}
