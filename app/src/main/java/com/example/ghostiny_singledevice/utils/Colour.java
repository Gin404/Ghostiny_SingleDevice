package com.example.ghostiny_singledevice.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 颜色枚举类
 * 颜色与rgb上下限与代号
 */
public enum Colour {
    RED(255,170,88,0,88,0,1),//red
    BLACK(50,0,50,0,50,0,2),//black
    GREEN(100,0,255,10,100,0,4),//green
    YELLOW(255,140,255,140,117,0,8),//yellow
    ORANGE(255,150,180,75,110,0,16), //orange
    CYAN(200,0,255,100,255,100,32),//qing
    PINK(255,190,160,0,210,96,64),//pink
    GRAY(215,54,215,54,215,54,128),//gray
    BLUE(70,0,70,0,255,70,256),//blue
    BROWN(235,40,107,20,80,0,512),//brown
    WHITE(255,150,255,150,255,150,1024), //white
    PURPLE(255,70,120,0,255,70,2048);//purple

    private int rh, rl, gh, gl, bh, bl, code;
    private static final List<Colour> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    Colour(int rh, int rl, int gh, int gl, int bh, int bl, int code){
        this.rh = rh;
        this.rl = rl;
        this.gh = gh;
        this.gl = gl;
        this.bh = bh;
        this.bl = bl;
        this.code = code;
    }

    public int getRh() {
        return rh;
    }

    public void setRh(int rh) {
        this.rh = rh;
    }

    public int getRl() {
        return rl;
    }

    public void setRl(int rl) {
        this.rl = rl;
    }

    public int getGh() {
        return gh;
    }

    public void setGh(int gh) {
        this.gh = gh;
    }

    public int getGl() {
        return gl;
    }

    public void setGl(int gl) {
        this.gl = gl;
    }

    public int getBh() {
        return bh;
    }

    public void setBh(int bh) {
        this.bh = bh;
    }

    public int getBl() {
        return bl;
    }

    public void setBl(int bl) {
        this.bl = bl;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 返回随机枚举值
     * @return
     */
    public static Colour randomColour()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    public static Colour getNameByCode(int code){
        for (Colour colour : values()){
            if (colour.getCode() == code){
                return colour;
            }
        }

        return null;
    }
}
