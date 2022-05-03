package com.contec.phms.device.cms50d;

import android.util.Log;
import com.contec.cms50dj_jar.DeviceCommand;
import com.contec.cms50dj_jar.DeviceData50DJ_Jar;
import com.contec.cms50dj_jar.DeviceDataPedometerJar;
import com.contec.cms50dj_jar.DevicePackManager;
import com.contec.cms50dj_jar.MinData;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.device.template.SendCommand;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.util.CLog;
import com.contec.phms.util.FileOperation;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.PhmsSharedPreferences;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import u.aly.bs;

public class ReceiveThread extends com.contec.phms.device.template.ReceiveThread {
    private String TAG;
    private int mCountSetTime;
    public DeviceDataPedometerMin mDataPedometerMin;
    public DeviceData mDeviceData;
    public DeviceDataPedometerDay mDeviceDataPedometerDay;
    private DevicePackManager m_DevicePackManager;

    public ReceiveThread(DeviceService mDeviceService) {
        super(mDeviceService);
        this.m_DevicePackManager = new DevicePackManager();
        this.TAG = "com.contec.cms50dj_jar.DevicePackManager_ReceiveThread";
        this.mCountSetTime = 0;
        this.mDeviceData = new DeviceData();
        this.mDeviceDataPedometerDay = new DeviceDataPedometerDay();
        this.mDataPedometerMin = new DeviceDataPedometerMin();
        this.mCountSetTime = 0;
    }

