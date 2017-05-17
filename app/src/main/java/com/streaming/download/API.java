package com.streaming.download;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;


public interface API
{
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    @GET
    Call<ResponseBody> getManifest(@Url String url);
}
