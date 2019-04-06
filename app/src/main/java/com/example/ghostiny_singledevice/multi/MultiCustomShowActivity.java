package com.example.ghostiny_singledevice.multi;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ghostiny_singledevice.ActivityChangeService;
import com.example.ghostiny_singledevice.MainActivity;
import com.example.ghostiny_singledevice.R;
import com.example.ghostiny_singledevice.single.CustomShowActivity;
import com.example.ghostiny_singledevice.single.GameActivity;
import com.example.ghostiny_singledevice.utils.Colour;
import com.example.ghostiny_singledevice.utils.ImageTools;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MultiCustomShowActivity extends AppCompatActivity {
    private ImageView photo;
    private boolean hit;
    private Bitmap res;
    private Button cont;
    private boolean con = false;

    private ActivityChangeService myService;
    private ActivityChangeService.CommandBinder commandBinder;

    private ArrayList<Integer> rmCol;//可能会消失的颜色

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            commandBinder = (ActivityChangeService.CommandBinder) service;
            myService = commandBinder.getService();

            myService.setContCallBack(new ActivityChangeService.ContCallBack() {
                @Override
                public void contGame() {
                    Intent intent = new Intent(MultiCustomShowActivity.this, MultiGameActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("rmColor", rmCol);
                    startActivity(intent);
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
                            intent = new Intent(MultiCustomShowActivity.this, MultiRoomOwnerActivity.class);
                            break;
                        case 1:
                            intent = new Intent(MultiCustomShowActivity.this, MultiRoomOthersActivity.class);
                            break;
                        default:
                            break;
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            myService.setMemberLeaveCallBack2(new ActivityChangeService.MemberLeaveCallBack2() {
                @Override
                public void memberLeave2(int rmColor) {
                    rmCol.add(rmColor);
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
        setContentView(R.layout.activity_multi_custom_show);

        Intent startIntent = new Intent(this, ActivityChangeService.class);
        bindService(startIntent, serviceConnection, BIND_AUTO_CREATE);

        photo = (ImageView)findViewById(R.id.custom_photo);
        cont = (Button)findViewById(R.id.cont);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        Colour choice = (Colour)bundle.getSerializable("choice");
        boolean unluck = bundle.getBoolean("luck");
        String imageUri = bundle.getString("photoPath");
        rmCol = (ArrayList<Integer>)bundle.getSerializable("rmColor");


        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(imageUri)));
            Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.screamicon);

            bitmap = ImageTools.rotate(bitmap, 90);

            if (unluck){
                res = ImageTools.merge(bitmap, icon);
                con = false;
                cont.setText("Menu");
            }else {
                hit = ImageTools.colorRecg(bitmap, choice, 0.5);
                if (hit){
                    res = bitmap;
                    con = true;
                    //cont.setText("Continue");
                }else {
                    res = ImageTools.merge(bitmap, icon);
                    con = false;
                    cont.setText("Menu");
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
                myService.getCommandTask().send("-command cont");
            }
        });


    }
}
