package com.contec.phms.device.sp10w;

import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.device.template.SendCommand;
import com.contec.phms.eventbus.EventShowLastData;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.db.DeviceListDaoOperation;
import com.contec.phms.util.CLog;
import com.contec.phms.util.PageUtil;
import cn.com.contec.jar.sp10w.DeviceCommand;
import cn.com.contec.jar.sp10w.DeviceDataJar;
import cn.com.contec.jar.sp10w.DevicePackManager;

public class ReceiveThread extends com.contec.phms.device.template.ReceiveThread {
    private PackManager mPackManager = new PackManager();
    private DevicePackManager m_DevicePackManager = new DevicePackManager();

    public ReceiveThread(DeviceService mDeviceService) {
        super(mDeviceService);
    }

    public void arrangeMessage(byte[] buf, int length) {
        switch (this.m_DevicePackManager.arrangeMessage(buf, length, false)) {
            case 1:
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SendCommand.send(DeviceCommand.command_delData());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                CLog.i("ReceiveThread", "成功接收数据 发 送删除命令");
                DeviceManager.mDeviceBeanList.mState = 4;
                DeviceManager.m_DeviceBean.mState = 4;
                this.mPackManager.saveDeviceData_new(this.m_DevicePackManager);
                DeviceManager.m_DeviceBean.mProgress = 50;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(new EventShowLastData());
                return;
            case 2:
                DeviceManager.mDeviceBeanList.mState = 10;
                DeviceManager.m_DeviceBean.mState = 10;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                DeviceService.nextStep();
                CLog.i("ReceiveThread", "没有数据");
                return;
            case 3:
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                SendCommand.send(DeviceCommand.command_requestData());
                CLog.i("ReceiveThread", "对时间成功");
                return;
            case 4:
                CLog.i("ReceiveThread", "对时间失败");
                return;
            case 5:
                DeviceService.mReceiveFinished = true;
                CLog.i("ReceiveThread", "删除成功");
                DeviceDataJar _data = this.m_DevicePackManager.mDeviceDataJarsList.get(this.m_DevicePackManager.mDeviceDataJarsList.size() - 1);
                double _fvc = _data.mParamInfo.mFVC;
                String _receiveStr = String.valueOf(_fvc) + ";" + _data.mParamInfo.mPEF + ";" + _data.mParamInfo.mFEV1;
                String _date = PageUtil.processDate(_data.mPatientInfo.mYear, _data.mPatientInfo.mMonth, _data.mPatientInfo.mDay, _data.mPatientInfo.mHour, _data.mPatientInfo.mMin, 0);
                CLog.e("ReceiveThread", "*************_receiveStr: " + _receiveStr);
                DeviceListDaoOperation.getInstance().updateReceiveDataStr(DeviceManager.m_DeviceBean.mMacAddr, _date, _receiveStr);
                DeviceManager.mDeviceBeanList.mState = 6;
                DeviceManager.m_DeviceBean.mState = 6;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.nextStep();
                return;
            case 6:
                DeviceService.mReceiveFinished = true;
                CLog.i("ReceiveThread", "删除失败");
                DeviceDataJar _dataFalied = this.m_DevicePackManager.mDeviceDataJarsList.get(this.m_DevicePackManager.mDeviceDataJarsList.size() - 1);
                double _fvcF = _dataFalied.mParamInfo.mFVC;
                String _receiveStrF = String.valueOf(_fvcF) + ";" + _dataFalied.mParamInfo.mPEF + ";" + _dataFalied.mParamInfo.mFEV1;
                String _dateF = PageUtil.processDate(_dataFalied.mPatientInfo.mYear, _dataFalied.mPatientInfo.mMonth, _dataFalied.mPatientInfo.mDay, _dataFalied.mPatientInfo.mHour, _dataFalied.mPatientInfo.mMin, 0);
                CLog.e("ReceiveThread", "*************_receiveStr: " + _receiveStrF);
                DeviceListDaoOperation.getInstance().updateReceiveDataStr(DeviceManager.m_DeviceBean.mMacAddr, _dateF, _receiveStrF);
                DeviceManager.mDeviceBeanList.mState = 6;
                DeviceManager.m_DeviceBean.mState = 6;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.nextStep();
                return;
            case 7:
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                SendCommand.send(DeviceCommand.command_Time());
                CLog.i("ReceiveThread", "设置日期成功");
                return;
            case 8:
                CLog.i("ReceiveThread", "设置日期失败");
                return;
            default:
                return;
        }
    }
}
