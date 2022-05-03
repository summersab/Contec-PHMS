package com.contec.phms.device.cms50d;

import android.os.Message;
import com.alibaba.fastjson.asm.Opcodes;
import com.contec.cms50dj_jar.DeviceData50DJ_Jar;
import com.contec.cms50dj_jar.DeviceDataPedometerJar;
import com.contec.cms50dj_jar.MinData;
import com.contec.cms_ble_50d_jar.DeviceCommand;
import com.contec.cms_ble_50d_jar.DevicePackManager;
import com.contec.phms.App_phms;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.message.MessageManagerMain;
import com.contec.phms.manager.message.OrderList;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.util.CLog;
import com.contec.phms.util.FileOperation;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.PhmsSharedPreferences;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import u.aly.bs;

public class BluetoothLeDeviceService extends com.contec.phms.device.template.BluetoothLeDeviceService {
    private static final String TAG = BluetoothLeDeviceService.class.getSimpleName();
    public int dataCount = 0;
    public DeviceDataPedometerMin mDataPedometerMin;
    public DeviceData mDeviceData;
    public DeviceDataPedometerDay mDeviceDataPedometerDay;
    private DevicePackManager mDevicePackManager;
    private LoginUserDao mloginUserInfo;

    public void onCreate() {
        super.onCreate();
        LogI("创建服务---CMS50DBLE服务类");
        new DatasContainer();
        this.mloginUserInfo = PageUtil.getLoginUserInfo();
        this.mDevicePackManager = new DevicePackManager();
        this.mDeviceData = new DeviceData();
        this.mDeviceDataPedometerDay = new DeviceDataPedometerDay();
        this.mDataPedometerMin = new DeviceDataPedometerMin();
    }

    public void bleServiceNofityOpen() {
        sendData(DeviceCommand.getCompareVesion());
        MessageManagerMain.msg_ui_receive_dataing();
        LogI("设备连接成功，开始发送第一条命令-索要设备号");
    }

