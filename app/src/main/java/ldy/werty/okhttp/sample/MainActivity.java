package ldy.werty.okhttp.sample;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ldy.werty.okhttp.OkHttpUtil;
import com.ldy.werty.okhttp.OkRequestParams;
import com.ldy.werty.okhttp.response.OkHttpResBeanHandler;
import com.ldy.werty.okhttp.response.OkHttpResBitmapHandler;
import com.ldy.werty.okhttp.response.OkHttpResFileHandler;
import com.ldy.werty.okhttp.response.OkHttpResJsonHandler;
import com.ldy.werty.okhttp.response.OkHttpResStringHandler;

import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;
import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onTest(View view) {

//        OkRequestParams requestParams = new OkRequestParams();
//        requestParams.createRequestBody(OkRequestParams.MEDIA_TYPE_PLAIN, "看了解决空间空间");
////
//        OkHttpUtil.post("http://abc.ersan23.com/player/loginpic", requestParams, new OkHttpResStringHandler() {
//
//            @Override
//            public void onSuccess(Call call, int code, Headers headers, String response) {
//                Log.i("TAGTAG", "onSuccess call=[" + call + "];code=[" + call + "];headers=[" + headers + "];response=[" + response + "]");
//            }
//
//            @Override
//            public void onFailure(Call call, int code, Headers headers, int error, Throwable t) {
//                Log.i("TAGTAG", "onFailure call=[" + call + "];code=[" + call + "];headers=[" + headers + "];error=[" + error + "];t=[" + t + "]");
//            }
//        });


//        fileDown();
//        getBean();
//        getString();
//        getBitmap();
//        getJSONObject();
//        post();
//        postFile();

        fileDown();
    }

    /**
     * 获取bean
     */
    public void getBean() {
        OkRequestParams params = new OkRequestParams();
        params.put("", "");
        params.put("", "");
        OkHttpUtil.get("http://abc.ersan23.com/player/loginpic", params, new OkHttpResBeanHandler<TestBean>() {
            @Override
            public void onSuccess(Call call, int code, Headers headers, TestBean response) {
            }

            @Override
            public void onFailure(Call call, int code, Headers headers, int error, Throwable t) {
            }
        });
    }

    /**
     * 获取String
     */
    public void getString() {
        OkHttpUtil.get("http://r.babytree.com/bxuh4mV", new OkHttpResStringHandler() {

            @Override
            public void onSuccess(Call call, int code, Headers headers, String response) {
            }

            @Override
            public void onFailure(Call call, int code, Headers headers, int error, Throwable t) {
            }
        });
    }

    /**
     * 获取JSONObject
     */
    public void getJSONObject() {
        OkRequestParams params = new OkRequestParams();
        params.put("", "");
        params.put("", "");
        OkHttpUtil.get("https://api.test1.babytree-fpm.com/api/service_index/get_models_6_0", params, new OkHttpResJsonHandler() {

            @Override
            public void onSuccess(Call call, int code, Headers headers, JSONObject response) {
            }

            @Override
            public void onFailure(Call call, int code, Headers headers, int error, Throwable t) {
            }
        });
    }

    /**
     * 获取bitmap
     */
    public void getBitmap() {
        OkHttpUtil.get("http://images.csdn.net/20150817/1.jpg", new OkHttpResBitmapHandler() {

            @Override
            public void onSuccess(Call call, int code, Headers headers, Bitmap response) {
            }

            @Override
            public void onFailure(Call call, int code, Headers headers, int error, Throwable t) {
            }
        });
    }

    /**
     * post请求
     */
    public void post() {
        OkRequestParams params = new OkRequestParams();
        params.putHeader("aa", "aa323");
        params.put("werw", "er23wr");
        params.put("ewr", "we23rew");
        params.put("as", "wer23ew");
        params.put("asd", new File("sdf23sdf"));

        OkHttpUtil.post("https://www.baidu.com/", params, new OkHttpResJsonHandler() {

            @Override
            public void onSuccess(Call call, int code, Headers headers, JSONObject response) {
            }

            @Override
            public void onFailure(Call call, int code, Headers headers, int error, Throwable t) {
            }
        });
    }

    /**
     * 上传文件
     */
    public void postFile() {
        OkRequestParams params = new OkRequestParams();
        params.put("11", "113333");
        params.put("22", new File("12123123"), new File("567567567"));
        params.put("22", new File("sdfsdf"));

        OkHttpUtil.post("url", params, this, true, new OkHttpResJsonHandler() {

            @Override
            public void onSuccess(Call call, int code, Headers headers, JSONObject response) {
            }

            @Override
            public void onFailure(Call call, int code, Headers headers, int error, Throwable t) {
            }

            @Override
            public void onProgress(long curSize, long totalSize, float progress, boolean done) {

            }
        });
    }

    /**
     * 下载文件
     */
    public void fileDown() {
        String url = "http://down1.cnmo.com/app/diebaoguan/Diebao_Branches.apk";
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        String fileName = "hepburn-sunglasses-off.gif";

        OkHttpUtil.get(url, "fileDown", new OkHttpResFileHandler(dirPath, fileName) {

            @Override
            public void onSuccess(Call call, int code, Headers headers, File response) {
                Log.i("Werty", "onSuccess call=[" + call + "];code=[" + code + "];headers=[" + headers + "];response=[" + response + "];");
            }

            @Override
            public void onFailure(Call call, int code, Headers headers, int error, Throwable t) {
                Log.i("Werty", "onSuccess call=[" + call + "];code=[" + code + "];headers=[" + headers + "];error=[" + error + "];t=[" + t + "];");
            }

            @Override
            public void onProgress(long curSize, long totalSize, float progress, boolean done) {
                String str = "curSize=" + curSize + ";\ntotalSize=" + totalSize + ";\nprogress=" + progress + ";\ndone=" + done;
                Log.i("Werty", "onProgress t[" + str + "]");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtil.cancelTag("fileDown");
    }
}
