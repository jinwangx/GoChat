package com.jw.gochat.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {
	private final static String ERROR = "error";
	private static HttpUtils instance;
	private  static OkHttpClient mClient;

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

	private HttpUtils() {}

	public static HttpUtils getInstance() {
		if (instance == null) {
			synchronized (HttpUtils.class) {
				if (instance == null) {
					instance = new HttpUtils();
				}
			}
		}
		return instance;
	}


	/**
	 * 初始化请求引擎
	 * @param client
	 */
	public static void init(OkHttpClient client){
		mClient=client;
	}

	/**
	 * 直接get请求,同步请求
	 * @param baseUrl
	 * @param headers
	 * @param params
	 * @return
	 */
	public String get(String baseUrl, Map<String, String> headers,
						Map<String, String> params) {
		StringBuilder tempParams = new StringBuilder();
		String result=ERROR;
		try {
			//处理参数
			int pos = 0;
			for (String key : params.keySet()) {
				if (pos > 0) {
					tempParams.append("&");
				}
				//对参数进行URLEncoder
				tempParams.append(String.format("%s=%s", key, URLEncoder.encode(params.get(key), "utf-8")));
				pos++;
			}
			//补全请求地址
			String requestUrl = String.format("%s/%s?%s", baseUrl, tempParams.toString());
			//创建一个请求
			Request.Builder url = new Request.Builder().url(requestUrl);
			if (headers != null) {
				for (Map.Entry<String, String> me : headers.entrySet()) {
					url.addHeader(me.getKey(),me.getValue());
				}
			}
			Request request = url.build();
			Response response = mClient.newCall(request).execute();
			/* 发送请求并等待响应 */
			/* 若状态码为200 ok */
			if (response.code() == 200) {
				/* 读返回数据 */
				result = response.body().string();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 提交，同步请求
	 * @param url
	 * @param params
	 * @return
	 */
	public boolean post(String url, Map<String, String> headers,Map<String, String> params) {
		FormBody.Builder bodyBuilder = new FormBody.Builder();
		Request.Builder requestBuilder = new Request.Builder().url(url);
		String result = ERROR;
		if (headers != null) {
			for (Map.Entry<String, String> me : headers.entrySet()) {
				requestBuilder.addHeader(me.getKey(),me.getValue());
			}
		}
		if (params != null) {
			for (Map.Entry<String, String> me : params.entrySet()) {
				bodyBuilder.add(me.getKey(),me.getValue());
			}
		}
		RequestBody requestBody = bodyBuilder.build();
		Request request = requestBuilder.post(requestBody).build();
		try {
			Response response = mClient.newCall(request).execute();
			if (response.code() == 200) {
				/* 读返回数据 */
				result = response.body().string();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseResult(result);
	}

	/**
	 * 下载，同步请求
	 * @param url 完整url
	 * @param headers 请求头
	 * @param params 请求参数
	 * @param iconPath 下载到的路径
	 * @return
	 */
	public boolean download(String url, Map<String, String> headers,Map<String, String> params,
							Map<String,String> iconPath) {
		File localFile=new File(iconPath.get("file"));
		FormBody.Builder bodyBuilder = new FormBody.Builder();
		Request.Builder requestBuilder = new Request.Builder().url(url);
		String result = ERROR;
		if (headers != null) {
			for (Map.Entry<String, String> me : headers.entrySet()) {
				requestBuilder.addHeader(me.getKey(),me.getValue());
			}
		}
		if (params != null) {
			for (Map.Entry<String, String> me : params.entrySet()) {
				bodyBuilder.add(me.getKey(),me.getValue());
			}
		}
		RequestBody requestBody = bodyBuilder.build();
		Request request = requestBuilder.post(requestBody).build();
		try {
			Response response = mClient.newCall(request).execute();
			/* 若状态码为200 ok */
			if (response.code() == 200) {
				File dir = localFile.getParentFile();
				if (!dir.exists()) {
					dir.mkdirs();
				}

				/* 读返回数据 */
				InputStream stream = response.body().byteStream();
				FileOutputStream fos = new FileOutputStream(localFile);
				byte[] buffer = new byte[1024];
				int len = -1;
				while ((len = stream.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					fos.flush();
				}
				stream.close();
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseResult(result);
	}


	private boolean parseResult(String result) {
		if (ERROR.equals(result)) {
			return false;
		} else {
			JsonParser parser = new JsonParser();
			try {
				JsonObject root = parser.parse(result).getAsJsonObject();
				JsonPrimitive flagObject = root.getAsJsonPrimitive("flag");
				return flagObject.getAsBoolean();
			} catch (Exception e) {
				return false;
			}
		}
	}

	/**
	 * 上传，同步请求
	 * @param url url
	 * @param headers 请求头
	 * @param params 参数
	 * @param filePaths 本地icon路径
	 * @return
	 */
	public boolean upload(String url, Map<String, String> headers,Map<String, String> params,
						  Map<String, String> filePaths) {
		Map<String, File> files=new HashMap<String, File>();
		if (filePaths != null) {
			for (Map.Entry<String, String> me : filePaths.entrySet()) {
				files.put(me.getKey(),new File(me.getValue()));
			}
		}
		Request.Builder RequestBuilder = new Request.Builder();
		Request.Builder requestBuilderUrl = RequestBuilder.url(url);// 添加URL地址
		if (headers != null) {
			for (Map.Entry<String, String> me : headers.entrySet()) {
				requestBuilderUrl.addHeader(me.getKey(),me.getValue());
			}
		}
		MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
		multipartBodyBuilder.setType(MultipartBody.FORM);
		if (params != null){
			for (String key : params.keySet()) {
				multipartBodyBuilder.addFormDataPart(key, params.get(key));
			}
		}
		//遍历map中所有参数到builder
		if (files != null){
			for (Map.Entry<String, File> me : files.entrySet()) {
				RequestBody fileBody = RequestBody.create(MEDIA_TYPE_PNG, me.getValue());
				multipartBodyBuilder.addFormDataPart("file", me.getKey(), fileBody);
			}
		}

		//构建请求体
		RequestBody requestBody = multipartBodyBuilder.build();
		Request request = RequestBuilder.post(requestBody).build();

		String result=ERROR;
		try {
			Response response = mClient.newCall(request).execute();
			if (response.code() == 200) {
				/* 读返回数据 */
				result = response.body().string();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseResult(result);
	}

	/**
	 * post异步请求
	 * @param url
	 * @param headers
	 * @param params
	 * @param callBack
	 */
	public void post(String url, HashMap<String, String> headers,
						  HashMap<String, String> params, final ObjectCallBack callBack) {
		FormBody.Builder bodyBuilder = new FormBody.Builder();
		Request.Builder requestBuilder = new Request.Builder().url(url);
		if (headers != null) {
			for (Map.Entry<String, String> me : headers.entrySet()) {
				requestBuilder.addHeader(me.getKey(),me.getValue());
			}
		}
		if (params != null) {
			for (Map.Entry<String, String> me : params.entrySet()) {
				bodyBuilder.add(me.getKey(),me.getValue());
			}
		}
		RequestBody requestBody = bodyBuilder.build();
		final Request request = requestBuilder.post(requestBody).build();
		mClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				callBack.onError(0,"服务器异常");
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if(response.code()==200)
					callBack.onSuccess(response);
				else
					callBack.onError(0,"服务器异常");
			}
		});
	}

	/**
	 * 上传，异步请求
	 * @param url 接口地址
	 * @param headers 请求头
	 * @param params 参数
	 * @param <T>
	 */
	public <T>void upload(String url, HashMap<String, String> headers,
						  HashMap<String, Object> params, final ObjectCallBack callBack) {
		try {
			Request.Builder requestBuilder = new Request.Builder();
			Request.Builder requestBuilderUrl = requestBuilder.url(url);
			if (headers != null) {
				for (Map.Entry<String, String> me : headers.entrySet()) {
					requestBuilderUrl.addHeader(me.getKey(),me.getValue());
				}
			}

			MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
			//设置类型
			bodyBuilder.setType(MultipartBody.FORM);
			//追加参数
			for (String key : params.keySet()) {
				Object object = params.get(key);
				if (!(object instanceof File)) {
					bodyBuilder.addFormDataPart(key, object.toString());
				} else {
					File file = (File) object;
					bodyBuilder.addFormDataPart(key, file.getName(), RequestBody.create(null, file));
				}
			}
			//创建RequestBody
			RequestBody body = bodyBuilder.build();
			//创建Request
			final Request request = requestBuilderUrl.post(body).build();
			//单独设置参数 比如读取超时时间
			final Call call = mClient.newBuilder().writeTimeout(50, TimeUnit.SECONDS).build().newCall(request);
			call.enqueue(new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {
					callBack.onError(0,"服务器异常");
				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					if(response.code()==200)
						callBack.onSuccess(response);
					else
						callBack.onError(0,"服务器异常");
				}
			});
		} catch (Exception e) {
			callBack.onError(0,e.toString());
		}
	}

	public abstract class ObjectCallBack<T> {
		private Class<T> clazz;

		@SuppressWarnings("unchecked")
		public ObjectCallBack() {
			ParameterizedType type = (ParameterizedType) this.getClass()
					.getGenericSuperclass();
			this.clazz = (Class<T>) type.getActualTypeArguments()[0];
		}

		public abstract void onSuccess(T t);

		public abstract void onError(int error, String msg);

		public Class<T> getClazz() {
			return clazz;
		}

	}
}
