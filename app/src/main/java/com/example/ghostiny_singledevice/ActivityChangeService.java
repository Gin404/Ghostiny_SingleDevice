package com.example.ghostiny_singledevice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class ActivityChangeService extends Service {
    private CommandBinder commandBinder = new CommandBinder();
    private CommandTask commandTask;

    private ShowRmIdCallBack showRmIdCallBack;//收到房间号
    private MemberJoinCallBack memberJoinCallBack;//新成员加入
    private MemberLeaveCallBack memberLeaveCallBack;//准备时成员离开
    private MemberLeaveCallBack2 memberLeaveCallBack2;//游戏中成员离开
    private JoinRefuseCallBack joinRefuseCallBack;//房间满或者正在游戏
    private RoomNExistCallBack roomNExistCallBack;//房间不存在
    private JoinRoomCallBack joinRoomCallBack;//加入成功
    private StartCallBack startCallBack;//游戏开始
    private LuckCallBack luckCallBack;//倒霉否
    private LockCallBack lockCallBack;//锁屏
    private ContCallBack contCallBack;//游戏继续
    private EndCallBack endCallBack;//游戏结束


    public void setShowRmIdCallBack(ShowRmIdCallBack showRmIdCallBack){
        this.showRmIdCallBack = showRmIdCallBack;
    }

    public void setMemberJoinCallBack(MemberJoinCallBack memberJoinCallBack){
        this.memberJoinCallBack = memberJoinCallBack;
    }

    public void setMemberLeaveCallBack(MemberLeaveCallBack memberLeaveCallBack){
        this.memberLeaveCallBack = memberLeaveCallBack;
    }

    public void setMemberLeaveCallBack2(MemberLeaveCallBack2 memberLeaveCallBack2){
        this.memberLeaveCallBack2 = memberLeaveCallBack2;
    }

    public void setJoinRefuseCallBack(JoinRefuseCallBack joinRefuseCallBack){
        this.joinRefuseCallBack = joinRefuseCallBack;
    }

    public void setRoomNExistCallBack(RoomNExistCallBack roomNExistCallBack){
        this.roomNExistCallBack = roomNExistCallBack;
    }

    public void setStartCallBack(StartCallBack startCallBack){
        this.startCallBack = startCallBack;
    }

    public void setLuckCallBack(LuckCallBack luckCallBack){
        this.luckCallBack = luckCallBack;
    }

    public void setLockCallBack(LockCallBack lockCallBack){
        this.lockCallBack = lockCallBack;
    }

    public void setContCallBack(ContCallBack contCallBack){
        this.contCallBack = contCallBack;
    }

    public void setEndCallBack(EndCallBack endCallBack){
        this.endCallBack = endCallBack;
    }


    public CommandTask getCommandTask() {
        return commandTask;
    }

    public void setJoinRoomCallBack(JoinRoomCallBack joinRoomCallBack){
        this.joinRoomCallBack=joinRoomCallBack;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        commandTask = new CommandTask(listener);
        commandTask.execute();
    }

    private CommandListener listener = new CommandListener() {
        @Override
        public void onShowRoom(int roomId) {
            showRmIdCallBack.showRmId(roomId);
        }

        @Override
        public void onMemberJoin(int capacity) {
            memberJoinCallBack.memberJoin(capacity);
        }

        @Override
        public void onMemberLeave(int capacity) {
            memberLeaveCallBack.memberLeave(capacity);
        }

        @Override
        public void onMemberLeave2(int rmColor) {
            memberLeaveCallBack2.memberLeave2(rmColor);
        }

        @Override
        public void onRefuse() {
            joinRefuseCallBack.joinRefuse();
        }

        @Override
        public void onRoomNExist() {
            roomNExistCallBack.nExist();
        }

        @Override
        public void onJoinRoom(int capacity) {
            joinRoomCallBack.joinRoom(capacity);
        }


        @Override
        public void onGameStart() {
            startCallBack.skipToGame();
        }

        @Override
        public void onUnluck() {
            luckCallBack.setLuck(false);
        }

        @Override
        public void onLuck() {
            luckCallBack.setLuck(true);
        }

        @Override
        public void onLock(int colour) {
            lockCallBack.setLock(colour);
        }

        @Override
        public void onGameCont() {
            contCallBack.contGame();
        }

        @Override
        public void onGameEnd(int playerType, int curNum) {
            endCallBack.endGame(playerType, curNum);
        }


    };

    public ActivityChangeService() {
    }


    public class CommandBinder extends Binder {
        public ActivityChangeService getService(){
            return ActivityChangeService.this;
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("=====onBind=====");
        return commandBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        commandTask.closeChannel();
    }

    public interface ShowRmIdCallBack{
        void showRmId(int id);
    }

    public interface MemberJoinCallBack{
        void memberJoin(int capacity);
    }

    public interface MemberLeaveCallBack{
        void memberLeave(int capacity);
    }

    public interface MemberLeaveCallBack2{
        void memberLeave2(int rmColor);
    }

    public interface JoinRefuseCallBack{
        void joinRefuse();
    }

    public interface RoomNExistCallBack{
        void nExist();
    }

    public interface JoinRoomCallBack{
        void joinRoom(int capacity);
    }

    public interface StartCallBack{
        void skipToGame();
    }

    public interface EndCallBack{
        void endGame(int playerType, int curNum);
    }

    public interface ContCallBack{
        void contGame();
    }

    public interface LuckCallBack{
        void setLuck(boolean luck);
    }

    public interface LockCallBack{
        void setLock(int colour);
    }
}
