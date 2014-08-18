package com.lzw.commons;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by lzw on 14-4-29.
 */
public class Logger {
  public static boolean open=true;

  public static void d(String s) {
    if(open){
      Log.d("lzw", s + "");
    }
  }

  public static void d(String format, Object... args) {
    if(open){
      d(String.format(format, args));
    }
  }
}
