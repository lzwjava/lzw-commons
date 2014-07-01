package com.lzw.commons;

import android.os.Environment;

import java.io.File;

/**
 * Created by lzw on 14-6-27.
 */
public class PathUtils {
  public static String getSDcardDir(){
    return Environment.getExternalStorageDirectory().getPath()+"/";
  }

  public static void checkAndMkdirs(String dir){
    File file=new File(dir);
    if(file.exists()==false) file.mkdirs();
  }
}
