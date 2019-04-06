package com.example.ghostiny_singledevice;

import com.example.ghostiny_singledevice.box.StringReceivePacket;
import com.example.ghostiny_singledevice.core.Connector;
import com.example.ghostiny_singledevice.utils.CloseUtils;
import com.example.ghostiny_singledevice.utils.TCPConstants;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class TCPClient extends Connector {

    private CommandReceiveCallBack commandReceiveCallBack;

    public TCPClient(SocketChannel socketChannel) throws IOException {
        setup(socketChannel);
    }

    public void setCommandReceiveCallBack(CommandReceiveCallBack commandReceiveCallBack){
        this.commandReceiveCallBack = commandReceiveCallBack;
    }

    public void exit(){
        CloseUtils.close(this);
    }

    @Override
    public void onChannelClosed(SocketChannel channel) {
        super.onChannelClosed(channel);
        System.out.println("连接已经关闭，无法读取数据！");
    }

    @Override
    public void onReceiveNewMessage(StringReceivePacket packet){
        super.onReceiveNewMessage(packet);
        this.commandReceiveCallBack.publish(packet.string());
    }


    public static TCPClient start() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        // 连接本地，端口2000；超时时间3000ms
        socketChannel.connect(new InetSocketAddress(Inet4Address.getByName(TCPConstants.IP), TCPConstants.PORT_SERVER));

        System.out.println("已发起服务器连接，并进入后续流程～");
        //System.out.println("客户端信息：" + socketChannel.getLocalAddress().toString());

        //System.out.println("服务器信息：" + socketChannel.getRemoteAddress().toString());

        try {
            return new TCPClient(socketChannel);
        }catch (Exception e){
            System.out.println("连接异常");
            CloseUtils.close(socketChannel);
        }

        return null;
    }

    public interface CommandReceiveCallBack{
        void publish(String str);
    }
}
