package com.streaming.download;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.hookAllMethods;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;


public class Main implements IXposedHookLoadPackage
{
    private static ServiceConnection mServiceConn;
    private static Messenger mService;
    private static String data;

    private static Context mContext;
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable
    {
        if(lpparam.packageName.equals("com.gaana"))
        {
            findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    MethodHook hook = new MethodHook();
                    //findAndHookMethod("com.player_framework.b", lpparam.classLoader, "playMusic",
                    //        Context.class, String.class, hook);
                    Class<?> hookClass = findClass("com.player_framework.GaanaMusicService",lpparam.classLoader);
                    hookAllMethods(hookClass, "b", hook);
                }
            });
        }
    }

    private class MethodHook extends XC_MethodHook
    {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable
        {
            if(param.args.length != 2)
                return;
            mContext = ((Context) param.thisObject).getApplicationContext();
            data = (String) param.args[1];
            if(mService ==null | mServiceConn == null)
            {
                tryStartService(true);
            }
            else
            {
                sendMessage(data);
            }
        }
    }

    void sendMessage(String url)
    {
        Message msg = Message.obtain(null, 1);
        Bundle bundle = new Bundle(1);
        bundle.putString(DownloadService.STREAMING_URL_TAG, url);
        msg.setData(bundle);
        try
        {
            mService.send(msg);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    void tryStartService(final boolean sendMessage)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                mServiceConn = new ServiceConnection()
                {
                    @Override
                    public void onServiceConnected(ComponentName cn, IBinder binder)
                    {
                        try
                        {
                            mService = new Messenger(binder);
                            if(sendMessage)
                            {
                                sendMessage(data);
                            }
                        } catch (Throwable t)
                        {
                            XposedBridge.log(t);
                        }
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName cn)
                    {
                        mService = null;
                        mServiceConn = null;
                    }
                };
                ComponentName cn = new ComponentName(Main.class.getPackage().getName(), DownloadService.class.getName());
                Intent intent = new Intent();
                intent.setComponent(cn);
                mContext.bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);

            }
        }).start();
    }
}
