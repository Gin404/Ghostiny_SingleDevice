package com.example.ghostiny_singledevice;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ghostiny_singledevice.core.IoContext;
import com.example.ghostiny_singledevice.impl.IoSelectorProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

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
                if (tcpClient == null){
                    Log.d("tcpClient", "TCPClient is null");
                }
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
     * 本人加入成功：22 <房间人数> <成员昵称列表>
     * 本人离开：23（功能待定）
     * 其他人加入房间：30 <新人数> <加入成员名>
     * 其他人离开房间：31 <新人数> <离开成员名>
     * 其他人在游戏中离开房间：32 <随机消失的颜色> <离开成员名>
     * 房主变更为自己：33
     * 游戏开始：40
     * 本人选择颜色：
     *      选中倒霉色：50
     *      选中安全色：51
     * 锁屏：60
     * 解锁，游戏继续：61
     * 解锁，游戏结束：62 <当前房间人数>
     * @param values
     */
    @Override
    protected void onProgressUpdate(String... values) {
        try {
            JSONObject jsonObject = new JSONObject(values[0]);
            int command = jsonObject.getInt("command");

            switch (command){
                case 10:
                    int roomId = jsonObject.getInt("roomId");
                    listener.onShowRoom(roomId);
                    break;
                case 20:
                    listener.onRefuse();
                    break;
                case 21:
                    listener.onRoomNExist();
                    break;
                case 22:
                    int curNum = jsonObject.getInt("curNum");
                    JSONArray nameList = jsonObject.getJSONArray("names");
                    listener.onJoinRoom(curNum, nameList);
                    break;
                case 30:
                    int curNum1 = jsonObject.getInt("curNum");
                    String nickname = jsonObject.getString("nickName");
                    listener.onMemberJoin(curNum1, nickname);
                    break;
                case 31:
                    int curNum2 = jsonObject.getInt("curNum");
                    String nickname2 = jsonObject.getString("nickName");
                    listener.onMemberLeave(curNum2, nickname2);
                    break;
                case 32:
                    int rmColor = jsonObject.getInt("rmColor");
                    String nickname3 = jsonObject.getString("nickName");
                    listener.onMemberLeave2(rmColor, nickname3);
                    break;
                case 33:
                    listener.onNewOwner();
                    break;
                case 40:
                    listener.onGameStart();
                    break;
                case 51:
                    boolean luck = jsonObject.getBoolean("luck");
                    listener.onLuck(luck);
                    break;
                case 60:
                    int colour = jsonObject.getInt("color");
                    listener.onLock(colour);
                    break;
                case 61:
                    listener.onGameCont();
                    break;
                case 62:
                    int curNum3 = jsonObject.getInt("curNum");
                    listener.onGameEnd(curNum3);
                    break;
                case 63:
                    listener.onGameCont2();
                    break;
                case 64:
                    int curNum4 = jsonObject.getInt("curNum");
                    listener.onGameEnd2(curNum4);
                default:
                    break;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPostExecute(Integer status) {
        // TODO: 02/03/2019
    }

    /**
     * 发送请求
     * 规则：
     * 创建房间：10 <昵称>
     * 加入房间：20 <房间号> <昵称>
     * 开始游戏：30
     * 选择颜色：40 <颜色>
     * 继续：50
     * @param s
     */
    public void send(final String s){
        if (tcpClient == null){
            return;
        }
        new Thread(new Runnable(){
            @Override
            public void run() {
                tcpClient.send(s);
            }
        }).start();
    }

    public void closeChannel(){
        Log.d("tcpClient", "Client closed");
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
