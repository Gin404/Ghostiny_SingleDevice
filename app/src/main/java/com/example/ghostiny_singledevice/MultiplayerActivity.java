package com.example.ghostiny_singledevice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MultiplayerActivity extends AppCompatActivity {

    private Button create,join;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        create=(Button)findViewById(R.id.createbtn);
        join=(Button)findViewById(R.id.joinbtn);


        create.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MultiplayerActivity.this, MultiNumberActivity.class);
                startActivity(intent);
            }
        });

        join.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MultiplayerActivity.this, MultiRoomJoinActivity.class);
                startActivity(intent);
            }
        });
    }
}
