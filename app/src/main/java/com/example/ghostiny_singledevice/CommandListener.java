package com.example.ghostiny_singledevice;

public interface CommandListener {
    //通知游戏开始
    void onGameStart();
    //通知颜色变化
    void onColorChange(String color);
    //通知游戏结束
    void onGameEnd();
    //通知新游戏
    void onNewGame();
    //创建房间
    void onCreateRoom();
    //加入房间
    void onJoinRoom();
    //进入输入房间号界面
    void onJoinInput();
    //输入房间号
    void onInput();
    //退出房间
    void onLeaveRoom();
}
