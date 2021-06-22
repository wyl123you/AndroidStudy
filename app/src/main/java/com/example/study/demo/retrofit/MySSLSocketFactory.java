package com.example.study.demo.retrofit;

import android.util.Log;

import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MySSLSocketFactory extends SSLSocketFactory {

    private static final String TAG = "MySSLSocketFactory";

    private SSLContext context = SSLContext.getInstance("SSL");
    private TrustManager manager = null;

    public MySSLSocketFactory(KeyStore store) throws NoSuchAlgorithmException, KeyManagementException {
        manager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                Log.d(TAG, "chain.length = " + chain.length + "  authType = " + authType);
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        context.init(null, new TrustManager[]{manager}, null);
    }

    public SSLContext getSSLContext() {
        return context;
    }

    public X509TrustManager getTrustManager() {
        return (X509TrustManager) manager;
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return new String[0];
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return new String[0];
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) {
        return null;
    }

    @Override
    public Socket createSocket(String host, int port) {
        return null;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) {
        return null;
    }

    @Override
    public Socket createSocket(InetAddress host, int port) {
        return null;
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) {
        return null;
    }
}
