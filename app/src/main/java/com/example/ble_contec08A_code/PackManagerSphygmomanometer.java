package com.example.ble_contec08A_code;

import android.util.Log;
import com.alibaba.cchannel.CloudChannel;
import com.contec.phms.util.Constants;
import java.util.ArrayList;
import java.util.List;
import u.aly.dp;

public class PackManagerSphygmomanometer {
    public static final int ARG_PRESSURE = 2;
    public static final int HIGH_PRESSURE = 1;
    public static final int LOW_PRESSURE = 3;
    public static final int PULSE_RATE = 4;
    private String Tag = "PackManagerSphygmomanometer";
    int _packCount = 0;
    public int mAllCount;
    public int mCount;
    public DeviceDataSphygmomanometer mDataSphy;
    public List<DeviceDataSphygmomanometer> mSphy = new ArrayList();

    public byte arrangeMessage(byte[] buf, int length) {
        int k;
        byte message = 0;
        byte[] curPack = new byte[64];
        int len = 0;
        boolean isGetPackId = false;
        int i = 0;
        int k2 = 0;
        while (i < length) {
            byte value = buf[i];
            if (isGetPackId) {
                k = k2 + 1;
                curPack[k2] = value;
                if (k >= len) {
                    isGetPackId = false;
                    message = processData(curPack);
                }
            } else {
                isGetPackId = true;
                k = 0;
                len = PackLen(value);
                if (len == 0) {
                    message = 0;
                    isGetPackId = false;
                } else {
                    curPack = new byte[len];
                    int k3 = 0 + 1;
                    curPack[0] = value;
                    if (len == 1) {
                        message = processData(curPack);
                        isGetPackId = false;
                        k = k3;
                    } else {
                        k = k3;
                    }
                }
            }
            i++;
            k2 = k;
        }
        return message;
    }

    public byte processData(byte[] pack) {
        switch (pack[0]) {
            case Constants.UPDATEXML_UPLOAD_FAIL:
                this.mCount = ((pack[4] << 7) | (pack[3] & 255)) & 65535;
                this.mAllCount = ((pack[2] << 7) | (pack[1] & 255)) & 65535;
                Log.e("+++++++++++++", "+++++++++++++");
                Log.e("wum========", "杩斿洖褰撳墠鏁版嵁鐨勬潯鏁?===" + this.mCount);
                Log.e("+++++++++++++", "+++++++++++++");
                if (this.mCount > 0) {
                    return Byte.MIN_VALUE;
                }
                return -127;
            case Constants.GENERATE_XML_FAIL:
                this._packCount++;
                this.mDataSphy = new DeviceDataSphygmomanometer();
                this.mDataSphy.dataSphy = new int[15];
                this.mDataSphy.dataSphy[0] = (byte) (pack[2] & 255);
                this.mDataSphy.dataSphy[1] = (byte) (pack[3] & 255);
                this.mDataSphy.dataSphy[2] = (byte) (pack[4] & 255);
                this.mDataSphy.dataSphy[3] = (byte) (pack[5] & 255);
                this.mDataSphy.dataSphy[4] = (byte) (pack[6] & 255);
                this.mDataSphy.dataSphy[5] = (byte) (pack[7] & 255);
                this.mDataSphy.dataSphy[6] = (byte) (pack[8] & 255);
                this.mDataSphy.dataSphy[7] = (byte) (pack[9] & 255);
                this.mDataSphy.dataSphy[8] = (byte) (pack[10] & 255);
                this.mDataSphy.dataSphy[9] = (byte) (pack[11] & 255);
                this.mDataSphy.dataSphy[10] = (byte) (pack[12] & 255);
                this.mDataSphy.dataSphy[11] = (byte) (pack[13] & 255);
                this.mDataSphy.dataSphy[12] = (byte) (pack[14] & 255);
                this.mDataSphy.dataSphy[13] = (byte) (pack[15] & 255);
                this.mDataSphy.dataSphy[14] = (byte) (pack[16] & 255);
                System.out.println(String.valueOf(this.mDataSphy.dataSphy[0]) + "骞?" + this.mDataSphy.dataSphy[1] + "鏈?" + this.mDataSphy.dataSphy[2] + "鏃?" + this.mDataSphy.dataSphy[3] + "鏃?" + this.mDataSphy.dataSphy[4] + "鍒?" + this.mDataSphy.dataSphy[5] + "绉?");
                Log.e("=======", "时间：：：" + this.mDataSphy.dataSphy[0] + "年" + this.mDataSphy.dataSphy[1] + "月" + this.mDataSphy.dataSphy[2] + "日" + this.mDataSphy.dataSphy[3] + "时" + this.mDataSphy.dataSphy[4] + "分" + this.mDataSphy.dataSphy[5] + "秒");
                this.mSphy.add(this.mDataSphy);
                if ((pack[1] & 64) == 64) {
                    this._packCount = 0;
                    return 67;
                } else if (this._packCount != 10) {
                    return -120;
                } else {
                    this._packCount = 0;
                    return 66;
                }
            case Constants.GET_LIST_ERROR:
                if (Check(pack)) {
                    return -112;
                }
                return -111;
            case -15:
                if (Check(pack)) {
                    return 32;
                }
                return 33;
            case -14:
                if (Check(pack)) {
                    return 48;
                }
                return 49;
            case -13:
                if (!Check(pack) || (pack[1] & 255) != 0) {
                    return CloudChannel.SDK_VERSION;
                }
                return dp.n;
            case -10:
                if (Check(pack)) {
                    return 112;
                }
                return 113;
            default:
                return 0;
        }
    }

    public int PackLen(byte packHead) {
        switch (packHead) {
            case Constants.UPDATEXML_UPLOAD_FAIL:
                return 6;
            case Constants.GENERATE_XML_FAIL:
                return 18;
            case Constants.GET_LIST_ERROR:
                return 2;
            case -15:
                return 10;
            case -14:
                return 8;
            case -13:
                return 3;
            case -12:
                return 3;
            case -11:
                return 10;
            case -10:
                return 10;
            default:
                return 0;
        }
    }

    public boolean Check(byte[] pack) {
        int CHECK_SUM = 0;
        int _size = pack.length - 1;
        for (int i = 0; i < _size; i++) {
            CHECK_SUM += pack[i] & 255;
        }
        if ((CHECK_SUM & Byte.MAX_VALUE) == (pack[pack.length - 1] & Byte.MAX_VALUE)) {
            return true;
        }
        return false;
    }

    static int unPack(byte[] pack, int i) {
        byte key = pack[10];
        switch (i) {
            case 1:
                int low = ((key << 7) & 128) | (pack[11] & 255);
                int high = ((key << 6) & 128) | (pack[12] & 255);
                int ret = (high << 8) | low;
                Log.e("=========", "直接打印血压高压值：：：" + ((high << 8) | low));
                return ret;
            case 2:
                return (byte) (((key << 5) & 128) | (pack[13] & 255));
            case 3:
                return (byte) (((key << 4) & 128) | (pack[14] & 255));
            case 4:
                return (byte) ((((byte) (((key << 2) & 128) | (pack[16] & 255))) << 8) | ((byte) (((key << 3) & 128) | (pack[15] & 255))));
            default:
                return 0;
        }
    }
}
