package com.downloadmanager.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import com.downloadmanager.view_holder.Views;
import com.skull.R;

/**
 * This class very useful if you want to show a progressDialog
 * that matching the app theme, then use this class.
 *
 * @author shibaprasad
 * @version 1.0
 */
public class ProgressDialog {
    private Dialog dialog;
    private TextView progressText;

    /**
     * public constructor.
     *
     * @param context      com.downloadmanager.activity context.
     * @param progressText the waiting text.
     */
    public ProgressDialog(Context context, boolean cancelable, String progressText) {
        dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.abs_progress_dialog);
        dialog.setCancelable(cancelable);
        Views.dialog_fillParent(dialog);

        this.progressText = (TextView) dialog.findViewById(R.id.title);
        this.progressText.setText(progressText);
    }

    /**
     * Show the progress dialog.
     */
    public void show() {
        this.dialog.show();
    }

    /**
     * Close the progress dialog.
     */
    public void close() {
        this.dialog.dismiss();
    }

    /**
     * Get the progress dialog.
     */
    public Dialog getDialog() {
        return this.dialog;
    }


}
