package com.skull;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.ImageView;

import com.downloadmanager.application.App;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.squareup.okhttp.OkHttpClient;
import com.webservices.RestApi.RestApi;
import com.webservices.models.Banner;
import com.webservices.models.Category;
import com.webservices.models.MovieByCategory;
import com.webservices.urlmanager.UrlManager;
import com.webservices.utility.LoggingInterceptor;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Administrator on 4/5/2017.
 */

public class MyApplication extends App {
    ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
    boolean connected = false;
    public static String typeFace = "Quicksand-Regular.otf";
    public static String typeFace2 = "BoldRegular.otf";
    public float fontSize;
    public static MyApplication me;
    public static ImageLoader imageCoverLoader, imageProfileLoader;
    public static DisplayImageOptions options;
    public float btnFontSize;
    public static SweetAlertDialog pd = null;
    public List<Banner> modelBanner = new ArrayList<>();
    public List<Category> modelCategory = new ArrayList<>();
    public List<MovieByCategory> modelMovieByCategory = new ArrayList<>();
    public List<MovieByCategory> modelAllMovies = new ArrayList<>();
    private final int CacheSize = 52428800; // 50MB
    private final int MinFreeSpace = 2048; // 2MB


    public List<MovieByCategory> getModelAllMovies() {
        return modelAllMovies;
    }

    public void setModelAllMovies(List<MovieByCategory> modelAllMovies) {
        this.modelAllMovies = modelAllMovies;
    }

    public List<MovieByCategory> getModelMovieByCategory() {
        return modelMovieByCategory;
    }

    public void setModelMovieByCategory(List<MovieByCategory> modelMovieByCategory) {
        this.modelMovieByCategory = modelMovieByCategory;
    }

    public List<Banner> getModelBanner() {
        return modelBanner;
    }

    public void setModelBanner(List<Banner> modelBanner) {
        this.modelBanner = modelBanner;
    }

    public List<Category> getModelCategory() {
        return modelCategory;
    }

    public void setModelCategory(List<Category> modelCategory) {
        this.modelCategory = modelCategory;
    }

    public void onCreate() {
        super.onCreate();
        me = this;
        fontSize = me.getResources().getDimension(R.dimen._10sdp);
        btnFontSize = me.getResources().getDimension(R.dimen._8sdp);
        imageCoverLoader = ImageLoader.getInstance();
        imageProfileLoader = ImageLoader.getInstance();
        // imageCoverLoader.init(ImageLoaderConfiguration.createDefault(this));

      /*  ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(786, 1024) // default = device screen dimensions
                .diskCacheExtraOptions(786, 1024, null)
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();


        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.no_image)
                .showImageForEmptyUri(R.drawable.no_image)
                .showImageOnFail(R.drawable.no_image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(0))
                .build();

        imageCoverLoader.init(config);
        // imageCoverLoader.init(ImageLoaderConfiguration.createDefault(this));


        ImageLoaderConfiguration config2 = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(786, 1024) // default = device screen dimensions
                .diskCacheExtraOptions(786, 1024, null)
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();
        imageProfileLoader.init(config2);*/


        loadImageSetting();

    }

    public static MyApplication getInstance() {
        return me;
    }

    //For heder add
    public static RestApi getRestApi() {
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.interceptors().add(new LoggingInterceptor());
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(UrlManager.SERVER_URL).client(httpClient).build();
        return retrofit.create(RestApi.class);
    }

    public void showLoading(Context context) {

        pd = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(context.getResources().getColor(R.color.colorPrimary));
        pd.setTitleText(getResources().getString(R.string.txt_loading));
        pd.setCancelable(false);
        pd.show();
    }

    public void cancelDialog() {
        if (pd != null) {
            pd.dismiss();
            pd.cancel();
            pd = null;

        }
    }

    public void showServerFailureDialog(Context context) {
        final SweetAlertDialog taskCompDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        taskCompDialog
                .setTitleText("Error")
                .setContentText("Cannot connect to server");
        taskCompDialog.setCancelable(false);
        taskCompDialog.show();

    }

    public boolean isInternetConnect(Context context) {

        if (isOnline()) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()) {
                try {
                    URL url = new URL("https://www.google.com/");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    // urlc.setRequestProperty("User-Agent", "test");
                    // urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1000); // mTimeout is in seconds
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (IOException e) {
                    // Log.i("warning", "Error checking internet connection", e);
                    showServerFailureDialog(getInstance());
                    return false;
                }
            }
            return false;
        } else {
            return false;
        }
    }


    public boolean isOnline() {
        try {
            connectivityManager = (ConnectivityManager) getInstance()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }


    public void loadImageSetting() {
        // Universal Loader options and configuration.
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                // Bitmaps in RGB_565 consume 2 times less memory than in ARGB_8888.
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(false)
                .showImageOnLoading(R.drawable.no_image)
                .showImageForEmptyUri(R.drawable.no_image)
                .showImageOnFail(R.drawable.no_image)
                .cacheOnDisk(true)
                .build();
        Context context = this;
        File cacheDir = StorageUtils.getCacheDirectory(context);
        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .defaultDisplayImageOptions(options)
                .build();
        //ImageLoader.getInstance().init(config);
        imageCoverLoader = ImageLoader.getInstance();
        imageCoverLoader.init(config);

        // Check cache size
        long size = 0;
        File[] filesCache = cacheDir.listFiles();
        for (File file : filesCache) {
            size += file.length();
        }
        if (cacheDir.getUsableSpace() < MinFreeSpace || size > CacheSize) {
            ImageLoader.getInstance().getDiskCache().clear();
            imageCoverLoader.getDiskCache().clear();
        }


    }

    public void setImageUrl(ImageView img, String url) {


        //Use difrenet option if needed
        // Options used for the backdrop image in movie and tv details and gallery
        DisplayImageOptions optionsWithFade = new DisplayImageOptions.Builder()
                // Bitmaps in RGB_565 consume 2 times less memory than in ARGB_8888.
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(500))
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(false)
                .showImageOnLoading(R.drawable.no_image)
                .showImageForEmptyUri(R.drawable.no_image)
                .showImageOnFail(R.drawable.no_image)
                .cacheOnDisk(true)
                .build();
        DisplayImageOptions optionsWithoutFade = new DisplayImageOptions.Builder()
                // Bitmaps in RGB_565 consume 2 times less memory than in ARGB_8888.
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(false)
                .showImageOnLoading(R.drawable.no_image)
                .showImageForEmptyUri(R.drawable.no_image)
                .showImageOnFail(R.drawable.no_image)
                .cacheOnDisk(true)
                .build();

        // Options used for the backdrop image in movie and tv details and gallery
        DisplayImageOptions backdropOptionsWithFade = new DisplayImageOptions.Builder()
                // Bitmaps in RGB_565 consume 2 times less memory than in ARGB_8888.
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(500))
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(false)
                .showImageOnLoading(R.drawable.no_image)
                .showImageForEmptyUri(R.drawable.no_image)
                .showImageOnFail(R.drawable.no_image)
                .cacheOnDisk(true)
                .build();
        DisplayImageOptions backdropOptionsWithoutFade = new DisplayImageOptions.Builder()
                // Bitmaps in RGB_565 consume 2 times less memory than in ARGB_8888.
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(false)
                .showImageOnLoading(R.drawable.no_image)
                .showImageForEmptyUri(R.drawable.no_image)
                .showImageOnFail(R.drawable.no_image)
                .cacheOnDisk(true)
                .build();

        //Lets only use 1 to test
        imageCoverLoader.displayImage(url, img, optionsWithFade);

    }
}
