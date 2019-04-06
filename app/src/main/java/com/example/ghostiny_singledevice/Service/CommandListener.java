package com.example.ghostiny_singledevice.Service;

public interface CommandListener {
    //通知游戏开始
    void onGameStart();
    //通知颜色变化
    void onColorChange(String color);
    //通知游戏结束
    void onGameEnd();
    //通知新游戏
    void onNewGame();
}
