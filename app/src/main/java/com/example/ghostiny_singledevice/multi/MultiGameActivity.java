package com.example.ghostiny_singledevice.multi;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ghostiny_singledevice.ActivityChangeService;
import com.example.ghostiny_singledevice.R;
import com.example.ghostiny_singledevice.single.CustomCameraActivity;
import com.example.ghostiny_singledevice.single.GameActivity;
import com.example.ghostiny_singledevice.utils.Colour;

import java.util.ArrayList;

/**
 * 选择颜色界面
 */
public class MultiGameActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int TAKE_PHOTO = 1;

    private Colour colour;
    private Button takePhoto;
    private TextView textView;
    private boolean selected = false;
    private int colorNum;
    private Button toBeInvis;
    private boolean locked;



    private final Colour[] colours = Colour.values();

    private ActivityChangeService myService;
    private ActivityChangeService.CommandBinder commandBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
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
                    assert clr != null;
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
                public void endGame(int playerType, int curNum) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("curNum", curNum);
                    Intent intent = null;
                    //别人倒霉色拍完照点击继续，跳回房间等待
                    switch (playerType) {
                        case 0:
                            intent = new Intent(MultiGameActivity.this, MultiRoomOwnerActivity.class);
                            break;
                        case 1:
                            intent = new Intent(MultiGameActivity.this, MultiRoomOthersActivity.class);
                            break;
                        default:
                            break;
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            //成员离开，随机消失一个非倒霉颜色
            myService.setMemberLeaveCallBack2(new ActivityChangeService.MemberLeaveCallBack2() {
                @Override
                public void memberLeave2(int rmColor) {
                    Colour clr = Colour.getNameByCode(rmColor);
                    Resources res = getResources();
                    assert clr != null;
                    int id = res.getIdentifier(clr.toString().toLowerCase(),"id",getPackageName());
                    findViewById(id).setVisibility(View.INVISIBLE);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

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

        Intent startIntent = new Intent(this, ActivityChangeService.class);
        bindService(startIntent, serviceConnection, BIND_AUTO_CREATE);

        Intent pIntent = getIntent();
        colorNum = pIntent.getIntExtra("currentNum", 0);

        takePhoto = (Button) findViewById(R.id.camerabutton);
        textView = (TextView)findViewById(R.id.selectColor);

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
                    Toast.makeText(getApplicationContext(), "You must pick a color",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (locked){
                    Toast.makeText(getApplicationContext(), "Someone else is taking photo",Toast.LENGTH_LONG).show();
                    return;
                }
                toBeInvis.setVisibility(View.INVISIBLE);

                //View.INVISIBLE  Not visible but still in position
                myService.getCommandTask().send("" + colour.getCode());
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
        setIntent(intent);
        Log.d("newIntent", "游戏继续");
        ArrayList<Integer> invs = intent.getExtras().getIntegerArrayList("rmColor");
        Resources res = getResources();
        Colour clr;
        for (int i : invs){
            clr = Colour.getNameByCode(i);
            assert clr != null;
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
}
