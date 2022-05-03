package com.contec.phms.device.cms50f;

import com.contec.jar.cms50ew.DeviceCommand;
import com.contec.jar.cms50ew.DeviceDataIW;
import com.contec.jar.cms50ew.DevicePackManager;
import com.contec.jar.cms50ew.DevicePackManager50F;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.device.template.SendCommand;
import com.contec.phms.eventbus.EventShowLastData;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.message.OrderList;
import com.contec.phms.db.DeviceListDaoOperation;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.db.localdata.Spo2DataDao;
import com.contec.phms.util.CLog;
import com.contec.phms.util.PageUtil;
import java.util.ArrayList;
import java.util.Calendar;
import u.aly.bs;

public class ReceiveThread extends com.contec.phms.device.template.ReceiveThread {
    boolean bGetPackId = false;
    byte[] curPack = new byte[9];
    int dataCount = 0;
    int i;
    int k = 0;
    int len = 0;
    int mDataIndex;
    DeviceData mData_Trend = new DeviceData(new byte[4]);
    DeviceData mDeviceData;
    int mPI = 1;
    DevicePackManager50F mPackManager = new DevicePackManager50F();
    int mSettimeCount = 0;
    byte value;

    public ReceiveThread(DeviceService mDeviceService) {
        super(mDeviceService);
    }

    public void arrangeMessage(byte[] buf, int length) {
        byte _back = this.mPackManager.arrangeMessage(buf, length);
        CLog.e("ReceiveThread", "---------0xB2-----------" + Integer.toHexString(_back & 255));
        switch (_back) {
            case -79:
                byte[] _request_data = DeviceCommand.REQUEST_PI_TYPE();
                DevicePackManager.doPack(_request_data);
                SendCommand.send(_request_data);
                CLog.d("ReceiveThread", "---------0xB1-----------");
                return;
            case -78:
                byte[] _time = DeviceCommand.SET_TIME();
                DevicePackManager.doPack(_time);
                SendCommand.send(_time);
                return;
            case 8:
                byte[] _request_time = DeviceCommand.REQUEST_DATA_DATE(this.mDataIndex);
                DevicePackManager.doPack(_request_time);
                SendCommand.send(_request_time);
                return;
            case 10:
                this.dataCount = this.mPackManager.mDataCount;
                if (this.dataCount > 0) {
                    DeviceManager.m_DeviceBean.mProgress = 20;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    SendCommand.send(DeviceCommand.REQUEST_DATA(0, false));
                    return;
                }
                DeviceManager.mDeviceBeanList.mState = 10;
                DeviceManager.m_DeviceBean.mState = 10;
                DeviceManager.m_DeviceBean.mProgress = 0;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                return;
            case 14:
                this.mPI = this.mPackManager.mPI;
                if (this.mPI == 1) {
                    DeviceManager.m_DeviceBean.mProgress = 20;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    SendCommand.send(DeviceCommand.REQUEST_DATA(0, false));
                    return;
                }
                DeviceManager.m_DeviceBean.mProgress = 20;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                byte[] _request_data2 = DeviceCommand.REQUEST_DATA_COUNT();
                DevicePackManager.doPack(_request_data2);
                SendCommand.send(_request_data2);
                return;
            case 18:
                if (this.mPackManager.mSaveDeviceDataIW != null) {
                    CLog.e("ReceiveThread", "mSaveDeviceDataIW:" + this.mPackManager.mSaveDeviceDataIW._Min);
                    init_DataIW(this.mPackManager.mSaveDeviceDataIW);
                } else {
                    CLog.e("ReceiveThread", "mSaveDeviceDataIW:null");
                    this.mDeviceData = new DeviceData(new byte[4]);
                }
                if (this.mPI != 1) {
                    byte[] _request_pidata = DeviceCommand.REQUEST_DATA(this.mDataIndex, true);
                    DevicePackManager.doPack(_request_pidata);
                    SendCommand.send(_request_pidata);
                    return;
                }
                return;
            case 24:
                if (this.mPackManager.mSaveDeviceDataIW != null) {
                    SendCommand.send(DeviceCommand.DELETE_DATA(0, true));
                    CLog.e("ReceiveThread", "接收完毕");
                    CLog.e("ReceiveThread", "mSaveDeviceDataIW:" + this.mPackManager.mSaveDeviceDataIW._Min);
                    init_DataIW(this.mPackManager.mSaveDeviceDataIW);
                    if (this.mData_Trend.mDataList.size() > 0) {
                        addData(this.mData_Trend);
                    }
                    byte[] _value = (byte[]) this.mData_Trend.mDataList.get(this.mData_Trend.mDataList.size() - 1);
                    DeviceListDaoOperation.getInstance().updateReceiveDataStr(DeviceManager.m_DeviceBean.mMacAddr, PageUtil.processDate((_value[0] & 255) + 2000, _value[1] & 255, _value[2] & 255, _value[3] & 255, _value[4] & 255, _value[5] & 255), String.valueOf(_value[6] & 255) + ";" + (_value[7] & 255));
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(new EventShowLastData());
                    DeviceManager.mDeviceBeanList.mState = 6;
                    DeviceManager.m_DeviceBean.mState = 6;
                    DeviceManager.m_DeviceBean.mProgress = 0;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                } else {
                    DeviceManager.mDeviceBeanList.mState = 10;
                    DeviceManager.m_DeviceBean.mState = 10;
                    DeviceManager.m_DeviceBean.mProgress = 0;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                }
                DeviceService.mReceiveFinished = true;
                return;
            case 33:
                this.mSettimeCount++;
                if (this.mSettimeCount == 2) {
                    byte[] _request_data3 = DeviceCommand.REQUEST_PI_TYPE();
                    DevicePackManager.doPack(_request_data3);
                    SendCommand.send(_request_data3);
                    return;
                }
                return;
            case 34:
                this.mSettimeCount++;
                if (this.mSettimeCount == 2) {
                    byte[] _request_data4 = DeviceCommand.REQUEST_PI_TYPE();
                    DevicePackManager.doPack(_request_data4);
                    SendCommand.send(_request_data4);
                    return;
                }
                return;
            case OrderList.DS_FILTER_DATAS /*35*/:
                CLog.e("ReceiveThread", "ReceiveThread");
                return;
            default:
                return;
        }
    }

