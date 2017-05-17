package com.streaming.download;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;


public class StreamLink {
    private static ServiceConnection mServiceConn;
    private static Messenger mService;
    private static String data;

    private static Context mContext;


    public StreamLink(Context context, String url) {
        this.mContext = context;

        this.data = url;

        if (mService == null | mServiceConn == null) {
            tryStartService(true);
        } else {
            sendMessage(data);
        }
    }

    void sendMessage(String url) {
        Message msg = Message.obtain(null, 1);
        Bundle bundle = new Bundle(1);
        bundle.putString(DownloadService.STREAMING_URL_TAG, url);
        msg.setData(bundle);
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    void tryStartService(final boolean sendMessage) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mServiceConn = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName cn, IBinder binder) {
                        try {
                            mService = new Messenger(binder);
                            if (sendMessage) {
                                sendMessage(data);
                            }
                        } catch (Throwable t) {
                            Log.e("Exception", "" + t.toString());
                        }
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName cn) {
                        mService = null;
                        mServiceConn = null;
                    }
                };
                ComponentName cn = new ComponentName(StreamLink.class.getPackage().getName(), DownloadService.class.getName());
                Intent intent = new Intent(mContext, DownloadService.class);
                //intent.setComponent(cn);


                mContext.bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);

            }
        })
                .start();
    }
}