    public void arrangeMessage(byte[] buf, int length) {
        int sportdays;
        CLog.i(TAG, "代码执行到cms50D Ble设备的arrangeMessage method中");
        if (buf != null && buf.length > 0) {
            switch (this.mDevicePackManager.arrangeMessage(buf, length)) {
                case -112:
                    LogI("------设置目标卡路里【成功】,查看单次血氧脉率是否有数据--\n");
                    sendData(DeviceCommand.GET_DATA_SIZE(0));
                    return;
                case -111:
                    LogI("------设置目标卡路里[失败]\n结束------");
                    return;
                case 16:
                    LogI("------校时【成功】,开始设置计步时段------\n");
                    int _stepStart = 0;
                    int _stepEnd = 0;
                    if (this.mloginUserInfo.mAmactivity != null && !this.mloginUserInfo.mAmactivity.equalsIgnoreCase(bs.b)) {
                        _stepStart = Integer.parseInt(this.mloginUserInfo.mAmactivity);
                    }
                    if (this.mloginUserInfo.mPmactivity != null && !this.mloginUserInfo.mPmactivity.equalsIgnoreCase(bs.b)) {
                        _stepEnd = Integer.parseInt(this.mloginUserInfo.mPmactivity);
                    }
                    if (_stepStart == 0 && _stepEnd == 0) {
                        _stepStart = 8;
                        _stepEnd = 20;
                    }
                    sendData(DeviceCommand.SET_STEP_TIME(_stepStart, _stepEnd));
                    return;
                case 17:
                    LogI("校时失败");
                    return;
                case 32:
                    LogI("------计步时段设置【成功】,开始设置体重参数------\n");
                    if (this.mloginUserInfo == null || this.mloginUserInfo.mWeight == null || this.mloginUserInfo.mWeight.equals(bs.b)) {
                        sendData(DeviceCommand.SET_WEIGHT(50.0d));
                        return;
                    } else {
                        sendData(DeviceCommand.SET_WEIGHT(Double.parseDouble(this.mloginUserInfo.mWeight)));
                        return;
                    }
                case Opcodes.FALOAD:
                    LogI("------体重参数设置【成功】,开始设置身高参数------\n");
                    if (this.mloginUserInfo == null || this.mloginUserInfo.mHeight == null || this.mloginUserInfo.mHeight.equals(bs.b)) {
                        sendData(DeviceCommand.SET_HEIGHT(160.0d));
                        return;
                    } else {
                        sendData(DeviceCommand.SET_HEIGHT(Double.parseDouble(this.mloginUserInfo.mHeight)));
                        return;
                    }
                case 49:
                    LogI("体重参数设置失败");
                    return;
                case Opcodes.AALOAD:
                    LogI("------身高参数设置【成功】,开始设置目标卡路里------\n");
                    int targetCalorie = 1000;
                    if (this.mloginUserInfo.mSportTargetCal != null && !this.mloginUserInfo.mSportTargetCal.equals(bs.b) && (targetCalorie = Integer.parseInt(this.mloginUserInfo.mSportTargetCal)) == 0) {
                        targetCalorie = 1000;
                    }
                    int _sensitivity = PhmsSharedPreferences.getInstance(App_phms.getInstance().getBaseContext()).getInt("Sensitivity" + this.mloginUserInfo.mUID, 2);
                    try {
                        sportdays = Integer.parseInt(this.mloginUserInfo.mSportdays);
                    } catch (Exception e) {
                        sportdays = 50;
                    }
                    sendData(DeviceCommand.SET_CALORIE(targetCalorie, sportdays, _sensitivity));
                    return;
                case 64:
                    LogI("------单次血氧【有数据】------\n");
                    this.dataCount++;
                    sendData(DeviceCommand.GET_SPO2_POINT(0));
                    return;
                case 65:
                    LogI("------单次血氧【无数据】,查看以天为单位的计步器数据---\n");
                    sendData(DeviceCommand.GET_DATA_SIZE(1));
                    return;
                case OrderList.DM_NEXT_DEVICE_OR_POLLING /*66*/:
                    LogI("------单次血氧单次数据接收中...");
                    sendData(DeviceCommand.GET_SPO2_POINT(1));
                    return;
                case 67:
                    LogI("血氧单次数据接收完毕，请求以天为单位的计步器数据");
                    sendData(DeviceCommand.GET_SPO2_POINT(127));
                    save_sp02_data();
                    delayTime(100);
                    sendData(DeviceCommand.GET_DATA_SIZE(1));
                    return;
                case 69:
                    LogE("---版本号获取成功---");
                    sendData(DeviceCommand.SET_TIME());
                    return;
                case 80:
                    LogI("------全天总步数【有数据】----");
                    this.dataCount++;
                    sendData(DeviceCommand.GET_STEP_DAY(0));
                    return;
                case 81:
                    LogI("------全天总步数无数据，请求全天5分钟为单位的计步器数据------");
                    sendData(DeviceCommand.GET_DATA_SIZE(2));
                    return;
                case 82:
                    LogI("------全天总步数有数据接收中...");
                    sendData(DeviceCommand.GET_STEP_DAY(1));
                    return;
                case 83:
                    LogI("------全天总步数数据接收完毕，请求全天5分钟为单位的计步器数据------");
                    sendData(DeviceCommand.GET_STEP_DAY(127));
                    save_pedometer_day();
                    delayTime(200);
                    sendData(DeviceCommand.GET_DATA_SIZE(2));
                    return;
                case Opcodes.IADD:
                    LogI("------全天5分钟数据有------");
                    this.dataCount++;
                    sendData(DeviceCommand.GET_STEP_BASE_MINDATA(1));
                    return;
                case Opcodes.LADD:
                    LogI("------全天5分钟数据无，结束.\n\n\n");
                    if (this.dataCount > 0) {
                        toUpload();
                        return;
                    } else {
                        noNewData();
                        return;
                    }
                case 98:
                    LogI("------单个5分钟接收中...");
                    sendData(DeviceCommand.GET_STEP_MIN_DATA(1));
                    return;
                case 99:
                    LogI("------单个5分钟接收完毕------");
                    sendData(DeviceCommand.GET_STEP_BASE_MINDATA(1));
                    return;
                case 100:
                    LogI("------全天5分钟基本数据回复------");
                    sendData(DeviceCommand.GET_STEP_MIN_DATA(0));
                    return;
                case 101:
                    LogI("------5分钟全部数据接收完毕.\n");
                    delayTime(200);
                    sendData(DeviceCommand.GET_STEP_MIN_DATA(127));
                    save_pedometer_min();
                    delayTime(200);
                    toUpload();
                    return;
                default:
                    return;
            }
        }
    }

