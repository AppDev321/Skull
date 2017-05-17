package com.streaming.download;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;



public class ServiceGenerator
{
    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor()
            {
                @Override
                public Response intercept(Chain chain) throws IOException
                {
                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .addHeader("User-Agent",System.getProperty("http.agent"));

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            })
            .cookieJar(new CookieJar() {
                private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies)
                {
                    if(url.toString().contains("master.m3u8"))
                        cookieStore.put(url.host(), cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            })
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(3, TimeUnit.MINUTES)
            .connectTimeout(10,TimeUnit.SECONDS)
            .build();

    private static Retrofit.Builder builder = new Retrofit.Builder();

    public static <S> S createService(Class<S> serviceClass)
    {
        Retrofit retrofit = builder.client(httpClient).baseUrl(Constant.baseUrl).build();
        return retrofit.create(serviceClass);
    }
}