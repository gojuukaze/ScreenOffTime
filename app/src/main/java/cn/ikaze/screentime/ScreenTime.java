package cn.ikaze.screentime;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by gojuukaze on 2017/8/12.
 * Email: i@ikaze.uu.me
 */

public class ScreenTime {

    public static int getScreenOffTime(Context context) {
        int screenOffTime = 0;
        try {
            screenOffTime = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.SCREEN_OFF_TIMEOUT);
        } catch (Exception localException) {

        }
        return screenOffTime;
    }

    public static boolean setScreenOffTime(Context context, int timeout) {
        try {
            Settings.System.putInt(context.getContentResolver(),
                    Settings.System.SCREEN_OFF_TIMEOUT, timeout);
        } catch (Exception localException) {
            localException.printStackTrace();
            return false;
        }
        return true;
    }


}
