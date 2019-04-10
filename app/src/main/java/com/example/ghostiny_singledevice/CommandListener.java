package com.example.ghostiny_singledevice;

import org.json.JSONArray;

public interface CommandListener {
    //通知房间号
    void onShowRoom(int id);

    //新成员加入
    void onMemberJoin(int capacity, String nickName);

    //准备时成员离开
    void onMemberLeave(int capacity, String nickName);

    //游戏中成员离开
    void onMemberLeave2(int rmColor, String nickName);

    //成为新房主
    void onNewOwner();

    //加入失败（人满或正在游戏）
    void onRefuse();

    //加入失败（房间不存在）
    void onRoomNExist();

    //加入房间成功
    void onJoinRoom(int capacity, JSONArray nameList);

    //通知游戏开始
    void onGameStart();

    //通知走运
    void onLuck(boolean luck);

    //通知锁屏
    void onLock(int colour);

    //通知游戏继续
    void onGameCont();

    //通知游戏结束
    void onGameEnd(int curNum);

}
