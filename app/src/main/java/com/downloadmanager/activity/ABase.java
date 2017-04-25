package com.downloadmanager.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Window;
import android.widget.Toast;

import com.downloadmanager.application.App;
import com.skull.R;


/**
 * ABase is the base class that helps its subclass to implement the basic characterises which it have.
 * ABase can give other version name and version code of the com.downloadmanager.application.
 *
 * @author shibaprasad
 * @version 1.0
 */
public class ABase extends Activity {

    //------------------ PUBLIC KEY FOR OPENING ACTIVITIES. -------------------------------------------------------------------------------------------------//
    public static final String ACTION_UPDATE = "INTENT_ACTION_UPDATE";
    public static final String ACTION_MESSAGE = "ACTION_MESSAGE";

    public static final float TITLE_SIZE = 18f;
    public static final float INPUT_SIZE = 17.44f;
    public static final float DEFAULT_SIZE = 18f;
    protected static final int ON_CREATE = 0;
    protected static final int ON_START = 1;
    protected static final int ON_RESUME = 2;
    protected static final int ON_PAUSE = 3;
    protected static final int ON_DESTROY = 4;

    protected Context context;
    //Application reference object.
    protected App app;
    //Vibrator object that is useful for vibrating the device.
    protected Vibrator vibrator;
    protected String versionName, versionCode; //Version name and code of the com.downloadmanager.application.
    protected Resources resources; //resource object for getting the res resource file.
    protected int LifeCycle;




    /**
     * Show a toast message and vibrate.
     *
     * @param context      context
     * @param input        boolean value indicating that the device should vibrate or not.
     * @param toastMessage the text which to be toasted.
     */
    public static void makeToast(Context context, boolean input, String toastMessage) {
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        if (input)
            vibrator.vibrate(20);
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * System calls the functions to initialize the first com.downloadmanager.activity creation  process.
     *
     * @param bundle system gives the bundle to save the primitive data throughout the life cycle.
     */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //set the lifecycle status.
        LifeCycle = ON_CREATE;

        //initialize the useful object.
        context = ABase.this;
        app = (App) getApplication();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        resources = getResources();


        //the current version code and name of com.downloadmanager.application.
        versionCode = String.valueOf(app.versionCode);
        versionName = app.versionName;

        //set the request to hide the action bar.
        requestWindowFeature(Window.FEATURE_NO_TITLE);

    }

    /**
     * System callback: called after onCreate() method.
     */
    @Override
    public void onStart() {
        super.onStart();
        LifeCycle = ON_START;
    }

    /**
     * System callback: before after user switch to other com.downloadmanager.application.
     */
    @Override
    public void onPause() {
        super.onPause();
        LifeCycle = ON_PAUSE;
    }

    /**
     * System callback: called after user come back to this app from other app.
     */
    @Override
    public void onResume() {
        super.onResume();
        LifeCycle = ON_RESUME;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    /**
     * System callback: called before com.downloadmanager.activity is going to destroy.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        LifeCycle = ON_DESTROY;



        //call the gc.
        Runtime.getRuntime().gc();
        app.clearApplicationData();
    }





    public void makeToast(boolean input, String toastMessage) {
        if (input)
            vibrator.vibrate(20);
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
    }

}
