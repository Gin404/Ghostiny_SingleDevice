package com.example.ghostiny_singledevice.core;

/**
 * 发送包
 */
public abstract class SendPacket extends Packet{
    private boolean isCanceled;

    public abstract byte[] bytes();

    public boolean isCanceled(){
        return isCanceled;
    }
}
