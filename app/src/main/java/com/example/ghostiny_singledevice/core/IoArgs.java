package com.example.ghostiny_singledevice.core;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class IoArgs {
    private int limit = 256;
    private byte[] byteBuffer = new byte[256];
    private ByteBuffer buffer = ByteBuffer.wrap(byteBuffer);

    /**
     * 从bytes中读取数据
     * @param bytes
     * @param offset
     * @return
     */
    public int readFrom(byte[] bytes, int offset){
        int size = Math.min(bytes.length - offset, buffer.remaining());
        buffer.put(bytes, offset, size);
        return size;
    }

    /**
     * 向bytes写入数据
     * @param bytes
     * @param offset
     * @return
     */
    public int writeTo(byte[] bytes, int offset){
        int size = Math.min(bytes.length - offset, buffer.remaining());

        buffer.get(bytes, offset, size);
        return size;
    }

    /**
     * 从socketchannel读数据
     * @param channel
     * @return
     * @throws IOException
     */
    public int readFrom(SocketChannel channel) throws IOException {
        startWriting();
        int bytesProduced = 0;
        while (buffer.hasRemaining()){
            int len = channel.read(buffer);
            if (len < 0){
                throw new EOFException();
            }
            bytesProduced += len;
        }
        finishWriting();

        return bytesProduced;
    }

    /**
     * 向socketchannel写数据
     * @param channel
     * @return
     * @throws IOException
     */
    public int writeTo(SocketChannel channel) throws IOException {
        int bytesProduced = 0;
        while (buffer.hasRemaining()){
            int len = channel.write(buffer);
            if (len < 0){
                throw new EOFException();
            }
            bytesProduced += len;
        }

        return bytesProduced;
    }

    public void startWriting(){
        buffer.clear();
        //定义容纳区间
        buffer.limit(limit);
    }

    public void finishWriting(){
        buffer.flip();
    }

    /**
     * 设置单次写的容纳区间
     * @param limit
     */
    public void setLimit(int limit){
        this.limit = limit;
    }

    public void writeLength(int total) {
        buffer.putInt(total);
    }

    public int readLength(){
        return buffer.getInt();
    }

    public int capacity() {
        return buffer.capacity();
    }

    public interface IoArgsEventListener {
        void onStarted(IoArgs args);

        void onCompleted(IoArgs args);
    }

}

