package com.github.tangyi.common.utils.okhttp;

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.*;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

@Slf4j
public class SSLSocketClient {

	public static SSLSocketFactory getSSLSocketFactory() {
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, getTrustManager(), new SecureRandom());
			return sslContext.getSocketFactory();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static TrustManager[] getTrustManager() {
		return new TrustManager[]{new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[]{};
			}
		}};
	}

	public static HostnameVerifier getHostnameVerifier() {
		return (requestedHost, remoteServerSession) -> requestedHost.equalsIgnoreCase(
				remoteServerSession.getPeerHost());
	}

	public static X509TrustManager getX509TrustManager() {
		X509TrustManager trustManager = null;
		try {
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
					TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init((KeyStore) null);
			TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
			if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
				throw new IllegalArgumentException(
						"unexpected default trust manager: " + Arrays.toString(trustManagers));
			}
			trustManager = (X509TrustManager) trustManagers[0];
		} catch (Exception e) {
			log.error("getX509TrustManager failed", e);
		}
		return trustManager;
	}
}
