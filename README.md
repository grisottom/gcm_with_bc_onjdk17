Making a TLS connection with GCM using JDK 1.7 and BouncyCastle.
 
```bash
JAVA_HOME="/usr/lib/jvm/java-7-openjdk-amd64/jre" mvn clean test
```

This fork deals TLS connection targeting a self signed certificate. 

To avoid this error
```org.bouncycastle.tls.TlsFatalAlert: certificate_unknown```

I changed the original <b>sslContext</b> to load certificate from a truststore
```
        KeyStore truststore = KeyStore.getInstance("JKS");
        char[] pwdArray = "changeit".toCharArray();
        truststore.load(null, pwdArray);
        String currentUsersHomeDir = System.getProperty("user.home");
        FileInputStream fis = new FileInputStream(currentUsersHomeDir + "/tmp/cacerts.jks");
        truststore.load(fis, pwdArray);
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(truststore, new TrustSelfSignedStrategy()).build();
```

### Truststore

Supose we have

* ```serverURL = "https://url.domain"```, using certificate *.domain

dowload the public certificate to file:

* ```cert_domain.pem```

crate local truststore and import the certficate

* ```keytool -importcert -storepass changeit  -noprompt -alias cert_domain -file cert_domain.pem -storetype JKS -keystore ~/tmp/cacert.jks```


Thank you Andrei Cristian Petcu for the original project and for sharring the knownledg on Stackoverflow.