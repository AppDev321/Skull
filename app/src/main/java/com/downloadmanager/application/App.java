package com.downloadmanager.application;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;

import com.downloadmanager.connectivity_system.DownloadFunctions;
import com.downloadmanager.data_handler_system.DataHandler;
import com.downloadmanager.object_holder.SettingsHolder;
import com.downloadmanager.tools.StorageUtils;
import com.skull.MyApplication;
import com.skull.R;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;


/**
 * <p><b>App</b> is the sub class of the {@link android.app.Application}. So it is the main
 * class that is initialized as long as the AIO app is running.
 * </p>
 * <p>So it is very useful class for our project code structure.</p>
 */
public class App extends Application implements Serializable {

    public static final boolean IS_DEBUGGING = true;

    public static boolean isDownloadServiceForeground = false;

    /**
     * <p>The <b>Version code</b> of the com.downloadmanager.application.</p>
     */
    public String versionCode;
    /**
     * <p>The <b>Version Name</b> of the com.downloadmanager.application.</p>
     */
    public String versionName;
    public File updateFile;
    /**
     * <p>The {@link com.downloadmanager.data_handler_system.DataHandler} holds the global reference of
     * our vital object management classes.</p>
     */
    private DataHandler dataHandler;
    private SettingsHolder settingsHolder;
    /**
     * <p>The {@link com.downloadmanager.connectivity_system.DownloadFunctions} is the main bridge of the connection
     * between {@link com.downloadmanager.application.App} to {@link  com.downloadmanager.download_manager.services.DownloadTask}
     * {@link com.downloadmanager.download_manager.services.DownloadController}</p>
     */
    private DownloadFunctions downloadFunctions;
    /**
     * <p>A variable of <b>SharedPreference</b>. Ths object reference is used as a global preference
     * to manage the single skeleton of saving key and value parse.</p>
     */
    private SharedPreferences preferences;


    /**
     * Register a log view to the console.
     */
    public static void log(char tagChar, String tagName, String message) {
        if (IS_DEBUGGING) {
            if (tagChar == 'i')
                Log.i(tagName, message);
            if (tagChar == 'e')
                Log.e(tagName, message);
            if (tagChar == 'w')
                Log.w(tagName, message);
            if (tagChar == 'd')
                Log.d(tagName, message);
        }

    }

    public static void log(char tag, Class<?> class_name, String message) {
        log(tag, class_name.getName(), message);
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * System call back this onCreate() method when the Application get first initialized.
     */
    @SuppressLint("WorldWriteableFiles")
    @Override
    public void onCreate() {
        super.onCreate();


        dataHandler = DataHandler.getIntense(this);
        preferences = getSharedPreferences("Application preferences", Context.MODE_WORLD_WRITEABLE);
        dataHandler.setDownloadFunctions(this);
        downloadFunctions = dataHandler.getDownloadFunctions();

        initSetting();

        //set the com.downloadmanager.application version code and name.
        initVersionCodeName();

    }



    public void initSetting() {
        try {
            StorageUtils.mkdirs(SettingsHolder.PATH);
            settingsHolder = SettingsHolder.read();
            if (settingsHolder == null) {
                settingsHolder = new SettingsHolder();
            }

            StorageUtils.mkdirs(StorageUtils.FILE_ROOT +getResources().getString(R.string.app_name)+"/");


        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * <p>System call back method the app run out of memory. </p>
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    private void initVersionCodeName() {
        try {
            versionCode = "" + getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            versionName = "" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException error) {
            error.printStackTrace();
        }
    }

    /**
     * Get the global data handler object reference. see {@link com.downloadmanager.data_handler_system.DataHandler}
     *
     * @return the Global DataHandler object.
     */
    public DataHandler getDataHandler() {
        return this.dataHandler;
    }

    public SettingsHolder getSettingsHolder() {
        return this.settingsHolder;
    }

    /**
     * Set the global DataHandler object of the {@link com.downloadmanager.application.App} class.
     */
    public synchronized void setDataHandler() {
        this.dataHandler = DataHandler.getIntense(this);
    }

    /**
     * Get the global preference object reference.
     *
     * @return the com.downloadmanager.application's global shared preference object reference.
     */
    public SharedPreferences getPreference() {
        return this.preferences;
    }

    /**
     * Get the global download function object.
     *
     * @return DownloadFunctions
     */
    public DownloadFunctions getDownloadFunctions() {
        return this.downloadFunctions;
    }




    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (s.toLowerCase().contains("cache") && !s.toLowerCase().contains("app_sslcache")) {
                    if (deleteDir(new File(appDir, s))) {
                        Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                    }
                }
            }
        }
    }


}

