package com.lzw.commons;

import android.content.Context;
import android.content.Intent;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lzw on 14-6-30.
 */
public class AVUtils {
  public static JSONObject getJsonByAVIntent(Intent intent) throws JSONException {
    String data = intent.getExtras().getString("com.avos.avoscloud.Data");
    Logger.d("data="+data);
    return new JSONObject(data);
  }

  public static void syncFeedBack(Context cxt) {
    //FeedbackAgent agent = new FeedbackAgent(cxt);
    //agent.sync();
  }

  public static void startFeedBackActivity(Context cxt) {
    //FeedbackAgent agent = new FeedbackAgent(cxt);
    //agent.startDefaultThreadActivity();
  }

}
