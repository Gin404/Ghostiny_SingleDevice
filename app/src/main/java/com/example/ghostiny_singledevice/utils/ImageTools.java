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

        int w = src.getWidth();//480
        int h = src.getHeight();//640
        int ww = waterMark.getWidth();//1856
        int wh = waterMark.getHeight();//2625

        float scaleW = ((float) h)/ww;
        float scaleH = ((float)w)/wh;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleW, scaleH);
        matrix.postRotate(90);
        Bitmap wm = Bitmap.createBitmap(waterMark, 0, 0, ww, wh, matrix, true);

        Bitmap newb = Bitmap.createBitmap( w, h, Bitmap.Config.ARGB_8888 );

        Canvas cv = new Canvas(newb);

        cv.drawBitmap( src, 0, 0, null );

        cv.drawBitmap( wm, 0, 0, null );


        return newb;
    }

    /**
     * 颜色识别
     * @param bitmap
     * @param colour
     * @param threshold
     * @return
     */
    public static boolean colorRecg(Bitmap bitmap, Colour colour, double threshold){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int total = w*h;
        int hits = 0;
        int r,g,b;



        for (int i = 0; i < w; i++){
            for (int j = 0; j < h; j++){
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

        double ratio = ((double) hits)/total;

        if (!(ratio < threshold)){
            return true;
        }
        return false;
    }

    public static Bitmap rotate(Bitmap bitmap, float degree){
        if (bitmap == null){
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.setRotate(degree);

        Bitmap newBM = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        if (newBM.equals(bitmap)) {
            return newBM;
        }
        bitmap.recycle();
        return newBM;
    }


}
