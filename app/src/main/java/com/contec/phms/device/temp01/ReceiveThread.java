package com.contec.phms.device.temp01;

import android.util.Log;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.device.template.SendCommand;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.CLog;
import com.contec.phms.util.PageUtil;
import com.example.temp.bean.EarTempertureDataJar;
import com.example.temp.bm77_code.DeviceCommand;
import com.example.temp.bm77_code.DevicePackManager;

public class ReceiveThread extends com.contec.phms.device.template.ReceiveThread {
    DeviceData mDeviceData = new DeviceData();
    DevicePackManager mDevicePackManager = new DevicePackManager();
    private byte[] mPacks = new byte[1024];

    public ReceiveThread(DeviceService mDeviceService) {
        super(mDeviceService);
    }

    public void arrangeMessage(byte[] buf, int length) {
        switch (this.mDevicePackManager.arrangeMessage(buf, length)) {
            case 1:
                DeviceManager.mDeviceBeanList.mState = 4;
                DeviceManager.m_DeviceBean.mState = 4;
                DeviceManager.m_DeviceBean.mProgress = 70;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                if (this.mDevicePackManager.m_DeviceDatas.size() > 9) {
                    int _saveNum = 0;
                    for (int i = 0; i < this.mDevicePackManager.m_DeviceDatas.size(); i++) {
                        _saveNum++;
                        EarTempertureDataJar _data = this.mDevicePackManager.m_DeviceDatas.get(i);
                        Log.e("ReceiveThread", "满2条了，开始存储*************:" + _data.m_data);
                        if (PageUtil.compareDate(_data.m_saveDate) != null) {
                            _data.m_saveDate = PageUtil.getStringTime(System.currentTimeMillis());
                        }
                        this.mDeviceData.mDataList.add(_data);
                        if (_saveNum == 9) {
                            CLog.d("ReceiveThread", "满2条了，开始存储*************");
                            this.mDeviceData.m_receiveDate = _data.m_saveDate;
                            this.mDeviceData.init();
                            this.mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                            DatasContainer.mDeviceDatas.add(this.mDeviceData);
                            this.mDeviceData.mDataList.clear();
                            _saveNum = 0;
                        } else if (i == this.mDevicePackManager.m_DeviceDatas.size() - 1) {
                            this.mDeviceData.m_receiveDate = _data.m_saveDate;
                            this.mDeviceData.init();
                            this.mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                            DatasContainer.mDeviceDatas.add(this.mDeviceData);
                            CLog.d("ReceiveThread", "结束了 ，存储所有的数据 *************");
                        }
                    }
                } else {
                    for (int i2 = 0; i2 < this.mDevicePackManager.m_DeviceDatas.size(); i2++) {
                        EarTempertureDataJar _data2 = this.mDevicePackManager.m_DeviceDatas.get(i2);
                        CLog.d("ReceiveThread", " 得到的date: " + _data2.m_saveDate);
                        if (PageUtil.compareDate(_data2.m_saveDate) != null) {
                            _data2.m_saveDate = PageUtil.getStringTime(System.currentTimeMillis());
                        }
                        this.mDeviceData.mDataList.add(_data2);
                    }
                    this.mDeviceData.m_receiveDate = this.mDevicePackManager.m_DeviceDatas.get(this.mDevicePackManager.m_DeviceDatas.size() - 1).m_saveDate;
                    this.mDeviceData.init();
                    this.mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                    DatasContainer.mDeviceDatas.add(this.mDeviceData);
                    this.mDeviceData = null;
                }
                SendCommand.send(DeviceCommand.command_delData());
                return;
            case 2:
                CLog.i("ReceiveThread", "接收失败");
                return;
            case 3:
                CLog.dT("ReceiveThread", "校正时间成功******");
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            SendCommand.send(DeviceCommand.command_queryDataNum());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            case 4:
                CLog.dT("ReceiveThread", "对时失败");
                DeviceManager.mDeviceBeanList.mState = 5;
                DeviceManager.m_DeviceBean.mState = 5;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                return;
            case 5:
                Log.e("ReceiveThread", "删除成功");
                DeviceManager.mDeviceBeanList.mState = 6;
                DeviceManager.m_DeviceBean.mState = 6;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                return;
            case 6:
                Log.e("ReceiveThread", "删除失败");
                DeviceManager.mDeviceBeanList.mState = 6;
                DeviceManager.m_DeviceBean.mState = 6;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                return;
            case 7:
                DeviceManager.mDeviceBeanList.mState = 10;
                DeviceManager.m_DeviceBean.mState = 10;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                CLog.i("ReceiveThread", "没有数据");
                return;
            case 8:
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            SendCommand.send(DeviceCommand.command_VerifyTime());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            case 9:
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            SendCommand.send(DeviceCommand.command_requestAllData());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            default:
                return;
        }
    }
}
