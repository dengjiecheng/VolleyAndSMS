package com.example.volleytest.command.exector;

import android.util.Log;

import com.example.volleytest.Utils.L;
import com.example.volleytest.Utils.StringUtils;
import com.example.volleytest.command.CommandMessage.Command;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by djc on 2017/4/27.
 */

public class OkHttpCommandExec extends CommandExec<String> {
    private final static String TAG="OkHttpCommandExec";
    private OkHttpClient mOkHttpClient;
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private File sdcache;

    public OkHttpCommandExec(File cacheDir,Command mCommand) {
        super(mCommand);
        this.sdcache = cacheDir;
        initOkHttpClient();
    }

    private void initOkHttpClient() {
        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        mOkHttpClient = builder.build();
    }
    @Override
    public String startExecCommand() {
        return null;
    }

    @Override
    public boolean cancelExecCommand() {
        return false;
    }

    @Override
    public boolean senCommandMessag(String jsonMsg) {
        return false;
    }

    @Override
    public boolean isFinish() {
        return true;
    }
    /**
     * get异步请求
     * "http://www.baidu.com"
     * "GET"
     */

    private void getAsynHttp(String url, String method) {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        requestBuilder.method(method, null);
        Request request = requestBuilder.build();
        Call mcall = mOkHttpClient.newCall(request);

        mcall.enqueue(new Callback() {
            @Override

            public void onFailure(Call call, IOException e) {


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null != response.cacheResponse()) {
                    String str = response.cacheResponse().toString();
                    Log.i("wangshu", "cache---" + str);
                } else {
                    response.body().string();
                    String str = response.networkResponse().toString();
                    Log.i("wangshu", "network---" + str);
                }
            }
        });

    }


    /**
     * post异步请求
     */

    private void postAsynHttp() {
        RequestBody formBody = new FormBody.Builder()
                .add("size", "10")
                .build();
        Request request = new Request.Builder()

                .url("http://api.1-blog.com/biz/bizserver/article/list.do")

                .post(formBody)

                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);
            }


        });

    }


    /**
     * 异步上传文件
     * "/sdcard/wangshu.txt"
     * "https://api.github.com/markdown/raw"
     */

    private void postAsynFile(String filePath, String url) {
        File file = new File(filePath);
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override

            public void onFailure(Call call, IOException e) {

            }


            @Override

            public void onResponse(Call call, Response response) throws IOException {
                Log.i("wangshu", response.body().string());
            }

        });

    }


    /**
     * 异步下载文件
     * String url = "http://img.my.csdn.net/uploads/201603/26/1458988468_5804.jpg";
     * String filePath "/sdcard/wangshu.jpg"
     */

    private void downAsynFile(String url, final String filePath) {
        if(StringUtils.isEmpty(url)||StringUtils.isEmpty(filePath)){
           Logger.e("参数错误：url="+url+" filePath="+filePath);
            return;
        }
        Logger.e("下载：url="+url+" filePath="+filePath);
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            InputStream inputStream = response.body().byteStream();
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(new File(filePath));
                byte[] buffer = new byte[2048];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    L.i(TAG,"下载的长度："+len);
                    fileOutputStream.write(buffer, 0, len);
                }
                fileOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Logger.d("文件下载成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        mOkHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//            @Override
//            public void onResponse(Call call, Response response) {
//                InputStream inputStream = response.body().byteStream();
//                FileOutputStream fileOutputStream = null;
//                try {
//                    fileOutputStream = new FileOutputStream(new File(filePath));
//                    byte[] buffer = new byte[2048];
//                    int len;
//                    while ((len = inputStream.read(buffer)) != -1) {
//                        fileOutputStream.write(buffer, 0, len);
//                    }
//                    fileOutputStream.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Logger.d("文件下载成功");
//            }
//        });

    }


    private void sendMultipart() {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "wangshu")
                .addFormDataPart("image", "wangshu.jpg",
                        RequestBody.create(MEDIA_TYPE_PNG, new File("/sdcard/wangshu.jpg")))
                .build();
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + "...")
                .url("https://api.imgur.com/3/image")
                .post(requestBody)
                .build();


        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("wangshu", response.body().string());
            }

        });

    }


    public String execCommand(Command mCommand) {
//        Logger.i();
//                if (commandName.equalsIgnoreCase("downLoadFile")){
                    String additionInfo = mCommand.getAdditionInfo();
                    try {
                        JSONObject js = new JSONObject(additionInfo);
                        if (js != null) {
                            String mSavePath = js.getString("SavePath");
                            String filename = js.getString("FileName");
                            String downLoadUrl = js.getString("DownLoadUrl");

                            String filePath=mSavePath+"/"+filename;
                            if (StringUtils.isNotEmpty(downLoadUrl)&&StringUtils.isNotEmpty(filePath)){
                                downAsynFile(downLoadUrl, filePath);
                                return "开始下载文件";
                            }
                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
        Logger.d("命令执行完毕");
        return "";
    }
}
