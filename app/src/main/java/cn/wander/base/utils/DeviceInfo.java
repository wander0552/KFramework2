package cn.wander.base.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by wander on 2016/4/25.
 * email 805677461@qq.com
 */
public class DeviceInfo {

    // 屏幕宽度（像素）WelcomeActivity onResume之后才有值
    public static int WIDTH;

    // 屏幕高度（像素）WelcomeActivity onResume之后才有值
    public static int HEIGHT;

    // 屏幕密度（0.75 / 1.0 / 1.5）WelcomeActivity onResume之后才有值
    public static float DENSITY;

    // 屏幕密度DPI（120 / 160 / 240）WelcomeActivity onResume之后才有值
    public static int DENSITY_DPI;

    public static float SCALED_DENSITY;

    public static void init(Context context) {
        initScreenInfo(context);
    }

    public static void initScreenInfo(Context context) {
        if (WIDTH == 0) {
            try {
                DisplayMetrics dm = new DisplayMetrics();
                ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
                WIDTH = Math.min(dm.widthPixels, dm.heightPixels);
                HEIGHT = Math.max(dm.widthPixels, dm.heightPixels);
                DENSITY = dm.density;
                DENSITY_DPI = dm.densityDpi;
                SCALED_DENSITY = dm.scaledDensity;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
