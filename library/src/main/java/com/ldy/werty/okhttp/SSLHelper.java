package com.ldy.werty.okhttp;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by lidongyang on 2017/7/17.
 */
public class SSLHelper {

    private SSLHelper() {
    }

    public static void configSSL(OkHttpClient.Builder builder) {
        X509TrustManager manager = getX509TrustManager();
        SSLSocketFactory sslFac = getSSLFactory(manager);
        if (sslFac != null) {
            builder.sslSocketFactory(sslFac);
            builder.hostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }

    }

    private static X509TrustManager getX509TrustManager() {
        return new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    private static SSLSocketFactory getSSLFactory(X509TrustManager manager) {
        try {
            TrustManager[] e = new TrustManager[]{manager};
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, e, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

}
