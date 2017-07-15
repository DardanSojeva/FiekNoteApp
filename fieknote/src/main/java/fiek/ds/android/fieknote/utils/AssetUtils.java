
package fiek.ds.android.fieknote.utils;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


public class AssetUtils {

    public static boolean exists(String fileName, String path,
                                 AssetManager assetManager) throws IOException {
        for (String currentFileName : assetManager.list(path)) {
            if (currentFileName.equals(fileName)) {
                return true;
            }
        }
        return false;
    }


    public static String[] list(String path, AssetManager assetManager)
            throws IOException {
        String[] files = assetManager.list(path);
        Arrays.sort(files);
        return files;
    }


    /**
     * Loads a file into string
     */
    public static String readFile(String fileName, AssetManager assetManager) throws IOException {
        InputStream input;
        input = assetManager.open(fileName);
        int size = input.available();
        byte[] buffer = new byte[size];
        input.read(buffer);
        input.close();
        return new String(buffer);
    }
}
