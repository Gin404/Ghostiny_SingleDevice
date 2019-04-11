package com.example.ghostiny_singledevice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.json.JSONArray;

public class ActivityChangeService extends Service {
    private CommandBinder commandBinder = new CommandBinder();
    private CommandTask commandTask;

    private ShowRmIdCallBack showRmIdCallBack;//收到房间号
    private MemberJoinCallBack memberJoinCallBack;//新成员加入
    private MemberLeaveCallBack memberLeaveCallBack;//准备时成员离开
    private MemberLeaveCallBack2 memberLeaveCallBack2;//游戏中成员离开
    private NewOwnerCallBack newOwnerCallBack;//成为新房主
    private JoinRefuseCallBack joinRefuseCallBack;//房间满或者正在游戏
    private RoomNExistCallBack roomNExistCallBack;//房间不存在
    private JoinRoomCallBack joinRoomCallBack;//加入成功
    private StartCallBack startCallBack;//游戏开始
    private LuckCallBack luckCallBack;//倒霉否
    private LockCallBack lockCallBack;//锁屏
    private ContCallBack contCallBack;//游戏继续拍照者
    private EndCallBack endCallBack;//游戏结束拍照者
    private ContCallBack2 contCallBack2;//游戏继续
    private EndCallBack2 endCallBack2;//游戏结束

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

    public void setNewOwnerCallBack(NewOwnerCallBack newOwnerCallBack){
        this.newOwnerCallBack = newOwnerCallBack;
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

    public void setContCallBack2(ContCallBack2 contCallBack2){
        this.contCallBack2 = contCallBack2;
    }

    public void setEndCallBack2(EndCallBack2 endCallBack2){
        this.endCallBack2 = endCallBack2;
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
        public void onMemberJoin(int capacity, String nickName) {
            memberJoinCallBack.memberJoin(capacity, nickName);
        }

        @Override
        public void onMemberLeave(int capacity, String nickName) {
            memberLeaveCallBack.memberLeave(capacity, nickName);
        }

        @Override
        public void onMemberLeave2(int rmColor, String nickName) {
            memberLeaveCallBack2.memberLeave2(rmColor, nickName);
        }

        @Override
        public void onNewOwner() {
            newOwnerCallBack.asNewOwner();
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
        public void onJoinRoom(int capacity, JSONArray nameList) {
            joinRoomCallBack.joinRoom(capacity, nameList);
        }


        @Override
        public void onGameStart() {
            startCallBack.skipToGame();
        }

        @Override
        public void onLuck(boolean luck) {
            luckCallBack.setLuck(luck);
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
        public void onGameCont2() {
            contCallBack2.contGame2();
        }

        @Override
        public void onGameEnd(int curNum) {
            endCallBack.endGame(curNum);
        }

        @Override
        public void onGameEnd2(int curNum) {
            endCallBack2.endGame2(curNum);
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
    public boolean onUnbind(Intent intent) {
        System.out.println("=====onUnBind=====");
        return super.onUnbind(intent);
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
        void memberJoin(int capacity, String nickName);
    }

    public interface MemberLeaveCallBack{
        void memberLeave(int capacity, String nickName);
    }

    public interface MemberLeaveCallBack2{
        void memberLeave2(int rmColor, String nickName);
    }

    public interface NewOwnerCallBack{
        void asNewOwner();
    }

    public interface JoinRefuseCallBack{
        void joinRefuse();
    }

    public interface RoomNExistCallBack{
        void nExist();
    }

    public interface JoinRoomCallBack{
        void joinRoom(int capacity, JSONArray nameList);
    }

    public interface StartCallBack{
        void skipToGame();
    }

    public interface EndCallBack{
        void endGame(int curNum);
    }

    public interface ContCallBack{
        void contGame();
    }

    public interface EndCallBack2{
        void endGame2(int curNum);
    }

    public interface ContCallBack2{
        void contGame2();
    }

    public interface LuckCallBack{
        void setLuck(boolean luck);
    }

    public interface LockCallBack{
        void setLock(int colour);
    }
}
