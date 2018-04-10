package com.github.gcm_with_bc_onjdk17;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jsse.provider.BouncyCastleJsseProvider;

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

        HttpPost out = new HttpPost("https://connectis-broker.dev.connectis.org/sp/ars");
        CloseableHttpResponse execute = httpClient.execute(out);
        return sslConnectionSocketFactory;
    }

}
