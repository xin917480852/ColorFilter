package com.example.materialdwsign;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * Created by Administrator on 2014/12/20 0020.
 */
//������
public class ImageHelper {

    public static Bitmap handleImageEffect(Bitmap bm, float hue, float saturation, float lum) {
        //获取一个大小和bm一样的可编辑的空图片
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        //画布 是大小为bmp的空图片
        Canvas canvas = new Canvas(bmp);
        //画笔 设置了抵锯齿
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //颜色矩阵 关于hue的
        ColorMatrix hueMatrix = new ColorMatrix();
        hueMatrix.setRotate(0, hue);//0代表红色
        hueMatrix.setRotate(1, hue);//1代表绿色
        hueMatrix.setRotate(2, hue);//2代表蓝色
        //饱和度的颜色矩阵
        ColorMatrix saturationMatrix = new ColorMatrix();
        //设置图片的饱和度
        saturationMatrix.setSaturation(saturation);
        //亮度的颜色矩阵
        ColorMatrix lumMatrix = new ColorMatrix();
	    //设置图片的亮度
        lumMatrix.setScale(lum, lum, lum, 1);
        //imageMatrix包含这三个属性
        ColorMatrix imageMatrix = new ColorMatrix();
        imageMatrix.postConcat(hueMatrix);
        imageMatrix.postConcat(saturationMatrix);
        imageMatrix.postConcat(lumMatrix);
        //给画笔添加颜色过滤
        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
        //在画布上用paint重新画图
        canvas.drawBitmap(bm, 0, 0, paint);
        return bmp;
    }

    public static Bitmap handleImageNegative(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        int color;
        int r, g, b, a;

        Bitmap bmp = Bitmap.createBitmap(width, height
                , Bitmap.Config.ARGB_8888);
        //像素点保存到数组里面
        int[] oldPx = new int[width * height];
        int[] newPx = new int[width * height];

//        public void getPixels (int[] pixels, int offset, int stride, int x, int y, int width, int height)
//        参数
//        pixels      接收位图颜色值的数组
//        offset      写入到pixels[]中的第一个像素索引值
//        stride      pixels[]中的行间距个数值(必须大于等于位图宽度)。可以为负数
//        x          　从位图中读取的第一个像素的x坐标值。
//        y           从位图中读取的第一个像素的y坐标值
//        width    　　从每一行中读取的像素宽度
//        height 　　　读取的行数
        bm.getPixels(oldPx, 0, width, 0, 0, width, height);
            //取出每一个像素点然后对它的ARGB分别进行操作
        for (int i = 0; i < width * height; i++) {
            color = oldPx[i];
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            a = Color.alpha(color);
            //算法
            r = 255 - r;
            g = 255 - g;
            b = 255 - b;

            if (r > 255) {
                r = 255;
            } else if (r < 0) {
                r = 0;
            }
            if (g > 255) {
                g = 255;
            } else if (g < 0) {
                g = 0;
            }
            if (b > 255) {
                b = 255;
            } else if (b < 0) {
                b = 0;
            }
            newPx[i] = Color.argb(a, r, g, b);
        }
        //设置它的像素点
        bmp.setPixels(newPx, 0, width, 0, 0, width, height);
        return bmp;
    }

    public static Bitmap handleImagePixelsOldPhoto(Bitmap bm) {
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
                Bitmap.Config.ARGB_8888);
        int width = bm.getWidth();
        int height = bm.getHeight();
        int color = 0;
        int r, g, b, a, r1, g1, b1;

        int[] oldPx = new int[width * height];
        int[] newPx = new int[width * height];

        bm.getPixels(oldPx, 0, bm.getWidth(), 0, 0, width, height);
        for (int i = 0; i < width * height; i++) {
            color = oldPx[i];
            a = Color.alpha(color);
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            //算法
            r1 = (int) (0.393 * r + 0.769 * g + 0.189 * b);
            g1 = (int) (0.349 * r + 0.686 * g + 0.168 * b);
            b1 = (int) (0.272 * r + 0.534 * g + 0.131 * b);

            if (r1 > 255) {
                r1 = 255;
            }
            if (g1 > 255) {
                g1 = 255;
            }
            if (b1 > 255) {
                b1 = 255;
            }

            newPx[i] = Color.argb(a, r1, g1, b1);
        }
        bmp.setPixels(newPx, 0, width, 0, 0, width, height);
        return bmp;
    }

    public static Bitmap handleImagePixelsRelief(Bitmap bm) {
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
                Bitmap.Config.ARGB_8888);
        int width = bm.getWidth();
        int height = bm.getHeight();
        int color = 0, colorBefore = 0;
        int a, r, g, b;
        int r1, g1, b1;

        int[] oldPx = new int[width * height];
        int[] newPx = new int[width * height];

        bm.getPixels(oldPx, 0, bm.getWidth(), 0, 0, width, height);
        for (int i = 1; i < width * height; i++) {
            colorBefore = oldPx[i - 1];
            a = Color.alpha(colorBefore);
            r = Color.red(colorBefore);
            g = Color.green(colorBefore);
            b = Color.blue(colorBefore);

            color = oldPx[i];
            r1 = Color.red(color);
            g1 = Color.green(color);
            b1 = Color.blue(color);

            r = (r - r1 + 127);
            g = (g - g1 + 127);
            b = (b - b1 + 127);
            if (r > 255) {
                r = 255;
            }
            if (g > 255) {
                g = 255;
            }
            if (b > 255) {
                b = 255;
            }
            newPx[i] = Color.argb(a, r, g, b);
        }
        bmp.setPixels(newPx, 0, width, 0, 0, width, height);
        return bmp;
    }
}