    private void save_sp02_data() {
        ArrayList<Object> _dataAll = this.mDevicePackManager.getmSpo2h().getmSpo2PointList();
        int _size = _dataAll.size();
        if (_size > 30) {
            int num = 0;
            List<byte[]> _list = new ArrayList<>();
            DeviceData50DJ_Jar _dj = new DeviceData50DJ_Jar();
            for (int i = 0; i < _size; i++) {
                num++;
                byte[] _datadate = (byte[]) _dataAll.get(i);
                String _dateRecord = PageUtil.process_Date((_datadate[0] & 255) + 2000, _datadate[1] & 255, _datadate[2] & 255, _datadate[3] & 255, _datadate[4] & 255, _datadate[5] & 255);
                FileOperation.writeToSDCard("接收过来的数据时间：" + _dateRecord, "Cms50D_BLE_BT");
                CLog.dT(TAG, "判断时间是否合法：" + _dateRecord);
                byte[] provalue = PageUtil.compareDate(_dateRecord);
                if (provalue != null) {
                    _datadate[0] = provalue[0];
                    _datadate[1] = provalue[1];
                    _datadate[2] = provalue[2];
                    _datadate[3] = provalue[3];
                    _datadate[4] = provalue[4];
                    _datadate[5] = provalue[5];
                    FileOperation.writeToSDCard("时间不合法骄正之后的数据时间：" + PageUtil.getDateFormByte(_datadate[0], _datadate[1], _datadate[2], _datadate[3], _datadate[4], _datadate[5]), "Cms50D_BLE_BT");
                }
                _list.add((byte[]) _dataAll.get(i));
                if (num % 29 == 0) {
                    _dj.setmSp02DataList(_list);
                    byte[] date = (byte[]) _dataAll.get(i);
                    DeviceData deviceData = this.mDeviceData;
                    deviceData.mDate = new int[]{(date[0] + 2000) - 1990, date[1], date[2], date[3], date[4], date[5]};
                    this.mDeviceData.mSaveDate = new Date((date[0] + 2000) - 1990, date[1], date[2], date[3], date[4]);
                    this.mDeviceData.mDataList.add(_dj);
                    this.mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                    DatasContainer.mDeviceDatas.add(this.mDeviceData);
                    this.mDeviceData.mDataList.clear();
                    _list.clear();
                } else if (i == _size - 1) {
                    _dj.setmSp02DataList(_list);
                    byte[] date2 = (byte[]) _dataAll.get(i);
                    DeviceData deviceData2 = this.mDeviceData;
                    deviceData2.mDate = new int[]{(date2[0] + 2000) - 1990, date2[1], date2[2], date2[3], date2[4], date2[5]};
                    this.mDeviceData.mSaveDate = new Date((date2[0] + 2000) - 1990, date2[1], date2[2], date2[3], date2[4]);
                    this.mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                    this.mDeviceData.mDataList.add(_dj);
                    DatasContainer.mDeviceDatas.add(this.mDeviceData);
                    this.mDeviceData.mDataList.clear();
                    _list.clear();
                }
            }
            return;
        }
        List<byte[]> _list2 = new ArrayList<>();
        DeviceData50DJ_Jar _dj2 = new DeviceData50DJ_Jar();
        for (int i2 = 0; i2 < _size; i2++) {
            byte[] _datadate2 = (byte[]) _dataAll.get(i2);
            String _dateRecord2 = PageUtil.process_Date((_datadate2[0] & 255) + 2000, _datadate2[1] & 255, _datadate2[2] & 255, _datadate2[3] & 255, _datadate2[4] & 255, _datadate2[5] & 255);
            FileOperation.writeToSDCard("接收过来的数据时间：" + _dateRecord2, "Cms50D_BLE_BT");
            CLog.dT(TAG, "判断时间是否合法：" + _dateRecord2);
            byte[] provalue2 = PageUtil.compareDate(_dateRecord2);
            if (provalue2 != null) {
                _datadate2[0] = provalue2[0];
                _datadate2[1] = provalue2[1];
                _datadate2[2] = provalue2[2];
                _datadate2[3] = provalue2[3];
                _datadate2[4] = provalue2[4];
                _datadate2[5] = provalue2[5];
                FileOperation.writeToSDCard("时间不合法骄正之后的数据时间：" + PageUtil.getDateFormByte(_datadate2[0], _datadate2[1], _datadate2[2], _datadate2[3], _datadate2[4], _datadate2[5]), "Cms50D_BLE_BT");
            }
            _list2.add((byte[]) _dataAll.get(i2));
            _dj2.setmSp02DataList(_list2);
        }
        byte[] date3 = (byte[]) _dataAll.get(0);
        DeviceData deviceData3 = this.mDeviceData;
        deviceData3.mDate = new int[]{(date3[0] + 2000) - 1990, date3[1], date3[2], date3[3], date3[4], date3[5]};
        this.mDeviceData.mSaveDate = new Date((date3[0] + 2000) - 1990, date3[1], date3[2], date3[3], date3[4]);
        this.mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
        this.mDeviceData.mDataList.add(_dj2);
        DatasContainer.mDeviceDatas.add(this.mDeviceData);
    }

