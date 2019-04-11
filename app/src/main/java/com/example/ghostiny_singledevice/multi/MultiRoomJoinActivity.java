package com.example.ghostiny_singledevice.multi;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ghostiny_singledevice.ActivityChangeService;
import com.example.ghostiny_singledevice.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultiRoomJoinActivity extends Activity {

    private Button join;
    private TextView enter;
    private EditText roomId;
    private String rId;
    private ActivityChangeService myService;
    private ActivityChangeService.CommandBinder commandBinder;
    private SharedPreferences sharedPreferences;
    private Intent startIntent;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("multiJoin", "service 绑定");
            commandBinder = (ActivityChangeService.CommandBinder)service;
            myService = commandBinder.getService();


            myService.setJoinRoomCallBack(new ActivityChangeService.JoinRoomCallBack() {
                @Override
                public void joinRoom(int capacity, JSONArray nameList) {
                    Set<String> nameSet = new HashSet<>();
                    try {
                        for (int i = 0; i < nameList.length(); i++){
                            nameSet.add((String)nameList.get(i));
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet("nameSet", nameSet);
                    editor.apply();

                    Intent intent = new Intent(MultiRoomJoinActivity.this, MultiWaitActivity.class);
                    intent.putExtra("currNum", capacity);
                    intent.putExtra("roomId", rId);
                    startActivity(intent);
                }
            });

            myService.setJoinRefuseCallBack(new ActivityChangeService.JoinRefuseCallBack() {
                @Override
                public void joinRefuse() {
                    Toast.makeText(MultiRoomJoinActivity.this, "該房間已滿人或您已在房間中", Toast.LENGTH_SHORT).show();
                }
            });

            myService.setRoomNExistCallBack(new ActivityChangeService.RoomNExistCallBack() {
                @Override
                public void nExist() {
                    Toast.makeText(MultiRoomJoinActivity.this, "該房間不存在", Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("multiJoin", "service 解除绑定");
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_room_join);

        startIntent = new Intent(this, ActivityChangeService.class);
        join=(Button)findViewById(R.id.join_btn);
        roomId=(EditText) findViewById(R.id.room_id);
        enter=(TextView)findViewById ( R.id.textView11 );

        AssetManager mgr=getAssets();  //设置字体
        Typeface typeface=Typeface.createFromAsset(mgr,"font/TM.ttf");
        join.setTypeface(typeface);
        roomId.setTypeface(typeface);
        enter.setTypeface(typeface);

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rId = roomId.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isowner", false);
                editor.apply();
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("req", 20);
                    jsonObject.put("roomId", rId);
                    jsonObject.put("nickName", sharedPreferences.getString("myName", "nobody"));
                    myService.getCommandTask().send(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("multiJoin", "onStart");
        startIntent = new Intent(this, ActivityChangeService.class);
        bindService(startIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("multiJoin", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("multiJoin", "onStop");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        Log.d("multiJoin", "onDestroy");
        unbindService(serviceConnection);
        super.onDestroy();
    }

    // TODO: 06/04/2019 这里请加一个返回键
}
