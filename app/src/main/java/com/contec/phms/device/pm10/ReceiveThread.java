package com.contec.phms.device.pm10;

import android.util.Log;
import com.contec.jar.pm10.DevicePackManager;
import com.contec.jar.pm10.PrintBytes;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.device.template.SendCommand;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;

public class ReceiveThread extends com.contec.phms.device.template.ReceiveThread {
    public static final byte e_back_caseinfo = -32;
    public static final byte e_back_dateresponse = -96;
    public static final byte e_back_deletedata = -64;
    public static final byte e_back_settime = -14;
    public static final byte e_back_single_caseinfo = -31;
    public static final byte e_back_single_data = -48;
    public static final byte e_back_stop_transfer = -10;
    public int PackLen = 64;
    int _caseLen = 0;
    int _dataCount = 0;
    boolean _havaNew = false;
    int _progress = 0;
    int _receCount = 1;
    int _recedataCount = 0;
    boolean bGetPackId = false;
    byte[] curPack = new byte[64];
    int i;
    int k = 0;
    int len = 0;
    int mCount = 0;
    DeviceData mData;
    DevicePackManager mPackManager = new DevicePackManager();
    byte value;

    public ReceiveThread(DeviceService mDeviceService) {
        super(mDeviceService);
    }

    public void arrangeMessage(byte[] buf, int length) {
        byte[] pack = this.mPackManager.arrangeMessage(buf, length);
        if (! pack.equals(0)) {
            switch (pack[0]) {
                case -64:
                    DeviceService.mReceiveFinished = true;
                    DeviceManager.mDeviceBeanList.mState = 6;
                    DeviceManager.m_DeviceBean.mState = 6;
                    DeviceManager.m_DeviceBean.mProgress = 0;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    return;
                case -48:
                    for (int i = 0; i < 50; i += 2) {
                        this.mData.CaseData[(this._recedataCount * 50) + i] = pack[i + 10 + 1 + 1];
                        this.mData.CaseData[(this._recedataCount * 50) + i + 1] = pack[i + 10 + 1];
                    }
                    this._recedataCount++;
                    this._dataCount--;
                    this._progress++;
                    if (this._progress == 20) {
                        System.out.println(((((this._receCount - 2) * (this._caseLen / 25)) + this._recedataCount) * 100) / ((this._caseLen / 25) * this.mCount));
                        DeviceManager.m_DeviceBean.mProgress = ((((this._receCount - 2) * (this._caseLen / 25)) + this._recedataCount) * 100) / ((this._caseLen / 25) * this.mCount);
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                        this._progress = 0;
                    }
                    if (this._dataCount == 0) {
                        addData(this.mData);
                        if (this._receCount - 1 == this.mCount) {
                            SendCommand.send(DeviceCommand.GET_DATA_RE(0));
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            SendCommand.send(DeviceCommand.DELETE_DATA(0, 0));
                            return;
                        }
                        SendCommand.send(DeviceCommand.GET_DATA_INFO(2, this._receCount));
                        CLog.e("88888888888888888888", "---------------->>>>>_receCount");
                        return;
                    }
                    return;
                case Constants.UPDATEXML_UPLOAD_FAIL /*-32*/:
                    this.mCount = this.mPackManager.mCount;
                    Log.e("病例信息个数", "---------------------->>::" + this.mCount);
                    Log.e("病例索引", "---------------------->>::" + this._receCount);
                    if (this.mCount > 0) {
                        SendCommand.send(DeviceCommand.GET_DATA_INFO(2, this._receCount));
                        Log.e("发送命令：", "---------------------->>::" + this._receCount);
                        return;
                    }
                    DeviceManager.mDeviceBeanList.mState = 10;
                    DeviceManager.m_DeviceBean.mState = 10;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    DeviceService.mReceiveFinished = true;
                    return;
                case Constants.GENERATE_XML_FAIL /*-31*/:
                    this._receCount++;
                    this.mData = new DeviceData();
                    PrintBytes.printData(pack);
                    this._havaNew = true;
                    int _year = ((pack[3] << 7) | (pack[4] & 255)) & 65535;
                    CLog.e("8888888888888888851ssss", "---------------->>>>>" + _year);
                    byte[] _data = {(byte) (_year - 2000), pack[5], pack[6], pack[7], pack[8], pack[9], pack[14], pack[15], 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                    dealData(_data, pack[16]);
                    dealData(_data, pack[17]);
                    dealData(_data, pack[18]);
                    dealData(_data, pack[19]);
                    dealData(_data, pack[20]);
                    dealData(_data, pack[21]);
                    if ((_data[9] | _data[10] | _data[11] | _data[12] | _data[13] | _data[14] | _data[15] | _data[16] | _data[17] | _data[18] | _data[19] | _data[20] | _data[21]) > 0) {
                        _data[8] = 0;
                    }
                    this.mData.mDate = new int[6];
                    this.mData.mDate[0] = _year;
                    this.mData.mDate[1] = pack[5];
                    this.mData.mDate[2] = pack[6];
                    this.mData.mDate[3] = pack[7];
                    this.mData.mDate[4] = pack[8];
                    this.mData.mDate[5] = pack[9];
                    this.mData.setmUploadType("case");
                    this.mData.setmDataType("ECG(PM10)");
                    this.mData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                    this.mData.TrendData = _data;
                    this._caseLen = (pack[10] << 21) | (pack[11] << 14) | (pack[12] << 7) | pack[13];
                    this.mData.CaseData = new byte[(this._caseLen * 2)];
                    this._dataCount = this._caseLen / 25;
                    this._recedataCount = 0;
                    SendCommand.send(DeviceCommand.GET_DATA(this._receCount - 1));
                    return;
                case -14:
                    SendCommand.send(DeviceCommand.GET_DATA_INFO(1, 0));
                    return;
                case -1:
                    this.mData.CaseData = this.mPackManager.mDeviceData.CaseData;
                    addData(this.mData);
                    if (this._receCount - 1 == this.mCount) {
                        SendCommand.send(DeviceCommand.GET_DATA_RE(0));
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        SendCommand.send(DeviceCommand.DELETE_DATA(0, 0));
                        return;
                    }
                    SendCommand.send(DeviceCommand.GET_DATA_INFO(2, this._receCount));
                    CLog.e("88888888888888888888", "---------------->>>>>_receCount");
                    return;
                default:
                    return;
            }
        }
    }

    public void processData(byte[] pack) {
        switch (pack[0]) {
            case -64:
                DeviceService.mReceiveFinished = true;
                DeviceManager.mDeviceBeanList.mState = 6;
                DeviceManager.m_DeviceBean.mState = 6;
                DeviceManager.m_DeviceBean.mProgress = 0;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                return;
            case -48:
                byte[] _pack = unPack(pack);
                for (int i = 0; i < 50; i += 2) {
                    this.mData.CaseData[(this._recedataCount * 50) + i] = _pack[i + 10 + 1 + 1];
                    this.mData.CaseData[(this._recedataCount * 50) + i + 1] = _pack[i + 10 + 1];
                }
                this._recedataCount++;
                this._dataCount--;
                this._progress++;
                if (this._progress == 20) {
                    System.out.println(((((this._receCount - 2) * (this._caseLen / 25)) + this._recedataCount) * 100) / ((this._caseLen / 25) * this.mCount));
                    DeviceManager.m_DeviceBean.mProgress = ((((this._receCount - 2) * (this._caseLen / 25)) + this._recedataCount) * 100) / ((this._caseLen / 25) * this.mCount);
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    this._progress = 0;
                }
                if (this._dataCount == 0) {
                    addData(this.mData);
                    if (this._receCount - 1 == this.mCount) {
                        SendCommand.send(DeviceCommand.GET_DATA_RE(0));
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        SendCommand.send(DeviceCommand.DELETE_DATA(0, 0));
                        return;
                    }
                    SendCommand.send(DeviceCommand.GET_DATA_INFO(2, this._receCount));
                    CLog.e("88888888888888888888", "---------------->>>>>_receCount");
                    return;
                }
                return;
            case Constants.UPDATEXML_UPLOAD_FAIL /*-32*/:
                this._havaNew = false;
                this.mCount = ((pack[2] << 7) | (pack[1] & 255)) & 65535;
                Log.e("病例信息个数", "---------------------->>::" + this.mCount);
                Log.e("病例索引", "---------------------->>::" + this._receCount);
                if (this.mCount > 0) {
                    SendCommand.send(DeviceCommand.GET_DATA_INFO(2, this._receCount));
                    Log.e("发送命令：", "---------------------->>::" + this._receCount);
                    return;
                }
                DeviceManager.mDeviceBeanList.mState = 10;
                DeviceManager.m_DeviceBean.mState = 10;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                return;
            case Constants.GENERATE_XML_FAIL /*-31*/:
                this._receCount++;
                this.mData = new DeviceData();
                this._havaNew = true;
                int _year = ((pack[3] << 7) | (pack[4] & 255)) & 65535;
                CLog.e("88888888888888888888", "---------------->>>>>" + _year);
                byte[] _data = {(byte) (_year - 2000), pack[5], pack[6], pack[7], pack[8], pack[9], pack[14], pack[15], 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                dealData(_data, pack[16]);
                dealData(_data, pack[17]);
                dealData(_data, pack[18]);
                dealData(_data, pack[19]);
                dealData(_data, pack[20]);
                dealData(_data, pack[21]);
                if ((_data[9] | _data[10] | _data[11] | _data[12] | _data[13] | _data[14] | _data[15] | _data[16] | _data[17] | _data[18] | _data[19] | _data[20] | _data[21]) > 0) {
                    _data[8] = 0;
                }
                this.mData.mDate = new int[6];
                this.mData.mDate[0] = _year;
                this.mData.mDate[1] = pack[5];
                this.mData.mDate[2] = pack[6];
                this.mData.mDate[3] = pack[7];
                this.mData.mDate[4] = pack[8];
                this.mData.mDate[5] = pack[9];
                this.mData.setmUploadType("case");
                this.mData.setmDataType("ECG(PM10)");
                this.mData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                this.mData.TrendData = _data;
                this._caseLen = (pack[10] << 21) | (pack[11] << 14) | (pack[12] << 7) | pack[13];
                this.mData.CaseData = new byte[(this._caseLen * 2)];
                this._dataCount = this._caseLen / 25;
                this._recedataCount = 0;
                SendCommand.send(DeviceCommand.GET_DATA(this._receCount - 1));
                return;
            case -14:
                SendCommand.send(DeviceCommand.GET_DATA_INFO(1, 0));
                return;
            case -10:
                if (this.mCount > 0) {
                    DeviceService.mReceiveFinished = true;
                    DeviceManager.mDeviceBeanList.mState = 6;
                    DeviceManager.m_DeviceBean.mState = 6;
                    DeviceManager.m_DeviceBean.mProgress = 0;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    return;
                }
                DeviceService.mReceiveFinished = true;
                DeviceManager.mDeviceBeanList.mState = 10;
                DeviceManager.m_DeviceBean.mState = 10;
                DeviceManager.m_DeviceBean.mProgress = 0;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                return;
            default:
                return;
        }
    }

    public void addData(DeviceData deviceData) {
        ((DeviceData) deviceData).setmDataType("ECG(PM10)");
        DatasContainer.mDeviceDatas.add(deviceData);
    }

    static byte[] unPack(byte[] pack) {
        for (int i = 0; i < 8; i++) {
            if (i == 7) {
                int i2 = (i * 7) + 11;
                pack[i2] = (byte) (pack[i2] | ((pack[i + 3] << 7) & 128));
            } else {
                for (int j = 0; j < 7; j++) {
                    int i3 = (i * 7) + 11 + j;
                    pack[i3] = (byte) (pack[i3] | ((pack[i + 3] << (7 - j)) & 128));
                }
            }
        }
        return pack;
    }

    void dealData(byte[] pdata, byte pDal) {
        switch (pDal) {
            case 0:
                pdata[8] = 1;
                return;
            case 1:
                pdata[8] = 0;
                pdata[9] = 1;
                return;
            case 2:
                pdata[8] = 0;
                pdata[10] = 1;
                return;
            case 3:
                pdata[8] = 0;
                pdata[11] = 1;
                return;
            case 4:
                pdata[8] = 0;
                pdata[12] = 1;
                return;
            case 5:
                pdata[8] = 0;
                pdata[13] = 1;
                return;
            case 6:
                pdata[8] = 0;
                pdata[14] = 1;
                return;
            case 7:
                pdata[8] = 0;
                pdata[15] = 1;
                return;
            case 8:
                pdata[8] = 0;
                pdata[16] = 1;
                return;
            case 9:
                pdata[8] = 0;
                pdata[17] = 1;
                return;
            case 10:
                pdata[8] = 0;
                pdata[18] = 1;
                return;
            case 11:
                pdata[8] = 0;
                pdata[19] = 1;
                return;
            case 12:
                pdata[8] = 0;
                pdata[20] = 1;
                return;
            case 13:
                pdata[8] = 0;
                pdata[21] = 1;
                return;
            default:
                return;
        }
    }
}
