package com.example.bm77_contec08A_code;

import android.os.Environment;
import android.util.Log;
import com.alibaba.cchannel.CloudChannel;
import com.example.ble_bm77_contec08a.DeviceData;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import u.aly.dp;

public class DevicePackManager {
    public static int Flag_User = 0;
    public static final int e_pack_blood = 70;
    public static final int e_pack_hand_back = 72;
    public static final int e_pack_is_new = 65;
    public static final int e_pack_oxyen = 71;
    public static final int e_pack_replay_confirm = 74;
    int Blood_count;
    int Oxyen_count;
    boolean bGetPackId = false;
    byte[] curPack = new byte[9];
    int i;
    int k = 0;
    int len = 0;
    public DeviceData mDeviceData = new DeviceData();
    public ArrayList<DeviceData> mDeviceDatas = new ArrayList<>();
    byte value;

    public static int packlength(int pHead) {
        switch (pHead) {
            case 65:
                return 5;
            case 70:
                return -6;
            case 71:
                return -6;
            case 72:
                return 5;
            case e_pack_replay_confirm /*74*/:
                return 4;
            default:
                return 0;
        }
    }

    public byte arrangeMessage(byte[] buf, int length, int pType) {
        PrintBytes.printData(buf, length);
        byte _return = 0;
        this.i = 0;
        while (this.i < length) {
            this.value = buf[this.i];
            if (this.bGetPackId) {
                byte[] bArr = this.curPack;
                int i = this.k;
                this.k = i + 1;
                bArr[i] = this.value;
                if (this.k >= this.len) {
                    if (this.curPack[0] != 71 && this.curPack[0] != 70) {
                        this.bGetPackId = false;
                        _return = processData(this.curPack, pType);
                    } else if (this.len < 10) {
                        if (this.curPack[0] == 71) {
                            this.len = Data_len(new byte[]{this.curPack[2], this.curPack[3], this.curPack[4], this.curPack[5]});
                        } else if (this.curPack[0] == 70) {
                            this.len = Data_len(new byte[]{this.curPack[2], this.curPack[3], this.curPack[4]});
                        }
                        byte[] _data = new byte[this.curPack.length];
                        for (int j = 0; j < this.curPack.length; j++) {
                            _data[j] = this.curPack[j];
                        }
                        this.curPack = new byte[this.len];
                        for (int j2 = 0; j2 < _data.length; j2++) {
                            this.curPack[j2] = _data[j2];
                        }
                    } else {
                        this.bGetPackId = false;
                        _return = processData(this.curPack, pType);
                    }
                }
            } else if (this.value >= 0 && packlength(this.value) > 0) {
                this.bGetPackId = true;
                this.k = 0;
                this.len = packlength(this.value);
                this.curPack = new byte[this.len];
                byte[] bArr2 = this.curPack;
                int i2 = this.k;
                this.k = i2 + 1;
                bArr2[i2] = this.value;
                if (this.len == 1) {
                    _return = processData(this.curPack, pType);
                    this.bGetPackId = false;
                }
            } else if (this.value >= 0 && packlength(this.value) < 0) {
                this.bGetPackId = true;
                this.k = 0;
                this.len = 6;
                this.curPack = new byte[this.len];
                byte[] bArr3 = this.curPack;
                int i3 = this.k;
                this.k = i3 + 1;
                bArr3[i3] = this.value;
            }
            this.i++;
        }
        return _return;
    }

