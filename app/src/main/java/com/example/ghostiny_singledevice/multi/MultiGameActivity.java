package com.example.ghostiny_singledevice.multi;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ghostiny_singledevice.ActivityChangeService;
import com.example.ghostiny_singledevice.MainActivity;
import com.example.ghostiny_singledevice.R;
import com.example.ghostiny_singledevice.utils.Colour;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

/**
 * 选择颜色界面
 */
public class MultiGameActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int TAKE_PHOTO = 1;

    private Colour colour;
    private Button takePhoto;
    private TextView textView;
    private ImageView back;
    private boolean selected = false;
    private int colorNum;
    private Button toBeInvis;
    private boolean locked;

    private RecyclerView recyclerView;
    private MemberAdaptor adaptor;

    private SharedPreferences sharedPreferences;

    private final Colour[] colours = Colour.values();

    private ActivityChangeService myService;
    private ActivityChangeService.CommandBinder commandBinder;

    private Intent startIntent;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("multiGame", "service 绑定");
            commandBinder = (ActivityChangeService.CommandBinder)service;
            myService = commandBinder.getService();

            myService.setLuckCallBack(new ActivityChangeService.LuckCallBack() {
                @Override
                public void setLuck(boolean luck) {
                    // 启动相机程序
                    Intent intent = new Intent(MultiGameActivity.this, MultiCustomCameraActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("choice", colour);
                    bundle.putBoolean("luck", luck);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            myService.setLockCallBack(new ActivityChangeService.LockCallBack() {
                @Override
                public void setLock(int colour) {
                    locked = true;
                    Colour clr = Colour.getNameByCode(colour);
                    Resources res = getResources();
                    if (clr == null){
                        throw new NullPointerException("clr 为空");
                    }
                    int id = res.getIdentifier(clr.toString().toLowerCase(),"id",getPackageName());
                    findViewById(id).setVisibility(View.INVISIBLE);
                }
            });

            myService.setContCallBack(new ActivityChangeService.ContCallBack() {
                @Override
                public void contGame() {
                    locked = false;
                }
            });

            myService.setEndCallBack(new ActivityChangeService.EndCallBack() {
                @Override
                public void endGame(int curNum) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("curNum", curNum);
                    Intent intent = null;
                    //别人倒霉色拍完照点击继续，跳回房间等待
                    intent = new Intent(MultiGameActivity.this, MultiWaitActivity.class);

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            //成员离开，随机消失一个非倒霉颜色
            myService.setMemberLeaveCallBack2(new ActivityChangeService.MemberLeaveCallBack2() {
                @Override
                public void memberLeave2(int rmColor, String nickName) {
                    Set<String> names = sharedPreferences.getStringSet("nameSet", null);
                    if (names == null){
                        throw new NullPointerException("names 为空");
                    }
                    names.remove(nickName);
                    refreshNames(names);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet("nameSet", names);
                    editor.apply();

                    Colour clr = Colour.getNameByCode(rmColor);
                    Resources res = getResources();
                    if (clr == null){
                        throw new NullPointerException("clr 为空");
                    }
                    int id = res.getIdentifier(clr.toString().toLowerCase(),"id",getPackageName());
                    findViewById(id).setVisibility(View.INVISIBLE);
                }
            });

            //--------------------------------------
            /*myService.setNewOwnerCallBack(new ActivityChangeService.NewOwnerCallBack() {
                @Override
                public void asNewOwner() {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isowner", true);
                    editor.apply();
                }
            });*/
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("multiGame", "service 解除绑定");
        }
    };


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_game);

        startIntent = new Intent(this, ActivityChangeService.class);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);

        Intent pIntent = getIntent();
        colorNum = pIntent.getIntExtra("currentNum", 0);

        takePhoto = (Button) findViewById(R.id.camerabutton);
        textView = (TextView)findViewById(R.id.selectColor);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view_game);

        Set<String> names = sharedPreferences.getStringSet("nameSet", null);
        refreshNames(names);

        back = (ImageView) findViewById(R.id.imageView2_m);

        //将后(12-colourNum)个颜色按钮置为隐形
        Resources res = getResources();
        Button temp = null;
        for (int i = colorNum; i < colours.length; i++){

            temp = findViewById(res.getIdentifier(colours[i].toString().toLowerCase(),"id",getPackageName()));
            temp.setVisibility(View.INVISIBLE);  //View.INVISIBLE  Not visible but still in position
        }


        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selected){
                    Toast.makeText(getApplicationContext(), "請選擇一種顏色",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (locked){
                    Toast.makeText(getApplicationContext(), "其他玩家正在拍照",Toast.LENGTH_LONG).show();
                    return;
                }
                toBeInvis.setVisibility(View.INVISIBLE);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("req", 40);
                    jsonObject.put("color", colour.getCode());
                    myService.getCommandTask().send(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        });

        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onBackPressed();
                return false;
            }
        });

        AssetManager mgr=getAssets();   //设置字体
        Typeface typeface=Typeface.createFromAsset(mgr,"font/TM.ttf");
        takePhoto.setTypeface(typeface);
        textView.setTypeface ( typeface );
    }


    //singleTask模式下，拍完照跳回，把退出的人的颜色置为隐形
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //Intent startIntent = new Intent(this, ActivityChangeService.class);
        //bindService(startIntent, serviceConnection, BIND_AUTO_CREATE);
        setIntent(intent);
        Log.d("newIntent", "游戏继续");
        Bundle newBundle = getIntent().getExtras();
        ArrayList<Integer> invs = newBundle.getIntegerArrayList("rmColor");
        Resources res = getResources();
        Colour clr;
        for (int i : invs){
            clr = Colour.getNameByCode(i);
            if (clr == null){
                throw new NullPointerException("clr 为空");
            }
            int id = res.getIdentifier(clr.toString().toLowerCase(),"id",getPackageName());
            findViewById(id).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 处理相应点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        Resources res = getResources();
        final int id = v.getId();

        for (int i = 0; i < colorNum; i++){
            String s = colours[i].toString().toLowerCase();
            if (id == res.getIdentifier(s,"id",getPackageName())){
                textView.setText(s);
                colour = colours[i];
                toBeInvis = (Button) v;
                selected = true;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(startIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("multiGameActivity", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("multiGameActivity", "onStop");
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
        Log.d("multiGameActivity", "onDestroy");
    }

    //退成员颜色没变少
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("multiGameActivity", "onBackPressed");
        unbindService(serviceConnection);
        stopService(startIntent);
        clearSharedPre();
        startActivity(new Intent(MultiGameActivity.this, MainActivity.class));
    }

    protected void refreshNames(Set<String> names){
        if (names == null){
            throw new NullPointerException("names 为空");
        }
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        adaptor = new MemberAdaptor(names);
        recyclerView.setAdapter(adaptor);
    }

    public void clearSharedPre(){
        Set<String> names = sharedPreferences.getStringSet("nameSet", null);
        if (names == null){
            throw new NullPointerException("names 为空");
        }
        names.clear();
        sharedPreferences.edit().clear().putStringSet("nameSet", names).apply();
    }
}
