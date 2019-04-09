package com.example.ghostiny_singledevice.single;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ghostiny_singledevice.ActivityChangeService;
import com.example.ghostiny_singledevice.MainActivity;
import com.example.ghostiny_singledevice.R;
import com.example.ghostiny_singledevice.multi.MultiGameActivity;
import com.example.ghostiny_singledevice.utils.Colour;


public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView  back1;
    public static final int TAKE_PHOTO = 1;

    private Colour colour, unluck;
    private Button takePhoto;
    private TextView textView;
    private boolean selected = false;
    private int colorNum;
    private Button toBeInvis;

    private final Colour[] colours = Colour.values();

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        back1=(ImageView)findViewById(R.id.back1) ;
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent (GameActivity.this, SingleNumberActivity.class);
                startActivity(intent);
            }
        });



        Intent pIntent = getIntent();
        colorNum = pIntent.getIntExtra("num", 12);

        //倒霉色
        unluck = Colour.randomColour(colorNum);
        Log.d("gameactivy", unluck.toString() + " is unlucky");

        takePhoto = (Button) findViewById(R.id.camerabutton);
        textView = (TextView)findViewById(R.id.selectColor);

        //将后12-colourNum个颜色按钮置为隐形
        Resources res = getResources();
        Button temp = null;
        for (int i = colorNum; i < colours.length; i++){
            temp = findViewById(res.getIdentifier(colours[i].toString().toLowerCase(),"id",getPackageName()));
            temp.setVisibility(View.INVISIBLE);  //View.INVISIBLE  Not visible but still in position
        }


        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected == false){
                    Toast.makeText(getApplicationContext(), "",Toast.LENGTH_SHORT).show();
                    return;
                }
                toBeInvis.setVisibility(View.INVISIBLE); //View.INVISIBLE  Not visible but still in position

                // 启动相机程序
                Intent intent = new Intent(GameActivity.this, CustomCameraActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("unluck", unluck);
                bundle.putSerializable("choice", colour);
                intent.putExtras(bundle);
                startActivity(intent);
            }


        });
        AssetManager mgr=getAssets();   //设置字体
        Typeface typeface=Typeface.createFromAsset(mgr,"font/TM.ttf");
        takePhoto.setTypeface(typeface);
        textView.setTypeface ( typeface );
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
                toBeInvis = (Button) v;
                selected = true;
            }
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(GameActivity.this, MainActivity.class));
    }
}