    private void save_pedometer_day() {
        int size = this.mDevicePackManager.getmPedometerJar().getmPedometerDataDayList().size();
        if (size > 15) {
            int num = 0;
            List<byte[]> _list = new ArrayList<>();
            DeviceDataPedometerJar _djPedometerDay = new DeviceDataPedometerJar();
            for (int i = 0; i < size; i++) {
                byte[] _datadate = (byte[]) this.mDevicePackManager.getmPedometerJar().getmPedometerDataDayList().get(i);
                String _strDataDate = PageUtil.getDateFormByte(_datadate[0], _datadate[1], _datadate[2], (byte) 0, (byte) 0, (byte) 0);
                CLog.dT(TAG, "判断时间是否合法：" + _strDataDate);
                byte[] provalue = PageUtil.compareDate(_strDataDate);
                if (provalue != null) {
                    _datadate[0] = provalue[0];
                    _datadate[1] = provalue[1];
                    _datadate[2] = provalue[2];
                }
                _list.add((byte[]) this.mDevicePackManager.getmPedometerJar().getmPedometerDataDayList().get(i));
                num++;
                if (num % 14 == 0) {
                    _djPedometerDay.setmPedometerDataDayList(_list);
                    byte[] date = _djPedometerDay.getmPedometerDataDayList().get(0);
                    this.mDeviceDataPedometerDay.mSaveDate = new Date((date[0] + 2000) - 1990, date[1], date[2], 8, 8);
                    DeviceData deviceData = this.mDeviceData;
                    deviceData.mDate = new int[]{(date[0] + 2000) - 1990, date[1], date[2], 8, 8, 8};
                    FileOperation.writeToSDCard("计步器以天为单位数据文件名：：" + (date[0] + 2000) + "-" + date[1] + "-" + date[2], "Cms50D_BT_Pedometer");
                    this.mDeviceDataPedometerDay.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                    this.mDeviceDataPedometerDay.mDataList.add(_djPedometerDay);
                    DatasContainer.mDeviceDatas.add(this.mDeviceDataPedometerDay);
                    this.mDeviceData.mDataList.clear();
                    _list.clear();
                } else if (i == size - 1) {
                    _djPedometerDay.setmPedometerDataDayList(_list);
                    byte[] date2 = _djPedometerDay.getmPedometerDataDayList().get(0);
                    this.mDeviceDataPedometerDay.mSaveDate = new Date((date2[0] + 2000) - 1990, date2[1], date2[2], 8, 8);
                    DeviceData deviceData2 = this.mDeviceData;
                    deviceData2.mDate = new int[]{(date2[0] + 2000) - 1990, date2[1], date2[2], 8, 8, 8};
                    FileOperation.writeToSDCard("计步器以天为单位数据文件名：：" + ((date2[0] + 2000) - 1990) + "-" + date2[1] + "-" + date2[2], "Cms50D_BT_Pedometer");
                    this.mDeviceDataPedometerDay.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                    this.mDeviceDataPedometerDay.mDataList.add(_djPedometerDay);
                    _list.clear();
                    DatasContainer.mDeviceDatas.add(this.mDeviceDataPedometerDay);
                }
            }
            return;
        }
        DeviceDataPedometerJar _djPedometerDay2 = new DeviceDataPedometerJar();
        List<byte[]> _list2 = new ArrayList<>();
        for (int i2 = 0; i2 < size; i2++) {
            byte[] _datadate2 = (byte[]) this.mDevicePackManager.getmPedometerJar().getmPedometerDataDayList().get(i2);
            String _strDataDate2 = PageUtil.getDateFormByte(_datadate2[0], _datadate2[1], _datadate2[2], (byte) 0, (byte) 0, (byte) 0);
            CLog.dT(TAG, "判断时间是否合法：" + _strDataDate2);
            byte[] provalue2 = PageUtil.compareDate(_strDataDate2);
            if (provalue2 != null) {
                _datadate2[0] = provalue2[0];
                _datadate2[1] = provalue2[1];
                _datadate2[2] = provalue2[2];
            }
            _list2.add((byte[]) this.mDevicePackManager.getmPedometerJar().getmPedometerDataDayList().get(i2));
        }
        _djPedometerDay2.setmPedometerDataDayList(_list2);
        byte[] date3 = (byte[]) this.mDevicePackManager.getmPedometerJar().getmPedometerDataDayList().get(0);
        DeviceData deviceData3 = this.mDeviceData;
        deviceData3.mDate = new int[]{(date3[0] + 2000) - 1990, date3[1], date3[2], 8, 8, 8};
        this.mDeviceDataPedometerDay.mSaveDate = new Date((date3[0] + 2000) - 1990, date3[1], date3[2], 8, 8);
        CLog.d(TAG, "  mDeviceDataPedometerDay.mSaveDate:" + this.mDeviceDataPedometerDay.mSaveDate.toLocaleString());
        this.mDeviceDataPedometerDay.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
        this.mDeviceDataPedometerDay.mDataList.add(_djPedometerDay2);
        DatasContainer.mDeviceDatas.add(this.mDeviceDataPedometerDay);
    }

