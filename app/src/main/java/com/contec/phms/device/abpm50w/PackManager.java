package com.contec.phms.device.abpm50w;

import com.contec.phms.App_phms;
import com.contec.phms.device.template.SendCommand;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import u.aly.bs;
import u.aly.dp;

public class PackManager extends com.contec.phms.device.template.PackManager {
    public static final int[] PackLength;
    private final int e_pack_data1 = 8;
    private final int e_pack_data2 = 9;
    private final int e_pack_datatime = 7;
    private final int e_pack_deviceID = 18;
    private final int e_pack_recordnum = 22;
    private int mCount;
    private int mCount_Ordinary;
    private int mCount_move;
    DeviceData mDeviceData = new DeviceData(new byte[4]);
    boolean mMyVector_Ordinary = false;
    boolean mMyVector_move = false;
    private int mPack_Count;
    private int mProgress;
    private String mTimeString;
    private int mType;

    static {
        int[] iArr = new int[128];
        iArr[1] = 6;
        iArr[2] = 6;
        iArr[7] = 8;
        iArr[8] = 8;
        iArr[9] = 5;
        iArr[16] = 4;
        iArr[18] = 8;
        iArr[19] = 4;
        iArr[21] = 6;
        iArr[22] = 6;
        iArr[23] = 4;
        iArr[24] = 4;
        PackLength = iArr;
    }

    public void processPack(byte[] pack, int count) {
    }

    public byte[] unPack(byte[] pack) {
        int len = PackLength[pack[0]];
        for (int i = 2; i < len; i++) {
            pack[i] = (byte) (pack[i] & ((pack[1] << (9 - i)) | Byte.MAX_VALUE));
        }
        return pack;
    }

    public byte[] doPack(byte[] pack) {
        int len;
        if (pack != null && (len = PackLength[pack[0]]) > 0) {
            pack[1] = Byte.MIN_VALUE;
            for (int i = 2; i < len; i++) {
                pack[1] = (byte) (pack[1] | ((pack[i] & 128) >> (9 - i)));
                pack[i] = (byte) (pack[i] | 128);
            }
        }
        return pack;
    }

