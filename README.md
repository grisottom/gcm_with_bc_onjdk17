This is a failed attempt at making a TLS connection with GCM using JDK 1.7 and BouncyCastle.

Any advice is welcome :)
 
This fails

```bash
JAVA_HOME="/usr/lib/jvm/java-7-openjdk-amd64/jre" mvn clean install
```

And this passes

```bash
JAVA_HOME="/usr/lib/jvm/java-8-openjdk-amd64/jre" mvn clean install
```

I need it to pass with JDK 1.7