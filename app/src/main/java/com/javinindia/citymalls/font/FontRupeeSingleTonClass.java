package com.javinindia.citymalls.font;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Ashish on 09-12-2016.
 */

public class FontRupeeSingleTonClass {
    private static FontRupeeSingleTonClass instance;
    private static Typeface typeface;

    public static FontRupeeSingleTonClass getInstance(Context context) {
        synchronized (FontRupeeSingleTonClass.class) {
            if (instance == null) {
                instance = new FontRupeeSingleTonClass();
                if(context != null){
                    typeface = Typeface.createFromAsset(context.getResources().getAssets(), "rupee_foradian.ttf");
                }

            }
            return instance;
        }
    }

    public Typeface getTypeFace() {
        return typeface;
    }
}