    public void arrangeMessage(byte[] buf, int length) {
        switch (this.m_DevicePackManager.arrangeMessage(buf, length)) {
            case 1:
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(100);
                            ReceiveThread.this.mCountSetTime = 0;
                            SendCommand.send(DeviceCommand.correctionDateTime());
                            Log.i(ReceiveThread.this.TAG, "得到设备号 发送校时命令");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            case 2:
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(100);
                            LoginUserDao _log = PageUtil.getLoginUserInfo();
                            int _calTarget = 1000;
                            if (_log.mSportTargetCal != null && !_log.mSportTargetCal.equals(bs.b)) {
                                _calTarget = Integer.parseInt(_log.mSportTargetCal);
                            }
                            int _sensitivity = PhmsSharedPreferences.getInstance(App_phms.getInstance().getBaseContext()).getInt("Sensitivity" + _log.mUID, 2);
                            SendCommand.send(DeviceCommand.setPedometerInfo(_log.mHeight, _log.mWeight, 0, 24, _calTarget, 1, _sensitivity));
                            Log.i(ReceiveThread.this.TAG, "对时成功  设置计步器信息" + _sensitivity);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            case 3:
                this.mCountSetTime++;
                if (this.mCountSetTime == 3) {
                    Log.i(this.TAG, "对时间3次失败*******");
                    DeviceManager.mDeviceBeanList.mState = 5;
                    DeviceManager.m_DeviceBean.mState = 5;
                    DeviceService.mReceiveFinished = true;
                    this.mCountSetTime = 0;
                    return;
                } else if (this.mCountSetTime < 3) {
                    new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(100);
                                Log.i(ReceiveThread.this.TAG, "对时间" + ReceiveThread.this.mCountSetTime + " 次失败*******从新发起对时");
                                SendCommand.send(DeviceCommand.correctionDateTime());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    return;
                } else {
                    return;
                }
            case 4:
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(100);
                            SendCommand.send(DeviceCommand.dayPedometerDataCommand());
                            Log.i(ReceiveThread.this.TAG, "血氧无新数据  请求以天为单位的 计步器数据");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            case 5:
                DeviceManager.m_DeviceBean.mProgress = 20;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                Log.i(this.TAG, " 整个血氧数据接收完成  发送以天为单位请求数据命令");
                saveSpo2Data();
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(100);
                            SendCommand.send(DeviceCommand.dayPedometerDataCommand());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            case 6:
                Log.i(this.TAG, "一包血氧数据接收完毕 ");
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(100);
                            SendCommand.send(DeviceCommand.dataUploadSuccessCommand());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            case 7:
                Log.i(this.TAG, "血氧数据接收失败  请求以天为单位的 计步器数据");
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(100);
                            SendCommand.send(DeviceCommand.dayPedometerDataCommand());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            case 8:
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(500);
                            SendCommand.send(DeviceCommand.getDataFromDevice());
                            Log.i(ReceiveThread.this.TAG, "设置计步器 成功  发送 请求血氧数据命令");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            case 9:
                DeviceManager.mDeviceBeanList.mState = 5;
                DeviceManager.m_DeviceBean.mState = 5;
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(100);
                            SendCommand.send(DeviceCommand.getDataFromDevice());
                            Log.i(ReceiveThread.this.TAG, "设置计步器 失败    发送 请求血氧数据命令 ");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            case 10:
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(100);
                            SendCommand.send(DeviceCommand.dayPedometerDataSuccessCommand());
                            Log.i(ReceiveThread.this.TAG, "以天为单位计步器 数据 一包上传完成  发送上传完成命令");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            case 11:
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(100);
                            SendCommand.send(DeviceCommand.dayPedometerDataCommand());
                            Log.i(ReceiveThread.this.TAG, "以天为单位计步器 数据上一包上传成功  请求下一包数据 ");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            case 12:
                saveDaypedometerData();
                DeviceManager.m_DeviceBean.mProgress = 30;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(100);
                            SendCommand.send(DeviceCommand.minPedometerDataCommand());
                            Log.i(ReceiveThread.this.TAG, "以天为单位计步器 数据  全部   上传成功  请求以分为单位的数据");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            case 13:
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(100);
                            SendCommand.send(DeviceCommand.minPedometerDataCommand());
                            Log.i(ReceiveThread.this.TAG, " 以天为单位计步器 数据上传失败 请求以分为单位的数据");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            case 14:
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(100);
                            SendCommand.send(DeviceCommand.minPedometerDataSuccessCommand());
                            Log.i(ReceiveThread.this.TAG, "以 分 为单位 计步器数据 一包上传完成 发送上传完成命令");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            case 15:
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(100);
                            SendCommand.send(DeviceCommand.minPedometerDataCommand());
                            Log.i(ReceiveThread.this.TAG, "以分为单位计步器 数据 一包上传完成  发送请求下一包的命令");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            case 16:
                savePedometerMindata();
                DeviceManager.mDeviceBeanList.mState = 4;
                DeviceManager.m_DeviceBean.mState = 4;
                DeviceManager.m_DeviceBean.mProgress = 50;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                Log.i(this.TAG, " 以分为单位计步器 数据 全部 上传成功 关闭socket ");
                return;
            case 17:
                Log.i(this.TAG, " 以天为单位 计步器无新数据    请求以分为单位的数据");
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(100);
                            SendCommand.send(DeviceCommand.minPedometerDataCommand());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            case 18:
                DeviceManager.mDeviceBeanList.mState = 10;
                DeviceManager.m_DeviceBean.mState = 10;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                Log.i(this.TAG, " 以分为单位 计步器无新数据   ");
                DeviceService.mReceiveFinished = true;
                return;
            case 19:
                DeviceManager.mDeviceBeanList.mState = 5;
                DeviceManager.m_DeviceBean.mState = 5;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                Log.i(this.TAG, " 以分为单位 计步器数据上传失败   ");
                return;
            case 20:
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(100);
                            Log.i(ReceiveThread.this.TAG, " 请求下一包血氧数据   ");
                            SendCommand.send(DeviceCommand.getDataFromDevice());
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

    private void savePedometerMindata() {
        DeviceDataPedometerJar _pedometerDatamin = this.m_DevicePackManager.getM_DeviceDataPedometers();
        if (_pedometerDatamin.getmPedometerDataMinList().size() > 29) {
            int num = 0;
            List<MinData> _list = new ArrayList<>();
            DeviceDataPedometerJar _pedometerDatamin_temp = new DeviceDataPedometerJar();
            for (int i = 0; i < _pedometerDatamin.getmPedometerDataMinList().size(); i++) {
                num++;
                if (num % 29 == 0) {
                    _pedometerDatamin_temp.setmPedometerDataMinList(_list);
                    _list.clear();
                    byte[] date_min = _pedometerDatamin.getmPedometerDataMinList().get(0).mStartDate;
                    this.mDataPedometerMin.mSaveDate = new Date((date_min[0] + 2000) - 1990, date_min[1], date_min[2], date_min[3], 0);
                    this.mDataPedometerMin.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                    this.mDataPedometerMin.mDataList.add(_pedometerDatamin_temp);
                    DatasContainer.mDeviceDatas.add(this.mDataPedometerMin);
                    this.mDeviceData.mDataList.clear();
                } else {
                    _list.add(_pedometerDatamin.getmPedometerDataMinList().get(i));
                    if (i == _pedometerDatamin.getmPedometerDataMinList().size() - 1) {
                        _pedometerDatamin_temp.setmPedometerDataMinList(_list);
                        _list.clear();
                        byte[] date_min2 = _pedometerDatamin.getmPedometerDataMinList().get(0).mStartDate;
                        this.mDataPedometerMin.mSaveDate = new Date((date_min2[0] + 2000) - 1990, date_min2[1], date_min2[2], date_min2[3], 0);
                        this.mDataPedometerMin.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                        this.mDataPedometerMin.mDataList.add(_pedometerDatamin_temp);
                        DatasContainer.mDeviceDatas.add(this.mDataPedometerMin);
                    }
                }
            }
            return;
        }
        byte[] date_min3 = _pedometerDatamin.getmPedometerDataMinList().get(0).mStartDate;
        this.mDataPedometerMin.mSaveDate = new Date((date_min3[0] + 2000) - 1990, date_min3[1], date_min3[2], date_min3[3], 0);
        this.mDataPedometerMin.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
        this.mDataPedometerMin.mDataList.add(_pedometerDatamin);
        DatasContainer.mDeviceDatas.add(this.mDataPedometerMin);
    }

    private void saveDaypedometerData() {
        DeviceDataPedometerJar _pedometerData = this.m_DevicePackManager.getM_DeviceDataPedometers();
        if (_pedometerData.getmPedometerDataDayList().size() > 15) {
            int num = 0;
            List<byte[]> _list = new ArrayList<>();
            DeviceDataPedometerJar _djPedometerDay = new DeviceDataPedometerJar();
            for (int i = 0; i < _pedometerData.getmPedometerDataDayList().size(); i++) {
                byte[] _datadate = _pedometerData.getmPedometerDataDayList().get(i);
                String _strDataDate = PageUtil.getDateFormByte(_datadate[0], _datadate[1], _datadate[2], (byte) 0, (byte) 0, (byte) 0);
                CLog.dT(this.TAG, "Determine if time is correct: " + _strDataDate);
                byte[] provalue = PageUtil.compareDate(_strDataDate);
                if (provalue != null) {
                    _datadate[0] = provalue[0];
                    _datadate[1] = provalue[1];
                    _datadate[2] = provalue[2];
                }
                _list.add(_pedometerData.getmPedometerDataDayList().get(i));
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
                } else if (i == _pedometerData.getmPedometerDataDayList().size() - 1) {
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
        for (int i2 = 0; i2 < _pedometerData.getmPedometerDataDayList().size(); i2++) {
            byte[] _datadate2 = _pedometerData.getmPedometerDataDayList().get(i2);
            String _strDataDate2 = PageUtil.getDateFormByte(_datadate2[0], _datadate2[1], _datadate2[2], (byte) 0, (byte) 0, (byte) 0);
            CLog.dT(this.TAG, "判断时间是否合法：" + _strDataDate2);
            byte[] provalue2 = PageUtil.compareDate(_strDataDate2);
            if (provalue2 != null) {
                _datadate2[0] = provalue2[0];
                _datadate2[1] = provalue2[1];
                _datadate2[2] = provalue2[2];
            }
        }
        byte[] date3 = _pedometerData.getmPedometerDataDayList().get(0);
        DeviceData deviceData3 = this.mDeviceData;
        deviceData3.mDate = new int[]{(date3[0] + 2000) - 1990, date3[1], date3[2], 8, 8, 8};
        this.mDeviceDataPedometerDay.mSaveDate = new Date((date3[0] + 2000) - 1990, date3[1], date3[2], 8, 8);
        CLog.d(this.TAG, "  mDeviceDataPedometerDay.mSaveDate:" + this.mDeviceDataPedometerDay.mSaveDate.toLocaleString());
        this.mDeviceDataPedometerDay.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
        this.mDeviceDataPedometerDay.mDataList.add(_pedometerData);
        DatasContainer.mDeviceDatas.add(this.mDeviceDataPedometerDay);
    }

    private void saveSpo2Data() {
        DeviceData50DJ_Jar _djData = this.m_DevicePackManager.getDeviceData50dj();
        if (_djData.getmSp02DataList().size() > 30) {
            int num = 0;
            List<byte[]> _list = new ArrayList<>();
            DeviceData50DJ_Jar _dj = new DeviceData50DJ_Jar();
            for (int i = 0; i < _djData.getmSp02DataList().size(); i++) {
                num++;
                byte[] _datadate = _djData.getmSp02DataList().get(i);
                String _strDataDate = PageUtil.getDateFormByte(_datadate[0], _datadate[1], _datadate[2], _datadate[3], _datadate[4], _datadate[5]);
                FileOperation.writeToSDCard("接收过来的数据时间：" + _strDataDate, "Cms50D_BT");
                CLog.dT(this.TAG, "Determine if time is correct: " + _strDataDate);
                byte[] provalue = PageUtil.compareDate(_strDataDate);
                if (provalue != null) {
                    _datadate[0] = provalue[0];
                    _datadate[1] = provalue[1];
                    _datadate[2] = provalue[2];
                    _datadate[3] = provalue[3];
                    _datadate[4] = provalue[4];
                    _datadate[5] = provalue[5];
                    FileOperation.writeToSDCard("时间不合法骄正之后的数据时间：" + PageUtil.getDateFormByte(_datadate[0], _datadate[1], _datadate[2], _datadate[3], _datadate[4], _datadate[5]), "Cms50D_BT");
                }
                _list.add(_djData.getmSp02DataList().get(i));
                if (num % 29 == 0) {
                    _dj.setmSp02DataList(_list);
                    byte[] date = _djData.getmSp02DataList().get(i);
                    DeviceData deviceData = this.mDeviceData;
                    deviceData.mDate = new int[]{(date[0] + 2000) - 1990, date[1], date[2], date[3], date[4], date[5]};
                    this.mDeviceData.mSaveDate = new Date((date[0] + 2000) - 1990, date[1], date[2], date[3], date[4]);
                    this.mDeviceData.mDataList.add(_dj);
                    this.mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                    DatasContainer.mDeviceDatas.add(this.mDeviceData);
                    this.mDeviceData.mDataList.clear();
                    _list.clear();
                } else if (i == _djData.getmSp02DataList().size() - 1) {
                    _dj.setmSp02DataList(_list);
                    byte[] date2 = _djData.getmSp02DataList().get(i);
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
        for (int i2 = 0; i2 < _djData.getmSp02DataList().size(); i2++) {
            byte[] _datadate2 = _djData.getmSp02DataList().get(i2);
            String _strDataDate2 = PageUtil.getDateFormByte(_datadate2[0], _datadate2[1], _datadate2[2], _datadate2[3], _datadate2[4], _datadate2[5]);
            FileOperation.writeToSDCard("接收过来的数据时间：" + _strDataDate2, "Cms50D_BT");
            byte[] provalue2 = PageUtil.compareDate(_strDataDate2);
            CLog.dT(this.TAG, "Determine if time is correct: " + _strDataDate2);
            if (provalue2 != null) {
                _datadate2[0] = provalue2[0];
                _datadate2[1] = provalue2[1];
                _datadate2[2] = provalue2[2];
                _datadate2[3] = provalue2[3];
                _datadate2[4] = provalue2[4];
                _datadate2[5] = provalue2[5];
                FileOperation.writeToSDCard("时间不合法骄正之后的数据时间：" + PageUtil.getDateFormByte(_datadate2[0], _datadate2[1], _datadate2[2], _datadate2[3], _datadate2[4], _datadate2[5]), "Cms50D_BT");
            }
        }
        byte[] date3 = _djData.getmSp02DataList().get(0);
        DeviceData deviceData3 = this.mDeviceData;
        deviceData3.mDate = new int[]{(date3[0] + 2000) - 1990, date3[1], date3[2], date3[3], date3[4], date3[5]};
        this.mDeviceData.mSaveDate = new Date((date3[0] + 2000) - 1990, date3[1], date3[2], date3[3], date3[4]);
        this.mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
        this.mDeviceData.mDataList.add(_djData);
        DatasContainer.mDeviceDatas.add(this.mDeviceData);
    }

    private int getformat(byte a) {
        return Integer.parseInt(new StringBuilder().append(a).toString(), 16);
    }
}
