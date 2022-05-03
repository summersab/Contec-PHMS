package serial.jni;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import java.io.File;
import java.io.IOException;

public class DataUtils {
    public static final int DISPLAY_GAIN_10 = 33;
    public static final int DISPLAY_GAIN_5 = 32;
    public static final int DISPLAY_GAIN__20 = 34;
    public static final int DISPLAY_MODE_12x1 = 0;
    public static final int DISPLAY_MODE_2x6_CHEST = 4;
    public static final int DISPLAY_MODE_2x6_LIMB = 3;
    public static final int DISPLAY_MODE_6x2 = 1;
    public static final int DISPLAY_MODE_6x2EX = 2;
    public static final int DISPLAY_SPEED_125 = 16;
    public static final int DISPLAY_SPEED_25 = 17;
    public static final int DISPLAY_SPEED_50 = 18;
    private String demoFile = null;
    private String mAddress = null;
    private BluetoothConnect mBlu = null;
    private Context mContext = null;
    private SerialPort mGather = null;

    public DataUtils() {
        try {
            this.mGather = new SerialPort(new File(SerialPort.DEVICE_NAME));
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public DataUtils(String file) {
        this.demoFile = file;
        try {
            this.mGather = new SerialPort(new File(this.demoFile));
            GLView.isGather = true;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public DataUtils(Context context, String addr, Handler handler) {
        this.mContext = context;
        this.mAddress = addr;
        this.mGather = new SerialPort();
        this.mBlu = new BluetoothConnect(this.mContext, this.mGather, this.mAddress, handler);
    }

    public void gatherStart(Object thiz) {
        this.mGather.initNativeMsg(thiz);
        if (this.mBlu != null) {
            this.mBlu.Communicate();
            Log.e("DataUtils", "gatherStart");
        } else if (this.demoFile != null) {
            this.mGather.setDemoFilePath(this.demoFile);
            this.mGather.Sopen(1);
        } else {
            this.mGather.Sopen(0);
        }
    }

    public void gatherEnd() {
        if (this.mBlu != null) {
            this.mBlu.bluetoothDestroy();
            Log.e("DataUtils", "gatherEnd");
            return;
        }
        this.mGather.Sclose();
    }

    public SerialPort getGather() {
        return this.mGather;
    }

    public void setDisplayMode(int mode) {
        switch (mode) {
            case 0:
                DrawUtils.setDisplayMode(0);
                return;
            case 1:
                DrawUtils.setDisplayMode(1);
                return;
            case 2:
                DrawUtils.setDisplayMode(2);
                return;
            case 3:
                DrawUtils.setDisplayMode(3);
                DrawUtils.setDisplayMode2x6(0);
                return;
            case 4:
                DrawUtils.setDisplayMode(3);
                DrawUtils.setDisplayMode2x6(1);
                return;
            default:
                DrawUtils.setDisplayMode(0);
                return;
        }
    }

    public void setSpeed(int speed) {
        switch (speed) {
            case 16:
                DrawUtils.setDisplaySpeed(1);
                return;
            case 17:
                DrawUtils.setDisplaySpeed(2);
                return;
            case 18:
                DrawUtils.setDisplaySpeed(3);
                return;
            default:
                DrawUtils.setDisplaySpeed(2);
                return;
        }
    }

    public void setGain(int gain) {
        switch (gain) {
            case 32:
                DrawUtils.setDisplayGain(1);
                return;
            case 33:
                DrawUtils.setDisplayGain(2);
                return;
            case 34:
                DrawUtils.setDisplayGain(3);
                return;
            default:
                DrawUtils.setDisplayGain(1);
                return;
        }
    }

    public void setFilter(int filter) {
        this.mGather.setFilter(filter);
    }

    public void saveCase(String path, String name, int length) {
        this.mGather.saveCase(path, name, length);
    }

    public void cancelCase() {
        this.mGather.cancelCase();
    }
}
