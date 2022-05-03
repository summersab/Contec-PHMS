package com.contec.phms.device.cms50k;

import android.os.Environment;
import com.alibaba.fastjson.asm.Opcodes;
import com.contec.jar.cms50k.DeviceCommand;
import com.contec.jar.cms50k.DeviceDataECG;
import com.contec.jar.cms50k.DevicePackManager;
import com.contec.phms.App_phms;
import com.contec.phms.device.pm10.DeviceData;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.device.template.SendCommand;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.message.OrderList;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.util.CLog;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.PrintBytes;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import u.aly.bs;

public class ReceiveThread extends com.contec.phms.device.template.ReceiveThread {
    boolean IsHaveData = false;
    int Progress = 0;
    DeviceDataSpO2Fragment mDataSpO2Fragment;
    DevicePackManager mPackManager = new DevicePackManager();
    DeviceDataPedometer mPedometer;
    DeviceDataPedometerMin mPedometerMin;
    SpO2Fragment mSpO2Fragment;
    DeviceDataSpo2Point mSpo2Point;
    LoginUserDao mloginUserInfo = PageUtil.getLoginUserInfo();

    public ReceiveThread(DeviceService mDeviceService) {
        super(mDeviceService);
    }

    public static String printData(byte[] pack, int count) {
        String _temp = bs.b;
        for (int i = 0; i < count; i++) {
            _temp = String.valueOf(_temp) + " " + Integer.toHexString(pack[i]);
        }
        return _temp;
    }

