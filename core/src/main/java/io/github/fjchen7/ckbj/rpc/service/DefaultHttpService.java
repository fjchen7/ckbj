package io.github.fjchen7.ckbj.rpc.service;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class DefaultHttpService extends HttpService {
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");
    private String url;
    private OkHttpClient httpClient;

    public DefaultHttpService(String url) {
        this(url, createOkHttpClient());
    }

    public DefaultHttpService(String url, OkHttpClient httpClient) {
        this.url = url;
        this.httpClient = httpClient;
    }

    private static OkHttpClient createOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    @Override
    public String executeRequest(String body) throws IOException {
        RequestBody requestBody = RequestBody.create(body, JSON_MEDIA_TYPE);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        okhttp3.Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }
}
