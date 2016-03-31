package com.ds.yam3ah.yam3ah;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class JSONParser {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";
	BufferedReader in = null;

	// constructor
	public JSONParser() {

	}

	public JSONObject getJSONFromUrl(String url) {

		// Making HTTP request
		try {
			HttpClient client = getNewHttpClient();
			URI link = null;
			try {
				link = new URI(url);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpGet request = new HttpGet();
			request.setURI(link);
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String l = "";
			String nl = System.getProperty("line.separator");
			while ((l = in.readLine()) != null) {
				Log.v("While", "" + l);
				sb.append(l + nl);
			}
			json = sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}

	public void ssl(Context ctx) {
		// Load CAs from an InputStream
		// (could be from a resource or ByteArrayInputStream or ...)
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			// From
			// https://www.washington.edu/itconnect/security/ca/load-der.crt

			InputStream caInput = ctx.getAssets().open("load-der.crt");
			// InputStream caInput = new BufferedInputStream(new
			// FileInputStream("load-der.crt"));
			Certificate ca;
			try {
				ca = cf.generateCertificate(caInput);
				System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
			} finally {
				caInput.close();
			}

			// Create a KeyStore containing our trusted CAs
			String keyStoreType = KeyStore.getDefaultType();
			KeyStore keyStore = KeyStore.getInstance(keyStoreType);
			keyStore.load(null, null);
			keyStore.setCertificateEntry("ca", ca);

			// Create a TrustManager that trusts the CAs in our KeyStore
			String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
			tmf.init(keyStore);

			// Create an SSLContext that uses our TrustManager
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, tmf.getTrustManagers(), null);

			// Tell the URLConnection to use a SocketFactory from our SSLContext
			URL url = new URL("https://certs.cac.washington.edu/CAtest/");
			HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
			urlConnection.setSSLSocketFactory(context.getSocketFactory());
			InputStream in = urlConnection.getInputStream();
		} catch (Exception e) {

		}

	}

	/*
	 * public HttpClient getNewHttpClient() { try { KeyStore trustStore =
	 * KeyStore.getInstance(KeyStore.getDefaultType()); trustStore.load(null,
	 * null);
	 * 
	 * SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
	 * sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	 * 
	 * HttpParams params = new BasicHttpParams();
	 * HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	 * HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
	 * 
	 * SchemeRegistry registry = new SchemeRegistry(); registry.register(new
	 * Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	 * registry.register(new Scheme("https", sf, 443));
	 * 
	 * ClientConnectionManager ccm = new ThreadSafeClientConnManager(params,
	 * registry);
	 * 
	 * return new DefaultHttpClient(ccm, params); } catch (Exception e) { return
	 * new DefaultHttpClient(); } }
	 */

	public static HttpClient getHttpsClient(HttpClient client) {
		try {
			X509TrustManager x509TrustManager = new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[] { x509TrustManager }, null);
			SSLSocketFactory sslSocketFactory = new ExSSLSocketFactory(sslContext);
			sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager clientConnectionManager = client.getConnectionManager();
			SchemeRegistry schemeRegistry = clientConnectionManager.getSchemeRegistry();
			schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
			return new DefaultHttpClient(clientConnectionManager, client.getParams());
		} catch (Exception ex) {
			return null;
		}
	}

	

	
	public static String sendData(String url, List<NameValuePair> parameters) {
	        String resutString="";
	        StringBuilder builder = new StringBuilder();
	        HttpClient client = getHttpsClient(new DefaultHttpClient());
	        HttpParams params = new BasicHttpParams();
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

	        try {
	            HttpPost httpPost = new HttpPost(url);
	            httpPost.setEntity(new UrlEncodedFormEntity(parameters));

	            HttpResponse response = client.execute(httpPost);
	            StatusLine statusLine = response.getStatusLine();
	            int statusCode = statusLine.getStatusCode();

	            if (statusCode == 200) {
	                HttpEntity entityResponse = response.getEntity();
	                InputStream content = entityResponse.getContent();
	                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	                String line=null;
	                while ((line = reader.readLine()) != null) {
	                    builder.append(line+"\n");
	                }
	                reader.close();
	                resutString=builder.toString();
	                Log.d("","Error seding data"+resutString);
	                
	            } else {
	                Log.d("","Error seding data");
	            }
	        } catch (ConnectTimeoutException e) {
	            Log.w("Connection Tome Out", e);
	        } catch (ClientProtocolException e) {
	            Log.w("ClientProtocolException", e);
	        } catch (SocketException e) {
	            Log.w("SocketException", e);
	        } catch (IOException e) {
	            Log.w("IOException", e);
	        }
	        return resutString;
	    }
	public static HttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}
}
