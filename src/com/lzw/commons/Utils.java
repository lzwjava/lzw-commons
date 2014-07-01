package com.lzw.commons;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {
  public static BufferedReader bufferedReader(String url) throws IOException,
      ClientProtocolException, UnsupportedEncodingException {
    HttpGet get = new HttpGet(url);
    DefaultHttpClient client = new DefaultHttpClient();
    HttpResponse response = client.execute(get);
    HttpEntity entity = response.getEntity();
    InputStream stream = entity.getContent();
    BufferedReader reader = new BufferedReader(new InputStreamReader(stream,
        "GBK"));
    return reader;
  }

  public static String readFile(String path) throws FileNotFoundException,
      IOException {
    InputStream input = new FileInputStream(new File(path));
    DataInputStream dataInput = new DataInputStream(input);
    byte[] bytes = new byte[input.available()];
    dataInput.readFully(bytes);
    String text = new String(bytes);
    input.close();
    dataInput.close();
    return text;
  }

  public static Bitmap urlToBitmap(String url) throws ClientProtocolException,
      IOException {
    return BitmapFactory.decodeStream(inputStreamFromUrl(url));
  }

  public static InputStream inputStreamFromUrl(String url) throws IOException,
      ClientProtocolException {
    DefaultHttpClient client = new DefaultHttpClient();
    HttpGet get = new HttpGet(url);
    HttpResponse response = client.execute(get);
    HttpEntity entity = response.getEntity();
    InputStream stream = entity.getContent();
    return stream;
  }

  public static Bitmap bitmapFromFile(File file) throws FileNotFoundException {
    return BitmapFactory.decodeStream(new BufferedInputStream(
        new FileInputStream(file)));
  }

  public static void inputToOutput(FileOutputStream outputStream,
                                   InputStream inputStream) throws IOException {
    byte[] buffer = new byte[1024];
    int len;
    while ((len = inputStream.read(buffer)) != -1) {
      outputStream.write(buffer, 0, len);
    }
    outputStream.close();
    inputStream.close();
  }

  public static byte[] readStream(InputStream inStream) throws Exception {
    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int len = 0;
    while ((len = inStream.read(buffer)) != -1) {
      outStream.write(buffer, 0, len);
    }
    outStream.close();
    inStream.close();
    return outStream.toByteArray();
  }

  public static byte[] getBytesFromUrl(String url) throws Exception {
    InputStream in = inputStreamFromUrl(url);
    return readStream(in);
  }

  public static Bitmap saveBitmapLocal(String bitmapUrl, File bitmapFile)
      throws IOException, FileNotFoundException, ClientProtocolException {
    Bitmap resultBitmap;
    if (bitmapFile.exists()) {
    } else {
      Log.i("lzw", bitmapFile.getAbsolutePath());
      bitmapFile.createNewFile();

      FileOutputStream outputStream = new FileOutputStream(bitmapFile);
      InputStream inputStream = Utils.inputStreamFromUrl(bitmapUrl);
      Utils.inputToOutput(outputStream, inputStream);
    }
    resultBitmap = Utils.bitmapFromFile(bitmapFile);
    return resultBitmap;
  }

  public static Bitmap getBitmapFromUrl(String logoUrl, String filmEnName,
                                        String appPath) throws IOException, FileNotFoundException,
      ClientProtocolException {
    Bitmap resultBitmap;
    String logoPath = appPath + "logo/";
    File dir = new File(logoPath);
    if (dir.exists() == false) {
      dir.mkdirs();
    }
    File logoLocalFile = new File(logoPath + filmEnName + ".jpg");
    resultBitmap = Utils.saveBitmapLocal(logoUrl, logoLocalFile);
    return resultBitmap;
  }

  public static void bytesToFile(final File file, byte[] bytes)
      throws FileNotFoundException, IOException {
    FileOutputStream output = new FileOutputStream(file);
    output.write(bytes);
    output.close();
  }

  public static ProgressDialog showSpinnerDialog(Activity activity) {
    ProgressDialog dialog = new ProgressDialog(activity);
    dialog.setMessage(activity.getString(R.string.hard_loading));
    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    dialog.setCancelable(true);
    dialog.show();
    return dialog;
  }

  public static ProgressDialog showHorizontalDialog(Activity activity) {
    ProgressDialog dialog = new ProgressDialog(activity);
    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    dialog.setCancelable(true);
    dialog.show();
    return dialog;
  }

  public static void toast(Context context, int strId) {
    toastIt(context, strId, false);
  }


  public static void toast(Context context, String str) {
    Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
  }

  private static void toastIt(Context context, int strId, boolean isLong) {
    int ti;
    if (isLong) ti = Toast.LENGTH_LONG;
    else ti = Toast.LENGTH_SHORT;
    Toast.makeText(context, context.getString(strId), ti).show();
  }

  public static void toastLong(Context context, int strId) {
    toastIt(context, strId, true);
  }

  public static boolean hasSDcard() {
    // TODO Auto-generated method stub
    return Environment.MEDIA_MOUNTED.equals(Environment
        .getExternalStorageState());
  }

  public static int dip2px(Context context, float dipValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dipValue * scale + 0.5f);
  }


  public static String format(int curPos) {
    int sec1 = curPos / 1000;
    int min = sec1 / 60;
    int sec = sec1 % 60;
    int tm = min / 10, mm = min % 10;
    int ts = sec / 10, ms = sec % 10;
    String str = String.format("%d%d:%d%d", tm, mm, ts, ms);
    return str;
  }

  public static void clearDir(String dir) {
    File file = new File(dir);
    File[] fs = file.listFiles();
    for (File f : fs) {
      f.delete();
    }
  }

  public static String prettyFormat(Date date) {
    String dateStr;
    String am_pm = "am";
    SimpleDateFormat format = new SimpleDateFormat("M-d h");
    SimpleDateFormat format1 = new SimpleDateFormat("aa", Locale.ENGLISH);
    if (format1.format(date).equals("PM")) {
      am_pm = "pm";
    }
    dateStr = format.format(date) + am_pm;
    return dateStr;
  }

  public static void alertDialog(Activity activity, String s) {
    new AlertDialog.Builder(activity).setMessage(s).show();
  }

  public static void alertDialog(Activity activity, int msgId) {
    new AlertDialog.Builder(activity).
        setMessage(activity.getString(msgId)).show();
  }

  public static void alertIconDialog(Activity activity, String s, int iconId) {
    new AlertDialog.Builder(activity).setTitle(R.string.tips)
        .setMessage(s).setIcon(iconId).show();
  }

  public static int getWindowWidth(Activity cxt) {
    int width;
    DisplayMetrics metrics = cxt.getResources().getDisplayMetrics();
    width = metrics.widthPixels;
    return width;
  }

  public static long getLongByTimeStr(String begin) throws ParseException {
    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SS");
    String origin = "00:00:00.00";
    Date parse = format.parse(begin);
    return parse.getTime() - format.parse(origin).getTime();
  }

  public static long getPassTime(long st) {
    return System.currentTimeMillis() - st;
  }

  public static String getEquation(int finalNum, int delta) {
    String equation;
    int abs = Math.abs(delta);
    if (delta >= 0) {
      equation = String.format("%d+%d=%d", finalNum - delta, abs, finalNum);
    } else {
      equation = String.format("%d-%d=%d", finalNum - delta, abs, finalNum);
    }
    return equation;
  }

  public static Uri getCacheUri(String path, String url) {
    Uri uri = Uri.parse(url);
    uri = Uri.parse("cache:" + path + ":" + uri.toString());
    return uri;
  }

  public static void notify(Context context, String msg, String title, Class<?> toClz, int notifyId) {
    PendingIntent pend = PendingIntent.getActivity(context, 0,
        new Intent(context, toClz), 0);
    Notification.Builder builder = new Notification.Builder(context);
    int icon = context.getApplicationInfo().icon;
    builder.setContentIntent(pend)
        .setSmallIcon(icon)
        .setWhen(System.currentTimeMillis())
        .setTicker(msg)
        .setContentTitle(title)
        .setContentText(msg)
        .setAutoCancel(true);

    NotificationManager man = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    man.notify(notifyId, builder.getNotification());
  }

  public static void cancelNotification(Context ctx, int notifyId) {
    String ns = Context.NOTIFICATION_SERVICE;
    NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
    nMgr.cancel(notifyId);
  }

  public static String getStringByFile(File f) throws IOException {
    StringBuilder builder = new StringBuilder();
    BufferedReader br = new BufferedReader(new FileReader(f));
    String line;
    while ((line = br.readLine()) != null) {
      builder.append(line);
    }
    br.close();
    return builder.toString();
  }

  public static String getShortUrl(String longUrl) throws IOException, JSONException {
    if (longUrl.startsWith("http") == false) {
      throw new IllegalArgumentException("longUrl must start with http");
    }
    String url = "https://api.weibo.com/2/short_url/shorten.json";
    HttpPost post = new HttpPost(url);
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("access_token", "2.00_hkjqBR1dbuCc632289355qerfeD"));
    params.add(new BasicNameValuePair("url_long", longUrl));
    post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
    HttpResponse res = new DefaultHttpClient().execute(post);
    if (res.getStatusLine().getStatusCode() == 200) {
      String str = EntityUtils.toString(res.getEntity());
      JSONObject json = new JSONObject(str);
      JSONArray arr = json.getJSONArray("urls");
      JSONObject urls = arr.getJSONObject(0);
      if (urls.getBoolean("result")) {
        return urls.getString("url_short");
      } else {
        return null;
      }
    }
    return null;
  }

  public static String getGb2312Encode(String s) throws UnsupportedEncodingException {
    return URLEncoder.encode(s, "gb2312");
  }

  public static void goActivity(Context cxt, Class<?> clz) {
    Intent intent = new Intent(cxt, clz);
    cxt.startActivity(intent);
  }

  public static void installApk(Context context, String path) {
    Intent intent1 = new Intent();
    intent1.setAction(Intent.ACTION_VIEW);
    File file = new File(path);
    Log.i("lzw", file.getAbsolutePath());
    intent1.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent1);
  }

  public static void showInfoDialog(Activity cxt, String msg, String title) {
    cxt = modifyDialogContext(cxt);
    AlertDialog.Builder builder = getBaseDialogBuilder(cxt);
    builder.setMessage(msg)
        .setPositiveButton(cxt.getString(R.string.right), null)
        .setTitle(title)
        .show();
  }

  public static Activity modifyDialogContext(Activity cxt) {
    Activity parent = cxt.getParent();
    if (parent != null) {
      return parent;
    } else {
      return cxt;
    }
  }

  public static AlertDialog.Builder getBaseDialogBuilder(Activity cxt) {
    cxt = modifyDialogContext(cxt);
    return new AlertDialog.Builder(cxt).setTitle(R.string.tips).setIcon(R.drawable.icon_info_2);
  }

  public static String getStrByRawId(Context ctx, int id) throws UnsupportedEncodingException {
    InputStream is = ctx.getResources().openRawResource(id);
    BufferedReader br = new BufferedReader(new InputStreamReader(is, "gbk"));
    String line;
    StringBuilder sb = new StringBuilder();
    try {
      while ((line = br.readLine()) != null) {
        sb.append(line + "\n");
      }
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return sb.toString();
  }

  public static void showInfoDialog(Activity cxt, int msgId, int titleId) {
    showInfoDialog(cxt, cxt.getString(msgId), cxt.getString(titleId));
  }

  public static void notifyMsg(Context cxt, Class<?> toClz, int titleId, int msgId, int notifyId) {
    notifyMsg(cxt, toClz, cxt.getString(titleId), cxt.getString(msgId), notifyId);
  }

  public static String getTodayDayStr() {
    String dateStr;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    dateStr = sdf.format(new Date());
    return dateStr;
  }

  public static Ringtone getDefaultRingtone(Context ctx, int type) {

    return RingtoneManager.getRingtone(ctx,
        RingtoneManager.getActualDefaultRingtoneUri(ctx, type));

  }

  public static Uri getDefaultRingtoneUri(Context ctx, int type) {
    return RingtoneManager.getActualDefaultRingtoneUri(ctx, type);
  }

  public static boolean isEmpty(Activity activity, String str, String prompt) {
    if (str.isEmpty()) {
      toast(activity, prompt);
      return true;
    }
    return false;
  }

  public static String getWifiMac(Context cxt) {
    WifiManager wm = (WifiManager) cxt.getSystemService(Context.WIFI_SERVICE);
    return wm.getConnectionInfo().getMacAddress();
  }

  public static String quote(String str) {
    return "'" + str + "'";
  }

  public static String formatString(Context cxt, int id, Object... args) {
    return String.format(cxt.getString(id), args);
  }

  public static void notifyMsg(Context context, Class<?> clz, String title, String msg, int notifyId) {
    int icon = context.getApplicationInfo().icon;
    PendingIntent pend = PendingIntent.getActivity(context, 0,
        new Intent(context, clz), 0);
    Notification.Builder builder = new Notification.Builder(context);
    builder.setContentIntent(pend)
        .setSmallIcon(icon)
        .setWhen(System.currentTimeMillis())
        .setTicker(msg)
        .setContentTitle(title)
        .setContentText(msg)
        .setAutoCancel(true);
    NotificationManager man = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    man.notify(notifyId, builder.getNotification());
  }

  public static void sleep(int partMilli) {
    try {
      Thread.sleep(partMilli);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void setLayoutTopMargin(View view, int topMargin) {
    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)
        view.getLayoutParams();
    lp.topMargin = topMargin;
    view.setLayoutParams(lp);
  }
}
