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
import android.widget.TextView;

import com.example.ghostiny_singledevice.ActivityChangeService;
import com.example.ghostiny_singledevice.MainActivity;
import com.example.ghostiny_singledevice.R;

public class MultiRoomOwnerActivity extends AppCompatActivity {

    private Button startBtn;
    private TextView roomId;
    private TextView currentNum;
    private int curNum;

    private ActivityChangeService myService;

    private ActivityChangeService.CommandBinder commandBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            commandBinder = (ActivityChangeService.CommandBinder)service;
            myService = commandBinder.getService();

            myService.setMemberJoinCallBack(new ActivityChangeService.MemberJoinCallBack() {
                @Override
                public void memberJoin(int capacity) {
                    //如果新成员加入成功，更新当前人数
                    if (currentNum != null){
                        currentNum.setText(capacity);
                        curNum = capacity;
                    }
                }
            });

            myService.setMemberLeaveCallBack(new ActivityChangeService.MemberLeaveCallBack() {
                @Override
                public void memberLeave(int capacity) {
                    //如果成员离开，更新当前人数
                    if (currentNum != null){
                        currentNum.setText(capacity);
                        curNum = capacity;
                    }
                }
            });

            myService.setStartCallBack(new ActivityChangeService.StartCallBack() {
                @Override
                public void skipToGame() {
                    Intent intent = new Intent(MultiRoomOwnerActivity.this, MultiGameActivity.class);
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
        setContentView(R.layout.activity_multi_room_owner);
        Intent startIntent = new Intent(this, ActivityChangeService.class);
        bindService(startIntent, serviceConnection, BIND_AUTO_CREATE);

        Intent intent = getIntent();

        roomId=(TextView)findViewById(R.id.room_id);
        currentNum = (TextView)findViewById(R.id.current_num);

        roomId.setText(intent.getStringExtra("roomId"));

        startBtn=(Button)findViewById(R.id.multistart_btn);
        startBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                myService.getCommandTask().send("-command start");
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Bundle bundle = intent.getExtras();
        currentNum.setText(bundle.getString("curNum"));
    }
}
