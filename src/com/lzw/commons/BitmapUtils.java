package com.lzw.commons;

import android.content.Context;
import android.graphics.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lzw on 14-6-27.
 */
public class BitmapUtils {
  public static void saveBitmapToPath(Bitmap bitmap, String imagePath) {
    // TODO Auto-generated method stub
    FileOutputStream out = null;
    File file = new File(imagePath);
    if (file.getParentFile().exists() == false) {
      file.getParentFile().mkdirs();
    }
    try {
      out = new FileOutputStream(imagePath);
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
      out.flush();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (out != null) out.close();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public static Bitmap loadImage(ImageLoader imageLoader,
                                 String localPath, String url, int width) {
    // TODO Auto-generated method stub
    File f = new File(localPath);
    if (!f.exists()) {
      HttpUtils.downloadUrlToPath(url, localPath);
    }
    Bitmap bitmap = ImageLoader.decodeSampledBitmapFromPath(localPath, width);
    if (bitmap != null)
      imageLoader.addBitmapToMemoryCache(url, bitmap);
    return bitmap;
  }

  public static Bitmap getBitmapFromUrl(String urlStr) throws IOException {
    HttpURLConnection conn;
    URL url = new URL(urlStr);
    conn = (HttpURLConnection) url.openConnection();
    BufferedInputStream bif = HttpUtils.getBufferedInput(conn);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] bytes = new byte[8192];
    int n;
    while ((n = bif.read(bytes)) != -1) {
      out.write(bytes, 0, n);
    }
    bif.close();
    byte[] picBytes = out.toByteArray();
    return BitmapFactory.decodeByteArray(picBytes, 0, picBytes.length);
  }

  public static Bitmap getBitmapFromUrlByStream(String urlStr) throws IOException {
    HttpURLConnection conn;
    URL url = new URL(urlStr);
    conn = (HttpURLConnection) url.openConnection();
    BufferedInputStream bif = HttpUtils.getBufferedInput(conn);
    return BitmapFactory.decodeStream(bif);
  }

  public static Bitmap toRoundBitmap(Bitmap bitmap) {
    int width = bitmap.getWidth();
    int height = bitmap.getHeight();
    float roundPx;
    float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
    if (width <= height) {
      roundPx = width / 2;
      top = 0;
      bottom = width;
      left = 0;
      right = width;
      height = width;
      dst_left = 0;
      dst_top = 0;
      dst_right = width;
      dst_bottom = width;
    } else {
      roundPx = height / 2;
      float clip = (width - height) / 2;
      left = clip;
      right = width - clip;
      top = 0;
      bottom = height;
      width = height;
      dst_left = 0;
      dst_top = 0;
      dst_right = height;
      dst_bottom = height;
    }
    Bitmap output = Bitmap.createBitmap(width,
        height, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(output);

    final int color = 0xff424242;
    final Paint paint = new Paint();
    final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
    final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
    final RectF rectF = new RectF(dst);

    paint.setAntiAlias(true);

    canvas.drawARGB(0, 0, 0, 0);
    paint.setColor(color);
    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    canvas.drawBitmap(bitmap, src, dst, paint);
    return output;
  }

  public static Bitmap getBitmapCacheNull(ImageLoader imageLoader, String localPath, String imgUrl, int width) {
    Bitmap bitmap = null;
    if (imgUrl != null) {
      bitmap = imageLoader.getBitmapFromMemoryCache(imgUrl);
      if (bitmap == null) {
        bitmap = loadImage(imageLoader, localPath, imgUrl, width);
      }
    }
    return bitmap;
  }

  public static Bitmap getPhotoCacheLocalNet(Context cxt, ImageLoader imageLoader,
                                             String localPath, String imgUrl, int width,
                                             int emptyId) {
    Bitmap bitmap = getBitmapCacheNull(imageLoader, localPath, imgUrl, width);
    if (bitmap == null) {
      bitmap = BitmapFactory.
          decodeResource(cxt.getResources(), emptyId);
    }
    return bitmap;
  }

  public static Bitmap getBitmapCacheLocalNet(Context cxt, ImageLoader imageLoader,
                                              String localPath, String imgUrl, int width) {
    Bitmap bitmap = getBitmapCacheNull(imageLoader, localPath, imgUrl, width);
    if (bitmap == null) {
      bitmap = BitmapFactory.
          decodeResource(cxt.getResources(), R.drawable.empty_photo);
    }
    return bitmap;
  }
}
