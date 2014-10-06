package com.lzw.commons;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lzw on 14-6-27.
 */
public class HttpUtils {
  public static String httpGet(HttpClient httpClient, String url, String... pairs) throws IOException {
    HttpEntity entity = httpGetEntity(httpClient, url, pairs);
    return EntityUtils.toString(entity);
  }

  public static org.apache.http.HttpEntity httpGetEntity(HttpClient httpClient, String url, String... pairs) throws IOException {
    String entityContent = null;
      url = makeGetSrl(url, pairs);
      HttpGet get = new HttpGet(url);
      HttpResponse response = httpClient.execute(get);
      //print("%d", response.getStatusLine().getStatusCode());
      return response.getEntity();
  }

  public static String makeGetSrl(String url, String... pairs) {
    if (!url.endsWith("?")) {
      url += "?";
    }
    List<NameValuePair> params = new LinkedList<NameValuePair>();
    int len = pairs.length;
    for (int i = 0; i < len / 2; i++) {
      params.add(new BasicNameValuePair(pairs[2 * i], pairs[2 * i + 1]));
    }
    String paramsStr = URLEncodedUtils.format(params, "utf-8");
    url += paramsStr;
    return url;
  }

  public static void downloadUrlToPath(String url2, String path) {
    // TODO Auto-generated method stub
    BufferedInputStream bInput = null;
    BufferedOutputStream bOutput = null;
    try {
      HttpURLConnection conn = null;
      URL url = new URL(url2);
      conn = (HttpURLConnection) url.openConnection();
      bInput = getBufferedInput(conn);
      bOutput = getBufferedOutput(path);
      byte[] buffer = new byte[1024];
      int cnt;
      while ((cnt = bInput.read(buffer)) != -1) {
        bOutput.write(buffer, 0, cnt);
      }
      bOutput.flush();
      if (conn != null) {
        conn.disconnect();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (bInput != null)
          bInput.close();
        if (bOutput != null)
          bOutput.close();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public static BufferedOutputStream getBufferedOutput(String path)
      throws IOException, FileNotFoundException {
    BufferedOutputStream bOutput;
    File file = new File(path);
    if (file.getParentFile().exists() == false) {
      file.getParentFile().mkdirs();
    }
    file.createNewFile();
    bOutput = new BufferedOutputStream(new FileOutputStream(file));
    return bOutput;
  }

  public static BufferedInputStream getBufferedInput(HttpURLConnection conn)
      throws MalformedURLException, IOException {
    BufferedInputStream bInput;
    conn.setConnectTimeout(10000);
    conn.setReadTimeout(15000);
    conn.setDoInput(true);
    conn.setDoOutput(true);
    bInput = new BufferedInputStream(conn.getInputStream());
    return bInput;
  }

  public static int httpGetLen(String urlStr) throws Exception {
    int total = 150000;
    HttpURLConnection conn = null;
    try {
      URL url = new URL(urlStr);
      conn = (HttpURLConnection) url.openConnection();
      conn.setConnectTimeout(5 * 1000);
      int code = conn.getResponseCode();
      total = conn.getContentLength();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
    return total;
  }

  public static boolean noWifi(Context cxt) {
    ConnectivityManager man = (ConnectivityManager)
        cxt.getSystemService(cxt.CONNECTIVITY_SERVICE);
    NetworkInfo.State state = man.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
    if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
      return false;
    } else {
      return true;
    }
  }

  public static JSONObject getJsonFromUrl(String url) throws IOException, JSONException {
    HttpGet httpGet = new HttpGet(url);
    HttpResponse res = new DefaultHttpClient().execute(httpGet);
    JSONObject json = null;
    int code = res.getStatusLine().getStatusCode();
    if (code == 200 || code == 206) {
      String str = EntityUtils.toString(res.getEntity());
      json = new JSONObject(str);
    }
    return json;
  }

  public static String httpGetEntityStr(String url, String... pairs) throws IOException {
    DefaultHttpClient httpClient = new DefaultHttpClient();
    return httpGetEntityStr(httpClient, url, pairs);
  }

  private static String httpGetEntityStr(HttpClient httpClient, String url, String... pairs) throws IOException {
    HttpResponse httpResponse = httpGetResponse(httpClient, url, pairs);
    return EntityUtils.toString(httpResponse.getEntity());
  }

  public static int httpGetStatusCode(HttpClient client, String url, String... pairs) throws IOException {
    HttpResponse res = httpGetResponse(client, url, pairs);
    int code = res.getStatusLine().getStatusCode();
    return code;
  }

  public static HttpResponse httpGetResponse(HttpClient client, String url, String... pairs)
      throws IOException {
    int len = pairs.length;
    for (int i = 0; i < len; i++) {
      if (i != 0 && i % 2 == 0) {
        url += "&";
      } else if (i % 2 == 1) {
        url += "=";
      }
      url += URLEncoder.encode(pairs[i]);
    }
    Log.i("lzw", "get url=" + url);
    HttpGet get = new HttpGet(url);
    return client.execute(get);
  }

  public static HttpPost getBasePost(String url, String[] pairs) {
    if (pairs.length % 2 != 0) {
      throw new IllegalArgumentException("pairs should be paired");
    }
    HttpResponse response;
    HttpPost post = new HttpPost(url);
    String s = "";
    for (int i = 0; i < pairs.length / 2; i++) {
      if (i != 0) {
        s += "&";
      }
      s += pairs[i * 2] + "=" + pairs[i * 2 + 1];
    }
    BasicHttpEntity entity = new BasicHttpEntity();
    entity.setContent(new ByteArrayInputStream(s.getBytes()));
    entity.setContentType("application/x-www-form-urlencoded");
    int length = s.length();
    entity.setContentLength(length);
    post.setEntity(entity);
    return post;
  }

  public static HttpResponse getResponse(HttpClient httpClient, String url, String... pairs) throws IOException {
    HttpPost post = getBasePost(url, pairs);
    HttpResponse response;
    HttpContext httpContext = new BasicHttpContext();
    response = httpClient.execute(post, httpContext);
    HttpHost targetHost = (HttpHost) httpContext.
        getAttribute(ExecutionContext.HTTP_TARGET_HOST);
    HttpUriRequest realRequest = (HttpUriRequest) httpContext.
        getAttribute(ExecutionContext.HTTP_REQUEST);
    Logger.d("host=" + targetHost.toString() + " realRequest=" + realRequest.getURI());
    return response;
  }

  public static String post(HttpClient httpClient,
                            String url, String... pairs) throws IOException {
    HttpResponse response = null;
    String entityContent = null;
    response = getResponse(httpClient, url, pairs);
    entityContent = EntityUtils.toString(response.getEntity(), "gb2312");
    return entityContent;
  }

  public static boolean isConnectNet() {
    String url = "http://www.baidu.com";
    try {
      URL url1 = new URL(url);
      HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
      conn.connect();
      conn.disconnect();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public static boolean hasWifi(Context cxt) {
    ConnectivityManager man = (ConnectivityManager)
        cxt.getSystemService(cxt.CONNECTIVITY_SERVICE);
    NetworkInfo.State state = man.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
    if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
      return true;
    } else {
      return false;
    }
  }

  public static boolean isConnect(Context cxt) {
    ConnectivityManager conMan = (ConnectivityManager)
        cxt.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo.State mobile = conMan.getNetworkInfo(ConnectivityManager
        .TYPE_MOBILE).getState();
    NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager
        .TYPE_WIFI).getState();
    if (mobile == NetworkInfo.State.CONNECTED ||
        wifi == NetworkInfo.State.CONNECTED) {
      return true;
    } else return false;
  }
}
