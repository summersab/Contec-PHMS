package com.contec.phms.device.contec08aw;

import com.alibaba.fastjson.asm.Opcodes;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.device.template.SendCommand;
import com.contec.phms.eventbus.EventShowLastData;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.db.DeviceListDaoOperation;
import com.contec.phms.util.CLog;
import com.contec.phms.util.PageUtil;
import com.example.bm77_contec08A_code.DeviceCommand;
import com.example.bm77_contec08A_code.DevicePackManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import u.aly.bs;

public class ReceiveThread extends com.contec.phms.device.template.ReceiveThread {
    public static final int e_pack_hand_back = 72;
    public static final int e_pack_oxygen_back = 71;
    public static final int e_pack_pressure_back = 70;
    public static boolean mIsNew;
    public static boolean mIsOld;
    private String TAG = "ReceiveThread-contec08aw";
    int _back;
    boolean isBPempty = true;
    boolean mCorrect_time = false;
    private int mMedian = 0;
    DevicePackManager mPackManager = new DevicePackManager();
    int mSizeBP = 0;
    private int mType = 0;

    public ReceiveThread(DeviceService mDeviceService) {
        super(mDeviceService);
    }

    public void arrangeMessage(byte[] buffer, int length) {
        int state = this.mPackManager.arrangeMessage(buffer, length, this.mType);
        int x = DevicePackManager.Flag_User;
        switch (state) {
            case Opcodes.FALOAD:
                CLog.i(this.TAG, "Sending `Correct_Time` command---");
                SendCommand.send(DeviceCommand.Correct_Time());
                return;
            case 64:
                CLog.i(this.TAG, "`Correct_Time` successful, send handshake---");
                SendCommand.send(DeviceCommand.REQUEST_HANDSHAKE());
                return;
            case 70:
                CLog.i(this.TAG, "Blood pressure data received---");
                ArrayList<byte[]> _dataList = this.mPackManager.mDeviceData.mData_blood;
                int _size = _dataList.size();
                this.mSizeBP = _size;
                CLog.e(this.TAG, "device name " + DeviceManager.m_DeviceBean.mDeviceName);
                CLog.i(this.TAG, "设备中要上传的设备个数：" + _size);
                if (_size == 0) {
                    CLog.e(this.TAG, "Sending response command to the device");
                    SendCommand.send(DeviceCommand.REPLAY_CONTEC08A());
                    CLog.e(this.TAG, "-------Pressure-----size=0--");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    DeviceManager.mDeviceBeanList.mProgress = 0;
                    DeviceManager.mDeviceBeanList.mState = 10;
                    DeviceManager.m_DeviceBean.mState = 10;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    DeviceService.mReceiveFinished = true;
                } else if (DeviceManager.m_DeviceBean.mDeviceName.equals("CONTEC08AW")) {
                    CLog.e(this.TAG, "----After the data is received, send a response command to the device");
                    SendCommand.send(DeviceCommand.REPLAY_CONTEC08A());
                    CLog.e(this.TAG, "-------Pressure-------");
                    CLog.e(this.TAG, "blood pressure");
                    DeviceManager.mDeviceBeanList.mProgress = 100;
                    DeviceManager.mDeviceBeanList.mState = 6;
                    DeviceManager.m_DeviceBean.mState = 6;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    DeviceService.mReceiveFinished = true;
                } else {
                    CLog.e(this.TAG, "----After the data is received, prepare to send a delete command to the device");
                    SendCommand.send(DeviceCommand.REQUEST_HANDSHAKE());
                    this.mType = 9;
                }
                if (DeviceManager.m_DeviceBean.mDeviceName.equals("CONTEC08AW")) {
                    CLog.e(this.TAG, "----将08a设备进行数据保存处理");
                    save_bloodpressure_data(_dataList, _size);
                    return;
                }
                return;
            case 71:
                CLog.e(this.TAG, "-------Oxygen-------");
                ArrayList<byte[]> _dataListOxyen = this.mPackManager.mDeviceData.mData_oxygen;
                int _size_oxyen = _dataListOxyen.size();
                for (int i = 0; i < _size_oxyen; i++) {
                    DeviceManager.mDeviceBeanList.mState = 4;
                    DeviceManager.m_DeviceBean.mState = 4;
                    byte[] _byte = _dataListOxyen.get(i);
                    DeviceData _mData = new DeviceData((byte[]) null);
                    _mData.mPack = new byte[8];
                    _mData.mPack[0] = _byte[2];
                    _mData.mPack[1] = _byte[3];
                    _mData.mPack[2] = _byte[4];
                    _mData.mPack[3] = _byte[5];
                    _mData.mPack[4] = _byte[6];
                    _mData.mPack[5] = _byte[7];
                    _mData.mPack[6] = _byte[0];
                    _mData.mPack[7] = _byte[1];
                    _mData.mDate = new int[6];
                    _mData.mDate[0] = (_byte[2] & 255) + 2000;
                    _mData.mDate[1] = _byte[3] & 255;
                    _mData.mDate[2] = _byte[4] & 255;
                    _mData.mDate[3] = _byte[5] & 255;
                    _mData.mDate[4] = _byte[6] & 255;
                    _mData.mDate[5] = _byte[7] & 255;
                    _mData.setmUploadType("trend");
                    _mData.setmDataType("contec08spo2");
                    _mData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                    _mData.setSaveDate();
                    DatasContainer.mDeviceDatas.add(_mData);
                    DeviceManager.mDeviceBeanList.mProgress = (((i + 1) * 40) / _size_oxyen) + 60;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    if (i == _size_oxyen - 1) {
                        DeviceListDaoOperation.getInstance().updateReceiveDataStr(DeviceManager.m_DeviceBean.mMacAddr, PageUtil.processDate((_mData.mPack[0] & 255) + 2000, _mData.mPack[1] & 255, _mData.mPack[2] & 255, _mData.mPack[3] & 255, _mData.mPack[4] & 255, _mData.mPack[5] & 255), String.valueOf(_mData.mPack[6] & 255) + ";" + (_mData.mPack[7] & 255));
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(new EventShowLastData());
                    }
                }
                if (_size_oxyen == 0) {
                    if (this.mSizeBP == 0) {
                        DeviceManager.mDeviceBeanList.mState = 10;
                        DeviceManager.m_DeviceBean.mState = 10;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    } else {
                        DeviceManager.mDeviceBeanList.mProgress = 100;
                        DeviceManager.mDeviceBeanList.mState = 6;
                        DeviceManager.m_DeviceBean.mState = 6;
                        DeviceManager.m_DeviceBean.mProgress = 0;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    }
                    DeviceService.mReceiveFinished = true;
                    return;
                }
                SendCommand.send(DeviceCommand.REQUEST_HANDSHAKE());
                return;
            case 72:
                switch (this.mType) {
                    case 0:
                        this.mType = 3;
                        CLog.i(this.TAG, "Sending `Correct_Time` command---");
                        SendCommand.send(DeviceCommand.correct_time_notice);
                        return;
                    case 1:
                        this.mType = 2;
                        SendCommand.send(DeviceCommand.REQUEST_OXYGEN());
                        return;
                    case 2:
                        this.mType = 5;
                        SendCommand.send(DeviceCommand.DELETE_OXYGEN());
                        return;
                    case 3:
                        this.mType = 1;
                        if (x == 17) {
                            this.mType = 7;
                        } else {
                            this.mType = 1;
                        }
                        CLog.i(this.TAG, "Handshake successful，requesting blood pressure data");
                        SendCommand.send(DeviceCommand.REQUEST_BLOOD_PRESSURE());
                        return;
                    case 7:
                        this.mType = 8;
                        SendCommand.send(DeviceCommand.REQUEST_OXYGEN());
                        return;
                    case 8:
                        this.mType = 5;
                        SendCommand.send(DeviceCommand.DELETE_OXYGEN());
                        return;
                    case 9:
                        this.mType = 5;
                        CLog.e(this.TAG, "----Sending delete command");
                        SendCommand.send(DeviceCommand.DELETE_BP());
                        return;
                    default:
                        return;
                }
            case 80:
                CLog.i(this.TAG, "----Blood pressure data deleted successfully");
                DeviceManager.mDeviceBeanList.mState = 6;
                DeviceManager.m_DeviceBean.mState = 6;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                save_bloodpressure_data();
                return;
            case 81:
                CLog.i(this.TAG, "----Blood pressure data deletion failed");
                save_bloodpressure_data();
                return;
            default:
                return;
        }
    }