    public void init_DataIW(DeviceDataIW _DataIW) {
        DeviceDataIW mData = _DataIW;
        this.mDeviceData = null;
        this.mDeviceData = new DeviceData(new byte[4]);
        this.mDeviceData.mDataList.clear();
        this.mDeviceData.mDataList.addAll(mData.valueList);
        this.mDeviceData.mDate = new int[6];
        this.mDeviceData.mDate[0] = mData._Year;
        this.mDeviceData.mDate[1] = mData._Month;
        this.mDeviceData.mDate[2] = mData._Day;
        this.mDeviceData.mDate[3] = mData._Hour;
        this.mDeviceData.mDate[4] = mData._Min;
        this.mDeviceData.mDate[5] = mData._Sec;
        LoginUserDao _loginUserInfo = PageUtil.getLoginUserInfo();
        this.mDeviceData.mUserName = _loginUserInfo.mUserName;
        if (_loginUserInfo.mBirthday != null && !_loginUserInfo.mBirthday.equalsIgnoreCase(bs.b)) {
            this.mDeviceData.mAge = ((long) Calendar.getInstance().get(1)) - Long.parseLong(_loginUserInfo.mBirthday.substring(0, 4));
        }
        if (_loginUserInfo.mHeight != null && !_loginUserInfo.mHeight.equalsIgnoreCase(bs.b)) {
            this.mDeviceData.mHeight = ((long) Double.parseDouble(_loginUserInfo.mHeight)) * 10;
        }
        if (_loginUserInfo.mWeight != null && !_loginUserInfo.mWeight.equalsIgnoreCase(bs.b)) {
            this.mDeviceData.mWeight = ((long) Double.parseDouble(_loginUserInfo.mWeight)) * 10;
        }
        if (_loginUserInfo.mSex != null && !_loginUserInfo.mSex.equalsIgnoreCase(bs.b)) {
            this.mDeviceData.mSex = Long.parseLong(_loginUserInfo.mSex) + 1;
        }
        if (_loginUserInfo.mAddress != null) {
            this.mDeviceData.mAddress = _loginUserInfo.mAddress;
        }
        if (_loginUserInfo.mPhone != null) {
            this.mDeviceData.mPhone = _loginUserInfo.mPhone;
        }
        this.mDeviceData.mPI = this.mPI;
        this.mDeviceData.makeInfos();
        this.mDeviceData.setSaveDate();
        this.mDeviceData.setmUploadType("case");
        this.mDeviceData.setmDataType(Spo2DataDao.SPO2);
        this.mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
        this.mDeviceData.makeSaveTime();
        addData(this.mDeviceData);
        Calendar _Calendar = mData.get_start_Calendar();
        _Calendar.add(12, -1);
        ArrayList<byte[]> _datas = mData.valueList;
        int _trend_size = (int) Math.ceil((((double) _datas.size()) * 1.0d) / 12.0d);
        for (int i = 0; i < _trend_size; i++) {
            byte[] _data_trend = _datas.get(i * 12);
            _Calendar.add(12, 1);
            byte[] compareDate = PageUtil.compareDate(_Calendar);
            byte[] _data = new byte[9];
            _data[0] = (byte) (_Calendar.get(1) - 2000);
            _data[1] = (byte) (_Calendar.get(2) + 1);
            _data[2] = (byte) _Calendar.get(5);
            _data[3] = (byte) _Calendar.get(11);
            _data[4] = (byte) _Calendar.get(12);
            _data[5] = (byte) _Calendar.get(13);
            _data[6] = _data_trend[0];
            _data[7] = _data_trend[1];
            if ((_data[6] == 1) && (_data[7] == 1 ) && (_data[6] & 127) < 'd' && (_data[6] & 127) > 0) {
                this.mData_Trend.mDataList.add(_data);
                this.mData_Trend.setmUploadType("trend");
                this.mData_Trend.setmDataType(Spo2DataDao.SPO2);
                this.mData_Trend.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
            }
            if (this.mData_Trend.mDataList.size() == 30) {
                this.mData_Trend.mDate = new int[6];
                this.mData_Trend.mDate[0] = _data[0];
                this.mData_Trend.mDate[1] = _data[1];
                this.mData_Trend.mDate[2] = _data[2];
                this.mData_Trend.mDate[3] = _data[3];
                this.mData_Trend.mDate[4] = _data[4];
                this.mData_Trend.mDate[5] = _data[5];
                addData(this.mData_Trend);
                this.mData_Trend.mDataList.clear();
                this.mData_Trend.mDate = null;
            }
        }
    }

    public void addData(DeviceData deviceData) {
        if (((DeviceData) deviceData).mDataList.size() == 0) {
            CLog.i("ReceiveThread", "No New Datas");
        } else {
            DatasContainer.mDeviceDatas.add(deviceData);
        }
    }
}
