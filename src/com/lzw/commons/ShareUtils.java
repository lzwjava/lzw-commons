package com.lzw.commons;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by lzw on 14-6-27.
 */
public class ShareUtils {
  public static void intentToShare(Context cxt,int titleId, int contentId) {
    intentToShare(cxt, cxt.getString(titleId), cxt.getString(contentId));
  }

  public static void intentToShare(Context context,String title, String shareContent) {
    Intent intent=new Intent(Intent.ACTION_SEND);
    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share));
    intent.putExtra(Intent.EXTRA_TEXT, shareContent);
    intent.putExtra(Intent.EXTRA_TITLE, title);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(Intent.createChooser(intent,context.getString(R.string.please_choose)));
  }

  public static void intentToGiveStar(Context cxt) {
    Uri uri = Uri.parse("market://details?id=" + cxt.getPackageName());
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setData(uri);
    try {
      cxt.startActivity(intent);
    } catch (Exception e) {
      e.printStackTrace();
      Utils.toast(cxt, R.string.noGiveStar);
    }
  }
}
