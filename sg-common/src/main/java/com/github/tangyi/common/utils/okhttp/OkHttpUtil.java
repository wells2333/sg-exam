package com.github.tangyi.common.utils.okhttp;

import com.github.tangyi.common.utils.JsonMapper;
import okhttp3.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {

	private static final Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);

	private static OkHttpUtil instance;

	private final OkHttpClient okHttpClient;

	@SuppressWarnings(value = {"uncheck"})
	public OkHttpUtil() {
		// 创建httpClient
		okHttpClient = new OkHttpClient().newBuilder()
				// 设置连接的超时时间
				.connectTimeout(10, TimeUnit.SECONDS)
				// 设置响应的超时时间
				.writeTimeout(30, TimeUnit.SECONDS)
				// 请求的超时时间
				.readTimeout(30, TimeUnit.SECONDS)
				// 配置忽略证书
				.sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager())
				.hostnameVerifier(SSLSocketClient.getHostnameVerifier())
				// 日志拦截器
				.addInterceptor(new LogInterceptor()).build();
	}

	public synchronized static OkHttpUtil getInstance() {
		if (instance == null)
			instance = new OkHttpUtil();
		return instance;
	}

	public String get(String url, Map<String, Object> header) throws Exception {
		return getResponseBody(url, header).string();
	}

	public ResponseBody getResponseBody(String url, Map<String, Object> header) throws Exception {
		return getResponse(url, header).body();
	}

	public Response getResponse(String url, Map<String, Object> header) throws Exception {
		Request.Builder builder = new Request.Builder().url(url).get();
		for (String key : header.keySet())
			builder.addHeader(key, header.get(key).toString());
		return okHttpClient.newCall(builder.build()).execute();
	}

	public String postJson(String url, Map<String, Object> header, Map<String, Object> data) {
		try {
			MediaType mediaType = MediaType.parse("application/json");
			String json = mapToJSONObject(data).toString();
			RequestBody body = RequestBody.create(mediaType, json);
			Request.Builder builder = new Request.Builder().url(url).post(body);
			for (String key : header.keySet())
				builder.addHeader(key, header.get(key).toString());
			return okHttpClient.newCall(builder.build()).execute().body().string();
		} catch (Exception e) {
			logger.error("postJson failed", e);
		}
		return null;
	}

	public String postJson(String url, Map<String, Object> header, Object object) {
		try {
			MediaType mediaType = MediaType.parse("application/json");
			String json = JsonMapper.getInstance().toJson(object);
			RequestBody body = RequestBody.create(mediaType, json);
			Request.Builder builder = new Request.Builder().url(url).post(body);
			for (String key : header.keySet())
				builder.addHeader(key, header.get(key).toString());
			return okHttpClient.newCall(builder.build()).execute().body().string();
		} catch (Exception e) {
			logger.error("postJson failed", e);
		}
		return null;
	}

	public String postForm(String url, Map<String, Object> header, Map<String, Object> data) throws Exception {
		logger.debug("post data:{}", data);
		FormBody.Builder formBuilder = new FormBody.Builder();
		for (String key : data.keySet())
			formBuilder.add(key, data.get(key).toString());
		Request.Builder builder = new Request.Builder().url(url).post(formBuilder.build());
		for (String key : header.keySet())
			builder.addHeader(key, header.get(key).toString());
		return okHttpClient.newCall(builder.build()).execute().body().string();
	}

	public String postFile(String url, Map<String, Object> header, String filePath) throws Exception {
		Request.Builder builder = new Request.Builder().url(url)
				.post(RequestBody.create(MediaType.parse("application/octet-stream"), new File(filePath)));
		for (String key : header.keySet())
			builder.addHeader(key, header.get(key).toString());
		Response response = okHttpClient.newCall(builder.build()).execute();
		if (!response.isSuccessful())
			throw new RuntimeException("请求接口失败！");
		return response.body().string();
	}

	public String put(String url, Map<String, Object> header, String file) throws Exception {
		RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), new File(file));
		Request.Builder builder = new Request.Builder().url(url).put(body);
		for (String key : header.keySet())
			builder.addHeader(key, header.get(key).toString());
		Response response = okHttpClient.newCall(builder.build()).execute();
		if (!response.isSuccessful())
			throw new RuntimeException("请求接口失败！");
		return response.body().string();
	}

	public String putBytes(String url, Map<String, Object> header, byte[] bytes) throws Exception {
		RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), bytes);
		Request.Builder builder = new Request.Builder().url(url).put(body);
		for (String key : header.keySet())
			builder.addHeader(key, header.get(key).toString());
		Response response = okHttpClient.newCall(builder.build()).execute();
		if (!response.isSuccessful())
			throw new RuntimeException("请求接口失败！");
		return response.body().string();
	}

	public String putStream(String url, Map<String, Object> header, InputStream inputStream) throws Exception {
		RequestBody body = OkHttpRequestBodyUtil.create(MediaType.parse("application/octet-stream"), inputStream);
		Request.Builder builder = new Request.Builder().url(url).put(body);
		for (String key : header.keySet())
			builder.addHeader(key, header.get(key).toString());
		Response response = okHttpClient.newCall(builder.build()).execute();
		if (!response.isSuccessful())
			throw new RuntimeException("请求接口失败！");
		return response.body().string();
	}

	public String patch(String url, Map<String, Object> header, Map<String, Object> data) throws Exception {
		logger.debug("patch data:{}", data);
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, mapToJSONObject(data).toString());
		Request.Builder builder = new Request.Builder().url(url).patch(body);
		for (String key : header.keySet())
			builder.addHeader(key, header.get(key).toString());
		return okHttpClient.newCall(builder.build()).execute().body().string();
	}

	public String delete(String url, Map<String, Object> header) throws Exception {
		return deleteResponse(url, header).body().string();
	}

	public Response deleteResponse(String url, Map<String, Object> header) throws Exception {
		Request.Builder builder = new Request.Builder().url(url).delete();
		for (String key : header.keySet())
			builder.addHeader(key, header.get(key).toString());
		return okHttpClient.newCall(builder.build()).execute();
	}

	private JSONObject mapToJSONObject(Map<String, Object> data) {
		JSONObject jsonObject = new JSONObject();
		for (String key : data.keySet()) {
			Object object = data.get(key);
			// 子节点
			if (object instanceof HashMap) {
				Map childMap = (Map) object;
				JSONObject childJsonObject = new JSONObject();
				for (Object childKey : childMap.keySet())
					childJsonObject.put(childKey.toString(), childMap.get(childKey));
				object = childJsonObject;
			}
			jsonObject.put(key, object);
		}
		return jsonObject;
	}
}
