package com.finki.messageshoot.Model.Helper;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

public class ThemeUtils {

    public static boolean isNightModeOn(Context context){
        int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES){
            Log.d("Tag", "Dark theme on");
            return true;
        } else {
            Log.d("Tag", "Light theme on");
            return false;
        }
    }

}
