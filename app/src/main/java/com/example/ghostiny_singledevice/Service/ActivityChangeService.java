package com.example.ghostiny_singledevice.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class ActivityChangeService extends Service {
    private CommandBinder commandBinder = new CommandBinder();
    private CommandTask commandTask;
    private StartCallBack startCallBack;
    private ColorRemoveCallBack colorRemoveCallBack;
    private EndCallBack endCallBack;
    private NewGameCallBack newGameCallBack;

    public void setStartCallBack(StartCallBack startCallBack){
        this.startCallBack = startCallBack;
    }

    public void setColorRemoveCallBack(ColorRemoveCallBack colorRemoveCallBack){
        this.colorRemoveCallBack = colorRemoveCallBack;
    }

    public CommandTask getCommandTask() {
        return commandTask;
    }

    public void setEndCallBack(EndCallBack endCallBack){
        this.endCallBack = endCallBack;
    }

    public void setNewGameCallBack(NewGameCallBack newGameCallBack){this.newGameCallBack = newGameCallBack;}

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
        public void onColorChange(String color) {
            colorRemoveCallBack.removeColor(color);
        }

        @Override
        public void onGameEnd() {
            endCallBack.endGame();
        }

        @Override
        public void onNewGame(){
            newGameCallBack.newGame();
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

    public interface ColorRemoveCallBack{
        void removeColor(String color);
    }

    public interface EndCallBack{
        void endGame();
    }

    public interface NewGameCallBack{
        void newGame();
    }
}
