package com.downloadmanager.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import com.downloadmanager.adapter.DownloadListAdapter;
import com.downloadmanager.application.App;
import com.downloadmanager.dialogs.YesNoDialog;
import com.downloadmanager.download_manager.services.DownloadService;
import com.downloadmanager.object_holder.DownloadData;
import com.downloadmanager.system_core.SystemIntent;
import com.downloadmanager.tools.StorageUtils;
import com.skull.R;

import java.io.File;
import java.util.Date;

import static com.downloadmanager.view_holder.Views.dialog_fillParent;

/**
 * <p>
 * <B>DownloadTaskOption </B>is the main class that is responsible for showing and operate a running download
 * task's functions, such as pause resume and restart etc.
 * </p>
 */
public class DownloadTaskOption implements View.OnClickListener {

    //the dialog that show the whole options.
    private Dialog dialog;

    //the dialog task title.
    private TextView title;

    //download materials.
    private String fileName, filePath, fileUrl, fileWebPage;
    private boolean isRunning = false;

    private DownloadListAdapter downloadListAdapter;
    private DownloadData downloadData;
    private Intent intent;
    private Context context;
    private App application;

    /**
     * Public constructor that initial all the components of this class.
     *
     * @param context com.downloadmanager.activity context.
     */
    public DownloadTaskOption(Context context, App app) {
        this.context = context;
        this.application = app;

        //set up the dialog.
        dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.abs_download_task_option);
        dialog_fillParent(dialog);

        //initialize the view of the dialog.
        title = (TextView) dialog.findViewById(R.id.title);

        TextView pause = (TextView) dialog.findViewById(R.id.pause);
        TextView resume = (TextView) dialog.findViewById(R.id.resume);
        TextView restart = (TextView) dialog.findViewById(R.id.restart);
;
        TextView remove = (TextView) dialog.findViewById(R.id.remove);
        TextView delete = (TextView) dialog.findViewById(R.id.delete);
        TextView property = (TextView) dialog.findViewById(R.id.property);

        //set the on-click event listener of the option buttons.
        pause.setOnClickListener(this);
        resume.setOnClickListener(this);
        restart.setOnClickListener(this);

        remove.setOnClickListener(this);
        delete.setOnClickListener(this);
        property.setOnClickListener(this);