    public void processData(byte[] pack) {
        unPack(pack);
        CLog.e("&*&*&*&*&", Integer.toHexString(pack[0] & 255));
        switch (pack[0]) {
            case 7:
                break;
            case 8:
                this.mDeviceData.mPack[5] = pack[2];
                this.mDeviceData.mPack[6] = pack[3];
                this.mDeviceData.mPack[7] = (byte) (((pack[6] & 255) << 8) | (pack[7] & 255));
                this.mDeviceData.mPack[8] = (byte) (((pack[4] & 255) << 8) | (pack[5] & 255));
                this.mDeviceData.m_nSys = ((pack[2] & 255) << 8) | (pack[3] & 255);
                this.mDeviceData.m_nMap = ((pack[4] & 255) << 8) | (pack[5] & 255);
                this.mDeviceData.m_nDia = ((pack[6] & 255) << 8) | (pack[7] & 255);
                return;
            case 9:
                switch (this.mType) {
                    case 1:
                        this.mPack_Count++;
                        this.mCount_Ordinary--;
                        this.mDeviceData.m_nHR = ((pack[2] & 255) << 8) | (pack[3] & 255);
                        this.mDeviceData.m_nTC = pack[4] & 255;
                        if (!isUploaded(Constants.UploadedDatas_abpm50, this.mTimeString)) {
                            this.mMyVector_Ordinary = true;
                            this.mDeviceData.setmDataType("abpm50wtrend");
                            this.mDeviceData.setmUploadType("trend");
                            this.mDeviceData.setmType(this.mType);
                            this.mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                            this.mDeviceData.setSaveDate();
                            CLog.e(TAG, "*******ORDINARY*******");
                            DatasContainer.mDeviceDatas.add(this.mDeviceData);
                            saveUploaded(this.mTimeString);
                        }
                        byte[] comddataok = {dp.n, Byte.MIN_VALUE, -1, -1};
                        doPack(comddataok);
                        SendCommand.send(comddataok);
                        this.mProgress = (this.mPack_Count * 100) / this.mCount;
                        DeviceManager.m_DeviceBean.mProgress = this.mProgress;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                        if (this.mCount_Ordinary != 0) {
                            return;
                        }
                        if (this.mMyVector_Ordinary) {
                            this.mDeviceData.mDataList.clear();
                            if (this.mCount_move == 0) {
                                datasFinished();
                                DeviceManager.mDeviceBeanList.mState = 6;
                                DeviceManager.m_DeviceBean.mState = 6;
                                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                                return;
                            }
                            return;
                        } else if (this.mCount_move == 0) {
                            datasFinished();
                            DeviceManager.mDeviceBeanList.mState = 10;
                            DeviceManager.m_DeviceBean.mState = 10;
                            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                            return;
                        } else {
                            return;
                        }
                    case 255:
                        this.mPack_Count++;
                        this.mCount_move--;
                        this.mDeviceData.m_nHR = ((pack[2] & 255) << 8) | (pack[3] & 255);
                        this.mDeviceData.m_nTC = pack[4] & 255;
                        if (!isUploaded(Constants.UploadedDatas_abpm50, this.mTimeString)) {
                            this.mMyVector_move = true;
                            this.mDeviceData.setmDataType("abpm50w");
                            this.mDeviceData.setmUploadType("case");
                            this.mDeviceData.setM_savedata();
                            this.mDeviceData.setmType(this.mType);
                            this.mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                            this.mDeviceData.setSaveDate();
                            this.mDeviceData.mDataList.add(1);
                            saveUploaded(this.mTimeString);
                        }
                        this.mProgress = (this.mPack_Count * 100) / this.mCount;
                        DeviceManager.m_DeviceBean.mProgress = this.mProgress;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                        byte[] comd_move_dataok = {dp.n, Byte.MIN_VALUE, -1, -1};
                        doPack(comd_move_dataok);
                        SendCommand.send(comd_move_dataok);
                        if (this.mCount_move != 0) {
                            return;
                        }
                        if (this.mMyVector_move) {
                            this.mDeviceData.setM_savedata();
                            this.mDeviceData.setmType(this.mType);
                            CLog.e(TAG, "*******MOVE*******");
                            DatasContainer.mDeviceDatas.add(this.mDeviceData);
                            this.mDeviceData.mDataList.clear();
                            DeviceManager.mDeviceBeanList.mState = 6;
                            DeviceManager.m_DeviceBean.mState = 6;
                            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                            datasFinished();
                            return;
                        }
                        if (this.mMyVector_Ordinary) {
                            DeviceManager.mDeviceBeanList.mState = 6;
                            DeviceManager.m_DeviceBean.mState = 6;
                            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                        } else {
                            DeviceManager.mDeviceBeanList.mState = 10;
                            DeviceManager.m_DeviceBean.mState = 10;
                            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                        }
                        datasFinished();
                        return;
                    default:
                        return;
                }
            case 18:
                this.mType = pack[7];
                this.mType &= 255;
                byte[] comd = {19, Byte.MIN_VALUE, -1, -1};
                doPack(comd);
                SendCommand.send(comd);
                return;
            case 22:
                this.mCount_Ordinary = pack[3];
                this.mCount_move = pack[5];
                this.mCount_move &= 255;
                this.mCount_Ordinary &= 255;
                this.mCount = this.mCount_move + this.mCount_Ordinary;
                if (this.mCount == 0) {
                    DeviceManager.mDeviceBeanList.mState = 10;
                    DeviceManager.m_DeviceBean.mState = 10;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    datasFinished();
                    break;
                } else {
                    DeviceManager.mDeviceBeanList.mState = 4;
                    DeviceManager.m_DeviceBean.mState = 4;
                    DeviceManager.m_DeviceBean.mProgress = 0;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    byte[] comdnumok = {23, Byte.MIN_VALUE, -1, -1};
                    doPack(comdnumok);
                    SendCommand.send(comdnumok);
                    break;
                }
            default:
                return;
        }
        this.mDeviceData.mDate = new int[5];
        this.mDeviceData.mDate[0] = ((pack[2] & 255) << 8) | (pack[3] & 255);
        this.mDeviceData.mDate[1] = pack[4] & 255;
        this.mDeviceData.mDate[2] = pack[5] & 255;
        this.mDeviceData.mDate[3] = pack[6] & 255;
        this.mDeviceData.mDate[4] = pack[7] & 255;
        this.mDeviceData.mPack = new byte[9];
        this.mDeviceData.mPack[0] = (byte) ((((pack[2] & 255) << 8) | (pack[3] & 255)) - 2000);
        this.mDeviceData.mPack[1] = pack[4];
        this.mDeviceData.mPack[2] = pack[5];
        this.mDeviceData.mPack[3] = pack[6];
        this.mDeviceData.mPack[4] = pack[7];
        this.mTimeString = new StringBuilder().append(this.mDeviceData.mDate[0]).append(this.mDeviceData.mDate[1]).append(this.mDeviceData.mDate[2]).append(this.mDeviceData.mDate[3]).append(this.mDeviceData.mDate[4]).toString();
    }

    public void saveUploaded(String save) {
        IOException e;
        FileWriter _fw = null;
        File _file = null;
        File _file2 = new File(Constants.UploadedDatas_abpm50);
        try {
            if (_file2.exists()) {
            }
            FileWriter _fw2 = new FileWriter(Constants.UploadedDatas_abpm50, true);
            try {
                _fw2.write(";" + save);
                _fw2.flush();
                _fw2.close();
            } catch (IOException e2) {
                e = e2;
                _file = _file2;
                _fw = _fw2;
                if (_fw != null) {
                    try {
                        _fw.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        e.printStackTrace();
                    }
                }
                if (_file != null) {
                }
                e.printStackTrace();
            }
        } catch (IOException e3) {
            e = e3;
            _file = _file2;
        }
    }

    public static boolean isUploaded(String path, String pTimeString) {
        StringBuffer sb = null;
        try {
            File file = new File(path);
            if (!file.exists() || file.isDirectory()) {
                throw new FileNotFoundException();
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuffer sb2 = new StringBuffer();
            try {
                for (String temp = br.readLine(); temp != null; temp = br.readLine()) {
                    sb2.append(String.valueOf(temp) + " ");
                }
                sb = sb2;
            } catch (Exception e) {
                sb = sb2;
            }
            String allString = bs.b;
            if (sb != null) {
                allString = sb.toString();
            }
            return allString.contains(pTimeString);
        } catch (Exception e2) {
        }
        return false;
    }

    public void initCmdPosition() {
    }
}
