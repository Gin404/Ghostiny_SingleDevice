package com.example.ghostiny_singledevice.multi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ghostiny_singledevice.R;

public class MultiNickNameActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private EditText nickNameET;
    private Button confirmBtn;
    private TextView TV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_nick_name);
        nickNameET = (EditText)findViewById(R.id.nickname_text);
        confirmBtn = (Button)findViewById(R.id.confirm_btn);
        TV = (TextView)findViewById ( R.id.title );
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);

        //set font
        AssetManager mgr=getAssets();  //设置字体
        Typeface typeface=Typeface.createFromAsset(mgr,"font/TM.ttf");
        nickNameET.setTypeface(typeface);
        TV.setTypeface(typeface);
        confirmBtn.setTypeface(typeface);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickName = nickNameET.getText().toString();
                if (nickName.length() == 0){
                    Toast.makeText(getApplicationContext(), "暱稱不能為空",Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("myName", nickName);
                editor.apply();

                Intent intent = new Intent(MultiNickNameActivity.this, MultiplayerActivity.class);
                startActivity(intent);
            }
        });
    }
}
