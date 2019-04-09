package com.example.ghostiny_singledevice.multi;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.ghostiny_singledevice.ActivityChangeService;
import com.example.ghostiny_singledevice.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * 联机模式：创建/加入房间
 */
public class MultiplayerActivity extends AppCompatActivity {
    private ImageButton create_room,join_room;
    private ImageView back;
    private ActivityChangeService myService;
    private int roomId = -1;
    private ActivityChangeService.CommandBinder commandBinder;
    private SharedPreferences sharedPreferences;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            commandBinder = (ActivityChangeService.CommandBinder)service;
            myService = commandBinder.getService();

            myService.setShowRmIdCallBack(new ActivityChangeService.ShowRmIdCallBack() {
                @Override
                public void showRmId(int id) {
                    roomId = id;

                    Set<String> nameSet = new HashSet<>();
                    nameSet.add(sharedPreferences.getString("myName", "nobody"));
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet("nameSet", nameSet);
                    editor.putInt("roomId", id);
                    editor.putInt("currNum", 1);
                    editor.apply();

                    if (roomId > -1){
                        //收到roomId并将roomId传给下一个activity展示
                        Intent intent = new Intent(MultiplayerActivity.this, MultiWaitActivity.class);
                        intent.putExtra("currNum", 1);
                        intent.putExtra("roomId", id+"");
                        startActivity(intent);
                    }
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

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);

        //启动并绑定服务
        final Intent startIntent = new Intent(this, ActivityChangeService.class);
        startService(startIntent);
        bindService(startIntent, serviceConnection, BIND_AUTO_CREATE);
        back = (ImageView)findViewById(R.id.back_btn);
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onBackPressed();
                return false;
            }

        });



        create_room=(ImageButton)findViewById(R.id.icon_create_room);
        create_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isowner", true);
                editor.apply();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("req", 10);
                    jsonObject.put("nickName", sharedPreferences.getString("myName", "nobody"));
                    myService.getCommandTask().send(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent startIntent = new Intent(this, ActivityChangeService.class);
        stopService(startIntent);
    }
}
