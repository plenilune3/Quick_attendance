package com.byunlove.pc.quick_attendance;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class TrustAllHost {

    public void httpsConnection(){ trustAllHosts(); }

    private void trustAllHosts() {

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {

                return new java.security.cert.X509Certificate[]{};

            }

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)

                    throws java.security.cert.CertificateException {
                // TODO Auto-generated method stub
            }

            @Override
            public void checkServerTrusted(

                    java.security.cert.X509Certificate[] chain, String authType)

                    throws java.security.cert.CertificateException {
                // TODO Auto-generated method stub
            }

        }};

        // Install the all-trusting trust manager
        try {

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        } catch (Exception e) { e.printStackTrace(); }

    }

}
