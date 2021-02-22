package com.github.gcm_with_bc_onjdk17;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jsse.provider.BouncyCastleJsseProvider;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.SSLContext;

import java.io.FileInputStream;
import java.io.IOException;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;

public class GcmWithBouncyCasteleOnJDK17 {

    public SSLConnectionSocketFactory getSslConnectionSocketFactory() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, KeyManagementException, IOException,
            KeyStoreException, CertificateException {
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.removeProvider(BouncyCastleJsseProvider.PROVIDER_NAME);
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        Security.insertProviderAt(new BouncyCastleJsseProvider(), 2);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
        System.out.println(cipher);
        
        // dealing with self signed certificate
        // SSLContext sslContext = SSLContexts.custom().build();

        KeyStore truststore = KeyStore.getInstance("JKS");
        char[] pwdArray = "changeit".toCharArray();
        truststore.load(null, pwdArray);
        String currentUsersHomeDir = System.getProperty("user.home");
        FileInputStream fis = new FileInputStream(currentUsersHomeDir + "/tmp/cacerts.jks");
        truststore.load(fis, pwdArray);
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(truststore, new TrustSelfSignedStrategy()).build();

        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .build();

        String serverURL = "https://url.domain";

        HttpPost out = new HttpPost(serverURL);
        CloseableHttpResponse execute = httpClient.execute(out);
        return sslConnectionSocketFactory;
    }

}
