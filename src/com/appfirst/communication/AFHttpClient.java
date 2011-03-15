/*
 * Copyright 2009-2011 AppFirst, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.appfirst.communication;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

/**
 * 
 * @author Bin Liu
 *
 */
public class AFHttpClient extends DefaultHttpClient{
	private String TAG = "AFHttpClient";
	
	/**
	 * My default constructor. 
	 */
	public AFHttpClient() {
		
	}
	
	public DefaultHttpClient getAFHttpClient() {
		try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            try {
				trustStore.load(null, null);
			} catch (CertificateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

            SSLSocketFactory sf = new AFSSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            return new DefaultHttpClient(ccm, params);
        } catch (NoSuchAlgorithmException nsae) {
            Log.e(TAG, nsae.getMessage());
            return new DefaultHttpClient();
        } catch (KeyManagementException kme) {
        	Log.e(TAG, kme.getMessage());
            return new DefaultHttpClient();
        } catch (KeyStoreException kse) {
        	Log.e(TAG, kse.getMessage());
            return new DefaultHttpClient();
        } catch (UnrecoverableKeyException uke) {
        	Log.e(TAG, uke.getMessage());
            return new DefaultHttpClient();
        }
    }

    private class AFSSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        
        public AFSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, 
        KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);
            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) 
                	throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) 
                	throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        /**
         * 
         */
        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) 
        throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        /**
         * 
         * 
         */
        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}
