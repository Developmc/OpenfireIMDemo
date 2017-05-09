package com.example.myopenfiredemo.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**BitmapUtil 工具类
 * Created by clement on 2017/4/21.
 */

public class BitmapUtil {

    /**byte[] → Bitmap
     * @param bytes
     * @return
     */
    public static Bitmap bytes2Bitmap(byte[] bytes){
        if(bytes!=null && bytes.length>0){
            return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        }
        return null;
    }

    /**Bitmap → byte[]
     * @param bitmap
     * @return
     */
    public static byte[] bitmap2Bytes(@NonNull Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**根据路径获得图片的bitmap
     * @param path
     * @return
     */
    public static Bitmap getBitmapFromPath(@NonNull String path){
        return BitmapFactory.decodeFile(path);
    }

    /**bitmap to String
     * @param bitmap
     * @return
     */
    public static String bitmap2String(@NonNull Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        // 转为byte数组
        byte[] photo = baos.toByteArray();
        return Base64.encodeToString(photo, Base64.DEFAULT);
    }

    /**String 2 Bitmap
     * @param bitmapString
     * @return
     */
    public static Bitmap string2Bitmap(@NonNull String bitmapString){
        Bitmap bitmap = null;
        try
        {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(bitmapString, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            return bitmap;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