    public static void writeToSDCard(String str, String deviceName) {
        String str2 = "\n" + str;
        if ("mounted".equals(Environment.getExternalStorageState())) {
            File file = new File(Environment.getExternalStorageDirectory(), String.valueOf(deviceName) + "log.txt");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileOutputStream outStream = new FileOutputStream(file, false);
                outStream.write(str2.getBytes());
                outStream.close();
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
    }

    public void arrangeMessage(byte[] buf, int length) {
        PrintBytes.printData(buf, length);
        byte _back = this.mPackManager.arrangeMessage(buf, length);
        if (!(_back == 0 || (_back & 255) == 136)) {
            CLog.e("cms50k", Integer.toHexString(_back));
        }
        switch (_back) {
            case -126:
                if (this.Progress < 70) {
                    this.Progress++;
                }
                DeviceManager.mDeviceBeanList.mState = 4;
                DeviceManager.m_DeviceBean.mState = 4;
                DeviceManager.m_DeviceBean.mProgress = this.Progress;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                SendCommand.send(DeviceCommand.GET_PULSE_FRAGMENT(1));
                return;
            case -125:
                this.Progress = 70;
                DeviceManager.mDeviceBeanList.mState = 4;
                DeviceManager.m_DeviceBean.mState = 4;
                DeviceManager.m_DeviceBean.mProgress = this.Progress;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                SendCommand.send(DeviceCommand.GET_PULSE_FRAGMENT(127));
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.mSpO2Fragment.PulseSegment = this.mPackManager.mSpo2h.PulseSegment;
                this.mSpO2Fragment.PulseTime = this.mPackManager.mSpo2h.PulseTime;
                this.mSpO2Fragment.PulseTime[0] = this.mSpO2Fragment.PulseTime[0] + 2000;
                SendCommand.send(DeviceCommand.GET_FRAGMENT_SIZE(1));
                return;
            case -110:
                if (this.Progress < 80) {
                    this.Progress++;
                }
                DeviceManager.mDeviceBeanList.mState = 4;
                DeviceManager.m_DeviceBean.mState = 4;
                DeviceManager.m_DeviceBean.mProgress = this.Progress;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                SendCommand.send(DeviceCommand.GET_SPO2_FRAGMENT(1));
                return;
            case -109:
                this.Progress = 80;
                DeviceManager.mDeviceBeanList.mState = 4;
                DeviceManager.m_DeviceBean.mState = 4;
                DeviceManager.m_DeviceBean.mProgress = this.Progress;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                SendCommand.send(DeviceCommand.GET_SPO2_FRAGMENT(127));
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                this.mSpO2Fragment.Spo2Segment = this.mPackManager.mSpo2h.Spo2Segment;
                this.mSpO2Fragment.Spo2Time = this.mPackManager.mSpo2h.Spo2Time;
                this.mSpO2Fragment.Spo2Time[0] = this.mSpO2Fragment.Spo2Time[0] + 2000;
                SendCommand.send(DeviceCommand.GET_FRAGMENT_SIZE(2));
                return;
            case -80:
                this.mSpO2Fragment = new SpO2Fragment();
                this.mDataSpO2Fragment = new DeviceDataSpO2Fragment();
                SendCommand.send(DeviceCommand.GET_PULSE_FRAGMENT(0));
                this.IsHaveData = true;
                return;
            case -79:
                if (this.IsHaveData) {
                    this.Progress = 100;
                    DeviceManager.mDeviceBeanList.mState = 4;
                    DeviceManager.m_DeviceBean.mState = 4;
                    DeviceManager.m_DeviceBean.mProgress = this.Progress;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    DeviceManager.mDeviceBeanList.mState = 6;
                    DeviceManager.m_DeviceBean.mState = 6;
                    DeviceManager.m_DeviceBean.mProgress = 0;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    DeviceService.mReceiveFinished = true;
                    return;
                }
                DeviceManager.mDeviceBeanList.mState = 10;
                DeviceManager.m_DeviceBean.mState = 10;
                DeviceManager.m_DeviceBean.mProgress = 0;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                return;
            case -78:
                SendCommand.send(DeviceCommand.GET_SPO2_FRAGMENT(0));
                return;
            case -77:
                if (this.IsHaveData) {
                    this.Progress = 100;
                    DeviceManager.mDeviceBeanList.mState = 6;
                    DeviceManager.m_DeviceBean.mState = 6;
                    DeviceManager.m_DeviceBean.mProgress = 0;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    DeviceService.mReceiveFinished = true;
                    return;
                }
                DeviceManager.mDeviceBeanList.mState = 10;
                DeviceManager.m_DeviceBean.mState = 10;
                DeviceManager.m_DeviceBean.mProgress = 0;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                return;
            case -76:
                SendCommand.send(DeviceCommand.GET_MOVE_FRAGMENT(0));
                return;
            case -75:
                dealSpO2Fragment(this.mSpO2Fragment);
                if (this.IsHaveData) {
                    this.Progress = 100;
                    DeviceManager.mDeviceBeanList.mState = 4;
                    DeviceManager.m_DeviceBean.mState = 4;
                    DeviceManager.m_DeviceBean.mProgress = this.Progress;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    DeviceManager.mDeviceBeanList.mState = 6;
                    DeviceManager.m_DeviceBean.mState = 6;
                    DeviceManager.m_DeviceBean.mProgress = 0;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    DeviceService.mReceiveFinished = true;
                    return;
                }
                DeviceManager.mDeviceBeanList.mState = 10;
                DeviceManager.m_DeviceBean.mState = 10;
                DeviceManager.m_DeviceBean.mProgress = 0;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                return;
            case -74:
                SendCommand.send(DeviceCommand.GET_MOVE_FRAGMENT(127));
                this.Progress = 100;
                DeviceManager.mDeviceBeanList.mState = 4;
                DeviceManager.m_DeviceBean.mState = 4;
                DeviceManager.m_DeviceBean.mProgress = this.Progress;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                this.mSpO2Fragment.MovementPoint = this.mPackManager.mSpo2h.MovementPoint;
                this.mSpO2Fragment.MovementTime = this.mPackManager.mSpo2h.MovementTime;
                this.mSpO2Fragment.MovementTime[0] = this.mSpO2Fragment.MovementTime[0] + 2000;
                this.mSpO2Fragment.MovementStart = this.mPackManager.mSpo2h.MovementStart;
                this.mSpO2Fragment.MovementEnd = this.mPackManager.mSpo2h.MovementEnd;
                dealSpO2Fragment(this.mSpO2Fragment);
                DeviceManager.mDeviceBeanList.mState = 6;
                DeviceManager.m_DeviceBean.mState = 6;
                DeviceManager.m_DeviceBean.mProgress = 0;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                return;
            case -73:
                if (this.Progress < 100) {
                    this.Progress++;
                }
                DeviceManager.mDeviceBeanList.mState = 4;
                DeviceManager.m_DeviceBean.mState = 4;
                DeviceManager.m_DeviceBean.mProgress = this.Progress;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                SendCommand.send(DeviceCommand.GET_MOVE_FRAGMENT(1));
                return;
            case 16:
                int _start = 0;
                int _end = 0;
                if (this.mloginUserInfo.mAmactivity != null && !this.mloginUserInfo.mAmactivity.equalsIgnoreCase(bs.b)) {
                    _start = Integer.parseInt(this.mloginUserInfo.mAmactivity);
                }
                if (this.mloginUserInfo.mPmactivity != null && !this.mloginUserInfo.mPmactivity.equalsIgnoreCase(bs.b)) {
                    _end = Integer.parseInt(this.mloginUserInfo.mPmactivity);
                }
                SendCommand.send(DeviceCommand.SET_STEP_TIME(_start, _end));
                return;
            case 17:
                DeviceManager.mDeviceBeanList.mState = 5;
                DeviceManager.m_DeviceBean.mState = 5;
                DeviceManager.m_DeviceBean.mProgress = 0;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                return;
            case 32:
                int _weight = 0;
                if (this.mloginUserInfo.mWeight != null && !this.mloginUserInfo.mWeight.equalsIgnoreCase(bs.b)) {
                    _weight = (int) (Double.parseDouble(this.mloginUserInfo.mWeight) * 1000.0d);
                }
                SendCommand.send(DeviceCommand.SET_WEIGHT(_weight));
                return;
            case 33:
                DeviceManager.mDeviceBeanList.mState = 5;
                DeviceManager.m_DeviceBean.mState = 5;
                DeviceManager.m_DeviceBean.mProgress = 0;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                return;
            case Opcodes.FALOAD:
                SendCommand.send(DeviceCommand.GET_DATA_SIZE(0));
                return;
            case 49:
                DeviceManager.mDeviceBeanList.mState = 5;
                DeviceManager.m_DeviceBean.mState = 5;
                DeviceManager.m_DeviceBean.mProgress = 0;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                return;
            case 64:
                SendCommand.send(DeviceCommand.GET_SPO2_POINT(0));
                this.IsHaveData = true;
                this.Progress = 10;
                DeviceManager.mDeviceBeanList.mState = 4;
                DeviceManager.m_DeviceBean.mState = 4;
                DeviceManager.m_DeviceBean.mProgress = this.Progress;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                return;
            case 65:
                SendCommand.send(DeviceCommand.GET_DATA_SIZE(1));
                return;
            case OrderList.DM_NEXT_DEVICE_OR_POLLING /*66*/:
                SendCommand.send(DeviceCommand.GET_SPO2_POINT(1));
                if (this.Progress < 20) {
                    this.Progress++;
                }
                DeviceManager.mDeviceBeanList.mState = 4;
                DeviceManager.m_DeviceBean.mState = 4;
                DeviceManager.m_DeviceBean.mProgress = this.Progress;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                return;
            case 67:
                this.Progress = 20;
                DeviceManager.mDeviceBeanList.mState = 4;
                DeviceManager.m_DeviceBean.mState = 4;
                DeviceManager.m_DeviceBean.mProgress = this.Progress;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                SendCommand.send(DeviceCommand.GET_SPO2_POINT(127));
                if (this.mPackManager.mSpo2h.Spo2Point.size() < 30) {
                    this.mSpo2Point = new DeviceDataSpo2Point();
                    this.mSpo2Point.mDataList = this.mPackManager.mSpo2h.Spo2Point;
                    addDataPoint(this.mSpo2Point);
                } else {
                    int _size = this.mPackManager.mSpo2h.Spo2Point.size();
                    ArrayList<Object> _dataAll = this.mPackManager.mSpo2h.Spo2Point;
                    this.mSpo2Point = new DeviceDataSpo2Point();
                    for (int i = 0; i < _size; i++) {
                        this.mSpo2Point.mDataList.add(_dataAll.get(i));
                        if (i == _size - 1) {
                            addDataPoint(this.mSpo2Point);
                        } else if (this.mSpo2Point.mDataList.size() == 30) {
                            addDataPoint(this.mSpo2Point);
                            this.mSpo2Point = new DeviceDataSpo2Point();
                        }
                    }
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
                SendCommand.send(DeviceCommand.GET_DATA_SIZE(1));
                return;
            case 80:
                SendCommand.send(DeviceCommand.GET_STEP_DAY(0));
                this.IsHaveData = true;
                return;
            case 81:
                SendCommand.send(DeviceCommand.GET_DATA_SIZE(3));
                return;
            case 82:
                if (this.Progress < 30) {
                    this.Progress++;
                }
                DeviceManager.mDeviceBeanList.mState = 4;
                DeviceManager.m_DeviceBean.mState = 4;
                DeviceManager.m_DeviceBean.mProgress = this.Progress;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                SendCommand.send(DeviceCommand.GET_STEP_DAY(1));
                return;
            case 83:
                SendCommand.send(DeviceCommand.GET_STEP_DAY(127));
                this.mPedometer = new DeviceDataPedometer();
                this.mPedometer.mDataList = this.mPackManager.mPedometerJar.getmPedometerDataDayList();
                addDataStepDay(this.mPedometer);
                SendCommand.send(DeviceCommand.GET_DATA_SIZE(2));
                return;
            case Opcodes.IADD:
                SendCommand.send(DeviceCommand.GET_STEP_MIN(1));
                this.Progress = 30;
                DeviceManager.mDeviceBeanList.mState = 4;
                DeviceManager.m_DeviceBean.mState = 4;
                DeviceManager.m_DeviceBean.mProgress = this.Progress;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                this.IsHaveData = true;
                return;
            case Opcodes.LADD:
                SendCommand.send(DeviceCommand.GET_DATA_SIZE(3));
                return;
            case 98:
                SendCommand.send(DeviceCommand.GET_STEP_MIN_DATA(1));
                return;
            case 99:
                if (this.Progress < 40) {
                    this.Progress++;
                }
                DeviceManager.mDeviceBeanList.mState = 4;
                DeviceManager.m_DeviceBean.mState = 4;
                DeviceManager.m_DeviceBean.mProgress = this.Progress;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                SendCommand.send(DeviceCommand.GET_STEP_MIN_DATA(127));
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e4) {
                    e4.printStackTrace();
                }
                SendCommand.send(DeviceCommand.GET_STEP_MIN(1));
                return;
            case 100:
                SendCommand.send(DeviceCommand.GET_STEP_MIN_DATA(0));
                return;
            case 101:
                this.Progress = 40;
                DeviceManager.mDeviceBeanList.mState = 4;
                DeviceManager.m_DeviceBean.mState = 4;
                DeviceManager.m_DeviceBean.mProgress = this.Progress;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                SendCommand.send(DeviceCommand.GET_STEP_MIN_DATA(127));
                this.mPedometerMin = new DeviceDataPedometerMin();
                this.mPedometerMin.mDataList = this.mPackManager.minDatas;
                addDataStepMin(this.mPedometerMin);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                SendCommand.send(DeviceCommand.GET_DATA_SIZE(3));
                return;
            case 112:
                SendCommand.send(DeviceCommand.GET_ECG_INFO(1));
                this.IsHaveData = true;
                return;
            case 113:
                SendCommand.send(DeviceCommand.GET_FRAGMENT_SIZE(0));
                return;
            case 114:
                SendCommand.send(DeviceCommand.GET_ECG_DATA(1));
                return;
            case 115:
                if (this.Progress < 50) {
                    this.Progress++;
                }
                DeviceManager.mDeviceBeanList.mState = 4;
                DeviceManager.m_DeviceBean.mState = 4;
                DeviceManager.m_DeviceBean.mProgress = this.Progress;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                SendCommand.send(DeviceCommand.GET_ECG_DATA(127));
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e5) {
                    e5.printStackTrace();
                }
                SendCommand.send(DeviceCommand.GET_ECG_INFO(1));
                return;
            case 116:
                this.Progress = 50;
                DeviceManager.mDeviceBeanList.mState = 4;
                DeviceManager.m_DeviceBean.mState = 4;
                DeviceManager.m_DeviceBean.mProgress = this.Progress;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                SendCommand.send(DeviceCommand.GET_ECG_DATA(127));
                ArrayList<DeviceDataECG> _ECGS = this.mPackManager.mEcgs;
                for (int i2 = 0; i2 < _ECGS.size(); i2++) {
                    DeviceData _ECG = new DeviceData();
                    _ECG.mDate = new int[6];
                    _ECG.mDate[0] = _ECGS.get(i2).Point_data[0];
                    _ECG.mDate[1] = _ECGS.get(i2).Point_data[1];
                    _ECG.mDate[2] = _ECGS.get(i2).Point_data[2];
                    _ECG.mDate[3] = _ECGS.get(i2).Point_data[3];
                    _ECG.mDate[4] = _ECGS.get(i2).Point_data[4];
                    _ECG.mDate[5] = _ECGS.get(i2).Point_data[5];
                    _ECG.TrendData = _ECGS.get(i2).Point_data;
                    _ECG.TrendData[8] = 1;
                    _ECG.CaseData = _ECGS.get(i2).ECG_Data;
                    addDataECG(_ECG);
                }
                SendCommand.send(DeviceCommand.GET_FRAGMENT_SIZE(0));
                return;
            case 117:
                SendCommand.send(DeviceCommand.GET_ECG_DATA(0));
                return;
            default:
                return;
        }
    }

