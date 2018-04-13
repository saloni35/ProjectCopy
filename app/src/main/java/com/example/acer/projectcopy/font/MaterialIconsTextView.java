package com.example.acer.projectcopy.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Acer on 4/12/2018.
 */

public class MaterialIconsTextView extends android.support.v7.widget.AppCompatTextView {

    private static Typeface sMaterialDesignIcons;

    public MaterialIconsTextView(Context context) {
        this(context, null);
    }

    public MaterialIconsTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialIconsTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) return;//Won't work in Eclipse graphical layout
        setTypeface();
    }

    private void setTypeface() {
        if (sMaterialDesignIcons == null) {
            sMaterialDesignIcons = Typeface.createFromAsset(getContext().getAssets(), "fonts/MaterialIcons-Regular.ttf");
        }
        setTypeface(sMaterialDesignIcons);
    }
}


