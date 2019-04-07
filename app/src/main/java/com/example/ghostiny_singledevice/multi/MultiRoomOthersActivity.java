package com.example.ghostiny_singledevice.multi;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.ghostiny_singledevice.ActivityChangeService;
import com.example.ghostiny_singledevice.R;

public class MultiRoomOthersActivity extends AppCompatActivity {

    private TextView currentNum, roomId;
    private int curNum;

    private ActivityChangeService.CommandBinder commandBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            commandBinder = (ActivityChangeService.CommandBinder)service;
            ActivityChangeService myService = commandBinder.getService();

            myService.setMemberJoinCallBack(new ActivityChangeService.MemberJoinCallBack() {
                @Override
                public void memberJoin(int capacity) {
                    if (currentNum != null) {
                        currentNum.setText(capacity);
                        curNum = capacity;
                    }
                }
            });

            myService.setMemberLeaveCallBack(new ActivityChangeService.MemberLeaveCallBack() {
                @Override
                public void memberLeave(int capacity) {
                    if (currentNum != null) {
                        currentNum.setText(capacity);
                        curNum = capacity;
                    }
                }
            });

            myService.setStartCallBack(new ActivityChangeService.StartCallBack() {
                @Override
                public void skipToGame() {
                    Intent intent=new Intent(MultiRoomOthersActivity.this, MultiGameActivity.class);
                    intent.putExtra("currentNum", curNum);
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
        setContentView(R.layout.activity_multi_room_others);

        Intent pIntent = getIntent();

        Intent startIntent = new Intent(this, ActivityChangeService.class);
        bindService(startIntent, serviceConnection, BIND_AUTO_CREATE);

        currentNum = (TextView)findViewById(R.id.current_num);
        roomId = (TextView)findViewById(R.id.room_id);

        curNum = pIntent.getIntExtra("capacity", 0);
        roomId.setText(pIntent.getStringExtra("roomId"));
        currentNum.setText("" + curNum);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        int curNum = bundle.getInt("curNum");
        currentNum.setText("" + curNum);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent stopIntent = new Intent(this, ActivityChangeService.class);
        stopService(stopIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
    // TODO: 06/04/2019 这里请加一个返回键
}
