package com.contec.jni;

public class HVException extends Exception {
    public static final int CREATETEMPDIRECTORY_ERR = 2;
    public static final int EBR_FAILED = 3;
    public static final int LOADFILE_ERR = 1;
    public static final int LOADLIB_ERR = 0;
    private static final long serialVersionUID = -98549090340463484L;
    public int ERRORCODE;

    public HVException(int code) {
        super("Error code:" + code);
        this.ERRORCODE = code;
    }
}
