package com.example.ghostiny_singledevice;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ghostiny_singledevice.utils.Colour;


public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int TAKE_PHOTO = 1;

    private Colour colour, unluck;
    private Button takePhoto;
    private TextView textView;
    private boolean selected = false;
    private int colorNum;

    private final Colour[] colours = Colour.values();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent pIntent = getIntent();
        colorNum = pIntent.getIntExtra("num", 12);

        //倒霉色
        unluck = Colour.randomColour();
        Log.d("ingame", unluck.toString() + " is unlucky");

        takePhoto = (Button) findViewById(R.id.camerabutton);

        //将后12-colourNum个颜色按钮置为隐形
        Resources res = getResources();
        Button temp = null;
        for (int i = colorNum; i < colours.length; i++){
            temp = findViewById(res.getIdentifier(colours[i].toString(),"id",getPackageName()));
            temp.setVisibility(View.GONE);
        }


        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected == false){
                    Toast.makeText(getApplicationContext(), "请选择颜色",Toast.LENGTH_SHORT).show();
                    return;
                }

                // 启动相机程序
                Intent intent = new Intent(GameActivity.this, CustomCameraActivity.class);
                startActivity(intent);
            }


        });
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("newIntent", "game new intent");
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
                v.setVisibility(View.GONE);
                selected = true;
            }
        }
    }
}
