package com.github.gcm_with_bc_onjdk17;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

public class GcmWithBouncyCasteleOnJDK17 {

    public SSLConnectionSocketFactory getSslConnectionSocketFactory() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, KeyManagementException, IOException {
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.removeProvider(BouncyCastleJsseProvider.PROVIDER_NAME);
        Security.insertProviderAt(new BouncyCastleProvider(), 0);
        Security.insertProviderAt(new BouncyCastleJsseProvider(), 1);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
        System.out.println(cipher);

        SSLContext sslContext = SSLContexts.custom()
                .build();

        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .build();

        HttpGet out = new HttpGet("https://cloudflare.com/");
        CloseableHttpResponse execute = httpClient.execute(out);
        return sslConnectionSocketFactory;
    }

}
