package com.example.ghostiny_singledevice.core;

import java.io.Closeable;
import java.io.IOException;

public abstract class Packet implements Closeable {
    protected byte type;
    protected int length;

    public byte type() {
        return type;
    }

    public int length() {
        return length;
    }

}
