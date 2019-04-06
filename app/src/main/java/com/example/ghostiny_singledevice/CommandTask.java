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
            client = new Socket("175.159.82.117", 105);

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

    /**
     * 接收服务器指令
     * 规则：
     * 已创建房间：10 <房间号>
     * 本人加入房间但被拒绝（达到人数上限或在游戏中）：20
     * 本人加入房间但被拒绝（房间不存在）：21
     * 本人加入成功：22
     * 本人离开：23（功能待定）
     * 其他人加入房间：30 <新人数>
     * 其他人离开房间：31 <新人数>
     * 其他人在游戏中离开房间：32 <随机消失的颜色>
     * 游戏开始：40
     * 本人选择颜色：
     *      选中倒霉色：50
     *      选中安全色：51
     * 锁屏：60
     * 解锁，游戏继续：61
     * 解锁，游戏结束：62 <玩家类型(房主/成员)> <当前房间人数>
     * @param values
     */
    @Override
    protected void onProgressUpdate(String... values) {
        String command = values[0].substring(0,2);

        switch (command){
            case "10":
                int roomId = Integer.parseInt(values[0].substring(3));
                listener.onShowRoom(roomId);
                break;
            case "20":
                listener.onRefuse();
                break;
            case "21":
                listener.onRoomNExist();
                break;
            case "22":
                int capacity = Integer.parseInt(values[0].substring(3));
                listener.onJoinRoom(capacity);
                break;
            case "30":
                int capacity1 = Integer.parseInt(values[0].substring(3));
                listener.onMemberJoin(capacity1);
                break;
            case "31":
                int capacity2 = Integer.parseInt(values[0].substring(3));
                listener.onMemberLeave(capacity2);
                break;
            case "32":
                int rmColor = Integer.parseInt(values[0].substring(3));
                listener.onMemberLeave2(rmColor);
                break;
            case "40":
                listener.onGameStart();
                break;
            case "50":
                listener.onUnluck();
                break;
            case "51":
                listener.onLuck();
                break;
            case "60":
                int colour = Integer.parseInt(values[0].substring(3));
                listener.onLock(colour);
                break;
            case "61":
                listener.onGameCont();
                break;
            case "62":
                String[] data = values[0].substring(2).split(" ");
                int playerType = Integer.parseInt(data[0]);
                int curNum = Integer.parseInt(data[1]);
                listener.onGameEnd(playerType, curNum);
                break;
            default:
                break;

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
