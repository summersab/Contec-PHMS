package com.contec.phms.device.cmssxt;

import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.device.template.SendCommand;
import com.contec.phms.eventbus.EventShowLastData;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.CLog;
import com.contec.phms.util.PageUtil;
import cn.com.contec.jar.cmssxt.CmssxtDataJar;
import cn.com.contec.jar.cmssxt.DeviceCommand;
import cn.com.contec.jar.cmssxt.DevicePackManager;

public class ReceiveThread extends com.contec.phms.device.template.ReceiveThread {
    private int mCheckTimeNum = 0;
    DeviceData mDeviceData = new DeviceData();
    private DevicePackManager mDevicePackManager = new DevicePackManager();

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
                        CmssxtDataJar _data = this.mDevicePackManager.m_DeviceDatas.get(i);
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
                        CmssxtDataJar _data2 = this.mDevicePackManager.m_DeviceDatas.get(i2);
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
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(new EventShowLastData());
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (Exception e) {
                        }
                        SendCommand.send(DeviceCommand.command_delData());
                    }
                }.start();
                CLog.i("ReceiveThread", "成功接收数据 , 发送命令删除数据");
                return;
            case 2:
                DeviceManager.mDeviceBeanList.mState = 5;
                DeviceManager.m_DeviceBean.mState = 5;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                CLog.i("ReceiveThread", "接收数据校验不成功或没有数据");
                return;
            case 3:
                DeviceManager.m_DeviceBean.mProgress = 0;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                CLog.i("ReceiveThread", "对时成功");
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (Exception e) {
                        }
                        SendCommand.send(DeviceCommand.command_requestData());
                    }
                }.start();
                return;
            case 4:
                SendCommand.send(DeviceCommand.command_VerifyTime());
                this.mCheckTimeNum++;
                if (this.mCheckTimeNum == 2) {
                    SendCommand.send(DeviceCommand.command_delData());
                }
                CLog.i("ReceiveThread", "对时失败");
                return;
            case 5:
                DeviceManager.mDeviceBeanList.mState = 6;
                DeviceManager.m_DeviceBean.mState = 6;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                CLog.i("ReceiveThread", "删除成功");
                return;
            case 6:
                DeviceManager.mDeviceBeanList.mState = 6;
                DeviceManager.m_DeviceBean.mState = 6;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                CLog.i("ReceiveThread", "删除失败");
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
                            Thread.sleep(500);
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
                            Thread.sleep(500);
                            SendCommand.send(DeviceCommand.command_VerifyTimeSS());
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
