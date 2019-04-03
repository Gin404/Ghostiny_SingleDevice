package com.example.ghostiny_singledevice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class ActivityChangeService extends Service {
    private CommandBinder commandBinder = new CommandBinder();
    private CommandTask commandTask;
    private StartCallBack startCallBack;

    private EndCallBack endCallBack;
    private NewGameCallBack newGameCallBack;
    private CreateRoomCallBack createRoomCallBack;
    private JoinRoomCallBack joinRoomCallBack;

    private LeaveRoomCallBack leaveRoomCallBack;

    public void setStartCallBack(StartCallBack startCallBack){
        this.startCallBack = startCallBack;
    }




    public void setEndCallBack(EndCallBack endCallBack){
        this.endCallBack = endCallBack;
    }

    public void setNewGameCallBack(NewGameCallBack newGameCallBack){
        this.newGameCallBack = newGameCallBack;
    }

    public void setCreateRoomCallBack(CreateRoomCallBack createRoomCallBack){
        this.createRoomCallBack=createRoomCallBack;
    }

    public CommandTask getCommandTask() {
        return commandTask;
    }

    public void setJoinRoomCallBack(JoinRoomCallBack joinRoomCallBack){
        this.joinRoomCallBack=joinRoomCallBack;
    }


    public void setLeaveRoomCallBack(LeaveRoomCallBack leaveRoomCallBack){
        this.leaveRoomCallBack=leaveRoomCallBack;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        commandTask = new CommandTask(listener);
        commandTask.execute();
    }

    private CommandListener listener = new CommandListener() {
        @Override
        public void onGameStart() {
            startCallBack.skipToGame();
        }

        @Override
        public void onGameEnd() {
            endCallBack.endGame();
        }

        @Override
        public void onNewGame(){
            newGameCallBack.newGame();
        }

        @Override
        public void onCreateRoom(){
            createRoomCallBack.createRoom();
        }

        @Override
        public void onJoinRoom(){
            joinRoomCallBack.joinRoom();
        }


        @Override
        public void onLeaveRoom(){
            leaveRoomCallBack.leaveRoom();
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

    public interface StartCallBack{
        void skipToGame();
    }



    public interface EndCallBack{
        void endGame();
    }

    public interface NewGameCallBack{
        void newGame();
    }

    public interface CreateRoomCallBack{
        void createRoom();
    }

    public interface JoinRoomCallBack{
        void joinRoom();
    }


    public interface LeaveRoomCallBack{
        void leaveRoom();
    }
}
