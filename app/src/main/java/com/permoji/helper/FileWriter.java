package com.permoji.helper;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by michael on 01/10/18.
 */

public class FileWriter {

    public static String writeImageToLocalDirectory(Bitmap bigIcon, String name) throws Exception {

        File outFile = null;
        String localDirectory = "/Permoji/";
        OutputStream stream = null;

        try {

            File dir = new File(Environment.getExternalStorageDirectory(), localDirectory);
            if(!dir.exists()) {
                dir.mkdirs();
                createNoMediaFile();
            }

            outFile = new File(Environment.getExternalStorageDirectory() + localDirectory,
                    name + ".png");
            if (outFile.exists()) {
                outFile.delete();
            }

            stream = new FileOutputStream(outFile);

            bigIcon.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
        }
        catch (SecurityException e) {
            Log.e("FileWriter",  e.getMessage());
            throw e;
        }
        catch (Exception e) {
            Log.e("FileWriter",  e.getMessage());
            throw e;
        }

        return outFile.getPath();
    }

    private static void createNoMediaFile() {
        File outFile = null;
        String localDirectory = "/Permoji/";
        OutputStream stream = null;

        outFile = new File(Environment.getExternalStorageDirectory() + localDirectory,
                ".nomedia");
    }
}
