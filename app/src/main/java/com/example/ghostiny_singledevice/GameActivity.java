package com.example.ghostiny_singledevice;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ghostiny_singledevice.utils.Colour;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO = 1;

    private Uri imageUri;
    private Colour colour, unluck;
    private Button takePhoto;
    private TextView textView;
    private boolean selected = false;

    private Button RED_DARK, DARK, GREEN_DARK,
            ORANGE_LIGHT, ORANGE_DARK, GREEN_LIGHT,
            RED_LIGHT, DARKER_GRAY, BLUE_BRIGHT,
            BLUE_DARK, BLUE_LIGHT, PURPLE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //倒霉色
        unluck = Colour.randomColour();

        takePhoto = (Button) findViewById(R.id.camerabutton);

        RED_DARK = (Button) findViewById(R.id.colorbutton1);
        DARK = (Button) findViewById(R.id.colorbutton2);
        GREEN_DARK = (Button) findViewById(R.id.colorbutton3);
        ORANGE_LIGHT = (Button) findViewById(R.id.colorbutton4);
        ORANGE_DARK = (Button) findViewById(R.id.colorbutton5);
        GREEN_LIGHT = (Button) findViewById(R.id.colorbutton6);
        RED_LIGHT = (Button) findViewById(R.id.colorbutton7);
        DARKER_GRAY = (Button) findViewById(R.id.colorbutton8);
        BLUE_BRIGHT = (Button) findViewById(R.id.colorbutton9);
        BLUE_DARK= (Button) findViewById(R.id.colorbutton10);
        BLUE_LIGHT = (Button) findViewById(R.id.colorbutton11);
        PURPLE= (Button) findViewById(R.id.colorbutton12);

        textView = (TextView)findViewById(R.id.selectColor);

        RED_DARK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Dark Red");
                colour = Colour.RED_DARK;
                RED_DARK.setVisibility(View.GONE);
                selected = true;
            }
        });

        DARK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Black");
                colour = Colour.DARK;
                DARK.setVisibility(View.GONE);
                selected = true;
            }
        });
        GREEN_DARK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Dark Green");
                colour = Colour.GREEN_DARK;
                GREEN_DARK.setVisibility(View.GONE);
                selected = true;
            }
        });
        ORANGE_LIGHT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Light Orange");
                colour = Colour.ORANGE_LIGHT;
                ORANGE_LIGHT.setVisibility(View.GONE);
                selected = true;
            }
        });
        ORANGE_DARK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Dark Orange");
                colour = Colour.ORANGE_DARK;
                ORANGE_DARK.setVisibility(View.GONE);
                selected = true;
            }
        });
        GREEN_LIGHT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Light Green");
                colour = Colour.GREEN_LIGHT;
                GREEN_LIGHT.setVisibility(View.GONE);
                selected = true;
            }
        });
        RED_LIGHT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Light Red");
                colour = Colour.RED_LIGHT;
                RED_LIGHT.setVisibility(View.GONE);
                selected = true;
            }
        });

        DARKER_GRAY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Dark Gray");
                colour = Colour.DARKER_GRAY;
                DARKER_GRAY.setVisibility(View.GONE);
                selected = true;
            }
        });

        BLUE_BRIGHT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Bright Blue");
                colour = Colour.BLUE_BRIGHT;
                BLUE_BRIGHT.setVisibility(View.GONE);
                selected = true;
            }
        });
        BLUE_DARK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Dark Blue");
                colour = Colour.BLUE_DARK;
                BLUE_DARK.setVisibility(View.GONE);
                selected = true;
            }
        });
        BLUE_LIGHT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Light Blue");
                colour = Colour.BLUE_LIGHT;
                BLUE_LIGHT.setVisibility(View.GONE);
                selected = true;
            }
        });

        PURPLE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Purple");
                colour = Colour.PURPLE;
                PURPLE.setVisibility(View.GONE);
                selected = true;
            }
        });



        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected == false){
                    Toast.makeText(getApplicationContext(), "请选择颜色",Toast.LENGTH_SHORT).show();
                    return;
                }
                // 创建File对象，用于存储拍照后的图片
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT < 24) {
                    imageUri = Uri.fromFile(outputImage);
                } else {
                    imageUri = FileProvider.getUriForFile(GameActivity.this, "com.example.ghostiny_singledevice.fileprovider", outputImage);
                }
                // 启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
            }


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Intent intent = new Intent(GameActivity.this, ShowPictureActivity.class);
                        intent.putExtra("photo", imageUri.toString());
                        intent.putExtra("color", colour);
                        intent.putExtra("unluck", unluck);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("newIntent", "game new intent");
    }
}
