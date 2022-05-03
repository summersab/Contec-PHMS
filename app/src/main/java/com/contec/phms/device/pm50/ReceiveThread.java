package com.contec.phms.device.pm50;

import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.device.template.SendCommand;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.message.OrderList;
import com.contec.phms.util.CLog;
import com.example.bm77_contec08A_code.DeviceCommand;
import com.example.bm77_contec08A_code.DevicePackManager;
import java.util.ArrayList;
import u.aly.bs;

public class ReceiveThread extends com.contec.phms.device.template.ReceiveThread {
    public static final int e_pack_hand_back = 72;
    public static final int e_pack_is_new = 65;
    public static final int e_pack_oxygen_back = 71;
    public static final int e_pack_pressure_back = 70;
    public static boolean mIsNew;
    public static boolean mIsOld;
    DeviceData _mData;
    boolean bGetPackId = false;
    byte[] buffer = new byte[1024];
    int bytes;
    byte[] curPack = new byte[9];
    int i;
    boolean is_move_have = false;
    boolean is_normal_have = false;
    int k = 0;
    int len = 0;
    private int mMedian = 0;
    DevicePackManager mPackManager = new DevicePackManager();
    int mSize_normal = 0;
    private int mType = 0;
    byte value;

    public ReceiveThread(DeviceService mDeviceService) {
        super(mDeviceService);
    }

    public void arrangeMessage(byte[] buf, int length) {
        switch (this.mPackManager.arrangeMessage(buf, length, this.mType)) {
            case 64:
                CLog.e("ReceiveThread", "0x40");
                DeviceManager.mDeviceBeanList.mState = 4;
                DeviceManager.m_DeviceBean.mState = 4;
                DeviceManager.m_DeviceBean.mProgress = 0;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                this.mType = 1;
                byte[] bArr = new byte[6];
                bArr[0] = 67;
                bArr[1] = 4;
                bArr[2] = 1;
                bArr[3] = 1;
                bArr[4] = 2;
                SendCommand.send(bArr);
                return;
            case OrderList.DM_NEXT_DEVICE_OR_POLLING /*66*/:
                CLog.e("ReceiveThread", "0x42");
                DeviceManager.mDeviceBeanList.mState = 10;
                DeviceManager.m_DeviceBean.mState = 10;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                return;
            case 70:
                byte[] bArr2 = new byte[4];
                bArr2[0] = 74;
                bArr2[1] = 70;
                bArr2[2] = 1;
                SendCommand.send(bArr2);
                ArrayList<byte[]> _data_move = this.mPackManager.mDeviceData.mData_blood;
                int mSize_move = _data_move.size();
                this._mData = new DeviceData((byte[]) null);
                DatasContainer.mDeviceDatas.clear();
                this._mData.mDataList.clear();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                CLog.e("Data", ">>>>>>>>>>" + mSize_move);
                if (mSize_move > 0) {
                    for (int i = 0; i < mSize_move; i++) {
                        byte[] _byte = _data_move.get(i);
                        this._mData.m_nSys = ((_byte[0] & 255) << 8) | (_byte[1] & 255);
                        this._mData.m_nDia = _byte[2] & 255;
                        this._mData.m_nHR = _byte[3] & 255;
                        this._mData.m_nMap = _byte[4] & 255;
                        this._mData.m_nTC = _byte[10] & Byte.MAX_VALUE;
                        this._mData.mDate = new int[5];
                        this._mData.mDate[0] = (_byte[5] & 255) + 2000;
                        this._mData.mDate[1] = _byte[6] & 255;
                        this._mData.mDate[2] = _byte[7] & 255;
                        this._mData.mDate[3] = _byte[8] & 255;
                        this._mData.mDate[4] = _byte[9] & 255;
                        this._mData.setmType(255);
                        this._mData.setmDataType("abpm50w");
                        this._mData.setmUploadType("case");
                        this._mData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                        this._mData.setSaveDate();
                        this._mData.setM_savedata();
                        this._mData.mDataList.add(_data_move.get(i));
                        this.is_move_have = true;
                        DeviceManager.m_DeviceBean.mProgress = (((i + 1) * 30) / mSize_move) + 20;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    }
                    DatasContainer.mDeviceDatas.add(this._mData);
                    byte[] bArr3 = new byte[6];
                    bArr3[0] = 67;
                    bArr3[1] = 4;
                    bArr3[2] = 1;
                    bArr3[3] = 1;
                    bArr3[4] = 2;
                    SendCommand.send(bArr3);
                } else if (this.is_move_have) {
                    this.is_move_have = false;
                    DeviceManager.mDeviceBeanList.mState = 6;
                    DeviceManager.m_DeviceBean.mState = 6;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    DeviceService.mReceiveFinished = true;
                } else {
                    DeviceManager.mDeviceBeanList.mState = 10;
                    DeviceManager.m_DeviceBean.mState = 10;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    DeviceService.mReceiveFinished = true;
                }
                this.mType = 1;
                return;
            case 72:
                switch (this.mType) {
                    case 0:
                        CLog.e("ReceiveThread", "correct_time_notice");
                        this.mType = 3;
                        SendCommand.send(DeviceCommand.Correct_Time_Abpm50());
                        return;
                    default:
                        return;
                }
            default:
                return;
        }
    }

    public String dateToString(int[] mDate) {
        String _temp = bs.b;
        for (int i = 0; i < mDate.length; i++) {
            _temp = String.valueOf(_temp) + byte2String((byte) mDate[i]);
        }
        return _temp;
    }

    public String byte2String(byte pack) {
        if (pack < 10) {
            return "0" + pack;
        }
        return new StringBuilder().append(pack).toString();
    }
}
