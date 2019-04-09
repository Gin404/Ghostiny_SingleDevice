package com.example.ghostiny_singledevice.multi;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ghostiny_singledevice.ActivityChangeService;
import com.example.ghostiny_singledevice.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

public class MultiWaitActivity extends AppCompatActivity {
    private Button startBtn;
    private TextView roomId;
    private TextView currentNum;
    private RecyclerView recyclerView;
    private int curNum;

    private ActivityChangeService myService;

    private ActivityChangeService.CommandBinder commandBinder;

    private SharedPreferences sharedPreferences;

    private MemberAdaptor adaptor;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName name, IBinder service) {
            commandBinder = (ActivityChangeService.CommandBinder)service;
            myService = commandBinder.getService();

            myService.setMemberJoinCallBack(new ActivityChangeService.MemberJoinCallBack() {
                @Override
                public void memberJoin(int currNum, String nickName) {
                    //如果新成员加入成功，更新当前人数和成员列表
                    Set<String> names = sharedPreferences.getStringSet("nameSet", null);
                    assert names != null;
                    names.add(nickName);
                    refreshNames(names);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet("nameSet", names);
                    editor.apply();

                    if (currentNum != null){
                        currentNum.setText("" + currNum);
                        curNum = currNum;
                    }
                }
            });

            myService.setMemberLeaveCallBack(new ActivityChangeService.MemberLeaveCallBack() {
                @Override
                public void memberLeave(int currNum, String nickName) {
                    //如果成员离开，更新当前人数和成员列表
                    Set<String> names = sharedPreferences.getStringSet("nameSet", null);
                    assert names != null;
                    names.remove(nickName);
                    refreshNames(names);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet("nameSet", names);
                    editor.apply();

                    if (currentNum != null){
                        currentNum.setText(currNum);
                        curNum = currNum;
                    }
                }
            });

            myService.setStartCallBack(new ActivityChangeService.StartCallBack() {
                @Override
                public void skipToGame() {
                    Intent intent = new Intent(MultiWaitActivity.this, MultiGameActivity.class);
                    intent.putExtra("currentNum", curNum);
                    startActivity(intent);
                }
            });

            myService.setNewOwnerCallBack(new ActivityChangeService.NewOwnerCallBack() {
                @Override
                public void asNewOwner() {
                    SharedPreferences.Editor editor = sharedPreferences .edit();
                    editor.putBoolean("isowner", true);
                    editor.apply();
                    startBtn.setVisibility(View.VISIBLE);
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
        setContentView(R.layout.activity_multi_wait);

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);

        Intent startIntent = new Intent(this, ActivityChangeService.class);
        bindService(startIntent, serviceConnection, BIND_AUTO_CREATE);

        Intent intent = getIntent();

        roomId=(TextView)findViewById(R.id.room_id);
        currentNum = (TextView)findViewById(R.id.current_num_wait);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        startBtn=(Button)findViewById(R.id.multistart_btn);

        Set<String> names = sharedPreferences.getStringSet("nameSet", null);
        refreshNames(names);

        curNum = intent.getIntExtra("currNum", 1);
        currentNum.setText(""+curNum);

        if (!sharedPreferences.getBoolean("isowner", false)){
            startBtn.setVisibility(View.INVISIBLE);
        }

        roomId.setText(intent.getStringExtra("roomId"));


        startBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (curNum < 2){
                    Toast.makeText(getApplicationContext(), "The number of players should larger than 3",Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("req", 30);
                    myService.getCommandTask().send(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Set<String> names = sharedPreferences.getStringSet("nameSet", null);
        refreshNames(names);
        setIntent(intent);
        Bundle bundle = intent.getExtras();
        currentNum.setText(" " + bundle.getInt("curNum"));
        if (sharedPreferences.getBoolean("isowner", false)){
            startBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent stopIntent = new Intent(this, ActivityChangeService.class);
        stopService(stopIntent);
    }

    protected void refreshNames(Set<String> names){
        assert names != null;
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        adaptor = new MemberAdaptor(names);
        recyclerView.setAdapter(adaptor);
    }
}