    public void dealSpO2Fragment(SpO2Fragment pFragment) {
        if (pFragment != null) {
            this.mDataSpO2Fragment = new DeviceDataSpO2Fragment();
            this.mDataSpO2Fragment.mDate = new int[6];
            this.mDataSpO2Fragment.mDate = pFragment.Spo2Time;
            LoginUserDao _loginUserInfo = PageUtil.getLoginUserInfo();
            this.mDataSpO2Fragment.mUserName = _loginUserInfo.mUserName;
            if (_loginUserInfo.mBirthday != null && !_loginUserInfo.mBirthday.equalsIgnoreCase(bs.b)) {
                this.mDataSpO2Fragment.mAge = ((long) Calendar.getInstance().get(1)) - Long.parseLong(_loginUserInfo.mBirthday.substring(0, 4));
            }
            if (_loginUserInfo.mHeight != null && !_loginUserInfo.mHeight.equalsIgnoreCase(bs.b)) {
                this.mDataSpO2Fragment.mHeight = ((long) Double.parseDouble(_loginUserInfo.mHeight)) * 10;
            }
            if (_loginUserInfo.mWeight != null && !_loginUserInfo.mWeight.equalsIgnoreCase(bs.b)) {
                this.mDataSpO2Fragment.mWeight = ((long) Double.parseDouble(_loginUserInfo.mWeight)) * 10;
            }
            if (_loginUserInfo.mSex != null && !_loginUserInfo.mSex.equalsIgnoreCase(bs.b)) {
                this.mDataSpO2Fragment.mSex = Long.parseLong(_loginUserInfo.mSex) + 1;
            }
            if (_loginUserInfo.mAddress != null) {
                this.mDataSpO2Fragment.mAddress = _loginUserInfo.mAddress;
            }
            if (_loginUserInfo.mPhone != null) {
                this.mDataSpO2Fragment.mPhone = _loginUserInfo.mPhone;
            }
            this.mDataSpO2Fragment.mDataList.add(pFragment);
            this.mDataSpO2Fragment.makeInfos();
            this.mDataSpO2Fragment.setSaveDate();
            this.mDataSpO2Fragment.setmUploadType("case");
            this.mDataSpO2Fragment.setmDataType("cms50k");
            this.mDataSpO2Fragment.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
            this.mDataSpO2Fragment.makeSaveTime();
            addDataFragment(this.mDataSpO2Fragment);
        }
    }

