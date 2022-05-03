package com.contec.phms.device.cms50ew;

import android.os.Message;
import com.contec.cms50ew.code.DeviceCommand;
import com.contec.cms50ew.code.DeviceDataIW;
import com.contec.cms50ew.code.DevicePackManager;
import com.contec.phms.device.cms50k.DeviceDataSpo2Point;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.message.MessageManagerMain;
import com.contec.phms.manager.message.OrderList;
import com.contec.phms.db.DeviceListDaoOperation;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.util.CLog;
import com.contec.phms.util.JFileOperation;
import com.contec.phms.util.JPageUtil;
import com.contec.phms.util.PageUtil;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class BluetoothLeDeviceService extends com.contec.phms.device.template.BluetoothLeDeviceService {
    private static final String TAG = "BluetoothLeDeviceService_cms50ew";
    boolean IsHaveData = false;
    private boolean isHaveData = false;
    DeviceDataSpo2Point mSpo2Point;
    private byte[] mUpdateDatas;
    DevicePackManager m_DevicePackManager;
    private LoginUserDao mloginUserInfo;
    private String updateBinPath;

    public void onCreate() {
        super.onCreate();
        LogI("创建服务---CMS50ewBLE服务类");
        new DatasContainer();
        this.m_DevicePackManager = new DevicePackManager();
        this.mloginUserInfo = PageUtil.getLoginUserInfo();
    }

    public void bleServiceNofityOpen() {
        sendData(DeviceCommand.SET_TIME());
        CLog.e(TAG, "对时操作，发送对时命令");
    }

    public void arrangeMessage(byte[] buf, int length) {
        CLog.i(TAG, "代码执行到cms50E Ble设备的arrangeMessage method中");
        if (buf != null && buf.length > 0) {
            switch (this.m_DevicePackManager.arrangeMessage(buf, length)) {
                case 16:
                    CLog.e(TAG, "校时成功,设置数据存储空间");
                    sendData(DeviceCommand.GET_DATA_SIZE(0));
                    return;
                case 64:
                    CLog.i(TAG, "单次血氧有数据 ,获取血氧数据");
                    MessageManagerMain.msg_ui_receive_dataing();
                    sendData(DeviceCommand.GET_SPO2_POINT(0));
                    this.IsHaveData = true;
                    return;
                case 65:
                    CLog.i(TAG, "单次血氧无数据");
                    noNewData();
                    return;
                case OrderList.DM_NEXT_DEVICE_OR_POLLING /*66*/:
                    CLog.i(TAG, "单次血氧数据接受中");
                    sendData(DeviceCommand.GET_SPO2_POINT(1));
                    return;
                case 67:
                    CLog.i(TAG, "血氧单次数据接收完毕");
                    delayTime(300);
                    LogI("发送删除命令");
                    sendData(DeviceCommand.GET_SPO2_POINT(127));
                    LogI("发送删除命令结束");
                    save_DeviceData_new();
                    return;
                default:
                    return;
            }
        }
    }

    private void save_DeviceData_new() {
        ArrayList<DeviceDataIW> mDeviceDataIWs = this.m_DevicePackManager.mDeviceDataIWs;
        if (mDeviceDataIWs != null) {
            int size = mDeviceDataIWs.size();
            LogI("Data size: " + size);
            if (size > 0) {
                DeviceDataIW mData = mDeviceDataIWs.get(0);
                DeviceData mDeviceData = new DeviceData(new byte[4]);
                mDeviceData.mDataList.clear();
                mDeviceData.mDate = new int[6];
                mDeviceData.mDate[0] = mData._Year;
                mDeviceData.mDate[1] = mData._Month;
                mDeviceData.mDate[2] = mData._Day;
                mDeviceData.mDate[3] = mData._Hour;
                mDeviceData.mDate[4] = mData._Min;
                mDeviceData.mDate[5] = mData._Sec;
                LogI("日期1: " + mDeviceData.mDate[0] + "-" + mDeviceData.mDate[1] + "-" + mDeviceData.mDate[2] + "-" + mDeviceData.mDate[3] + "-" + mDeviceData.mDate[4] + "-" + mDeviceData.mDate[5]);
                mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                for (int i = 0; i < size; i++) {
                    check_date(mDeviceDataIWs.get(i), i);
                    DeviceDataIW deviceDataIW = mDeviceDataIWs.get(i);
                    byte[] datas = new byte[9];
                    datas[0] = (byte) deviceDataIW._Year;
                    datas[1] = (byte) deviceDataIW._Month;
                    datas[2] = (byte) deviceDataIW._Day;
                    datas[3] = (byte) deviceDataIW._Hour;
                    datas[4] = (byte) deviceDataIW._Min;
                    datas[5] = (byte) deviceDataIW._Sec;
                    datas[6] = deviceDataIW._value_ew[6];
                    datas[7] = deviceDataIW._value_ew[7];
                    mDeviceData.mDataList.add(datas);
                    if ((i + 1) % 15 == 0 || (i == size - 1 && (i + 1) % 15 != 0)) {
                        mDeviceData.mDate = new int[6];
                        mDeviceData.mDate[0] = deviceDataIW._Year;
                        mDeviceData.mDate[1] = deviceDataIW._Month;
                        mDeviceData.mDate[2] = deviceDataIW._Day;
                        mDeviceData.mDate[3] = deviceDataIW._Hour;
                        mDeviceData.mDate[4] = deviceDataIW._Min;
                        mDeviceData.mDate[5] = deviceDataIW._Sec;
                        addData(mDeviceData);
                        mDeviceData.mDataList.clear();
                    }
                    if (i == size - 1) {
                        MessageManagerMain.msg_ui_receive_data_success();
                        toUpload();
                    }
                }
                return;
            }
            noNewData();
        }
    }

    private void save_DeviceData_old() {
        DeviceDataIW dataIW = this.m_DevicePackManager.mDeviceDataIW;
        if (dataIW != null) {
            int size = dataIW.valueList.size();
            LogI("Data size: " + size);
            if (size > 0) {
                DeviceDataIW mData = dataIW;
                DeviceData mDeviceData = new DeviceData(new byte[4]);
                mDeviceData.mDataList.clear();
                mDeviceData.mDate = new int[6];
                mDeviceData.mDate[0] = mData._Year;
                mDeviceData.mDate[1] = mData._Month;
                mDeviceData.mDate[2] = mData._Day;
                mDeviceData.mDate[3] = mData._Hour;
                mDeviceData.mDate[4] = mData._Min;
                mDeviceData.mDate[5] = mData._Sec;
                LogI("日期1: " + mDeviceData.mDate[0] + "-" + mDeviceData.mDate[1] + "-" + mDeviceData.mDate[2] + "-" + mDeviceData.mDate[3] + "-" + mDeviceData.mDate[4] + "-" + mDeviceData.mDate[5]);
                mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                for (int i = 0; i < size; i++) {
                    check_date(mData, i);
                    mDeviceData.mDataList.add(mData.valueList.get(i));
                    if (mDeviceData.mDataList.size() == 30) {
                        mDeviceData.mDate = new int[6];
                        mDeviceData.mDate[0] = mData.valueList.get(i)[0];
                        mDeviceData.mDate[1] = mData.valueList.get(i)[1];
                        mDeviceData.mDate[2] = mData.valueList.get(i)[2];
                        mDeviceData.mDate[3] = mData.valueList.get(i)[3];
                        mDeviceData.mDate[4] = mData.valueList.get(i)[4];
                        mDeviceData.mDate[5] = mData.valueList.get(i)[5];
                        addData(mDeviceData);
                        mDeviceData.mDataList.clear();
                        mDeviceData.mDate = null;
                    }
                    byte[] _lastValue = mData.valueList.get(mData.valueList.size() - 1);
                    DeviceListDaoOperation.getInstance().updateReceiveDataStr(DeviceManager.m_DeviceBean.mMacAddr, JPageUtil.processDate((_lastValue[0] & 255) + 2000, _lastValue[1] & 255, _lastValue[2] & 255, _lastValue[3] & 255, _lastValue[4] & 255, _lastValue[5] & 255), String.valueOf(_lastValue[6] & 255) + ";" + (_lastValue[7] & 255));
                    MessageManagerMain.msg_ui_receive_data_success();
                }
                addData(mDeviceData);
                toUpload();
                return;
            }
            noNewData();
        }
    }

    private void check_date(DeviceDataIW mData, int i) {
        String _dateRecord = JPageUtil.process_Date((mData._Year & 255) + 2000, mData._Month & 255, mData._Day & 255, mData._Hour & 255, mData._Min & 255, mData._Sec & 255);
        LogI("date2: " + _dateRecord + " blood oxygen: " + mData._value_ew[6] + " pulse rate: " + mData._value_ew[7]);
        JFileOperation.writeToSDCard(String.valueOf(getcureentbytetime()) + " data received: " + _dateRecord + "  " + mData._value_ew[6], "Cms50E_BLE");
        byte[] provalue = JPageUtil.compareDate(_dateRecord);
        if (provalue != null) {
            mData.valueList.get(i)[0] = provalue[0];
            mData.valueList.get(i)[1] = provalue[1];
            mData.valueList.get(i)[2] = provalue[2];
            mData.valueList.get(i)[3] = provalue[3];
            mData.valueList.get(i)[4] = provalue[4];
            mData.valueList.get(i)[5] = provalue[5];
            JFileOperation.writeToSDCard(String.valueOf(getcureentbytetime()) + " correct time: " + JPageUtil.process_Date((provalue[0] & 255) + 2000, provalue[1] & 255, provalue[2] & 255, provalue[3] & 255, provalue[4] & 255, provalue[5] & 255) + "  " + mData._value_ew[6], "Cms50E_BLE");
            LogI("代码执行到provalue里面");
            return;
        }
        JFileOperation.writeToSDCard(String.valueOf(getcureentbytetime()) + "时间正确，未对时间进行校正操作", "Cms50E_BLE");
        LogI("代码执行到provalue里面-NO");
    }

    protected void noNewData() {
        super.noNewData();
    }

    protected void toUpload() {
        LogI("开始上传");
        super.toUpload();
        this.isHaveData = false;
    }

    private void delayTime(int time) {
        try {
            Thread.sleep((long) time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addData(DeviceData deviceData) {
        DeviceData _data = (DeviceData) deviceData;
        DatasContainer.mDeviceDatas.add(_data);
        update_db(_data);
    }

    private void update_db(DeviceData _data) {
        byte[] _lastValue = (byte[]) _data.mDataList.get(_data.mDataList.size() - 1);
        DeviceListDaoOperation.getInstance().updateReceiveDataStr(DeviceManager.m_DeviceBean.mMacAddr, JPageUtil.processDate((_lastValue[0] & 255) + 2000, _lastValue[1] & 255, _lastValue[2] & 255, _lastValue[3] & 255, _lastValue[4] & 255, _lastValue[5] & 255), String.valueOf(_lastValue[6] & 255) + ";" + (_lastValue[7] & 255));
    }

    public void onDestroy() {
        super.onDestroy();
        LogI("call BluetoothLeDeviceService_cms50ewonDestroy method");
    }

    private String getcureentbytetime() {
        return new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss:SSS ").format(new Date(System.currentTimeMillis()));
    }

    public void LogI(String msg) {
        CLog.i(TAG, msg);
    }

    public void addDataECG(DeviceData deviceData) {
        com.contec.phms.device.pm10.DeviceData _data = (com.contec.phms.device.pm10.DeviceData) deviceData;
        if (_data.TrendData == null) {
            CLog.i(TAG, "No New Datas");
            return;
        }
        CLog.d(TAG, "存储数据***********");
        _data.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
        DatasContainer.mDeviceDatas.add(deviceData);
    }

    public void onEvent(Message message) {
    }
}
