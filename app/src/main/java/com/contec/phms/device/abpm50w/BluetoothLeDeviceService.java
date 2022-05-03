package com.contec.phms.device.abpm50w;

import android.os.Message;
import android.util.Log;
import com.contec.phms.App_phms;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.message.OrderList;
import com.contec.phms.util.CLog;
import com.contec.phms.util.DeviceNameUtils;
import com.contec.phms.util.FileOperation;
import com.contec.phms.util.PageUtil;
import com.example.ble_contec08A_code.CommandSphygmomanometer;
import com.example.ble_contec08A_code.DeviceDataSphygmomanometer;
import com.example.ble_contec08A_code.PackManagerSphygmomanometer;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class BluetoothLeDeviceService extends com.contec.phms.device.template.BluetoothLeDeviceService {
    private final String TAG = "BluetoothLeDeviceService_abpm50w";
    private byte arrangeMessage;
    private int count = 0;
    private boolean isHaveData = false;
    private int lastProgress = 0;
    private int mAllBoolPreNumber;
    private int mAllNoUploadDataNumber;
    private PackManagerSphygmomanometer packManager;

    public void bleServiceNofityOpen() {
        sendData(CommandSphygmomanometer.SET_TIME());
    }

    public void onCreate() {
        super.onCreate();
        this.packManager = new PackManagerSphygmomanometer();
        CLog.e("--DeviceService", "动态血压计开启服务");
    }

    public void arrangeMessage(byte[] buf, int length) {
        this.arrangeMessage = this.packManager.arrangeMessage(buf, length);
        if (this.arrangeMessage != 0 && (this.arrangeMessage & 255) != 136) {
            CLog.e("jar返回的信息", Integer.toHexString(this.arrangeMessage));
            switch (this.arrangeMessage) {
                case Byte.MIN_VALUE:
                    this.mAllBoolPreNumber = this.packManager.mAllCount;
                    this.mAllNoUploadDataNumber = this.packManager.mCount;
                    Log.i("BluetoothLeDeviceService_abpm50w", "0x80  mAllNoUploadDataNumber:" + this.mAllNoUploadDataNumber);
                    ui_update_progress(5);
                    sendData(CommandSphygmomanometer.GET_DATA(0));
                    return;
                case -127:
                    this.mAllBoolPreNumber = this.packManager.mAllCount;
                    this.mAllNoUploadDataNumber = this.packManager.mCount;
                    Log.i("BluetoothLeDeviceService_abpm50w", "mAllNoUploadDataNumber:" + this.mAllNoUploadDataNumber);
                    if (this.mAllNoUploadDataNumber == 0) {
                        noNewData();
                        return;
                    }
                    return;
                case 16:
                    CLog.e("血压发送对时", Integer.toHexString(this.arrangeMessage));
                    ui_update_progress(3);
                    sendData(CommandSphygmomanometer.GET_CLIP_INFO());
                    return;
                case 17:
                    sendData(CommandSphygmomanometer.SET_TIME());
                    return;
                case OrderList.DM_NEXT_DEVICE_OR_POLLING /*66*/:
                    int dataCount = (int) (((float) this.mAllNoUploadDataNumber) / 10.0f);
                    if (dataCount > 0) {
                        this.count++;
                        int progres = ((this.count * 35) / dataCount) + 5;
                        ui_update_progress(progres);
                        Log.i("jxx", "ui_update_progress method, progress %: " + progres + " dataCount:" + dataCount + " count:" + this.count);
                    }
                    sendData(CommandSphygmomanometer.GET_DATA(1));
                    return;
                case 67:
                    this.count = 0;
                    sendData(CommandSphygmomanometer.GET_DATA(126));
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ui_update_progress(50);
                    CLog.e("血压请求数据", Integer.toHexString(this.arrangeMessage));
                    List<DeviceDataSphygmomanometer> _listwear = this.packManager.mSphy;
                    DeviceData mDeviceData = new DeviceData((byte[]) null);
                    int size = _listwear.size();
                    CLog.e("BluetoothLeDeviceService_abpm50w", "------数据个数:" + size);
                    if (size > 0) {
                        for (int i = 0; i < size; i++) {
                            byte[] _data = new byte[12];
                            _data[0] = (byte) (_listwear.get(i).dataSphy[0] & 255);
                            _data[1] = (byte) (_listwear.get(i).dataSphy[1] & 255);
                            _data[2] = (byte) (_listwear.get(i).dataSphy[2] & 255);
                            _data[3] = (byte) (_listwear.get(i).dataSphy[3] & 255);
                            _data[4] = (byte) (_listwear.get(i).dataSphy[4] & 255);
                            _data[7] = (byte) (((_listwear.get(i).dataSphy[8] & 8) << 4) | _listwear.get(i).dataSphy[12]);
                            _data[5] = (byte) (((_listwear.get(i).dataSphy[8] & 2) << 6) | _listwear.get(i).dataSphy[10]);
                            _data[6] = (byte) (((_listwear.get(i).dataSphy[8] & 1) << 7) | _listwear.get(i).dataSphy[9]);
                            _data[8] = (byte) (((_listwear.get(i).dataSphy[8] & 4) << 5) | _listwear.get(i).dataSphy[11]);
                            _data[9] = (byte) (_listwear.get(i).dataSphy[5] & 255);
                            _data[10] = (byte) (((_listwear.get(i).dataSphy[8] & 16) << 3) | _listwear.get(i).dataSphy[13]);
                            _data[11] = (byte) (((_listwear.get(i).dataSphy[8] & 32) << 2) | _listwear.get(i).dataSphy[14]);
                            mDeviceData.m_nSys = (((_data[5] & 255) << 8) | (_data[6] & 255)) & 2047;
                            mDeviceData.m_nDia = _data[7];
                            mDeviceData.m_nHR = (((_data[11] & 255) << 8) | (_data[10] & 255)) & 2047;
                            mDeviceData.m_nMap = _data[8];
                            mDeviceData.m_nTC = 0;
                            mDeviceData.mDate = new int[6];
                            String _dateRecord = PageUtil.process_Date(_data[0] + 2000, _data[1], _data[2], _data[3], _data[4], _data[5]);
                            CLog.e("BluetoothLeDeviceService_abpm50w", " for 中的 date:" + _dateRecord);
                            byte[] provalue = PageUtil.compareDate(_dateRecord);
                            if (provalue != null) {
                                _data[0] = provalue[0];
                                _data[1] = provalue[1];
                                _data[2] = provalue[2];
                                _data[3] = provalue[3];
                                _data[4] = provalue[4];
                                _data[5] = provalue[5];
                                String _date = PageUtil.process_Date((provalue[0] & 255) + 2000, provalue[1] & 255, provalue[2] & 255, provalue[3] & 255, provalue[4] & 255, provalue[5] & 255);
                                FileOperation.writeToSDCard(String.valueOf(getcureentbytetime()) + " correct time: " + _date, "BluetoothLeDeviceService_abpm50w");
                                CLog.e("BluetoothLeDeviceService_abpm50w", " time is incorrect; corrected date: " + _date);
                            } else {
                                CLog.i("BluetoothLeDeviceService_abpm50w", "Correct time; no action required");
                                FileOperation.writeToSDCard(String.valueOf(getcureentbytetime()) + "Correct time; no action required", "BluetoothLeDeviceService_abpm50w");
                            }
                            mDeviceData.mDate[0] = _data[0];
                            mDeviceData.mDate[1] = _data[1];
                            mDeviceData.mDate[2] = _data[2];
                            mDeviceData.mDate[3] = _data[3];
                            mDeviceData.mDate[4] = _data[4];
                            mDeviceData.mDate[5] = _data[9];
                            mDeviceData.setmType(255);
                            mDeviceData.setmDataType(DeviceNameUtils.ABPM50W);
                            mDeviceData.setmUploadType(DeviceNameUtils.UPLOADTYPE_TREND);
                            mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                            mDeviceData.setSaveDate();
                            mDeviceData.setM_savedata();
                            mDeviceData.mDataList.add(_data);
                            if ((i + 1) % 15 == 0 || (i == size - 1 && (i + 1) % 15 != 0)) {
                                CLog.e("jxx", "满15条或者是最后一次循环，进行文件保存操作，i:" + i + " ,size:" + size);
                                DatasContainer.mDeviceDatas.add(mDeviceData);
                                mDeviceData.mDataList.clear();
                            }
                            if (i == size - 1) {
                                toUpload();
                            }
                        }
                        return;
                    }
                    noNewData();
                    return;
                default:
                    return;
            }
        }
    }

    private void ui_receiving(int i, int size) {
        CLog.i("BluetoothLeDeviceService_abpm50w", "call ui_receiving method " + i + " size:" + size);
        int progress = Math.round(((((float) (i + 1)) / ((float) size)) * 15.0f) + 35.0f);
        if (this.lastProgress != progress) {
            DeviceManager.m_DeviceBean.mProgress = progress;
            Log.i("jxx", "Current progress: " + DeviceManager.m_DeviceBean.mProgress);
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
            this.lastProgress = progress;
        }
    }

    private void ui_update_progress(int progress) {
        DeviceManager.m_DeviceBean.mProgress = progress;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
    }

    private String getcureentbytetime() {
        return new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss:SSS ").format(new Date(System.currentTimeMillis()));
    }

    protected void toUpload() {
        super.toUpload();
        this.isHaveData = false;
    }

    public void onEvent(Message message) {
    }
}