    public byte processData(byte[] pack, int pType) {
        byte _return = 0;
        switch (pack[0]) {
            case 65:
                return pack[0];
            case 70:
                this.mDeviceData.mData_blood.clear();
                Log.e("DevicePackManager", ">>>>>>>>>>>>>>>>>>>" + this.Blood_count);
                byte _return2 = pack[0];
                if (pType == 7) {
                    if ((pack[13] & Byte.MAX_VALUE) == 3) {
                        _return2 = pack[0];
                    } else {
                        _return2 = 0;
                    }
                }
                if (pType == 1 || pType == 7) {
                    byte[] _data = pack_Blood(pack);
                    for (int i = 0; i < this.Blood_count; i++) {
                        byte[] _blood = {_data[(i * 14) + 14 + 1], _data[(i * 14) + 14 + 2], _data[(i * 14) + 14 + 3], _data[(i * 14) + 14 + 4], _data[(i * 14) + 14 + 5], _data[(i * 14) + 14 + 6], _data[(i * 14) + 14 + 7], _data[(i * 14) + 14 + 9], _data[(i * 14) + 14 + 10], _data[(i * 14) + 14 + 11], _data[(i * 14) + 14 + 12], _data[(i * 14) + 14 + 13]};
                        this.mDeviceData.mData_blood.add(_blood);
                        Log.e("JAR", "blooddi:" + (_blood[2] & 255) + "  bloodgao:" + (((_blood[0] << 8) | _blood[1]) & 255));
                    }
                } else if (pType == 6) {
                    byte[] _data2 = pack_Blood(pack);
                    for (int i2 = 0; i2 < this.Blood_count; i2++) {
                        byte[] _blood2 = {_data2[(i2 * 14) + 14 + 1], _data2[(i2 * 14) + 14 + 2], _data2[(i2 * 14) + 14 + 3], _data2[(i2 * 14) + 14 + 4], _data2[(i2 * 14) + 14 + 5], _data2[(i2 * 14) + 14 + 6], _data2[(i2 * 14) + 14 + 7], _data2[(i2 * 14) + 14 + 9], _data2[(i2 * 14) + 14 + 10], _data2[(i2 * 14) + 14 + 11], _data2[(i2 * 14) + 14 + 12], _data2[(i2 * 14) + 14 + 13]};
                        this.mDeviceData.mData_normal_blood.add(_blood2);
                        byte x = _blood2[2];
                        Log.e("JAR", "blooddi:" + x + "  bloodgao:" + ((_blood2[0] << 8) | _blood2[1]));
                    }
                }
                Log.e("JAR", "e_pack_blood");
                return _return2;
            case 71:
                byte _return3 = pack[0];
                if (pType == 8) {
                    if ((pack[15] & Byte.MAX_VALUE) == 3) {
                        _return3 = pack[0];
                    } else {
                        _return3 = 0;
                    }
                }
                Log.e("JAR", "e_pack_oxyen");
                byte[] _oxygen_pack = pack_Oxygen(pack);
                for (int i3 = 0; i3 < this.Oxyen_count; i3++) {
                    byte[] _oxygen = {_oxygen_pack[(i3 * 11) + 16 + 1], _oxygen_pack[(i3 * 11) + 16 + 2], _oxygen_pack[(i3 * 11) + 16 + 3], _oxygen_pack[(i3 * 11) + 16 + 4], _oxygen_pack[(i3 * 11) + 16 + 5], _oxygen_pack[(i3 * 11) + 16 + 6], _oxygen_pack[(i3 * 11) + 16 + 7], _oxygen_pack[(i3 * 11) + 16 + 9], _oxygen_pack[(i3 * 11) + 16 + 10]};
                    this.mDeviceData.mData_oxygen.add(_oxygen);
                    Log.e("JAR", "Oxygen:" + (_oxygen[0] & 255) + "  Plus:" + (_oxygen[1] & 255));
                }
                return _return3;
            case 72:
                byte _return4 = pack[0];
                PrintBytes.printData(pack);
                Flag_User = pack[3];
                Log.e("JAR", "e_pack_hand_back");
                return _return4;
            case e_pack_replay_confirm /*74*/:
                switch (pType) {
                    case 1:
                        if (pack[2] != 1) {
                            _return = CloudChannel.SDK_VERSION;
                            break;
                        } else {
                            _return = dp.n;
                            break;
                        }
                    case 2:
                        if (pack[2] != 1) {
                            _return = 33;
                            break;
                        } else {
                            _return = 32;
                            break;
                        }
                    case 3:
                        if (pack[1] == 67) {
                            if (pack[2] != 1) {
                                _return = 49;
                                break;
                            } else {
                                _return = 48;
                                break;
                            }
                        } else {
                            Log.e("DevicePackManager", "校正时间返回");
                            if (pack[3] != 0) {
                                Log.e("DevicePackManager", "校正时间返回pack[3]=1");
                                _return = 66;
                                break;
                            } else {
                                Log.e("DevicePackManager", "校正时间返回pack[3]=0");
                                if (pack[2] != 1) {
                                    _return = 65;
                                    break;
                                } else {
                                    _return = 64;
                                    break;
                                }
                            }
                        }
                    case 5:
                        if (pack[2] != 1) {
                            _return = 81;
                            break;
                        } else {
                            _return = 80;
                            break;
                        }
                }
                Log.e("JAR", "e_pack_replay_confirm");
                return _return;
            default:
                return 0;
        }
    }

    public void writeObject(ArrayList<String> Arraystr) {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            File directory = new File(Environment.getExternalStorageDirectory(), "CONTEC/WT/");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(Environment.getExternalStorageDirectory(), "CONTEC/WT/correlation_data.txt");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileOutputStream outStream = new FileOutputStream(file);
                new ObjectOutputStream(outStream).writeObject(Arraystr);
                outStream.close();
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
    }

