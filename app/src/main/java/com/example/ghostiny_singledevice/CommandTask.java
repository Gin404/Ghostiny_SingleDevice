package com.example.ghostiny_singledevice;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ghostiny_singledevice.core.IoContext;
import com.example.ghostiny_singledevice.impl.IoSelectorProvider;

import java.io.IOException;

public class CommandTask extends AsyncTask<Void, String, Integer> {
    private CommandListener listener;
    private TCPClient tcpClient;
    private TCPClient.CommandReceiveCallBack commandReceiveCallBack = new TCPClient.CommandReceiveCallBack() {
        @Override
        public void publish(String str) {
            publishProgress(str);
        }
    };

    public CommandTask(CommandListener listener){
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(Void... voids){
        try {
            IoContext.setup()
                    .ioProvider(new IoSelectorProvider())
                    .start();

            tcpClient = null;

            try {
                tcpClient = TCPClient.start();
                assert tcpClient != null;
                tcpClient.setCommandReceiveCallBack(commandReceiveCallBack);
                if (tcpClient == null){
                    return null;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
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
        Log.d("command: ", values[0]);

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
                String[] data = values[0].substring(3).split(" ");
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
        if (tcpClient == null){
            return;
        }

        tcpClient.send(s);
    }

    public void closeChannel(){
        if (tcpClient != null){
            tcpClient.exit();
        }
        try {
            IoContext.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
