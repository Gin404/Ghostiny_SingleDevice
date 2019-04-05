package com.example.ghostiny_singledevice.multi;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
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

public class MultiCustomShowActivity extends AppCompatActivity {
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
                Intent intent1;
                if (con){
                    intent1 = new Intent(MultiCustomShowActivity.this, MultiGameActivity.class);
                }else {
                    intent1 = new Intent(MultiCustomShowActivity.this, MainActivity.class);
                }
                startActivity(intent1);
            }
        });


    }
}
