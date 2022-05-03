package com.contec.phms.device.sp10w;

import android.os.Message;
import android.util.Log;
import com.conect.json.CLog;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.message.MessageManagerMain;
import com.contec.phms.db.DeviceListDaoOperation;
import com.contec.phms.util.Constants;
import com.contec.sp10.code.CommandSpirometer;
import com.contec.sp10.code.DeviceDataJar;
import com.contec.sp10.code.PackManagerSpirometer;
import java.util.Timer;
import java.util.TimerTask;

public class BluetoothLeDeviceService extends com.contec.phms.device.template.BluetoothLeDeviceService {
    private static final String TAG = "BluetoothLeDeviceService_sp10";
    private int arrangeMessage;
    private int mAllSp10DataNumber;
    private int mInforIndex = 1;
    private int mNoUploadDataNumber;
    private PackManager mSaveDataPackManager;
    private Timer mTimer;
    private PackManagerSpirometer packManager;

    public void onCreate() {
        super.onCreate();
        LogI("创建服务---sp10BLE服务类");
        new DatasContainer();
        this.packManager = new PackManagerSpirometer();
        this.mSaveDataPackManager = new PackManager();
    }

    public void bleServiceNofityOpen() {
        sendData(CommandSpirometer.SET_TIME());
        MessageManagerMain.msg_ui_receive_dataing();
        LogI("设备连接成功，开始发送第一条命令-索要设备号");
    }

    public void arrangeMessage(byte[] buf, int length) {
        this.arrangeMessage = this.packManager.arrangeMessage(buf, length);
        LogI("call arrangeMessage method:" + this.arrangeMessage);
        if (this.arrangeMessage == 0 || (this.arrangeMessage & 255) == 136) {
            cancleTimer();
            timer(5000);
            return;
        }
        switch (this.arrangeMessage) {
            case 16:
                LogI("设置时间成功");
                sendData(CommandSpirometer.GET_STORAGE_INFOR());
                return;
            case 17:
                LogI("设置时间失败");
                sendData(CommandSpirometer.SET_TIME());
                return;
            case 19:
                this.mAllSp10DataNumber = this.packManager.storage[0];
                this.mNoUploadDataNumber = this.packManager.storage[1];
                Log.e("=======================", "所有空间数" + this.mAllSp10DataNumber);
                Log.e("=======================", "未上传数据空间数" + this.mNoUploadDataNumber);
                if (this.mNoUploadDataNumber == 0) {
                    noNewData();
                    return;
                }
                this.mInforIndex++;
                sendData(CommandSpirometer.GET_BASEINFOR(0));
                return;
            case 20:
                LogI("返回基本信息成功0x14");
                MessageManagerMain.msg_ui_receive_dataing();
                sendData(CommandSpirometer.GET_WAVE_DATA(0));
                return;
            case 21:
                LogI("返回基本信息成功0x15");
                if (this.mInforIndex > this.mNoUploadDataNumber) {
                    Log.e("=======================", "返回基本信息,删除数据");
                    sendData(CommandSpirometer.DELETE_DATA());
                } else {
                    Log.e("=======================", "继续请求基本信息");
                    sendData(CommandSpirometer.GET_BASEINFOR(1));
                }
                this.mInforIndex++;
                return;
            case 32:
                LogI("删除成功");
                this.mSaveDataPackManager.saveDeviceData_new(this.packManager);
                DeviceDataJar _data = this.packManager.mDeviceDataJarsList.get(this.packManager.mDeviceDataJarsList.size() - 1);
                double _fvc = _data.mParamInfo.mFVC;
                DeviceListDaoOperation.getInstance().updateReceiveDataStr(DeviceManager.m_DeviceBean.mMacAddr, String.valueOf(_data.mPatientInfo.mYear + 2000) + "-" + _data.mPatientInfo.mMonth + "-" + "-" + _data.mPatientInfo.mDay + _data.mPatientInfo.mHour + ":" + _data.mPatientInfo.mMin + ":" + "0", String.valueOf(_fvc) + ";" + _data.mParamInfo.mPEF + ";" + _data.mParamInfo.mFEV1);
                toUpload();
                this.mInforIndex = 0;
                return;
            case 33:
                sendData(CommandSpirometer.DELETE_DATA());
                return;
            default:
                return;
        }
    }

    private void timer(long delay) {
        this.mTimer = new Timer();
        this.mTimer.schedule(new TimerTask() {
            public void run() {
                if (BluetoothLeDeviceService.this.mTimer != null) {
                    BluetoothLeDeviceService.this.mTimer.cancel();
                    BluetoothLeDeviceService.this.mTimer.purge();
                    BluetoothLeDeviceService.this.mTimer = null;
                    BluetoothLeDeviceService.this.noNewData();
                }
            }
        }, delay);
    }

    private void cancleTimer() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer.purge();
            this.mTimer = null;
        }
    }

    public void onEvent(Message message) {
    }

    protected void noNewData() {
        CLog.i(TAG, "call 。。noNewData");
        disConnectBleDevice();
        MessageManagerMain.msg_ui_no_new_data();
        MessageManagerMain.msg_ConnectNextDeviceOrPollingOrCallBack();
    }

    public void LogI(String msg) {
        if (Constants.mTestFlag) {
            Log.i(TAG, msg);
        }
    }
}
