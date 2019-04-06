package com.example.ghostiny_singledevice.box;

import com.example.ghostiny_singledevice.core.SendPacket;

import java.io.IOException;

public class StringSendPacket extends SendPacket {
    private final byte[] bytes;

    public StringSendPacket(String msg){
        this.bytes = msg.getBytes();
        this.length = bytes.length;
    }
    @Override
    public byte[] bytes() {
        return bytes;
    }

    @Override
    public void close() throws IOException {

    }
}

