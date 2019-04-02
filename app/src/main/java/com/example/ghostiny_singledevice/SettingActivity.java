package com.example.ghostiny_singledevice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

public class SettingActivity extends AppCompatActivity {

    Switch switch1,switch2;
    AudioManager audioManager;
    Vibrator vibrator;
    ImageView mail;
    ImageButton back;
    SharedPreferences spre,vpre;
    final boolean falg = true,f=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        switch1=(Switch)findViewById(R.id.soundswitch);
        switch2=(Switch)findViewById(R.id.vibrationswitch);
        audioManager=(AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        vibrator = (Vibrator)this.getSystemService(this.VIBRATOR_SERVICE);
        back=(ImageButton) findViewById(R.id.returnbutton);

        /*public void onSwitchClicked(View view) {
            int id=view.getId();
            switch(id){
                case R.id.switch1:
                    if(switch1.isChecked()) {
                        //textview.setText("Switch 1 check ON by click on it");
                        audioManager.setSpeakerphoneOn(true);
                    }
                    else {
                        //textview.setText("Switch 1 check OFF by click on it");
                        audioManager.setSpeakerphoneOn(false);
                    }
                    break;
                case R.id.switch2:
                    if(switch2.isChecked()) {
                        vibrator.vibrate(1000);
                    }
                    else {
                        vibrator.cancel();
                    }
                    break;
            }
        }*/

        int maxStreamRing = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING); //最大音量
        final int curStreamRing = audioManager.getStreamVolume(AudioManager.STREAM_RING);//当前音量

        spre = getSharedPreferences("user", Context.MODE_PRIVATE);
        if (spre != null) {
            boolean name = spre.getBoolean("flag", falg);
            switch1.setChecked(name);
        }

        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switch1.isChecked()) {
                    //textview.setText("Switch 1 check ON by click on it");
                    audioManager.setSpeakerphoneOn(true);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,curStreamRing,AudioManager.FLAG_SHOW_UI);
                    SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("flag", true);
                    editor.commit();
                }
                else {
                    //textview.setText("Switch 1 check OFF by click on it");
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,AudioManager.FLAG_SHOW_UI);
                    SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("flag", false);
                    editor.commit();
                }
            }
        });


        vpre = getSharedPreferences("user", Context.MODE_PRIVATE);
        if (vpre != null) {
            boolean name = vpre.getBoolean("vf", f);
            switch2.setChecked(name);
        }

        switch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switch2.isChecked()) {
                    vibrator.vibrate(1000);
                    SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("vf", true);
                    editor.commit();
                }
                else {
                    vibrator.cancel();
                    SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("vf", false);
                    editor.commit();
                }
            }
        });




        mail = (ImageView) findViewById(R.id.feedbackimage);
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNormalDialog();
            }
        });

        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(SettingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }



    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(SettingActivity.this);
        /*normalDialog.setIcon(R.drawable.icon_dialog);*/
        normalDialog.setTitle("FEEDBACK");
        normalDialog.setMessage("You can send email to us.E-mail:Ghostiny@gmail.com.");
        normalDialog.setPositiveButton("send",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        Uri uri=Uri.parse("mailto:Ghostiny@gmail.com");
                        Intent it=new Intent(Intent.ACTION_SENDTO,uri);
                        startActivity(it);
                    }
                });
        normalDialog.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        dialog.dismiss();
                    }
                });
        // 显示
        normalDialog.show();
    }




}
