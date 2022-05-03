package com.contec.phms.device.cmsvesd;

public class DeviceOrder {
    public static final byte[] CONFIRM_TRANSFER = {-1, -33};
    public static final byte[] FILE_DATA = {-1, -1};
    public static final byte[] ORDER_TYPE_ECG;
    public static final byte[] ORDER_TYPE_SOUND;
    public static final byte[] ORDER_TYPE_SPO2;
    public static final byte[] UPLOAD_FILE;
    public static final byte[] UPLOAD_FILE_CONFIRM;
    public static final byte[] UPLOAD_FINISH = {-65, -1};

    static {
        byte[] bArr = new byte[2];
        bArr[0] = -80;
        UPLOAD_FILE_CONFIRM = bArr;
        byte[] bArr2 = new byte[2];
        bArr2[0] = -80;
        UPLOAD_FILE = bArr2;
        byte[] bArr3 = new byte[2];
        bArr3[0] = -64;
        ORDER_TYPE_ECG = bArr3;
        byte[] bArr4 = new byte[2];
        bArr4[0] = -65;
        ORDER_TYPE_SPO2 = bArr4;
        byte[] bArr5 = new byte[2];
        bArr5[0] = -81;
        ORDER_TYPE_SOUND = bArr5;
    }
}
