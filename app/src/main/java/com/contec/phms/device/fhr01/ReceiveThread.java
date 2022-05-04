package com.contec.phms.device.fhr01;

import android.util.Log;
import com.alibaba.fastjson.asm.Opcodes;
import com.contec.jar.fhr01.DeviceCommand;
import com.contec.jar.fhr01.DeviceData;
import com.contec.jar.fhr01.DevicePackManager;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.device.template.SendCommand;
import com.contec.phms.eventbus.EventShowLastData;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.db.DeviceListDaoOperation;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.PrintBytes;
import java.util.ArrayList;

public class ReceiveThread extends com.contec.phms.device.template.ReceiveThread {
    DevicePackManager mPackManager = new DevicePackManager();

    public ReceiveThread(DeviceService mDeviceService) {
        super(mDeviceService);
    }

    public void arrangeMessage(byte[] buf, int length) {
        PrintBytes.printData(buf, length);
        switch (this.mPackManager.arrangeMessage1(buf, length)) {
            case 9:
                DeviceManager.m_DeviceBean.mProgress = ((this.mPackManager.mDeviceDatas.mDatas.size() * 90) / this.mPackManager.mDeviceDatas.m_Data_Count) + 10;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                return;
            case 10:
                if (this.mPackManager.mDeviceDatas.m_Data_Count > 0) {
                    DeviceManager.mDeviceBeanList.mState = 4;
                    DeviceManager.m_DeviceBean.mState = 4;
                    DeviceManager.m_DeviceBean.mProgress = 0;
                    SendCommand.send(DeviceCommand.REQUEST_DATA);
                    return;
                }
                DeviceManager.mDeviceBeanList.mState = 10;
                DeviceManager.m_DeviceBean.mState = 10;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                return;
            case Opcodes.I2B:
                SendCommand.send(DeviceCommand.DELETE_DATA);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DeviceData _DeviceData_Trend = new DeviceData();
                ArrayList<DeviceData> mDatas = this.mPackManager.mDeviceDatas.mDatas;
                int _size = mDatas.size();
                for (int i = 0; i < _size; i++) {
                    int[] _time = mDatas.get(i).m_start_time;
                    DeviceData _DeviceData = new DeviceData();
                    byte[] _Value = new byte[7];
                    _Value[0] = (byte) (_time[0] - 2000);
                    _Value[1] = (byte) _time[1];
                    _Value[2] = (byte) _time[2];
                    _Value[3] = (byte) _time[3];
                    _Value[4] = (byte) _time[4];
                    _Value[5] = (byte) _time[5];
                    if (mDatas.get(i).mDatas.size() > 0) {
                        int x = mDatas.get(i).mDatas.size() / 2;
                        System.out.println("===========================");
                        System.out.println(mDatas.get(i).mDatas.get(x).length);
                        System.out.println("===========================");
                        _Value[6] = mDatas.get(i).mDatas.get(x)[0];
                        _DeviceData_Trend.mDataList.add(_Value);
                        _DeviceData.m_fetal_heart = mDatas.get(i).mDatas;
                        _DeviceData.m_iDay = _time[2];
                        _DeviceData.m_iHour = _time[3];
                        _DeviceData.m_iMinute = _time[4];
                        _DeviceData.m_iMonth = _time[1] + 1;
                        _DeviceData.m_iYear = _time[0];
                        _DeviceData.m_iSec = _time[5];
                        //_DeviceData.setSaveDate();
                        _DeviceData.mDate = new int[6];
                        _DeviceData.mDate[0] = _time[0];
                        _DeviceData.mDate[1] = _time[1] + 1;
                        _DeviceData.mDate[2] = _time[2];
                        _DeviceData.mDate[3] = _time[3];
                        _DeviceData.mDate[4] = _time[4];
                        _DeviceData.mDate[5] = _time[5];
                        //_DeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                        //_DeviceData.setmUploadType("case");
                        //_DeviceData.setmDataType("FHR01");
                        DatasContainer.mDeviceDatas.add(_DeviceData);
                    }
                }
                if (_DeviceData_Trend.mDataList.size() > 0) {
                    //byte[] _value = (byte[]) _DeviceData_Trend.mDataList.get(_DeviceData_Trend.mDataList.size() - 1);
                    //String _receiveStr = new StringBuilder().append(_value[6] & 255).toString();
                    //DeviceListDaoOperation.getInstance().updateReceiveDataStr(DeviceManager.m_DeviceBean.mMacAddr, PageUtil.processDate((_value[0] & 255) + 2000, (_value[1] & 255) + 1, _value[2] & 255, _value[3] & 255, _value[4] & 255, 0), _receiveStr);
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(new EventShowLastData());
                    //_DeviceData_Trend.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                    //_DeviceData_Trend.setmUploadType("trend");
                    //_DeviceData_Trend.setmDataType("FHR01");
                    DatasContainer.mDeviceDatas.add(_DeviceData_Trend);
                    DeviceManager.m_DeviceBean.mProgress = 0;
                    DeviceManager.mDeviceBeanList.mState = 6;
                    DeviceManager.m_DeviceBean.mState = 6;
                    DeviceManager.mDeviceBeanList.mState = 6;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    DeviceService.mReceiveFinished = true;
                    return;
                }
                DeviceManager.m_DeviceBean.mProgress = 0;
                DeviceManager.mDeviceBeanList.mState = 10;
                DeviceManager.m_DeviceBean.mState = 10;
                DeviceManager.mDeviceBeanList.mState = 10;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                return;
            case Opcodes.IF_ICMPLT:
                Log.e("ReceiveThread", "**********************");
                DeviceManager.mDeviceBeanList.mState = 10;
                DeviceManager.m_DeviceBean.mState = 10;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                return;
            default:
                return;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static int max(ArrayList<byte[]> pValue) {
        int _size = pValue.size();
        int _len = _size / 2;
        int max = 0;
        for (int i = 0; i < _len; i++) {
            int _m = (pValue.get(i)[0] & 255) > (pValue.get(i + _len)[0] & 255) ? pValue.get(i)[0] : pValue.get(i + _len)[0] & 255;
            if (max < _m) {
                max = _m;
            }
        }
        if (_size % 2 == 0 || max >= (pValue.get(_size - 1)[0] & 255)) {
            return max;
        }
        int max2 = pValue.get(_size - 1)[0] & 255;
        return max2;
    }
}
