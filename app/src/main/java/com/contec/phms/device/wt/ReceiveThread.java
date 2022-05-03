package com.contec.phms.device.wt;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.device.template.SendCommand;
import com.contec.phms.eventbus.EventShowLastData;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.db.localdata.PluseDataDao;
import com.contec.phms.util.CLog;
import com.contec.phms.util.PageUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import cn.com.contec.jar.wt100.DeviceCommand;
import cn.com.contec.jar.wt100.DevicePackManager;
import cn.com.contec.jar.wt100.WTDeviceDataJar;

public class ReceiveThread extends com.contec.phms.device.template.ReceiveThread {
    static int RECEIVER_DATA = 0;
    static int RECEIVER_DELETE = 0;
    static int RECEIVER_TIME = 0;
    private boolean ISPRINTTAG = false;
    private final String TAG = "WTReceiveThread";
    private String _version = "0";
    private ArrayList<WTDeviceDataJar> mArrayWtData = new ArrayList<>();
    DeviceData mDeviceData = new DeviceData();
    DevicePackManager mDevicePackManager = new DevicePackManager();
    Handler mhandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (100001 == msg.what) {
                if (ReceiveThread.RECEIVER_DATA == 0) {
                    SendCommand.send(DeviceCommand.command_requestData(ReceiveThread.this._version));
                    ReceiveThread.this.writeToSDCard(String.valueOf(ReceiveThread.this.getcureentbytetime()) + "\t\t第一次发送了请求数据的命令 无 回应，再发送一次\t\t请求数据命令");
                }
            } else if (100002 == msg.what && ReceiveThread.RECEIVER_DELETE == 0) {
                SendCommand.send(DeviceCommand.command_delData());
                ReceiveThread.this.writeToSDCard(String.valueOf(ReceiveThread.this.getcureentbytetime()) + "\t\t第一次发送了删除数据的命令 无 回应，再发送一次\t\t删除数据命令");
            }
        }
    };
    private final int processdata = 100001;
    private final int processdele = 100002;

    public ReceiveThread(DeviceService mDeviceService) {
        super(mDeviceService);
    }

    public void arrangeMessage(byte[] buf, int length) {
        HashMap<String, String> _resultmap = this.mDevicePackManager.arrangeMessage(buf, length);
        int state = Integer.valueOf(_resultmap.get(PluseDataDao.RESULT)).intValue();
        CLog.i("WTReceiveThread", "state  = " + state + "体重收到的数据是条数是  = " + this.mDevicePackManager.m_DeviceDatas.size());
        switch (state) {
            case 2:
                this.mhandler.removeMessages(100001);
                RECEIVER_DATA = 1;
                writeToSDCard(String.valueOf(getcureentbytetime()) + "\t\t接收\t\t体重数据,先缓存");
                this.mArrayWtData.clear();
                this.mArrayWtData = this.mDevicePackManager.m_DeviceDatas;
                parseWeightData(this.mArrayWtData);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SendCommand.send(DeviceCommand.command_delData());
                this.mhandler.sendEmptyMessageDelayed(100002, 1500);
                writeToSDCard(String.valueOf(getcureentbytetime()) + "\t\t发送\t\t删除数据命令");
                DeviceManager.m_DeviceBean.mProgress = 50;
                DeviceManager.mDeviceBeanList.mState = 4;
                DeviceManager.m_DeviceBean.mState = 4;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                break;
            case 3:
                RECEIVER_TIME = 1;
                writeToSDCard(String.valueOf(getcureentbytetime()) + "\t\t接收\t\t对时成功");
                this._version = _resultmap.get("version");
                DeviceManager.m_DeviceBean.mProgress = 0;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                SendCommand.send(DeviceCommand.command_requestData(this._version));
                this.mhandler.sendEmptyMessageDelayed(100001, 800);
                writeToSDCard(String.valueOf(getcureentbytetime()) + "\t\t发送\t\t请求数据命令");
                CLog.i("WTReceiveThread", "对时成功,请求数据  版本是  " + this._version);
                break;
            case 4:
                CLog.i("WTReceiveThread", "对时失败");
                RECEIVER_TIME = 1;
                writeToSDCard(String.valueOf(getcureentbytetime()) + "\t\t接收\t\t对时失败");
                if (RECEIVER_TIME == 1) {
                    RECEIVER_TIME = 2;
                    SendCommand.send(DeviceCommand.command_VerifyTime());
                    writeToSDCard(String.valueOf(getcureentbytetime()) + "\t\t第一次对时 失败，再发送\t\t对时命令");
                    break;
                }
                break;
            case 5:
                RECEIVER_DELETE = 1;
                this.mhandler.removeMessages(100002);
                DeviceService.mReceiveFinished = true;
                writeToSDCard(String.valueOf(getcureentbytetime()) + "\t\t接收\t\t删除数据成功");
                DeviceManager.mDeviceBeanList.mState = 6;
                DeviceManager.m_DeviceBean.mState = 6;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                CLog.i("WTReceiveThread", "删除数据成功");
                break;
            case 6:
                RECEIVER_DATA = 1;
                DeviceManager.mDeviceBeanList.mState = 10;
                DeviceManager.m_DeviceBean.mState = 10;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                CLog.i("WTReceiveThread", "无数据");
                writeToSDCard(String.valueOf(getcureentbytetime()) + "\t\t接收\t\t无数据");
                break;
        }
        System.out.println("state=" + state);
    }

    private void parseWeightData(ArrayList<WTDeviceDataJar> _arraylistWTData) {
        writeToSDCard(String.valueOf(getcureentbytetime()) + "\t\t接收\t\t已经接收到体重数据 ，总共" + _arraylistWTData.size() + "条");
        if (_arraylistWTData.size() > 9) {
            int _saveNum = 0;
            for (int i = 0; i < _arraylistWTData.size(); i++) {
                _saveNum++;
                WTDeviceDataJar _data = _arraylistWTData.get(i);
                this.mDeviceData.mDataList.add(_data);
                String _receiveStr = new StringBuilder().append(_data.getUserMeasureWeigth()).toString();
                String _receiveDate = PageUtil.processDate(_data.mPack[0] + 2000, _data.mPack[1], _data.mPack[2], _data.mPack[3], _data.mPack[4], _data.mPack[5]);
                CLog.i("WTReceiveThread", String.valueOf(i) + "  *************大于法制  接收到的数据 _receiveStr: " + _receiveStr + "  time :" + _receiveDate);
                writeToSDCard("\t\t第" + (i + 1) + "数据是    时间:" + _receiveDate + "  体重:" + _receiveStr + "kg");
                if (_saveNum == 9) {
                    CLog.d("WTReceiveThread", "满10条了，开始存储*************");
                    this.mDeviceData.m_receiveDate = _data.m_saveDate;
                    this.mDeviceData.init();
                    this.mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                    DatasContainer.mDeviceDatas.add(this.mDeviceData);
                    this.mDeviceData.mDataList.clear();
                    _saveNum = 0;
                } else if (i == _arraylistWTData.size() - 1) {
                    this.mDeviceData.m_receiveDate = _data.m_saveDate;
                    this.mDeviceData.init();
                    this.mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                    DatasContainer.mDeviceDatas.add(this.mDeviceData);
                    CLog.d("WTReceiveThread", "结束了 ，存储所有的数据 *************");
                }
            }
        } else {
            for (int i2 = 0; i2 < _arraylistWTData.size(); i2++) {
                WTDeviceDataJar _data2 = _arraylistWTData.get(i2);
                CLog.i("WTReceiveThread", "原代的  " + _arraylistWTData.get(i2));
                CLog.i("WTReceiveThread", "转换后的  " + _data2);
                String _receiveStr2 = new StringBuilder().append(_data2.getUserMeasureWeigth()).toString();
                String _receiveDate2 = PageUtil.processDate(_data2.mPack[0] + 2000, _data2.mPack[1], _data2.mPack[2], _data2.mPack[3], _data2.mPack[4], _data2.mPack[5]);
                writeToSDCard("\t\t第" + (i2 + 1) + "数据是    时间:" + _receiveDate2 + "  体重:" + _receiveStr2 + "kg");
                CLog.i("WTReceiveThread", "*************小于法制  接收到的数据 _receiveStr: " + _receiveStr2 + "  time :" + _receiveDate2);
                this.mDeviceData.mDataList.add(_data2);
            }
            this.mDeviceData.m_receiveDate = _arraylistWTData.get(_arraylistWTData.size() - 1).m_saveDate;
            this.mDeviceData.init();
            this.mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
            DatasContainer.mDeviceDatas.add(this.mDeviceData);
            this.mDeviceData = null;
        }
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(new EventShowLastData());
        DeviceManager.m_DeviceBean.mProgress = 50;
        DeviceManager.mDeviceBeanList.mState = 4;
        DeviceManager.m_DeviceBean.mState = 4;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        CLog.i("WTReceiveThread", "接收数据成功");
    }

    public void writeToSDCard(String str) {
        if (this.ISPRINTTAG) {
            String str2 = "\n" + str;
            if ("mounted".equals(Environment.getExternalStorageState())) {
                File directory = new File(Environment.getExternalStorageDirectory(), "CONTEC/WT/");
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                File file = new File(Environment.getExternalStorageDirectory(), "CONTEC/WT/PHMS_TEST_NEW.txt");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    FileOutputStream outStream = new FileOutputStream(file, true);
                    outStream.write(str2.getBytes());
                    outStream.close();
                } catch (FileNotFoundException e2) {
                    e2.printStackTrace();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
    }

    private String getcureentbytetime() {
        return new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss:SSS ").format(new Date(System.currentTimeMillis()));
    }
}
