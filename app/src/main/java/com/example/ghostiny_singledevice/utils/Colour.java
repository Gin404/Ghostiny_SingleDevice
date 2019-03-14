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
    RED_DARK(154,254,0,50,0,50), DARK(0,50,0,50,0,50), GREEN_DARK(52,152,103,203,0,50),
    ORANGE_LIGHT(205,255,137,237,1,101), ORANGE_DARK(205,255,86,186,0,50), GREEN_LIGHT(103,203,154,254,0,50),
    RED_LIGHT(205,255,18,118,18,118), DARKER_GRAY(120,220,120,220,120,220),BLUE_BRIGHT(0,50,171,255,205,255),
    BLUE_DARK(0,50,103,203,154,254), BLUE_LIGHT(1,101,131,231,179,255), PURPLE(120,220,52,152,154,254);

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
