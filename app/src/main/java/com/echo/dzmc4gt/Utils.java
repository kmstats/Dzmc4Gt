package com.echo.dzmc4gt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Utils {
    /**
     * 字节转换为bitmap
     *
     * @param data
     *            字节
     * @return 返回bitmap
     */
    public static Bitmap byteToBmp(byte[] data) {
        try {
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 压缩图片到100k以下
     *
     * @param image
     *            原图片
     * @return 压缩后的图片
     */
    private static Bitmap compressImage(Bitmap image) {
        Bitmap bitmap = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        try {
            ByteArrayInputStream isBm = new ByteArrayInputStream(
                    baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
            bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片

        } catch (Exception e) {
            Log.e("提示", e.toString());
        }
        return bitmap;
    }

    /**
     * 替换字符中的换行为<br>
     *
     * @param s
     *            原字符串
     * @return 替换后的字符串
     */
    private static String getBRString(String s) {
        if (s != null) {
            return s.replace("\r\n", "<br>");
        }else{
            return "";
        }
    }

    /**
     * Image转换为Base64码
     *
     * @param bitmap
     *            原图片
     * @return 图片Base64码
     */
    private static String imgToBase64(Bitmap bitmap) {
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
            out.close();

            byte[] imgBytes = out.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                Log.e("提示", e.toString());
            }
        }
    }
}