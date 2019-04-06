package com.example.ghostiny_singledevice.core;


import com.example.ghostiny_singledevice.box.StringReceivePacket;
import com.example.ghostiny_singledevice.box.StringSendPacket;
import com.example.ghostiny_singledevice.impl.SocketChannelAdapter;
import com.example.ghostiny_singledevice.impl.async.AsyncReceiveDispatcher;
import com.example.ghostiny_singledevice.impl.async.AsyncSendDispatcher;
import com.example.ghostiny_singledevice.utils.CloseUtils;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.UUID;

/**
 * 连接，包含接收方和发送方
 */

public class Connector implements Closeable , SocketChannelAdapter.OnChannelStatusChangedListener{
    private UUID key = UUID.randomUUID();
    private SocketChannel channel;
    private Sender sender;
    private Receiver receiver;
    private SendDispatcher sendDispatcher;
    private ReceiveDispatcher receiveDispatcher;


    public void setup(SocketChannel socketChannel) throws IOException {
        this.channel = socketChannel;

        IoContext context = IoContext.get();
        //
        SocketChannelAdapter adapter = new SocketChannelAdapter(channel, context.getIoProvider(), this);

        this.sender = adapter;
        this.receiver = adapter;

        sendDispatcher = new AsyncSendDispatcher(sender);
        receiveDispatcher = new AsyncReceiveDispatcher(receiver, receivePacketCallback);

        //启动接收
        receiveDispatcher.start();

    }

    public void send(String msg){
        SendPacket packet = new StringSendPacket(msg);
        sendDispatcher.send(packet);
    }


    @Override
    public void close() throws IOException {
        receiveDispatcher.close();
        sendDispatcher.close();
        sender.close();
        receiver.close();
        channel.close();
    }

    @Override
    public void onChannelClosed(SocketChannel channel) {
        CloseUtils.close(this);
    }

    public void onReceiveNewMessage(StringReceivePacket packet){
        System.out.println(key.toString() + ": length: " + packet.length + "content: " + packet.string());
    }

    private ReceiveDispatcher.ReceivePacketCallback receivePacketCallback = new ReceiveDispatcher.ReceivePacketCallback() {
        @Override
        public void onReceivePacketCompleted(StringReceivePacket packet) {
            if (packet instanceof StringReceivePacket){
                onReceiveNewMessage(packet);
            }
        }
    };

    public UUID getKey() {
        return key;
    }
}