    private void save_bloodpressure_data() {
        CLog.i(this.TAG, "invoke method save_bloodpressure_data()");
        ArrayList<byte[]> _dataList = this.mPackManager.mDeviceData.mData_blood;
        int _size = _dataList.size();
        this.mSizeBP = _size;
        save_bloodpressure_data(_dataList, _size);
    }

    private void save_bloodpressure_data(ArrayList<byte[]> _dataList, int _size) {
        DeviceData _mData = new DeviceData((byte[]) null);
        for (int i = 0; i < _size; i++) {
            DeviceManager.mDeviceBeanList.mState = 4;
            DeviceManager.m_DeviceBean.mState = 4;
            DeviceManager.m_DeviceBean.mProgress = 0;
            byte[] _byte = _dataList.get(i);
            byte[] _data = new byte[10];
            String _dateCompare = PageUtil.process_Date((_byte[5] & 255) + 2000, _byte[6] & 255, _byte[7] & 255, _byte[8] & 255, _byte[9] & 255, _byte[10] & 255);
            byte[] provalue = PageUtil.compareDate(_dateCompare);
            if (provalue != null) {
                _data[0] = provalue[0];
                _data[1] = provalue[1];
                _data[2] = provalue[2];
                _data[3] = provalue[3];
                _data[4] = provalue[4];
                _data[5] = _byte[0];
                _data[6] = _byte[1];
                _data[7] = _byte[2];
                _data[8] = _byte[4];
                _data[9] = provalue[5];
                _mData.mDate = new int[6];
                _mData.mDate[0] = (_byte[5] & 255) + 2000;
                _mData.mDate[1] = _byte[6] & 255;
                _mData.mDate[2] = _byte[7] & 255;
                _mData.mDate[3] = _byte[8] & 255;
                _mData.mDate[4] = _byte[9] & 255;
                _mData.mDate[5] = _byte[10] & 255;
                CLog.e(this.TAG, " for correction date:" + _dateCompare + "blood pressure: " + _byte[6]);
            } else {
                _data[0] = _byte[5];
                _data[1] = _byte[6];
                _data[2] = _byte[7];
                _data[3] = _byte[8];
                _data[4] = _byte[9];
                _data[5] = _byte[0];
                _data[6] = _byte[1];
                _data[7] = _byte[2];
                _data[8] = _byte[4];
                _data[9] = _byte[10];
                _mData.mDate = new int[6];
                _mData.mDate[0] = (_byte[5] & 255) + 2000;
                _mData.mDate[1] = _byte[6] & 255;
                _mData.mDate[2] = _byte[7] & 255;
                _mData.mDate[3] = _byte[8] & 255;
                _mData.mDate[4] = _byte[9] & 255;
                _mData.mDate[5] = _byte[10] & 255;
                String str = this.TAG;
                StringBuilder sb = new StringBuilder(" Time is correct; time has not been adjusted:");
                CLog.e(str, sb.append(PageUtil.process_Date((_byte[5] & 255) + 2000, _byte[6] & 255, _byte[7] & 255, _byte[8] & 255, _byte[9] & 255, _byte[10] & 255)).append("blood pressure: ").append(_byte[6]).toString());
            }
            _mData.setmUploadType("trend");
            _mData.setmDataType("contec08aw");
            _mData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
            _mData.setSaveDate();
            _mData.mDataList.add(_data);
            if (_mData.mDataList.size() == 10) {
                DatasContainer.mDeviceDatas.add(_mData);
                _mData.mDataList.clear();
            }
            DeviceManager.mDeviceBeanList.mProgress = (((i + 1) * 40) / this.mSizeBP) + 20;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
            if (i == _size - 1) {
                if (_mData.mDataList.size() > 0) {
                    DatasContainer.mDeviceDatas.add(_mData);
                    _mData.mDataList.clear();
                }
                int hiPre = ((_data[5] << 8) | (_data[6] & 255)) & 65535;
                int lowPre = _data[7] & 255;
                DeviceListDaoOperation.getInstance().updateReceiveDataStr(DeviceManager.m_DeviceBean.mMacAddr, PageUtil.processDate((_data[0] & 255) + 2000, _data[1] & 255, _data[2] & 255, _data[3] & 255, _data[4] & 255, _data[5] & 255), String.valueOf(hiPre) + ";" + lowPre);
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(new EventShowLastData());
            }
        }
    }

