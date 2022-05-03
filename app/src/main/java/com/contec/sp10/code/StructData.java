package com.contec.sp10.code;

import java.io.FileOutputStream;
import java.io.Serializable;

public class StructData implements Serializable {
    private static final long serialVersionUID = 1;
    public int mData;
    public int mIndex;

    public void save(FileOutputStream fos) {
        SaveHelper.saveInt(fos, this.mData);
        SaveHelper.saveInt(fos, this.mIndex);
    }
}
