package com.example.ghostiny_singledevice.multi;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ghostiny_singledevice.ActivityChangeService;
import com.example.ghostiny_singledevice.MainActivity;
import com.example.ghostiny_singledevice.MusicServer;
import com.example.ghostiny_singledevice.R;
import com.example.ghostiny_singledevice.ScreamMusicServer;
import com.example.ghostiny_singledevice.single.CustomShowActivity;
import com.example.ghostiny_singledevice.single.GameActivity;
import com.example.ghostiny_singledevice.utils.Colour;
import com.example.ghostiny_singledevice.utils.ImageTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Set;

public class MultiCustomShowActivity extends AppCompatActivity {
    private ImageView photo;
    private boolean hit;
    private Bitmap res;
    private Button cont;
    private boolean con = false;

    Intent intentBgm,intentScream;

    private ActivityChangeService myService;
    private ActivityChangeService.CommandBinder commandBinder;

    private ArrayList<Integer> rmCol;//可能会消失的颜色
    private SharedPreferences sharedPreferences;

    private Intent startIntent;


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("multiShow", "service 绑定");
            commandBinder = (ActivityChangeService.CommandBinder) service;
            myService = commandBinder.getService();

            myService.setContCallBack(new ActivityChangeService.ContCallBack() {
                @Override
                public void contGame() {
                    Intent intent = new Intent(MultiCustomShowActivity.this, MultiGameActivity.class);
                    startActivity(intent);
                }
            });

            myService.setEndCallBack(new ActivityChangeService.EndCallBack() {
                @Override
                public void endGame(int curNum) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("curNum", curNum);
                    Intent intent = null;
                    intent = new Intent(MultiCustomShowActivity.this, MultiWaitActivity.class);

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            /*myService.setMemberLeaveCallBack2(new ActivityChangeService.MemberLeaveCallBack2() {
                @Override
                public void memberLeave2(int rmColor, String nickName) {
                    Set<String> names = sharedPreferences.getStringSet("nameSet", null);
                    if (names == null){
                        throw new NullPointerException("names 为空");
                    }
                    names.remove(nickName);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet("nameSet", names);
                    editor.apply();
                    rmCol.add(rmColor);
                }
            });
*/
            /*myService.setNewOwnerCallBack(new ActivityChangeService.NewOwnerCallBack() {
                @Override
                public void asNewOwner() {
                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.putBoolean("isowner", true);
                    editor.apply();
                }
            });*/
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("multiShow", "service 解除绑定");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_custom_show);

        startIntent = new Intent(this, ActivityChangeService.class);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);

        photo = (ImageView)findViewById(R.id.custom_photo);
        cont = (Button)findViewById(R.id.cont);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        intentBgm=new Intent(this, MusicServer.class);
        intentScream=new Intent(this, ScreamMusicServer.class);

        Colour choice = (Colour)bundle.getSerializable("choice");
        boolean luck = bundle.getBoolean("luck");
        String imageUri = bundle.getString("photoPath");
        rmCol = bundle.getIntegerArrayList("rmColor");


        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(imageUri)));
            Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.screamicon1);

            bitmap = ImageTools.rotate(bitmap, 90);

            if (!luck){
                res = ImageTools.merge(bitmap, icon);
                con = false;
                cont.setText("退出遊戲");
                stopService(intentBgm);
                startService(intentScream);
            }else {
                cont.setText("繼續遊戲");
                hit = ImageTools.colorRecg(bitmap, choice, 0.5);
                if (hit){
                    res = bitmap;
                    con = true;
                }else {
                    res = ImageTools.merge(bitmap, icon);
                    con = false;
                }
            }

            photo.setImageBitmap(res);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        photo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    if (cont.getVisibility() == View.VISIBLE){
                        cont.setVisibility(View.GONE);
                    }else {
                        cont.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }
        });

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("req", 50);
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
        startIntent = new Intent(this, ActivityChangeService.class);
        bindService(startIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //unbindService(serviceConnection);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        unbindService(serviceConnection);
        stopService(startIntent);
        clearSharedPre();
        super.onBackPressed();
        startActivity(new Intent(MultiCustomShowActivity.this, MainActivity.class));
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
