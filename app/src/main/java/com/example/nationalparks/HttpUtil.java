package com.example.nationalparks;

import static org.apache.http.conn.ssl.SSLSocketFactory.SSL;

import com.splunk.rum.SplunkRum;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;

public class HttpUtil {
    public static Call.Factory buildOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1));
        try {
            // NOTE: This is really bad and dangerous. Don't ever do this in the real world.
            // it's only necessary because the demo endpoint uses a self-signed SSL cert.
            SSLContext sslContext = SSLContext.getInstance(SSL);
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            builder
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier(new AllowAllHostnameVerifier());

        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return SplunkRum.getInstance().createRumOkHttpCallFactory(builder.build());
    }

    private static final TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                               String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                               String authType) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }
    };
}
