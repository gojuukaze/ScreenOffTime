package cn.ikaze.screentime;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.service.quicksettings.TileService;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;


public class ScreenService extends TileService {
    int timeout;
    int timeoutChoose[] = new int[]{15000, 30000, 60000, 120000, 300000, 600000, 1800000};
    int maxChoose = 6;

    @Override
    public void onStartListening() {
        timeout = ScreenTime.getScreenOffTime(getApplicationContext());
        getQsTile().setLabel(secondToString(timeout));
        getQsTile().updateTile();
        super.onStartListening();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }

    @Override
    public void onClick() {
        super.onClick();
        if (!checkSystemWritePermission()) {
            Toast.makeText(this, "需要相关权限", Toast.LENGTH_SHORT).show();
            Intent dialogIntent = new Intent(getBaseContext(), MainActivity.class);
            Bundle bundle=new Bundle();
            bundle.putBoolean("open",true);
            dialogIntent.putExtras(bundle);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(dialogIntent);
            collapseStatusBar(this);
        }
        int pos = getNestPos();
        int new_timeout = timeoutChoose[pos];
        if (ScreenTime.setScreenOffTime(getApplicationContext(), new_timeout))
            timeout = new_timeout;

        getQsTile().setLabel(secondToString(timeout));
        getQsTile().updateTile();
    }

    private boolean checkSystemWritePermission() {
        boolean retVal = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(this);

        }
        return retVal;
    }

    private String secondToString(int timeout) {
        timeout = timeout / 1000;
        if (timeout / 60 == 0)
            return timeout + " s";
        else if (timeout / 60 / 60 == 0)
            return timeout / 60 + " min";
        else
            return timeout / 60 / 60 + " h";
    }

    private int getNestPos() {
        int i = 0;
        for (; i < timeoutChoose.length; i++) {
            if (timeoutChoose[i] == timeout)
                break;
        }
        if (i >= maxChoose)
            return 0;
        return i + 1;
    }

    public static void collapseStatusBar(Context context) {
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }
}
