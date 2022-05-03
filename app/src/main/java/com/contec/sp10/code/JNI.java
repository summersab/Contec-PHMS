package com.contec.sp10.code;

public class JNI {
    public static native long GetData(byte[] bArr, int i);

    public static native boolean GetPredicted(PatientInfo patientInfo, Param param);

    public static native double GetPtRate(long j, long j2, int i);

    public static native double GetPtTime(long j);

    public static native double GetPtVolume(int i, int i2);

    public static native int GetnScaler(byte[] bArr, int i);

    public static double getFlowVelocity(double volumes, double time) {
        return getData(volumes, time);
    }

    private static double getData(double volumes, double time) {
        return volumes / time;
    }

    static {
        System.loadLibrary("sp10inport");
    }
}
