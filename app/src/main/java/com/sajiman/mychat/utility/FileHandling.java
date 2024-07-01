package com.sajiman.mychat.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHandling {

    public static File makeImageFile(Context context) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File basePath = context.getExternalFilesDir(null);
            File appDirectory = new File(basePath, AppText.File_Base_Path);
            if (!appDirectory.exists()) {
                appDirectory.mkdirs();
            }
            return new File(appDirectory, AppText.File_NAME);
        }
        return null;
    }

    public static File writeImageToFile(File imageFile, Bitmap bitmap) {
        try {
            imageFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

}
