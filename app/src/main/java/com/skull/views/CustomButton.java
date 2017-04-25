package com.skull.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.skull.MyApplication;

/**
 * Created by Administrator on 4/5/2017.
 */

public class CustomButton extends TextView {

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomButton(Context context) {
        super(context);
    }

    public void setTypeface(Typeface tf, int style) {
        super.setTypeface(TypeFaces.getTypeFace(getContext(),
                MyApplication.typeFace));
        super.setTextSize(MyApplication.getInstance().btnFontSize);
    }
}