package com.example.ghostiny_singledevice.multi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ghostiny_singledevice.R;

public class MultiNickNameActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private EditText nickNameET;
    private Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_nick_name);
        nickNameET = (EditText)findViewById(R.id.nickname_text);
        confirmBtn = (Button)findViewById(R.id.confirm_btn);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickName = nickNameET.getText().toString();
                if (nickName.length() == 0){
                    Toast.makeText(getApplicationContext(), "Nick name cannot be null",Toast.LENGTH_SHORT).show();
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