    public ArrayList<String> readObject() throws FileNotFoundException, IOException, ClassNotFoundException {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            File file = new File(Environment.getExternalStorageDirectory(), "CONTEC/WT/correlation_data.txt");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return (ArrayList) new ObjectInputStream(new FileInputStream(file)).readObject();
        }
        return new ArrayList();
    }

    public static byte[] unPack(byte[] pack) {
        int len2 = pack.length;
        for (int i = 1; i < 8; i++) {
            pack[i] = (byte) (pack[i] & ((pack[0] << (8 - i)) | Byte.MAX_VALUE));
        }
        for (int i2 = 9; i2 < len2; i2++) {
            pack[i2] = (byte) (pack[i2] & ((pack[8] << ((len2 - 8) - (i2 - 8))) | Byte.MAX_VALUE));
        }
        return pack;
    }

    public static byte[] doPack(byte[] pack) {
        if (pack == null) {
            return null;
        }
        if (10 <= 0) {
            return pack;
        }
        pack[2] = Byte.MIN_VALUE;
        for (int i = 3; i < 9; i++) {
            pack[1] = (byte) (pack[1] | ((pack[i] & 128) >> (9 - i)));
            pack[i] = (byte) (pack[i] | 128);
        }
        return pack;
    }

    public int Data_len(byte[] pack) {
        int len2 = pack.length;
        for (int i = 1; i < len2; i++) {
            pack[i] = (byte) (pack[i] & ((pack[0] << (8 - i)) | Byte.MAX_VALUE));
        }
        if (len2 == 4) {
            int _oxyen_count = ((pack[1] & 255) << dp.n) | ((pack[2] & 255) << 8) | (pack[3] & 255);
            this.Oxyen_count = _oxyen_count;
            return (_oxyen_count * 11) + 16;
        } else if (len2 != 3) {
            return 0;
        } else {
            int _blood_count = ((pack[1] & 255) << 8) | (pack[2] & 255);
            this.Blood_count = _blood_count;
            return (_blood_count * 14) + 14;
        }
    }

    public byte[] pack_Blood(byte[] pBlood) {
        for (int i = 3; i < 10; i++) {
            pBlood[i] = (byte) (pBlood[i] & ((pBlood[2] << (10 - i)) | Byte.MAX_VALUE));
        }
        for (int i2 = 10; i2 < 14; i2++) {
            pBlood[i2] = (byte) (pBlood[i2] & ((pBlood[10] << (14 - i2)) | Byte.MAX_VALUE));
        }
        for (int j = 0; j < this.Blood_count; j++) {
            for (int i3 = (j * 14) + 14; i3 < (j * 14) + 14 + 8; i3++) {
                pBlood[i3] = (byte) (pBlood[i3] & ((pBlood[(j * 14) + 14] << ((((j * 14) + 14) + 8) - i3)) | Byte.MAX_VALUE));
            }
            for (int i4 = (j * 14) + 14 + 8; i4 < (j * 14) + 14 + 8 + 6; i4++) {
                pBlood[i4] = (byte) (pBlood[i4] & ((pBlood[((j * 14) + 14) + 8] << (((((j * 14) + 14) + 8) + 6) - i4)) | Byte.MAX_VALUE));
            }
        }
        return pBlood;
    }

    public byte[] pack_Oxygen(byte[] pOxygen) {
        for (int i = 3; i < 10; i++) {
            pOxygen[i] = (byte) (pOxygen[i] & ((pOxygen[2] << (10 - i)) | Byte.MAX_VALUE));
        }
        for (int i2 = 10; i2 < 16; i2++) {
            pOxygen[i2] = (byte) (pOxygen[i2] & ((pOxygen[10] << (16 - i2)) | Byte.MAX_VALUE));
        }
        for (int j = 0; j < this.Oxyen_count; j++) {
            for (int i3 = (j * 11) + 16; i3 < (j * 11) + 16 + 8; i3++) {
                pOxygen[i3] = (byte) (pOxygen[i3] & ((pOxygen[(j * 11) + 16] << (((j * 11) + 24) - i3)) | Byte.MAX_VALUE));
            }
            for (int i4 = (j * 11) + 16 + 8; i4 < (j * 11) + 16 + 8 + 3; i4++) {
                pOxygen[i4] = (byte) (pOxygen[i4] & ((pOxygen[((j * 11) + 16) + 8] << (((((j * 11) + 16) + 8) + 3) - i4)) | Byte.MAX_VALUE));
            }
        }
        return pOxygen;
    }
}
