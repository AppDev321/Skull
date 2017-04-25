package com.skull.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.skull.MyApplication;
import com.skull.R;

/**
 * Created by Administrator on 4/5/2017.
 */

public class TitleTextView extends TextView {

    public TitleTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TitleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleTextView(Context context) {
        super(context);
    }

    public void setTypeface(Typeface tf, int style) {
        super.setTypeface(TypeFaces.getTypeFace(getContext(),
                MyApplication.typeFace2));
       // super.setTextSize(MyApplication.getInstance().fontSize);
        //super.setTextColor(getContext().getResources().getColor(R.color.pink));
    }
}