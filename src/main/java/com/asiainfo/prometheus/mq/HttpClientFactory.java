// package com.asiainfo.prometheus.export;
package com.asiainfo.prometheus.mq;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.protocol.HttpContext;

public class HttpClientFactory {
    static RequestConfig requestConfig =
        RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();

    private static CloseableHttpAsyncClient asyncClient;
    private static HttpClient defaultClient;

    public static synchronized CloseableHttpAsyncClient getAsyncClient() {
        if (asyncClient == null) {
            asyncClient = HttpAsyncClients.custom().setDefaultRequestConfig(requestConfig)
                .setUserAgent("HomeCooked Trade HTTP Client").setMaxConnPerRoute(32).setMaxConnTotal(256).build();
            asyncClient.start();
        }

        return asyncClient;
    }

    public synchronized static HttpClient get()
        throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        if (defaultClient == null) {

            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}

                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
            }};

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);

            defaultClient = HttpClientBuilder.create().setUserAgent("Home-Cooked Order HTTP Client")
                .setRetryHandler(new StandardHttpRequestRetryHandler(3, false))
                .setServiceUnavailableRetryStrategy(new DefaultServiceUnavailableRetryStrategy()).setMaxConnPerRoute(32)
                .setMaxConnTotal(256).setSSLSocketFactory(sslConnectionSocketFactory).build();
        }
        return defaultClient;
    }

    public static RequestConfig getDefaultRequestConfig() {
        return requestConfig;
    }

    static class DefaultServiceUnavailableRetryStrategy implements ServiceUnavailableRetryStrategy {
        public final static int MAX_RETRIES = 3;
        public final static int RETRY_INTERVAL = 1000;

        @Override
        public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {
            return executionCount <= MAX_RETRIES
                && response.getStatusLine().getStatusCode() >= HttpStatus.SC_INTERNAL_SERVER_ERROR;
        }

        @Override
        public long getRetryInterval() {
            return RETRY_INTERVAL;
        }
    }
}