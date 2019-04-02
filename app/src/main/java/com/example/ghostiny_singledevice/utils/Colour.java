package com.example.ghostiny_singledevice.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 颜色枚举类
 * 颜色与rgb上下限
 */
public enum Colour {
    RED(255,170,88,0,88,0),//red
    BLACK(50,0,50,0,50,0),//black
    GREEN(100,0,255,10,100,0),//green
    YELLOW(255,140,255,140,117,0),//yellow
    ORANGE(255,150,180,75,110,0), //orange
    CYAN(200,0,255,100,255,100),//qing
    PINK(255,190,160,0,210,96),//pink
    GRAY(215,54,215,54,215,54),//gray
    BLUE(70,0,70,0,255,70),//blue
    BROWN(235,40,107,20,80,0),//brown
    WHITE(255,150,255,150,255,150), //white
    PURPLE(255,70,120,0,255,70);//purple

    private int rh, rl, gh, gl, bh, bl;
    private static final List<Colour> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    Colour(int rh, int rl, int gh, int gl, int bh, int bl){
        this.rh = rh;
        this.rl = rl;
        this.gh = gh;
        this.gl = gl;
        this.bh = bh;
        this.bl = bl;
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

    /**
     * 返回随机枚举值
     * @return
     */
    public static Colour randomColour()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