    private void save_bloodpressure_data(ArrayList<byte[]> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            byte[] _byte = dataList.get(i);
            byte[] _data = {_byte[5], _byte[6], _byte[7], _byte[8], _byte[9], _byte[0], _byte[1], _byte[2], _byte[4], _byte[10]};
            byte b = (byte) (((_data[5] << 8) | (_data[6] & 255)) & 65535);
            byte b2 = (byte) (_data[7] & 255);
        }
    }

    public String dateToString(int[] mDate) {
        String _temp = bs.b;
        for (int i = 0; i < mDate.length; i++) {
            _temp = String.valueOf(_temp) + byte2String((byte) mDate[i]);
        }
        return _temp;
    }

    public String byte2String(byte pack) {
        if (pack < 10) {
            return "0" + pack;
        }
        return new StringBuilder().append(pack).toString();
    }

    public static boolean isUploaded(String path, String pTimeString) {
        StringBuffer sb = null;
        try {
            File file = new File(path);
            if (!file.exists() || file.isDirectory()) {
                throw new Exception();
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuffer sb2 = new StringBuffer();
            try {
                for (String temp = br.readLine(); temp != null; temp = br.readLine()) {
                    sb2.append(String.valueOf(temp) + " ");
                }
                sb = sb2;
            } catch (Exception e) {
                sb = sb2;
            }
            String allString = bs.b;
            if (sb != null) {
                allString = sb.toString();
            }
            return allString.contains(pTimeString);
        } catch (Exception e2) {
        }
        return false;
    }
}
