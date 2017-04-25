package com.webservices.RestApi;


import com.skull.utils.Constants;
import com.webservices.models.ApiResponse;
import com.webservices.models.ParseRespone;
import com.webservices.models.RequestMovieByCat;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

public interface RestApi {

    @GET("getBanner.php")
    Call<ApiResponse> getMovieBanners();
    @GET("getCategories.php")
    Call<ApiResponse> getCategories();
    @GET("getAllMovies.php")
    Call<ApiResponse> getAllMoviesList();


    @POST("getMoviesByCat.php")
    Call<ApiResponse> getMovieByCategory(
            @Body RequestMovieByCat requestMovieByCat);
    @POST("getParseLink.php")
    Call<ParseRespone> getYouTubeParseUrl(@Body RequestMovieByCat requestMovieByCat);

}

