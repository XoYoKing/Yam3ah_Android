package com.ds.yam3ah.utils;

import android.app.Activity;
import android.graphics.Typeface;

/**
 * Created by Shivangi on 6/5/2015.
 */
public class Utils {

    public static Typeface Optima(Activity act) {
        return Typeface.createFromAsset(act.getAssets(), "Optima");
    }
}
