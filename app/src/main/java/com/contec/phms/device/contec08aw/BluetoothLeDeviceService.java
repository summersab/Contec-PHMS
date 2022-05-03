package com.contec.phms.device.contec08aw;

import android.os.Message;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.message.OrderList;
import com.contec.phms.util.CLog;
import com.example.ble_contec08A_code.CommandSphygmomanometer;
import com.example.ble_contec08A_code.DeviceDataSphygmomanometer;
import com.example.ble_contec08A_code.PackManagerSphygmomanometer;
import java.util.List;

public class BluetoothLeDeviceService extends com.contec.phms.device.template.BluetoothLeDeviceService {
    private final String TAG = "BluetoothLeDeviceService_wt";
    private byte arrangeMessage;
    private boolean isHaveData = false;
    private int mAllBoolPreNumber;
    private int mAllNoUploadDataNumber;
    private PackManagerSphygmomanometer packManager;

    public void bleServiceNofityOpen() {
        sendData(CommandSphygmomanometer.SET_TIME());
    }

    public void onCreate() {
        super.onCreate();
        this.packManager = new PackManagerSphygmomanometer();
        CLog.e("--DeviceService", "血压计开启服务");
    }

    public void arrangeMessage(byte[] buf, int length) {
        this.arrangeMessage = this.packManager.arrangeMessage(buf, length);
        if (this.arrangeMessage != 0 && (this.arrangeMessage & 255) != 136) {
            CLog.e("jar返回的信息", Integer.toHexString(this.arrangeMessage));
            switch (this.arrangeMessage) {
                case Byte.MIN_VALUE:
                    this.mAllBoolPreNumber = this.packManager.mAllCount;
                    this.mAllNoUploadDataNumber = this.packManager.mCount;
                    sendData(CommandSphygmomanometer.GET_DATA(0));
                    return;
                case -127:
                    this.mAllBoolPreNumber = this.packManager.mAllCount;
                    this.mAllNoUploadDataNumber = this.packManager.mCount;
                    if (this.mAllNoUploadDataNumber == 0) {
                        noNewData();
                        return;
                    }
                    return;
                case 16:
                    CLog.e("血压发送对时", Integer.toHexString(this.arrangeMessage));
                    sendData(CommandSphygmomanometer.GET_CLIP_INFO());
                    return;
                case 17:
                    sendData(CommandSphygmomanometer.SET_TIME());
                    return;
                case OrderList.DM_NEXT_DEVICE_OR_POLLING /*66*/:
                    sendData(CommandSphygmomanometer.GET_DATA(1));
                    return;
                case 67:
                    if (DeviceManager.m_DeviceBean.mDeviceName.equals("CONTEC08AW")) {
                        sendData(CommandSphygmomanometer.GET_DATA(126));
                    } else {
                        sendData(CommandSphygmomanometer.GET_DATA(127));
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    CLog.e("Request blood pressure data", Integer.toHexString(this.arrangeMessage));
                    List<DeviceDataSphygmomanometer> _listwear = this.packManager.mSphy;
                    DeviceData mDeviceData = new DeviceData((byte[]) null);
                    for (int i = 0; i < _listwear.size(); i++) {
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
                        mDeviceData.mDate = new int[6];
                        mDeviceData.mDate[0] = (_listwear.get(i).dataSphy[0] & 255) + 2000;
                        mDeviceData.mDate[1] = _data[1];
                        mDeviceData.mDate[2] = _data[2];
                        mDeviceData.mDate[3] = _data[3];
                        mDeviceData.mDate[4] = _data[4];
                        mDeviceData.mDate[5] = _data[5];
                        mDeviceData.setmUploadType("trend");
                        mDeviceData.setmDataType("contec08aw");
                        mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                        mDeviceData.setSaveDate();
                        mDeviceData.mDataList.add(_data);
                        DatasContainer.mDeviceDatas.add(mDeviceData);
                    }
                    toUpload();
                    return;
                default:
                    return;
            }
        }
    }

    protected void toUpload() {
        super.toUpload();
        this.isHaveData = false;
    }

    public void onEvent(Message message) {
    }
}