    public void addDataECG(com.contec.phms.device.template.DeviceData deviceData) {
        DeviceData _data = (DeviceData) deviceData;
        if (_data.TrendData == null) {
            CLog.i("ReceiveThread", "No New Datas");
            return;
        }
        CLog.d("ReceiveThread", "存储数据***********");
        _data.setmDataType("ECG(CMS50K)");
        _data.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
        DatasContainer.mDeviceDatas.add(deviceData);
    }

    public void addDataPoint(com.contec.phms.device.template.DeviceData deviceData) {
        DeviceDataSpo2Point _data = (DeviceDataSpo2Point) deviceData;
        if (_data.mDataList.size() == 0) {
            CLog.i("ReceiveThread", "No New Datas");
            return;
        }
        CLog.d("ReceiveThread", "存储数据***********");
        _data.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
        DatasContainer.mDeviceDatas.add(deviceData);
    }

    public void addDataStepMin(com.contec.phms.device.template.DeviceData deviceData) {
        DeviceDataPedometerMin _data = (DeviceDataPedometerMin) deviceData;
        if (_data.mDataList.size() == 0) {
            CLog.i("ReceiveThread", "No New Datas");
            return;
        }
        CLog.d("ReceiveThread", "存储数据***********");
        _data.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
        DatasContainer.mDeviceDatas.add(deviceData);
    }

    public void addDataStepDay(com.contec.phms.device.template.DeviceData deviceData) {
        DeviceDataPedometer _data = (DeviceDataPedometer) deviceData;
        if (_data.mDataList.size() == 0) {
            CLog.i("ReceiveThread", "No New Datas");
            return;
        }
        CLog.d("ReceiveThread", "存储数据***********");
        _data.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
        DatasContainer.mDeviceDatas.add(deviceData);
    }

    public void addDataFragment(com.contec.phms.device.template.DeviceData deviceData) {
        if (((DeviceDataSpO2Fragment) deviceData).mDataList.size() == 0) {
            CLog.i("ReceiveThread", "No New Datas");
            return;
        }
        CLog.d("ReceiveThread", "存储数据***********");
        DatasContainer.mDeviceDatas.add(deviceData);
    }

    public void addData(com.contec.phms.device.template.DeviceData deviceData) {
        if (((com.contec.phms.device.cms50ew.DeviceData) deviceData).mDataList.size() == 0) {
            CLog.i("ReceiveThread", "No New Datas");
            return;
        }
        CLog.d("ReceiveThread", "存储数据***********");
        DatasContainer.mDeviceDatas.add(deviceData);
    }
}
