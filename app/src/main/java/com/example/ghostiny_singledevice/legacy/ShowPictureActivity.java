package com.example.ghostiny_singledevice.legacy;

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

import com.example.ghostiny_singledevice.GameActivity;
import com.example.ghostiny_singledevice.MainActivity;
import com.example.ghostiny_singledevice.R;
import com.example.ghostiny_singledevice.utils.Colour;
import com.example.ghostiny_singledevice.utils.ImageTools;

import java.io.FileNotFoundException;

public class ShowPictureActivity extends AppCompatActivity {
    private ImageView picture;
    private boolean hit;
    private Bitmap res;
    private Button cont;
    private boolean con = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);

        cont = (Button)findViewById(R.id.cont);
        picture = (ImageView) findViewById(R.id.photo);
        Intent intent = getIntent();
        String imageUri = intent.getStringExtra("photo");
        Colour choice = (Colour)intent.getSerializableExtra("color");
        Colour unluck = (Colour)intent.getSerializableExtra("unluck");
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(imageUri)));

            Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.screamicon);

            if (choice.equals(unluck)){
                res = ImageTools.merge(bitmap, icon);
                con = false;
                cont.setText("Menu");
            }else {
                hit = ImageTools.colorRecg(bitmap, choice, 0.1);
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





            picture.setImageBitmap(res);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        picture.setOnTouchListener(new View.OnTouchListener() {
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
                    intent1 = new Intent(ShowPictureActivity.this, GameActivity.class);
                }else {
                    intent1 = new Intent(ShowPictureActivity.this, MainActivity.class);
                }
            }
        });
    }
}
