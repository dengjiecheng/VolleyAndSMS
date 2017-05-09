package com.example.volleytest.command.MyAsyncTask;

import android.os.Environment;

import com.example.volleytest.Utils.SafeAsyncTask;
import com.example.volleytest.Utils.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by djc on 2017/5/3.
 * 下载文件
 */

public class DownLoadAsyncTask extends SafeAsyncTask<Integer, Boolean> {
    private static final String TAG = "DownLoadAsyncTask";
    /* 下载保存路径 */
    private String mSavePath;
    private String filename = "";
    private String downLoadUrl;
    private boolean cancelUpdate = false;

    public DownLoadAsyncTask(String mSavePath, String filename, String downLoadUrl) {
        this.mSavePath = mSavePath;
        this.filename = filename;
        this.downLoadUrl = downLoadUrl;
    }

    @Override
    public Boolean call() throws Exception {
        return downLoad();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        cancelUpdate = true;
        return super.cancel(mayInterruptIfRunning);
    }

    public boolean isCancelUpdate() {
        return cancelUpdate;
    }

    public String getFileName(String url) {
        String filename = "";
//         从UrlConnection中获取文件名称
        try {
            URL myURL = new URL(url);
            URLConnection conn = myURL.openConnection();
            if (conn == null) {
                return null;
            }
            Map<String, List<String>> hf = conn.getHeaderFields();
            if (hf == null) {
                return null;
            }
            Set<String> key = hf.keySet();
            if (key == null) {
                return null;
            }
            boolean isok = false;
            for (String skey : key) {
                List<String> values = hf.get(skey);
                for (String value : values) {
                    String result;
                    try {
                        result = new String(value.getBytes("ISO-8859-1"),
                                "GBK");
                        int location = result.indexOf("filename");
                        if (location >= 0) {
                            result = result.substring(location
                                    + "filename".length());
                            filename = result
                                    .substring(result.indexOf("=") + 1);
                            isok = true;
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }// ISO-8859-1 UTF-8 gb2312
                }
                if (isok) {
                    break;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 从路径中获取
        if (filename == null || "".equals(filename)) {
            filename = url.substring(url.lastIndexOf("/") + 1);
//            filename = "IntelligentHome.apk";
        } else {
//                    L.e(TAG, "IntelligentHome==filename:" + filename);
        }
        return filename;
    }

    protected Boolean downLoad()throws Exception {
        // TODO 自动生成的方法存根
        if (StringUtils.isEmpty(downLoadUrl)){
            throw new IllegalArgumentException("下载链接为空");
        }
        if (StringUtils.isEmpty(filename)) {
            filename = getFileName(downLoadUrl);
        }
            // 判断SD卡是否存在，并且是否具有读写权限
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                if (StringUtils.isEmpty(mSavePath)) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory()
                            + "/";
                    mSavePath = sdpath + "download";
                }
                URL url = new URL(downLoadUrl);
                // 创建连接
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setRequestProperty("Accept-Encoding","identity");
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                if(conn.getResponseCode()==200){

                }
               int length =   conn.getContentLength();
                conn.connect();
                // 获取文件大小
                if(length==-1){
                     length = conn.getContentLength();
                }
                // 创建输入流
                InputStream is = conn.getInputStream();

                File file = new File(mSavePath);
                // 判断文件目录是否存在
                if (!file.exists()) {
                    file.mkdir();
                }
                File apkFile = new File(mSavePath, filename);
                FileOutputStream fos = new FileOutputStream(apkFile);
                int count = 0;
                // 缓存
                byte buf[] = new byte[1024];

                // 写入到文件中
                do {
                    int numread = is.read(buf);
                    count += numread;
                    cacluteAndPublishProgress( count,length);
                    if (numread <= 0) {
                        // 下载完成
                        break;
                    }
                    // 写入文件
                    fos.write(buf, 0, numread);
                } while (!cancelUpdate);// 点击取消就停止下载.
                fos.close();
                is.close();
            } else {
                throw new RuntimeException("外部SD卡不存在");
            }
            if (cancelUpdate) {
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
    }

    void cacluteAndPublishProgress( int count, int length)throws Exception{
        // 计算进度条位置
        int progress;
        if(length!=0){
            progress = (int) (((float) count / length) * 100);
        }else{
            progress = (int) (((float) count / count+100) * 100);
        }
//        L.d(TAG,"进度"+progress);
        publishProgress(progress,count,length);
    }
}
