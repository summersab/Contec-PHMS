package com.contec.phms.device.temp01;

import android.os.Message;
import com.contec.phms.App_phms;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.message.OrderList;
import com.contec.phms.db.DeviceListDaoOperation;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.JPageUtil;
import com.contec.phms.util.PageUtil;
import com.example.temp.bean.EarTempertureDataJar;
import com.example.temp.ble_code.CommandThermometer;
import com.example.temp.ble_code.PackManagerThermometer;
import java.util.Calendar;
import u.aly.bs;

public class BluetoothLeDeviceService extends com.contec.phms.device.template.BluetoothLeDeviceService {
    public static final String TAG = "BluetoothLeDeviceService_temp01";
    private byte arrangeMessage;
    private int count;
    private int countno;
    DeviceData mDeviceData = new DeviceData();
    PackManagerThermometer mDevicePackManager;

    public void onCreate() {
        super.onCreate();
        this.mDevicePackManager = new PackManagerThermometer();
        new DatasContainer();
    }

    public void bleServiceNofityOpen() {
        sendData(CommandThermometer.SET_TIME());
    }

    public void arrangeMessage(byte[] buf, int length) {
        this.arrangeMessage = this.mDevicePackManager.arrangeMessage(buf, length);
        if (!(this.arrangeMessage == 0 || (this.arrangeMessage & 255) == 136)) {
            CLog.e("Information returned", Integer.toHexString(this.arrangeMessage));
        }
        switch (this.arrangeMessage) {
            case Constants.UPDATEXML_UPLOAD_FAIL /*-32*/:
                this.count = ((this.mDevicePackManager.mSaveDataNumber[1] << 7) | (this.mDevicePackManager.mSaveDataNumber[0] & 255)) & 65535;
                this.countno = ((this.mDevicePackManager.mSaveDataNumber[3] << 7) | (this.mDevicePackManager.mSaveDataNumber[2] & 255)) & 65535;
                CLog.e("=================", "============================");
                CLog.e("Total number of records", "Total records: " + this.count);
                CLog.e("Records not uploaded", "Not uploaded: " + this.countno);
                CLog.e("=================", "============================");
                sendData(CommandThermometer.GET_DATA(0));
                return;
            case 16:
                sendData(CommandThermometer.GET_SIZE());
                return;
            case 17:
                sendData(CommandThermometer.SET_TIME());
                return;
            case 18:
                LogI("No new data");
                noNewData();
                return;
            case 32:
                LogI("Data successfully deleted");
                return;
            case OrderList.DM_NEXT_DEVICE_OR_POLLING /*66*/:
                sendData(CommandThermometer.GET_DATA(1));
                return;
            case 67:
                sendData(CommandThermometer.DELETE_DATA());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DeviceManager.mDeviceBeanList.mState = 4;
                DeviceManager.m_DeviceBean.mState = 4;
                DeviceManager.m_DeviceBean.mProgress = 70;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                if (this.mDevicePackManager.m_DeviceDatas.size() > 9) {
                    int _saveNum = 0;
                    for (int i = 0; i < this.mDevicePackManager.m_DeviceDatas.size(); i++) {
                        _saveNum++;
                        EarTempertureDataJar _data = this.mDevicePackManager.m_DeviceDatas.get(i);
                        CLog.e(TAG, "满2条了，开始存储*************:" + _data.m_data);
                        if (PageUtil.compareDate(_data.m_saveDate) != null) {
                            _data.m_saveDate = PageUtil.getStringTime(System.currentTimeMillis());
                        }
                        if (_saveNum == 9) {
                            CLog.d(TAG, "满2条了，开始存储*************");
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
                            CLog.d(TAG, "Finished saving all data *************");
                        }
                    }
                } else {
                    for (int i2 = 0; i2 < this.mDevicePackManager.m_DeviceDatas.size(); i2++) {
                        EarTempertureDataJar _data2 = this.mDevicePackManager.m_DeviceDatas.get(i2);
                        CLog.e(TAG, " get date: time: ======" + _data2.m_saveDate + " body temperature: ====" + _data2.m_data);
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
                LogI("Data successfully received");
                LogI("Send delete command");
                toUpload();
                return;
            default:
                return;
        }
    }

    private void save_DeviceData_old() {
        if (this.mDevicePackManager.m_DeviceDatas.size() > 9) {
            int _saveNum = 0;
            for (int i = 0; i < this.mDevicePackManager.m_DeviceDatas.size(); i++) {
                _saveNum++;
                EarTempertureDataJar _data = this.mDevicePackManager.m_DeviceDatas.get(i);
                if (PageUtil.compareDate(_data.m_saveDate) != null) {
                    _data.m_saveDate = PageUtil.getStringTime(System.currentTimeMillis());
                }
                this.mDeviceData.mDataList.add(_data);
                if (_saveNum == 9) {
                    this.mDeviceData.m_receiveDate = _data.m_saveDate;
                    this.mDeviceData.init();
                    this.mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                    addData(this.mDeviceData);
                    DatasContainer.mDeviceDatas.add(this.mDeviceData);
                    this.mDeviceData.mDataList.clear();
                    _saveNum = 0;
                } else if (i == this.mDevicePackManager.m_DeviceDatas.size() - 1) {
                    this.mDeviceData.m_receiveDate = _data.m_saveDate;
                    this.mDeviceData.init();
                    this.mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                    addData(this.mDeviceData);
                    DatasContainer.mDeviceDatas.add(this.mDeviceData);
                }
            }
            return;
        }
        for (int i2 = 0; i2 < this.mDevicePackManager.m_DeviceDatas.size(); i2++) {
            EarTempertureDataJar _data2 = this.mDevicePackManager.m_DeviceDatas.get(i2);
            if (PageUtil.compareDate(_data2.m_saveDate) != null) {
                _data2.m_saveDate = PageUtil.getStringTime(System.currentTimeMillis());
            }
            LogI("mDeviceData:" + this.mDeviceData);
            LogI("mDeviceData.mDataList:" + this.mDeviceData.mDataList);
            LogI("_data:" + _data2);
            this.mDeviceData.mDataList.add(_data2);
        }
        this.mDeviceData.m_receiveDate = this.mDevicePackManager.m_DeviceDatas.get(this.mDevicePackManager.m_DeviceDatas.size() - 1).m_saveDate;
        this.mDeviceData.init();
        this.mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
        addData(this.mDeviceData);
        this.mDeviceData = null;
    }

    private void save_DeviceData_new() {
        int size = this.mDevicePackManager.m_DeviceDatas.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                EarTempertureDataJar _data = this.mDevicePackManager.m_DeviceDatas.get(i);
                if (PageUtil.compareDate(_data.m_saveDate) != null) {
                    _data.m_saveDate = PageUtil.getStringTime(System.currentTimeMillis());
                }
                this.mDeviceData.mDataList.add(_data);
                if ((i + 1) % 15 == 0 || (i == size - 1 && (i + 1) % 15 != 0)) {
                    this.mDeviceData.m_receiveDate = _data.m_saveDate;
                    this.mDeviceData.init();
                    this.mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                    addData(this.mDeviceData);
                    this.mDeviceData.mDataList.clear();
                }
            }
        }
    }

    public void addData(DeviceData deviceData) {
        DatasContainer.mDeviceDatas.add(deviceData);
        update_db(deviceData);
    }

    private void update_db(DeviceData deviceData) {
        int[] mDate = deviceData.mDate;
        if (mDate[0] < 2000) {
            mDate[0] = Calendar.getInstance().get(1);
        }
        String processDate = JPageUtil.processDate(mDate[0], mDate[1], mDate[2], mDate[3], mDate[4], mDate[5]);
        CLog.i(TAG, "date: " + processDate);
        DeviceListDaoOperation.getInstance().updateReceiveDataStr(DeviceManager.m_DeviceBean.mMacAddr, processDate, bs.b);
    }

    public void LogI(String msg) {
        if (Constants.mTestFlag) {
            CLog.i(TAG, msg);
        }
    }

    public void onEvent(Message message) {
    }
}
