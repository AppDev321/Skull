package com.streaming.download;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DownloadService extends Service implements Handler.Callback
{
    public static final String STREAMING_URL_TAG = "STREAMING_URL_TAG";
    private static final String STREAM_INF_TAG = "#EXT-X-STREAM-INF";
    private static final String BANDWIDTH_ATTR = "BANDWIDTH";
    private static final Pattern BANDWIDTH_ATTR_REGEX = Pattern.compile(BANDWIDTH_ATTR + "=(\\d+),");
    private static final Pattern mPattern = Pattern.compile("segment([0-9]+)_.*");
    API api;

    //String url = "http://vodhls-vh.akamaihd.net/i/songs/57/1832657/21044908/21044908_,16,128,64,.mp4.csmil/master.m3u8?set-akamai-hls-revision=5&hdnts=st=1483643762~exp=1483661762~acl=/i/songs/57/1832657/21044908/21044908_,16,128,64,.mp4*~hmac=7294c5961f933cd19b7b3ee371e3fcb775aac160ebb4dcf6b69882c0f797cdf9";

    int total=0;
    int count =0;
    Handler mHandler = new Handler(this);
    static String lastUrl = "";
    static String urlTemp=Constant.folderTemp;

    final Messenger mMessenger = new Messenger(new ClientHandler());

    class ClientHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what==1)
            {
                String urlToProcess = msg.getData().getString(DownloadService.STREAMING_URL_TAG,"null");
                if(urlToProcess.equalsIgnoreCase("null"))// | lastUrl.equalsIgnoreCase(urlToProcess))
                    return;
                Log.e("XPOSED",urlToProcess);
                download(urlToProcess);
            }
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }


    public void download(final String urlParam)
    {
        api = ServiceGenerator.createService(API.class);

        urlTemp = urlParam;

        if(!urlParam.contains(".m3u8"))
        {
            Log.e("XPOSED",urlParam);
            makeToast("Downloading...");
            api.downloadFile(urlParam).enqueue(new Callback<ResponseBody>()
            {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                {
                    String filename = Utils.md5_digest(urlParam.getBytes())+Constant.fileExtension;
                    Utils.writeResponseBodyToDisk(filename,response.body(),false);
                    Toast.makeText(getApplicationContext(), "File saved: "+filename,Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t)
                {

                }
            });
            this.stopSelf();
            return;
        }

        //down the master manifest
        api.getManifest(urlParam).enqueue(getMasterPlaylist);
    }

    Callback<ResponseBody> getMasterPlaylist = new Callback<ResponseBody>()
    {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
        {
            try
            {
                if(response.body()==null)
                {
                    makeToast("Response body1 is empty");
                    return;
                }

                lastUrl = urlTemp;
                String res = response.body().string();
                String[] lines = res.split("\n");


                TreeMap<Integer,String> bitrateUrlMap = new TreeMap<>();

                for(int i=0;i<lines.length;i++)
                {
                    if(lines[i].startsWith(STREAM_INF_TAG))
                    {
                        Matcher matcher = BANDWIDTH_ATTR_REGEX.matcher(lines[i]);
                        if(matcher.find())
                        {
                            int bitrate = Integer.parseInt(matcher.group(1));
                            bitrateUrlMap.put(bitrate,lines[i+1]);
                            i++;
                        }
                    }

                }

                String url = bitrateUrlMap.get(bitrateUrlMap.lastKey());
                bitrateUrlMap.clear();

                //get the list of segments
                api.getManifest(url).enqueue(getPlaylist);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t)
        {
            makeToast("error1");
        }
    };

    Callback<ResponseBody> getPlaylist = new Callback<ResponseBody>()
    {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
        {
            try
            {
                if(response.body()==null)
                    makeToast("Response body2 is empty");
                else
                {
                    total = 0;
                    count = 0;
                    String res = response.body().string();

                    String[] lines = res.split("\n");

                    makeToast("Downloading..");

                    for(String line : lines)
                    {
                        if(line.startsWith("#"))
                            continue;
                        total++;
                        api.downloadFile(line).enqueue(segmentDownloader);
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t)
        {
            makeToast("error2");
        }
    };

    Callback<ResponseBody> segmentDownloader =new Callback<ResponseBody>()
    {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
        {
            HttpUrl url1 = call.request().url();
            List<String> seg = url1.pathSegments();
            String url = seg.get(seg.size()-1);
            Matcher matcher= mPattern.matcher(url);
            if(matcher.find())
            {
                String filename = matcher.group(1);
                if(Utils.writeResponseBodyToDisk(filename,response.body(),true))
                {
                    count++;
                    mHandler.sendEmptyMessage(1);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t)
        {
            makeToast("error3");
        }
    };

    void makeToast(String err)
    {
        Toast.makeText(getApplicationContext(),err,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean handleMessage(Message msg)
    {
        if(msg.what==1)
        {
            Log.e("XPOSED","Count: " +count);
            Log.e("XPOSED","Total: " +total);
            if(count!=0 && count==total)
            {
                Log.e("XPOSED","Done Count: " +count);
                Log.e("XPOSED","Done Total: " +total);
                int index = lastUrl.indexOf('?');
                String filename;
                if(index!=-1)
                    filename = Utils.md5_digest(lastUrl.substring(0,index).getBytes())+Constant.fileExtension;
                else
                    filename = Utils.md5_digest(lastUrl.getBytes())+Constant.fileExtension;

                boolean b = Utils.mergeFiles(total,filename);
                if(b)
                {
                    Toast.makeText(getApplicationContext(), "File saved: "+filename,Toast.LENGTH_LONG).show();
                    this.stopSelf();
                }
                count = total=0;
            }
        }
        return true;
    }
}
