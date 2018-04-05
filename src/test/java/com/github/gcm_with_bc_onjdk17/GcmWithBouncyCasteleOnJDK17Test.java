package com.github.gcm_with_bc_onjdk17;

import java8.util.function.Predicate;
import java8.util.stream.StreamSupport;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class GcmWithBouncyCasteleOnJDK17Test {

    private static final Set<String> ACCEPTED_CYPHER_SUITES = new HashSet<String>() {{
        add("TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384");
        add("TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256");
        add("TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384");
        add("TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");
        add("TLS_RSA_WITH_AES_256_GCM_SHA384");
    }};

    @Test
    public void testTlsGCM() throws KeyManagementException, NoSuchAlgorithmException, IOException, NoSuchProviderException, NoSuchPaddingException {
        GcmWithBouncyCasteleOnJDK17 gcmWithBouncyCasteleOnJDK17 = new GcmWithBouncyCasteleOnJDK17();
        SSLConnectionSocketFactory sslConnectionSocketFactory = gcmWithBouncyCasteleOnJDK17.getSslConnectionSocketFactory();
        SSLSocketFactory socketFactory = getSocketfactory(sslConnectionSocketFactory);

        String[] supportedCipherSuites = socketFactory.getSupportedCipherSuites();

        long acceptedCiphers = StreamSupport.stream(Arrays.asList(supportedCipherSuites)).filter(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                System.out.println(s);
                return ACCEPTED_CYPHER_SUITES.contains(s);
            }
        }).count();
        System.out.println(supportedCipherSuites.length);
        assertTrue(acceptedCiphers > 0);
    }

    private static SSLSocketFactory getSocketfactory(SSLConnectionSocketFactory f) {
        return Whitebox.getInternalState(f, "socketfactory");
    }

}
