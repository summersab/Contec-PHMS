package com.contec.jni;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.contec.phms.util.CLog;

public class HVnative {
    public static final int SEGMENTCOUNT = 100;
    public static Handler mHandler;
    String m_strFilePath;

    public static native int HVlzmaDe(byte[] bArr, byte[] bArr2, HVCallBack hVCallBack);

    public static native int HVlzmaEn(byte[] bArr, byte[] bArr2, HVCallBack hVCallBack);

    static {
        try {
            System.loadLibrary("contec");
        } catch (UnsatisfiedLinkError e) {
            CLog.e("loadlib", "error");
        }
    }

    public static void myCallbackFunc(String nMsg) {
        Message tMsg = new Message();
        Bundle tBundle = new Bundle();
        tBundle.putString("CMD", nMsg);
        tMsg.setData(tBundle);
        mHandler.sendMessage(tMsg);
    }

    public static boolean lzmaEn(String strDes, String strSrc, HVCallBack lzmaCallback) {
        if (HVlzmaEn(strDes.getBytes(), strSrc.getBytes(), lzmaCallback) == 0) {
            return false;
        }
        return true;
    }

    public static boolean lzmaDe(String strDes, String strSrc, HVCallBack lzmaCallback) {
        if (HVlzmaDe(strDes.getBytes(), strSrc.getBytes(), lzmaCallback) == 0) {
            return false;
        }
        return true;
    }
}
