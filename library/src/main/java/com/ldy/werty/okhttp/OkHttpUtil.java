package com.ldy.werty.okhttp;

import android.os.Handler;
import android.os.Looper;

import com.ldy.werty.okhttp.cookie.JavaNetCookieJar;
import com.ldy.werty.okhttp.progress.ProgressRequestBody;
import com.ldy.werty.okhttp.response.OkHttpCallback;
import com.ldy.werty.okhttp.server.OkHostnameVerifier;
import com.ldy.werty.okhttp.server.OkX509TrustManager;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ldy on 2015/12/29.
 */
public class OkHttpUtil {

    private static final String TAG = OkHttpUtil.class.getSimpleName();
    public static final int DEFAULT_CONNECT_TIMEOUT = 30 * 1000;
    public static final int DEFAULT_WRITE_TIMEOUT = 60 * 1000;
    public static final int DEFAULT_READ_TIMEOUT = 60 * 1000;

    private static OkHttpClient mOkHttpClient = null;
    private static Handler mOkHandler = null;

    public static void init() {
        getOkHttpClient();
    }

    public static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            try {
                mOkHttpClient = getHttpsBuilder().build();
                OkHttpLog.i(TAG, "getOkHttpClient mOkHttpClient[" + mOkHttpClient + "]");
            } catch (Throwable e) {
                e.printStackTrace();
                OkHttpLog.e(TAG, "getOkHttpClient e[" + e + "]");
                mOkHttpClient = getOkHttpBuilder().build();
            }
        }
        return mOkHttpClient;
    }

    /**
     * 获取支持正常 OkHttpClient.Builder
     * @return
     */
    private static OkHttpClient.Builder getOkHttpBuilder() {
        return new OkHttpClient().newBuilder()
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .cookieJar(new JavaNetCookieJar());
    }

    /**
     * 获取支持https OkHttpClient.Builder
     * @return
     */
    private static OkHttpClient.Builder getHttpsBuilder() {
        OkHttpClient.Builder builder = getOkHttpBuilder();
        builder.hostnameVerifier(new OkHostnameVerifier());
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            OkX509TrustManager trustManager = new OkX509TrustManager();
            sc.init(null, new TrustManager[]{trustManager}, new SecureRandom());
            builder.sslSocketFactory(sc.getSocketFactory());
            OkHttpLog.i(TAG, "createHttpsBuilder builder[" + builder + "]");
        } catch (Throwable e) {
            e.printStackTrace();
            OkHttpLog.e(TAG, "createHttpsBuilder e[" + e + "]");
        }
        return builder;
    }

    public static <T> void get(String url, OkHttpCallback<T> okHttpCallback) {
        get(url, null, okHttpCallback);
    }

    public static <T> void get(String url, OkRequestParams params, OkHttpCallback<T> okHttpCallback) {
        get(url, params, null, okHttpCallback);
    }

    public static <T> void get(String url, Object tag, OkHttpCallback<T> okHttpCallback) {
        get(url, null, tag, okHttpCallback);
    }

    public static <T> void get(String url, OkRequestParams params, Object tag, OkHttpCallback<T> okHttpCallback) {
        OkHttpLog.d(TAG, getFinalUrl(url, params));

        Call call = null;
        Callback callback = getCallBack(okHttpCallback);
        try {
            RequestBody requestBody = getRequestBody(params);
            Headers headers = getRequestHeaders(params);
            Request request = getRequest(getFinalUrl(url, params), requestBody, headers, tag);

            call = getOkHttpClient().newCall(request);
            call.enqueue(callback);
        } catch (Throwable e) {
            e.printStackTrace();
            callback.onFailure(call, new IOException("get", e));
        }
    }

    public static <T> void post(String url, OkHttpCallback<T> okHttpCallback) {
        post(url, null, okHttpCallback);
    }

    public static <T> void post(String url, OkRequestParams params, OkHttpCallback<T> okHttpCallback) {
        post(url, params, null, okHttpCallback);
    }

    public static <T> void post(String url, OkRequestParams params, Object tag, OkHttpCallback<T> okHttpCallback) {
        post(url, params, tag, false, okHttpCallback);
    }

    public static <T> void post(String url, OkRequestParams params, Object tag, boolean isProgress, OkHttpCallback<T> okHttpCallback) {
        OkHttpLog.d(TAG, getFinalUrl(url, params));

        Call call = null;
        Callback callback = getCallBack(okHttpCallback);
        try {
            RequestBody requestBody = getRequestBody(params, isProgress, okHttpCallback);
            Headers headers = getRequestHeaders(params);
            Request request = getRequest(url, requestBody, headers, tag);

            call = getOkHttpClient().newCall(request);
            call.enqueue(callback);
        } catch (Throwable e) {
            e.printStackTrace();
            callback.onFailure(call, new IOException("post", e));
        }
    }

    public static <T> void syncPost(String url, OkHttpCallback<T> okHttpCallback) {
        syncPost(url, null, okHttpCallback);
    }

    public static <T> void syncPost(String url, OkRequestParams params, OkHttpCallback<T> okHttpCallback) {
        syncPost(url, params, null, okHttpCallback);
    }

    public static <T> void syncPost(String url, OkRequestParams params, Object tag, OkHttpCallback<T> okHttpCallback) {
        syncPost(url, params, tag, false, okHttpCallback);
    }

    public static <T> void syncPost(String url, OkRequestParams params, Object tag, boolean isProgress, OkHttpCallback<T> okHttpCallback) {
        OkHttpLog.d(TAG, getFinalUrl(url, params));

        Call call = null;
        Callback callback = getCallBack(okHttpCallback);
        try {
            RequestBody requestBody = getRequestBody(params, isProgress, okHttpCallback);
            Headers headers = getRequestHeaders(params);
            Request request = getRequest(url, requestBody, headers, tag);

            call = getOkHttpClient().newCall(request);
            Response response = call.execute();
            if (response != null) {
                callback.onResponse(call, response);
            } else {
                callback.onFailure(call, new IOException("syncPost: response == null"));
            }
        } catch (Throwable e) {
            e.printStackTrace();
            callback.onFailure(call, new IOException("syncPost", e));
        }
    }

    public static void cancelTag(Object tag) {
        try {
            for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
            for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static <T> Callback getCallBack(OkHttpCallback<T> okHttpCallBack) {
        if (okHttpCallBack == null) {
            return OkHttpCallback.DEFAULT_CALLBACK;
        } else {
            okHttpCallBack.setHandler(getOkHandler());
            return okHttpCallBack;
        }
    }

    public static Request getRequest(String url, RequestBody requestBody, Headers headers, Object tag) {
        Request.Builder builder = new Request.Builder();
        if (requestBody != null) {
            builder.post(requestBody);
        }
        if (headers != null) {
            builder.headers(headers);
        }
        if (tag != null) {
            builder.tag(tag);
        }
        builder.url(url);
        return builder.build();
    }

    public static String getFinalUrl(String url, OkRequestParams params) {
        if (params != null) {
            String paramString = params.getParamString().trim();
            if (!paramString.equals("") && !paramString.equals("?")) {
                url += url.contains("?") ? "&" : "?";
                url += paramString;
            }
        }
        return url;
    }

    private static Headers getRequestHeaders(OkRequestParams params) {
        return params == null ? null : params.getRequestHeaders();
    }

    private static RequestBody getRequestBody(OkRequestParams params) {
        return params == null ? null : params.getRequestBody();
    }

    private static <T> RequestBody getRequestBody(OkRequestParams params, boolean isProgress, OkHttpCallback<T> okHttpCallback) {
        RequestBody requestBody = getRequestBody(params);
        if (requestBody != null && isProgress && okHttpCallback != null) {
            requestBody = new ProgressRequestBody(requestBody, okHttpCallback);
        }
        return requestBody;
    }

    private static Handler getOkHandler() {
        if (mOkHandler == null) {
            mOkHandler = new Handler(Looper.getMainLooper());
        }
        return mOkHandler;
    }
}
