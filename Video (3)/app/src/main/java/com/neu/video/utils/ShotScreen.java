package com.neu.video.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 86189 on 2021/4/13.
 * 屏幕截屏方法
 */

public class ShotScreen {
    private static final String TAG="shotscreen";
    @SuppressWarnings("unused")
    private static Bitmap takeScreenShot(Activity activity){
        View view =activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        System.out.println(statusBarHeight);
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap,0,statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return bitmap2;
    }
    @SuppressWarnings("unused")
    private static void savePic(Bitmap bitmap,String filename,Activity activity){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filename);
            if (fileOutputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();

                File localFile = new File(filename);

                /*if (localFile.exists()) {
                    localFile.delete();
                }
                try {
                    localFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                //Uri uri = Uri.fromFile(localFile);
                Uri uri = Uri.parse("file://"+localFile);
                intent.setData(uri);
                activity.sendBroadcast(intent);
            }
        }
        catch (FileNotFoundException e) {
            Log.d(TAG, "Exception:FileNotFoundException");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d(TAG, "IOException:IOException");
            e.printStackTrace();
        }
    }



    public static void shoot(Activity a){
        String fileName = "p_"+System.currentTimeMillis();//根据系统时间，保存图片名
        if (android.os.Environment.MEDIA_MOUNTED != "mounted") {
            fileName = "/sdcard/"+fileName+".png";
            System.out.println(fileName);
            ShotScreen.savePic(ShotScreen.takeScreenShot(a), fileName,a);
        }
        else{
            //fileName = "/data/data/"+a.getPackageName()+"/"+fileName+".png";
            fileName = "/storage/emulated/0/DCIM/Camera/"+fileName+".png";
            System.out.println(fileName);
            ShotScreen.savePic(ShotScreen.takeScreenShot(a), fileName,a);


        }
    }
}
