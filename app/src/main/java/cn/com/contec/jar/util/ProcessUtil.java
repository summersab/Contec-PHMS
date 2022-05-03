package cn.com.contec.jar.util;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import java.util.List;

public class ProcessUtil {
    public byte[] unPack(byte[] pack, int len) {
        for (int i = 2; i < len; i++) {
            pack[i] = (byte) (pack[i] & ((pack[1] << (9 - i)) | Byte.MAX_VALUE));
        }
        pack[1] = Byte.MIN_VALUE;
        return pack;
    }

    public byte[] doPack(byte[] pack, int len) {
        if (pack == null) {
            return null;
        }
        if (len <= 0) {
            return pack;
        }
        pack[1] = Byte.MIN_VALUE;
        for (int i = 2; i < len; i++) {
            pack[1] = (byte) (pack[1] | ((pack[i] & 128) >> (9 - i)));
            pack[i] = (byte) (pack[i] | 128);
        }
        return pack;
    }

    public void kill_background(Context p_Context) {
        ActivityManager activityManger = (ActivityManager) p_Context.getSystemService("activity");
        ActivityManager.MemoryInfo mInfo = new ActivityManager.MemoryInfo();
        Log.i("before kill ", new StringBuilder(String.valueOf(mInfo.availMem)).toString());
        activityManger.getMemoryInfo(mInfo);
        List<ActivityManager.RunningAppProcessInfo> list = activityManger.getRunningAppProcesses();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                ActivityManager.RunningAppProcessInfo apinfo = list.get(i);
                Log.e("pid", "pid " + apinfo.pid);
                Log.e("processName", "processName  " + apinfo.processName);
                Log.e("importance", "importance" + apinfo.importance);
                String[] pkgList = apinfo.pkgList;
                if (apinfo.importance > 100) {
                    for (String killBackgroundProcesses : pkgList) {
                        activityManger.killBackgroundProcesses(killBackgroundProcesses);
                    }
                }
            }
        }
        Log.i("after kill ", new StringBuilder(String.valueOf(mInfo.availMem)).toString());
    }
}