    private void save_pedometer_min() {
        int _size = this.mDevicePackManager.getmPedometerJar().getmPedometerDataMinList().size();
        if (_size > 29) {
            int num = 0;
            List<MinData> _list = new ArrayList<>();
            DeviceDataPedometerJar _pedometerDatamin_temp = new DeviceDataPedometerJar();
            for (int i = 0; i < _size; i++) {
                num++;
                if (num % 29 == 0) {
                    _pedometerDatamin_temp.setmPedometerDataMinList(_list);
                    _list.clear();
                    byte[] date_min = this.mDevicePackManager.getmPedometerJar().getmPedometerDataMinList().get(0).getmStartDate();
                    this.mDataPedometerMin.mSaveDate = new Date((date_min[0] + 2000) - 1990, date_min[1], date_min[2], date_min[3], 0);
                    this.mDataPedometerMin.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                    this.mDataPedometerMin.mDataList.add(_pedometerDatamin_temp);
                    DatasContainer.mDeviceDatas.add(this.mDataPedometerMin);
                    this.mDeviceData.mDataList.clear();
                } else {
                    com.contec.cms_ble_50d_jar.MinData minData = this.mDevicePackManager.getmPedometerJar().getmPedometerDataMinList().get(i);
                    byte[] _startDate = minData.getmStartDate();
                    List<byte[]> _mMinDataList = minData.getmMinDataList();
                    MinData minData2 = new MinData();
                    minData2.mStartDate = _startDate;
                    minData2.mMinDataList.clear();
                    minData2.mMinDataList.addAll(_mMinDataList);
                    _list.add(minData2);
                    if (i == _size - 1) {
                        _pedometerDatamin_temp.setmPedometerDataMinList(_list);
                        _list.clear();
                        byte[] date_min2 = this.mDevicePackManager.getmPedometerJar().getmPedometerDataMinList().get(0).getmStartDate();
                        this.mDataPedometerMin.mSaveDate = new Date((date_min2[0] + 2000) - 1990, date_min2[1], date_min2[2], date_min2[3], 0);
                        this.mDataPedometerMin.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                        this.mDataPedometerMin.mDataList.add(_pedometerDatamin_temp);
                        DatasContainer.mDeviceDatas.add(this.mDataPedometerMin);
                    }
                }
            }
            return;
        }
        DeviceDataPedometerJar _pedometerDatamin_temp2 = new DeviceDataPedometerJar();
        List<MinData> _list2 = new ArrayList<>();
        for (int i2 = 0; i2 < _size; i2++) {
            com.contec.cms_ble_50d_jar.MinData minData3 = this.mDevicePackManager.getmPedometerJar().getmPedometerDataMinList().get(i2);
            byte[] _startDate2 = minData3.getmStartDate();
            List<byte[]> _mMinDataList2 = minData3.getmMinDataList();
            MinData minData22 = new MinData();
            minData22.mStartDate = _startDate2;
            minData22.mMinDataList.clear();
            minData22.mMinDataList.addAll(_mMinDataList2);
            _list2.add(minData22);
        }
        _pedometerDatamin_temp2.setmPedometerDataMinList(_list2);
        byte[] date_min3 = this.mDevicePackManager.getmPedometerJar().getmPedometerDataMinList().get(0).getmStartDate();
        this.mDataPedometerMin.mSaveDate = new Date((date_min3[0] + 2000) - 1990, date_min3[1], date_min3[2], date_min3[3], 0);
        this.mDataPedometerMin.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
        this.mDataPedometerMin.mDataList.add(_pedometerDatamin_temp2);
        DatasContainer.mDeviceDatas.add(this.mDataPedometerMin);
    }

