package com.example.ghostiny_singledevice.single;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ghostiny_singledevice.MainActivity;
import com.example.ghostiny_singledevice.R;
import com.example.ghostiny_singledevice.utils.Colour;
import com.example.ghostiny_singledevice.utils.ImageTools;

import java.io.FileNotFoundException;

public class CustomShowActivity extends AppCompatActivity {
    private ImageView photo;
    private boolean hit;
    private Bitmap res;
    private Button cont;
    private boolean con = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_show);

        photo = (ImageView)findViewById(R.id.custom_photo);
        cont = (Button)findViewById(R.id.cont);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        Colour choice = (Colour)bundle.getSerializable("choice");
        Colour unluck = (Colour)bundle.getSerializable("unluck");
        String imageUri = bundle.getString("photoPath");


        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(imageUri)));
            Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.screamicon);

            bitmap = ImageTools.rotate(bitmap, 90);

            if (choice.equals(unluck)){
                res = ImageTools.merge(bitmap, icon);
                con = false;
                cont.setText("退出遊戲");
            }else {
                cont.setText("繼續遊戲");
                con = true;
                hit = ImageTools.colorRecg(bitmap, choice, 0.25);
                if (hit){
                    res = bitmap;
                }else {
                    res = ImageTools.merge(bitmap, icon);
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
                Intent intent1;
                if (con){
                    intent1 = new Intent(CustomShowActivity.this, GameActivity.class);
                }else {
                    intent1 = new Intent(CustomShowActivity.this, MainActivity.class);
                }
                startActivity(intent1);
            }
        });
    }
}
