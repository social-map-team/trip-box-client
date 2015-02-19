package com.socialmap.yy.travelbox.utils;

import android.content.Context;


public class DimensionUtils {
    private static Context context;

    public static void init(Context context) {
        DimensionUtils.context = context;
    }

    public static int px2dp(int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int dp2px(int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