        //service intent.
        intent = new Intent(context, DownloadService.class);
    }

    public void start(int position, DownloadListAdapter downloadAdapter) {
        downloadListAdapter = downloadAdapter;
        downloadData = downloadAdapter.getDownloadDataFromList(position);
        //set the file name , path , url and the web-page.
        fileName = downloadData.getFileName();
        filePath = downloadData.getFilePath();
        fileUrl = downloadData.getFileUrl();
        fileWebPage = downloadData.getFileWebpage();

        title.setText(fileName);

        //show the dialog.
        showTaskOptions();
    }

    /**
     * Show the dialog and the notify the download com.downloadmanager.adapter.
     */
    public void showTaskOptions() {
        dialog.show();
        Intent intent = new Intent();
        intent.setAction(ABase.ACTION_UPDATE);
        intent.putExtra("Index", downloadData.getId());
        this.context.sendBroadcast(intent);
    }

    /**
     * Destroy the dialog and notify the download com.downloadmanager.adapter.
     */
    public void destroy() {
        dialog.dismiss();
        Intent intent = new Intent();
        intent.setAction(ABase.ACTION_UPDATE);
        intent.putExtra("Index", downloadData.getId());
        this.context.sendBroadcast(intent);
    }

    /**
     * System call back this method when user click the resume button.
     */
    public void onPause() {
        destroy();
        intent.setAction(SystemIntent.INTENT_ACTION_START_SERVICE);
        intent.putExtra(SystemIntent.TYPE, SystemIntent.Types.PAUSE);
        intent.putExtra(SystemIntent.FILE_URL, fileUrl);
        intent.putExtra(SystemIntent.FILE_NAME, fileName);
        intent.putExtra(SystemIntent.FILE_PATH, filePath);
        intent.putExtra(SystemIntent.WEB_PAGE, fileWebPage);
        context.startService(intent);
        Intent intent = new Intent();
        intent.setAction(ABase.ACTION_UPDATE);
        intent.putExtra("Index", downloadData.getId());
        this.context.sendBroadcast(intent);
    }

    /**
     * System call back this method when user click the resume button.
     */
    public void onResume() {
        destroy();
        intent.setAction(SystemIntent.INTENT_ACTION_START_SERVICE);
        intent.putExtra(SystemIntent.TYPE, SystemIntent.Types.RESUME);
        intent.putExtra(SystemIntent.FILE_URL, fileUrl);
        intent.putExtra(SystemIntent.FILE_NAME, fileName);
        intent.putExtra(SystemIntent.FILE_PATH, filePath);
        intent.putExtra(SystemIntent.WEB_PAGE, fileWebPage);
        context.startService(intent);
        Intent intent = new Intent();
        intent.setAction(ABase.ACTION_UPDATE);
        intent.putExtra("Index", downloadData.getId());
        this.context.sendBroadcast(intent);
    }

    /**
     * System call back this method when user click the restart button.
     */
    public void onRestart() {
        YesNoDialog builder = new YesNoDialog(
                context, "Are you sure about restart the task ? ", new YesNoDialog.OnClick() {

            @Override
            public void onYesClick(Dialog dialog, TextView view) {
                destroy(); //close the main option dialog.
                intent.setAction(SystemIntent.INTENT_ACTION_START_SERVICE);
                intent.putExtra(SystemIntent.TYPE, SystemIntent.Types.RESTART);
                intent.putExtra(SystemIntent.FILE_URL, fileUrl);
                intent.putExtra(SystemIntent.FILE_NAME, fileName);
                intent.putExtra(SystemIntent.FILE_PATH, filePath);
                intent.putExtra(SystemIntent.WEB_PAGE, fileWebPage);
                context.startService(intent);
                Intent intent = new Intent();
                intent.setAction(ABase.ACTION_UPDATE);
                intent.putExtra("Index", downloadData.getId());
                context.sendBroadcast(intent);
                //close the yes-no dialog.
                dialog.dismiss();
            }

            @Override
            public void onNoClick(Dialog dialog, TextView view) {
                dialog.dismiss(); //just close the main dialog.
            }
        });
        builder.dialog.show();
    }

    public void remove() {
        YesNoDialog builder = new YesNoDialog(context, "Are you sure about remove the task ? \n" +
                "The task will be removed from" +
                " this list but the downloaded file can be found on sdcard. ", new YesNoDialog.OnClick() {

            @Override
            public void onYesClick(Dialog dialog, TextView view) {
                destroy();
                intent.setAction(SystemIntent.INTENT_ACTION_START_SERVICE);
                intent.putExtra(SystemIntent.TYPE, SystemIntent.Types.DELETE);
                intent.putExtra(SystemIntent.FILE_URL, fileUrl);
                intent.putExtra(SystemIntent.FILE_NAME, fileName);
                intent.putExtra(SystemIntent.FILE_PATH, filePath);
                intent.putExtra(SystemIntent.WEB_PAGE, fileWebPage);
                context.startService(intent);
                Intent intent = new Intent();
                intent.setAction(ABase.ACTION_UPDATE);
                intent.putExtra("Index", downloadData.getId());
                context.sendBroadcast(intent);
                dialog.dismiss();
            }

            @Override
            public void onNoClick(Dialog dialog, TextView view) {
                dialog.dismiss();
            }

        });
        builder.dialog.show();
    }

    /**
     * Delete a task.
     */
    public void delete() {
        YesNoDialog builder = new YesNoDialog(context, "Are you sure about delete the task ?\n"
                + "The downloaded file and the task will be deleted together.", new YesNoDialog.OnClick() {

            @Override
            public void onYesClick(Dialog dialog, TextView view) {
                destroy();
                intent.setAction(SystemIntent.INTENT_ACTION_START_SERVICE);
                intent.putExtra(SystemIntent.TYPE, SystemIntent.Types.DELETE_SOURCE);
                intent.putExtra(SystemIntent.FILE_URL, fileUrl);
                intent.putExtra(SystemIntent.FILE_NAME, fileName);
                intent.putExtra(SystemIntent.FILE_PATH, filePath);
                intent.putExtra(SystemIntent.WEB_PAGE, fileWebPage);
                context.startService(intent);
                Intent intent = new Intent();
                intent.setAction(ABase.ACTION_UPDATE);
                intent.putExtra("Index", downloadData.getId());
                context.sendBroadcast(intent);
                dialog.dismiss();
            }

            @Override
            public void onNoClick(Dialog dialog, TextView view) {
                dialog.dismiss();
            }
        });
        builder.dialog.show();

    }

    /**
     * Show the detail of the task.
     */
    private void property() {
        final Dialog propertyDialog = new Dialog(context);
        propertyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        propertyDialog.setContentView(R.layout.abs_completed_task_property);
        dialog_fillParent(propertyDialog);

        ((TextView) propertyDialog.findViewById(R.id.title)).setText(downloadData.getFileName()
                .substring(0, downloadData.getFileName().lastIndexOf('.')));
        ((TextView) propertyDialog.findViewById(R.id.name)).setText(downloadData.getFileName());
        ((TextView) propertyDialog.findViewById(R.id.path)).setText(downloadData.getFilePath());
        ((TextView) propertyDialog.findViewById(R.id.web_page)).setText(
                ((downloadData.getFileWebpage() == null || downloadData.getFileWebpage().length() < 1)
                        ? "Unknown Web page" : downloadData.getFileWebpage()));
        ((TextView) propertyDialog.findViewById(R.id.url)).setText(downloadData.getFileUrl());
        ((TextView) propertyDialog.findViewById(R.id.file_size)).setText(
                StorageUtils.size(new File(downloadData.getFilePath(), downloadData.getFileName() + ".download").length()) + "");

        ((TextView) propertyDialog.findViewById(R.id.file_extension)).setText(getExtension(downloadData.getFileName()));

        ((TextView) propertyDialog.findViewById(R.id.creation_date)).setText(
                new Date(new File(downloadData.getFilePath(), downloadData.getFileName()).lastModified()).toString());

        propertyDialog.findViewById(R.id.web_page_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(context, AWeb.class);
                intent.setAction(ABase.ACTION_OPEN_WEBVIEW);
                intent.putExtra(ABase.ACTION_LOAD_URL,
                        ((TextView) propertyDialog.findViewById(R.id.web_page)).getText().toString());
                if (!((TextView) propertyDialog.findViewById(R.id.web_page)).
                        getText().toString().equals("Unknown Web page")) {
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.enter, R.anim.out);
                }*/
            }
        });
        propertyDialog.show();
    }

    private String getExtension(String name) {
        return name.substring(name.lastIndexOf('.'));
    }

    public void onClick(View view) {
        final int id = view.getId();
        if (id == R.id.pause)
            onPause();
        else if (id == R.id.resume)
            onResume();

        else if (id == R.id.restart)
            onRestart();
        else if (id == R.id.remove)
            remove();
        else if (id == R.id.delete)
            delete();
        else if (id == R.id.property)
            property();
    }

}
