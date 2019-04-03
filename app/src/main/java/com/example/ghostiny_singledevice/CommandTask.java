package com.example.ghostiny_singledevice;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class CommandTask extends AsyncTask<Void, String, Integer> {
    private CommandListener listener;
    Socket client ;

    public CommandTask(CommandListener listener){
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            //host请自行更改
            client = new Socket("175.159.88.9", 345);

            InputStream inputStream = client.getInputStream();
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buf)) != -1) {
                String s = new String(buf, 0, len);
                publishProgress(s);
                System.out.println(s);
                //buf = new byte[1024];
            }
            inputStream.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        String command = values[0];
        if (command.equals("-start")){
            listener.onGameStart();
        }else if (command.equals("-end")){
            listener.onGameEnd();
        }else if (command.equals("-newGame")){
            listener.onNewGame();
        }else if (command.equals("-createRoom")) {
            listener.onCreateRoom();
        }else if (command.equals("-joinRoom")) {
            listener.onJoinRoom();
        } else if (command.equals("-joinInput")) {
            listener.onJoinInput();
        } else if (command.equals("-input")) {
                listener.onInput();
        }else if (command.equals("-leaveRoom")) {
            listener.onLeaveRoom();
        }
    }

    @Override
    protected void onPostExecute(Integer status) {
        // TODO: 02/03/2019
    }

    public void send(final String s){
        if (client == null){
            return;
        }
        new Thread(new SendService(s)).start();


    }

    private class SendService implements Runnable {
        private String msg;

        SendService(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            OutputStream os = null;
            try {
                os = client.getOutputStream();
                os.write(msg.getBytes());
                //os.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
