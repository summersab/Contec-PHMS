package serial.jni;

public abstract class NativeCallBack {
    public void callHRMsg(short hr) {
    }

    public void callProgressMsg(short progress) {
    }

    public void callCaseStateMsg(short state) {
    }

    public void callLeadOffMsg(short[] flagOff) {
    }

    public void callLeadOffMsg(String flagOff) {
    }

    public void callHBSMsg(short hbs) {
    }

    public void callBatteryMsg(short per) {
    }

    public void callCountDownMsg(short per) {
    }

    public void callWaveColorMsg(boolean flag) {
    }
}
