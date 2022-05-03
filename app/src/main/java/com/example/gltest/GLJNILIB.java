package com.example.gltest;

public class GLJNILIB {
    public static native void setEndQcom();

    public static native void setPreserveAttrib();

    public static native void setStartQcom(int i, int i2, int i3, int i4);

    static {
        System.loadLibrary("gl10jni");
    }
}
