package com.contec.phms.device.pm85;

import com.conect.json.CLog;
import com.contec.jar.pm85.DeviceCommand;
import com.contec.jar.pm85.DeviceData;
import com.contec.jar.pm85.DevicePackManager;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.device.template.SendCommand;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.device.ServiceBean;

public class ReceiveThread extends com.contec.phms.device.template.ReceiveThread {
    public static final int e_pack_DataCount = 7;
    public static final int e_pack_ECG_Data = 2;
    public static final int e_pack_accountInfo = 1;
    public static final int e_pack_account_re = 10;
    public static final int e_pack_check_signal = 12;
    public static final int e_pack_clock_invalid = 13;
    public static final int e_pack_date = 6;
    public static final int e_pack_date_re = 15;
    public static final int e_pack_ecg_re = 11;
    public static final int e_pack_match_re = 17;
    public static final int e_pack_noData = 31;
    public static final int e_pack_time = 5;
    public static final int e_pack_time_re = 14;
    DevicePackManager mPackManager;
    int mRec_Count;
    int x;

    public ReceiveThread(DeviceService mDeviceService) {
        super(mDeviceService);
        this.mPackManager = new DevicePackManager(ServiceBean.getInstance().mSocket);
        this.mRec_Count = 0;
        this.x = 0;
        this.x = 0;
    }

    public void arrangeMessage(byte[] buf, int length) {
        switch (this.mPackManager.arrangeMessage(buf, length)) {
            case 1:
                CLog.e("---------------e_pack_accountInfo--------------", "Account");
                DeviceManager.m_DeviceBean.mProgress = 20;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                return;
            case 2:
                this.x++;
                return;
            case 7:
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SendCommand.send(DeviceCommand.REQUEST_Account);
                CLog.e("---------------ReceiveThread--------------", "DataCount");
                if (this.mPackManager.mCount > 0) {
                    DeviceManager.mDeviceBeanList.mState = 4;
                    DeviceManager.m_DeviceBean.mState = 4;
                    DeviceManager.m_DeviceBean.mProgress = 0;
                    return;
                }
                return;
            case 10:
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                SendCommand.send(DeviceCommand.REQUEST_Data);
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                CLog.e("---------------e_pack_account_re--------------", "ECG_Data");
                return;
            case 11:
                this.mRec_Count++;
                byte _reques = DeviceCommand.RESPONSE_0B;
                CLog.e("---------------e_pack_ECG_Data--------------", "RESPONSE_0B" + this.mRec_Count);
                int sum_count = this.mPackManager.mCount;
                CLog.e("---------------e_pack_ECG_Data--------------", "RESPONSE_0B" + sum_count);
                if (sum_count == 1) {
                    DeviceManager.m_DeviceBean.mProgress = 80;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                } else {
                    DeviceManager.m_DeviceBean.mProgress = ((this.mRec_Count * 60) / sum_count) + 40;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                }
                DeviceManager.m_DeviceBean.mProgress = ((this.mRec_Count * 60) / sum_count) + 40;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceData _data = this.mPackManager.mDeviceData;
                DeviceData _PM85_data = new DeviceData();
                _PM85_data.mDataList = _data.mDatas;
                _PM85_data.mFlag = _data.mFlags;
                _PM85_data.mDate = new int[6];
                _PM85_data.mDate[0] = ((_data._date[1] & 255) << 8) | (_data._date[0] & 255);
                _PM85_data.mDate[1] = _data._date[2] & 255;
                _PM85_data.mDate[2] = _data._date[3] & 255;
                _PM85_data.mDate[3] = _data._time[0] & 255;
                _PM85_data.mDate[4] = _data._time[1] & 255;
                _PM85_data.mDate[5] = _data._time[2] & 255;
                _PM85_data.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                DatasContainer.mDeviceDatas.add(_PM85_data);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
                SendCommand.send(_reques);
                return;
            case 14:
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e4) {
                    e4.printStackTrace();
                }
                SendCommand.send(DeviceCommand.REQUEST_DATA_COUNT);
                return;
            case 15:
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e5) {
                    e5.printStackTrace();
                }
                byte[] _setTime = DeviceCommand.SET_TIME();
                DevicePackManager.doPack(_setTime);
                SendCommand.send(_setTime);
                return;
            case 31:
                int _length = this.mPackManager.mDeviceDatas.size();
                if (_length > 0) {
                    DeviceService.mReceiveFinished = true;
                    DeviceManager.mDeviceBeanList.mState = 6;
                    DeviceManager.m_DeviceBean.mState = 6;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    return;
                } else if (_length == 0) {
                    DeviceService.mReceiveFinished = true;
                    DeviceService.nextStep();
                    DeviceManager.mDeviceBeanList.mState = 10;
                    DeviceManager.m_DeviceBean.mState = 10;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }
}
