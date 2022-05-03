package com.contec.phms.device.wt;

import android.os.Environment;
import android.os.Message;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.db.DeviceListDaoOperation;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import com.contect.jar.blewt.CommandHealthy;
import com.contect.jar.blewt.PackManagerHealthy;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import cn.com.contec.jar.wt100.WTDeviceDataJar;
import u.aly.bs;

public class BluetoothLeDeviceService extends com.contec.phms.device.template.BluetoothLeDeviceService {
    private final String TAG = "BluetoothLeDeviceService_wt";
    private byte arrangeMessage;
    private boolean isHaveData = false;
    private ArrayList<WTDeviceDataJar> mArrayWtData = new ArrayList<>();
    DeviceData mDeviceData = new DeviceData();
    private PackManagerHealthy packManager;

    public void onCreate() {
        super.onCreate();
        this.packManager = new PackManagerHealthy();
        CLog.e("WT--DeviceService", "体重秤开启服务");
    }

    public void bleServiceNofityOpen() {
        sendData(CommandHealthy.SET_TIME());
    }

    public void arrangeMessage(byte[] buf, int length) {
        this.arrangeMessage = this.packManager.arrangeMessage(buf, length);
        if (!(this.arrangeMessage == 0 || (this.arrangeMessage & 255) == 136)) {
            CLog.e("jar返回的信息", Integer.toHexString(this.arrangeMessage));
        }
        switch (this.arrangeMessage) {
            case 16:
                sendData(CommandHealthy.GET_STRONGE(0));
                return;
            case 17:
                CLog.e("=========", "：：：：：对时失败==");
                sendData(CommandHealthy.SET_TIME());
                return;
            case 18:
                byte b = this.packManager.mWeight.datanumber[1];
                if (this.packManager.mWeight.datanumber[2] > 0) {
                    sendData(CommandHealthy.GET_SINGLE_DATA(0));
                    return;
                } else {
                    noNewData();
                    return;
                }
            case 19:
                sendData(CommandHealthy.GET_ALL_DATA(0));
                return;
            case 21:
                sendData(CommandHealthy.GET_SINGLE_DATA(1));
                return;
            case 22:
                sendData(CommandHealthy.GET_ALL_DATA(1));
                return;
            case 23:
                sendData(CommandHealthy.GET_DELETE());
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(100);
                            BluetoothLeDeviceService.this.mArrayWtData.clear();
                            BluetoothLeDeviceService.this.mArrayWtData = BluetoothLeDeviceService.this.packManager.m_DeviceDatas;
                            BluetoothLeDeviceService.this.parseWeightData(BluetoothLeDeviceService.this.mArrayWtData);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return;
            case 32:
                LogI("删除数据成功");
                return;
            case 33:
                sendData(CommandHealthy.GET_DELETE());
                LogI("删除数据失败");
                return;
            default:
                return;
        }
    }

    private void addData(DeviceData mDeviceData2) {
        DatasContainer.mDeviceDatas.add(mDeviceData2);
        update_db(mDeviceData2);
    }

    private void update_db(DeviceData mDeviceData2) {
        String m_saveDate = ((WTDeviceDataJar) mDeviceData2.mDataList.get(mDeviceData2.mDataList.size() - 1)).m_saveDate;
        LogI("日期：m_saveDate: " + m_saveDate);
        DeviceListDaoOperation.getInstance().updateReceiveDataStr(DeviceManager.m_DeviceBean.mMacAddr, m_saveDate, bs.b);
    }

    private void parseWeightData(ArrayList<WTDeviceDataJar> _arraylistWTData) {
        writeToSDCard(String.valueOf(getcureentbytetime()) + "\t\t接收\t\t已经接收到体重数据 ，总共" + _arraylistWTData.size() + "条");
        if (_arraylistWTData.size() > 9) {
            int _saveNum = 0;
            for (int i = 0; i < _arraylistWTData.size(); i++) {
                _saveNum++;
                WTDeviceDataJar _data = _arraylistWTData.get(i);
                this.mDeviceData.mDataList.add(_data);
                String _receiveStr = _data.m_data;
                String _receiveDate = PageUtil.processDate(_data.m_iYear + 2000, _data.m_iMonth, _data.m_iDay, _data.m_iHour, _data.m_iMinute, _data.m_iSecond);
                CLog.e("BluetoothLeDeviceService_wt", String.valueOf(i) + "  *************大于法制  接收到的数据 _receiveStr: " + _receiveStr + "  time :" + _receiveDate);
                writeToSDCard("\t\t第" + (i + 1) + "数据是    时间:" + _receiveDate + "  体重:" + _receiveStr + "kg");
                if (_saveNum == 9) {
                    CLog.d("BluetoothLeDeviceService_wt", "满10条了，开始存储*************");
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
                    CLog.d("BluetoothLeDeviceService_wt", "结束了 ，存储所有的数据 *************");
                }
                toUpload();
            }
            return;
        }
        for (int i2 = 0; i2 < _arraylistWTData.size(); i2++) {
            WTDeviceDataJar _data2 = _arraylistWTData.get(i2);
            CLog.i("BluetoothLeDeviceService_wt", "原代的  " + _arraylistWTData.get(i2));
            CLog.i("BluetoothLeDeviceService_wt", "转换后的  " + _data2);
            String _receiveStr2 = _data2.m_data;
            String _receiveDate2 = PageUtil.processDate(_arraylistWTData.get(i2).m_iYear + 2000, _arraylistWTData.get(i2).m_iMonth, _arraylistWTData.get(i2).m_iDay, _arraylistWTData.get(i2).m_iHour, _arraylistWTData.get(i2).m_iMinute, _arraylistWTData.get(i2).m_iSecond);
            writeToSDCard("\t\t第" + (i2 + 1) + "数据是    时间:" + _receiveDate2 + "  体重:" + _receiveStr2 + "kg");
            CLog.i("BluetoothLeDeviceService_wt", "*************小于法制  接收到的数据 _receiveStr: " + _receiveStr2 + "  time :" + _receiveDate2);
            this.mDeviceData.mDataList.add(_data2);
        }
        this.mDeviceData.m_receiveDate = _arraylistWTData.get(_arraylistWTData.size() - 1).m_saveDate;
        this.mDeviceData.init();
        this.mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
        DatasContainer.mDeviceDatas.add(this.mDeviceData);
        toUpload();
        this.mDeviceData = null;
    }

    private String getcureentbytetime() {
        return new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss:SSS ").format(new Date(System.currentTimeMillis()));
    }

    public void writeToSDCard(String str) {
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

    protected void toUpload() {
        LogI("开始上传");
        super.toUpload();
        this.isHaveData = false;
    }

    public void onEvent(Message message) {
    }

    public void LogI(String msg) {
        if (Constants.mTestFlag) {
            CLog.i("BluetoothLeDeviceService_wt", msg);
        }
    }
}
