package com.ldy.werty.okhttp.response;

import okhttp3.Response;

/**
 * callback in async thread
 *
 * Created by ldy on 2015/12/30.
 */
public abstract class OkHttpResResponseHandler extends OkHttpCallback<Response> {

    @Override
    protected final boolean isPostMainThread() {
        return false;
    }

    @Override
    protected Response parseResponse(Response response) throws Throwable {
        return response;
    }

}
