package serial.jni;

import android.content.res.AssetManager;
import android.util.Log;
import java.io.File;
import java.io.IOException;

public class SerialPort {
    public static String DEVICE_NAME = "/dev/ttyUSB250";

    public static native boolean createAssetAudioPlayer(AssetManager assetManager, String str);

    public static native void createBufferQueueAudioPlayer();

    public static native void createEngine();

    public static native void setPlayingAssetAudioPlayer(boolean z);

    public static native void shutdown();

    public native int BGBatteryDepackage(byte[] bArr);

    public native void BGDataAdd();

    public native void BGEnd();

    public native int BGLeadDepackage(byte[] bArr);

    public native int BGPaceDepackage(byte[] bArr);

    public native int BGPreEcgDepackage(byte[] bArr);

    public native void BGStart();

    public native String CharToString();

    public native void Sclose();

    public native int Sopen(int i);

    public native void cancelCase();

    public native short getBatteryInfo();

    public native void getBufIndex(int[] iArr);

    public native short[] getData();

    public native void getDataEX(short[] sArr);

    public native int getHBS();

    public native int getHR();

    public native void getLeadInfo(short[] sArr);

    public native int getPaperState();

    public native float getPrintingProgress();

    public native void initNativeMsg(Object obj);

    public native void saveCase(String str, String str2, int i);

    public native void setDemoFilePath(String str);

    public native void setFilter(int i);

    public native void startPRINT(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10);

    public native void stopPRINT();

    static {
        System.loadLibrary("Wave");
    }

    public SerialPort(File device) throws SecurityException, IOException {
        if (!device.canRead() || !device.canWrite()) {
            try {
                Log.e("SerialPort", "su");
                Process su = Runtime.getRuntime().exec("su");
                su.getOutputStream().write(("chmod 777 " + DEVICE_NAME + "\n" + "exit\n").getBytes());
                if (su.waitFor() != 0 || !device.canRead() || !device.canWrite()) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SecurityException();
            }
        }
    }

    public SerialPort() {
    }

    public void callBack(double progress) {
        Log.e("test", new StringBuilder().append(progress).toString());
    }
}
