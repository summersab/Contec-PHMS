package com.contec.phms.device.bc01;

import android.os.Message;
import android.util.Log;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.db.DeviceListDaoOperation;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.FileOperation;
import com.contec.phms.util.JPageUtil;
import com.example.ble_bc_code.CommandUran;
import com.example.ble_bc_code.DeviceUran;
import com.example.ble_bc_code.PackManagerUran;
import com.example.bm77_bc_code.BC401_Data;
import com.example.bm77_bc_code.BC401_Struct;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import u.aly.bs;

public class BluetoothLeDeviceService extends com.contec.phms.device.template.BluetoothLeDeviceService {
    private static final String TAG = "BluetoothLeDeviceService-bc01";
    public static boolean synchronous = false;
    private byte arrangeMessage;
    private int countno;
    private int mCount;
    private DeviceData mData;
    private boolean mToUpload = false;
    private DeviceUran mUrannumber = new DeviceUran();
    private PackManagerUran packManager;
    private Timer responseTime;

    public void onCreate() {
        super.onCreate();
        LogI("`bc01.onCreate()`--- Ble urine analyzer");
        this.packManager = new PackManagerUran();
        new DatasContainer();
    }

    public void bleServiceNofityOpen() {
        DeviceManager.m_DeviceBean.mProgress = 0;
        sendData(CommandUran.SET_TIME());
        synchronous = false;
    }

    public void arrangeMessage(byte[] buf, int length) {
        CLog.i(TAG, "`bc01.arrangeMessage()`");
        this.arrangeMessage = this.packManager.arrangeMessage(buf, length);
        if (this.arrangeMessage != 0 && (this.arrangeMessage & 255) != 136) {
            CLog.e("jar返回的信息", Integer.toHexString(this.arrangeMessage));
            switch (this.arrangeMessage) {
                case 16:
                    LogI("代码执行到这里1");
                    FileOperation.writeToSDCard(String.valueOf(getcureentbytetime()) + " 校时成功，请求数据", "BC401");
                    DeviceService.synchronous = true;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendData(CommandUran.GetData_Stroge(0));
                    return;
                case 17:
                    CLog.e("Display Data", "Time failed");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                    sendData(CommandUran.SET_TIME());
                    return;
                case 18:
                    LogI("代码执行到这里2");
                    this.mCount = this.mUrannumber.mStrongNUmber[0];
                    this.countno = this.mUrannumber.mStrongNUmber[1];
                    this.mCount = ((this.packManager.mUran.mDataNumber[1] & 32640) | (this.packManager.mUran.mDataNumber[0] & 255)) & 65535;
                    this.countno = ((this.packManager.mUran.mDataNumber[3] & 32640) | (this.packManager.mUran.mDataNumber[2] & 255)) & 65535;
                    if (this.countno > 0) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e3) {
                            e3.printStackTrace();
                        }
                        sendData(CommandUran.GET_ALL_DATA(0));
                        return;
                    }
                    noNewData();
                    FileOperation.writeToSDCard(String.valueOf(getcureentbytetime()) + " No new data", "BC401");
                    DeviceManager.mDeviceBeanList.mState = 10;
                    DeviceManager.m_DeviceBean.mState = 10;
                    DeviceService.mReceiveFinished = true;
                    return;
                case 19:
                    CLog.e("Send next set of data request commands", "Send data request commands");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e4) {
                        e4.printStackTrace();
                    }
                    sendData(CommandUran.GET_ALL_DATA(1));
                    return;
                case 20:
                    CLog.e(TAG, "All data received");
                    FileOperation.writeToSDCard(String.valueOf(getcureentbytetime()) + " Data returned", "BC401");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e5) {
                        e5.printStackTrace();
                    }
//                    sendData(CommandUran.GET_DELETE_DATA());
                    this.mData = new DeviceData();
                    BC401_Data _data = this.packManager.mBc401_Data;
                    this.mData.mDataList = _data.Structs;
                    int _dataSize = this.mData.mDataList.size();
                    for (int j = 0; j < _dataSize; j++) {
                        BC401_Struct _dataBC = (BC401_Struct) this.mData.mDataList.get(j);
                        String _time = (_dataBC.Year + 2000) + "-" + _dataBC.Month + "-" + _dataBC.Date + " " + _dataBC.Hour + ":" + _dataBC.Min + ":" + _dataBC.Sec;
                        String _value = "0" + _dataBC.URO + "0" + _dataBC.BLD + "0" + _dataBC.BIL + "0" + _dataBC.KET + "0" + _dataBC.GLU + "0" + _dataBC.PRO + "0" + _dataBC.PH + "0" + _dataBC.NIT + "0" + _dataBC.LEU + "0" + _dataBC.SG + "0" + _dataBC.VC + "9" + _dataBC.MAL + "9" + _dataBC.CR + "9" + _dataBC.UCA;
                        FileOperation.writeToSDCard("time: " + _time + "  " + _value, "BC401");
                        Log.e("%%%%%%%%%%%%%%", "%%%%%%%%%%%%%%");
                        Log.e("Output data incompatible with 14 panel test strips", "time: " + _time + ";" + _value + ";" + _dataSize);
                        Log.e("%%%%%%%%%%%%%%", "%%%%%%%%%%%%%%");
                    }
                    this.mData.mDate = new int[6];
                    //this.mData.mDate[0] = _data.Structs.get(0).Year + 2000;
                    //this.mData.mDate[1] = _data.Structs.get(0).Month;
                    //this.mData.mDate[2] = _data.Structs.get(0).Date;
                    //this.mData.mDate[3] = _data.Structs.get(0).Hour;
                    //this.mData.mDate[4] = _data.Structs.get(0).Min;
                    //this.mData.mDate[5] = _data.Structs.get(0).Sec;
                    this.mData.setmUploadType("trend");
                    this.mData.setmDataType("bc01");
                    this.mData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                    addData(this.mData);
                    toUpload();
                    return;
                case 32:
                    CLog.e(TAG, "数据删除成功");
                    return;
                case 33:
                    CLog.e(TAG, "删除失败，重发删除");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e6) {
                        e6.printStackTrace();
                    }
                    sendData(CommandUran.GET_DELETE_DATA());
                    return;
                default:
                    return;
            }
        }
    }

    private void initDeviceData() {
        this.mData = new DeviceData();
        this.mData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
    }

    private String getcureentbytetime() {
        return new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss:SSS ").format(new Date(System.currentTimeMillis()));
    }

    private void delayTime(int time) {
        try {
            Thread.sleep((long) time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void cancelResonseTimer() {
        if (this.responseTime != null) {
            this.responseTime.cancel();
            this.responseTime.purge();
            this.responseTime = null;
        }
    }

    public void addData(DeviceData deviceData) {
        DatasContainer.mDeviceDatas.add(deviceData);
        update_db();
    }

    private void update_db() {
        int[] mDate = this.mData.mDate;
        if (mDate[0] < 2000) {
            mDate[0] = Calendar.getInstance().get(1);
        }
        String processDate = JPageUtil.processDate(mDate[0], mDate[1], mDate[2], mDate[3], mDate[4], mDate[5]);
        CLog.i(TAG, "日期1: " + processDate);
        DeviceListDaoOperation.getInstance().updateReceiveDataStr(DeviceManager.m_DeviceBean.mMacAddr, processDate, bs.b);
    }

    public void LogI(String msg) {
        if (Constants.mTestFlag) {
            CLog.i(TAG, msg);
        }
    }

    public void onDestroy() {
        LogI("BluetoothLeDeviceService-bc01call onDestroy method");
        super.onDestroy();
    }

    public void onEvent(Message message) {
    }
}