    public void onEvent(Message message) {
    }

    private void testPedometerDay() {
        int size = this.mDevicePackManager.getmPedometerJar().getmPedometerDataDayList().size();
        if (size > 0) {
            ArrayList<Object> pedometerDataDayList = this.mDevicePackManager.getmPedometerJar().getmPedometerDataDayList();
            for (int i = 0; i < size; i++) {
                byte[] _temp = (byte[]) pedometerDataDayList.get(i);
                byte year = _temp[0];
                byte month = _temp[1];
                byte day = _temp[2];
                StringBuilder builder = new StringBuilder();
                builder.append("20" + year + "-");
                if (month < 10) {
                    builder.append("0" + month + "-");
                } else {
                    builder.append(String.valueOf(month) + "-");
                }
                if (day < 10) {
                    builder.append("0" + day);
                } else {
                    builder.append(day);
                }
                String recordeDate = builder.toString();
                int _steps = ((_temp[6] & 255) << 7) | (_temp[5] & 255);
                int _targ = ((_temp[8] & 255) << 7) | (_temp[7] & 255);
                int _cal = ((_temp[4] & 255) << 7) | (_temp[3] & 255);
                if (_cal != 32767) {
                    if (_steps != 32767) {
                        if (_targ != 32767) {
                            CLog.e("jxx", "cms50d pedometer Day 要上传的数据__日期：" + recordeDate + "__总步数:" + _steps + "__目标卡路里:" + _targ + "__总卡路里:" + _cal);
                        }
                    }
                }
            }
        }
    }

    private void testPedometerMin() {
    }

    private void delayTime(int time) {
        try {
            Thread.sleep((long) time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void LogI(String msg) {
        CLog.i(TAG, msg);
    }
}
