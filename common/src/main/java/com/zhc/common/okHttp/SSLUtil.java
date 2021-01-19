package com.zhc.common.okHttp;

//import com.zhangmen.braintrain.base.executors.Executor;
//import com.zhangmen.braintrain.base.res.BuildConfig;
//import com.zhangmen.braintrain.common.base.BaseApplication;
//import com.zhangmen.braintrain.common.utils.HttpDnsHelper;
//import com.zhangmen.track.event.net.RpcInfoInterceptor;

import com.zhc.common.BuildConfig;
import com.zhc.common.executors.Executor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
//import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 配置OkHttpClient
 */
public class SSLUtil {

    private static volatile SSLUtil mInstance;

    private OkHttpClient okHttpClient;
    private OkHttpClient.Builder builder;

    public static SSLUtil getInstance() {
        if (mInstance == null) {
            synchronized (SSLUtil.class) {
                if (mInstance == null) {
                    mInstance = new SSLUtil();
                    return mInstance;
                }
            }
        }
        return mInstance;
    }

    private SSLUtil() {
        initSslContext(null, null, null);
    }

    public OkHttpClient.Builder getOkHttpBuilder() {
        return builder;
    }

    public OkHttpClient getSslContext() {
        return okHttpClient;
    }

    public OkHttpClient getSslContext(InputStream[] certificates, InputStream bksFile,
                                      String password) {
        return initSslContext(certificates, bksFile, password);
    }

    private OkHttpClient initSslContext(InputStream[] certificates, InputStream bksFile, String password) {
        try {
            TrustManager[] trustManagers = prepareTrustManager(certificates);

            KeyManager[] keyManagers = prepareKeyManager(bksFile, password);

            SSLContext sslContext = SSLContext.getInstance("SSL");
            X509TrustManager trustManager =
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    };

            sslContext.init(keyManagers, new TrustManager[]{new MyTrustManager(trustManager)},
                    null);

            builder = new OkHttpClient.Builder()
//                    .dns(HttpDnsHelper.getInstance())
                    .hostnameVerifier((hostname, session) -> true)
                    .sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                    .addInterceptor(new HeaderInterceptor())
//                    .cache(new Cache(new File(BaseApplication.appSDPath, "http_cache"),
//                            20 * 1024 * 1024))
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .connectionPool(new ConnectionPool(5, 1, TimeUnit.MINUTES))
                    .dispatcher(new Dispatcher(Executor.INSTANCE.getIoExecutor())); //统一线程池

            //调试请求
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLogger());
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addNetworkInterceptor(interceptor);
            }

            okHttpClient = builder.build();
            return okHttpClient;
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        } catch (KeyManagementException e) {
            throw new AssertionError(e);
        } catch (KeyStoreException e) {
            throw new AssertionError(e);
        }
    }

    private TrustManager[] prepareTrustManager(InputStream... certificates) {
        if (certificates == null || certificates.length <= 0) return null;
        try {

            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(
                        certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null) certificate.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            TrustManagerFactory trustManagerFactory;

            trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            return trustManagerFactory.getTrustManagers();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
        try {
            if (bksFile == null || password == null) return null;

            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            clientKeyStore.load(bksFile, password.toCharArray());
            KeyManagerFactory keyManagerFactory =
                    KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, password.toCharArray());
            return keyManagerFactory.getKeyManagers();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }

    private static class MyTrustManager implements X509TrustManager {
        private X509TrustManager defaultTrustManager;
        private X509TrustManager localTrustManager;

        MyTrustManager(X509TrustManager localTrustManager)
                throws NoSuchAlgorithmException, KeyStoreException {
            TrustManagerFactory var4 =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            var4.init((KeyStore) null);
            defaultTrustManager = chooseTrustManager(var4.getTrustManagers());
            this.localTrustManager = localTrustManager;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            try {
                defaultTrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException ce) {
                localTrustManager.checkServerTrusted(chain, authType);
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    public void initHttpsConfig() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            X509TrustManager x509TrustManager = new X509TrustManager(){

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            sslContext.init(null,new TrustManager[]{x509TrustManager},new SecureRandom());
            HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
                public boolean verify(String s, SSLSession sslsession) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
