package com.example.ghostiny_singledevice.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;

/**
 * 图像工具类
 */
public class ImageTools {
    /**
     * 融合
     * @param src
     * @param waterMark
     * @return
     */
    public static Bitmap merge(Bitmap src, Bitmap waterMark){
        if( src == null ) {
            return null;
        }

        int w = src.getWidth();
        int h = src.getHeight();
        int ww = waterMark.getWidth();
        int wh = waterMark.getHeight();

        float scaleW = ((float) w)/ww;
        float scaleH = ((float)h)/wh;

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        matrix.postScale(scaleW, scaleH);
        Bitmap wm = Bitmap.createBitmap(waterMark, 0, 0, ww, wh, matrix, true);

        Bitmap newb = Bitmap.createBitmap( w, h, Bitmap.Config.ARGB_8888 );

        Canvas cv = new Canvas(newb);

        cv.drawBitmap( src, 0, 0, null );

        cv.drawBitmap( wm, -200, 200, null );


        return newb;
    }

    /**
     * 颜色识别
     * @param bitmap
     * @param colour
     * @param threshold
     * @return
     */
    public static boolean colorRecg(Bitmap bitmap, Colour colour, float threshold){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int total = w*h;
        int hits = 0;
        int r,g,b;



        for (int i = 0; i < h; i++){
            for (int j = 0; j < w; j++){
                int pixel = bitmap.getPixel(i, j);
                r = Color.red(pixel);
                g = Color.green(pixel);
                b = Color.blue(pixel);

                if (r < colour.getRh() && r > colour.getRl() &&
                        g < colour.getGh() && g > colour.getGl() &&
                        b < colour.getBh() && b > colour.getBl()){
                    ++hits;
                }
            }
        }

        float ratio = ((float) hits)/total;

        if (!(ratio < threshold)){
            return true;
        }
        return false;
    }


}
