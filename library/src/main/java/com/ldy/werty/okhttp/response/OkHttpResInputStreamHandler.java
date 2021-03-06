package com.ldy.werty.okhttp.response;

import java.io.InputStream;

import okhttp3.Response;

/**
 * Created by ldy on 2015/12/30.
 */
public abstract class OkHttpResInputStreamHandler extends OkHttpCallback<InputStream> {

    @Override
    protected final boolean isPostMainThread() {
        return false;
    }

    @Override
    protected InputStream parseResponse(Response response) throws Throwable {
        return response.body().byteStream();
    }

}
