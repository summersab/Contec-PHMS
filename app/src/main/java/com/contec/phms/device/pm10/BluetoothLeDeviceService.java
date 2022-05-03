package com.contec.phms.device.pm10;

import android.os.Message;
import android.util.Log;
import com.contec.jar.blepm10.CommandEcg;
import com.contec.jar.blepm10.PackManagerEcg;
import com.contec.jar.pm10.DeviceDataECG;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.manager.datas.DataList;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.message.MessageManagerMain;
import com.contec.phms.db.DeviceListDaoOperation;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import u.aly.bs;

public class BluetoothLeDeviceService extends com.contec.phms.device.template.BluetoothLeDeviceService {
    private static final String TAG = "BTLeDeviceService_pm10";
    private byte arrangeMessage;
    private int count;
    private int countno;
    DeviceData mData;
    private DeviceDataECG mECG;
    private PackManagerEcg packManager;

    public void onCreate() {
        super.onCreate();
        LogI("创建服务---PM10BLE服务类");
        this.packManager = new PackManagerEcg();
    }

    public void bleServiceNofityOpen() {
        LogI("对时操作，发送对时命令");
        sendData(CommandEcg.SET_TIME());
        MessageManagerMain.msg_ui_receive_dataing();
    }

    public void arrangeMessage(byte[] buf, int length) {
        this.arrangeMessage = this.packManager.arrangeMessage(buf, length);
        Log.e("jar返回的信息", Integer.toHexString(this.arrangeMessage));
        switch (this.arrangeMessage) {
            case 16:
                sendData(CommandEcg.GET_FRAGMENT_INFOR(3));
                delayTime(300);
                return;
            case 17:
                sendData(CommandEcg.SET_TIME());
                delayTime(300);
                return;
            case 112:
                LogI("心电图片段有,发送心电基本信息");
                MessageManagerMain.msg_ui_receive_dataing();
                this.mECG = new DeviceDataECG();
                sendData(CommandEcg.GET_BASE_INFOR(1));
                delayTime(300);
                return;
            case 113:
                this.mECG = new DeviceDataECG();
                this.countno = ((this.packManager.mEcg.UpLoad_Data[3] << 7) | (this.packManager.mEcg.UpLoad_Data[2] & 255)) & 65535;
                if (this.countno == 0) {
                    noNewData();
                    DeviceService.mReceiveFinished = true;
                    LogI("没有数据");
                    return;
                }
                return;
            case 114:
                LogI("心电一组数据接收中，继续发送下一组数据");
                sendData(CommandEcg.GET_GROUP_DATA(1));
                delayTime(300);
                return;
            case 115:
                LogI("心电一组接收完毕，继续发送片段基本新消息");
                sendData(CommandEcg.GET_GROUP_DATA(127));
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                }
                sendData(CommandEcg.GET_BASE_INFOR(1));
                delayTime(300);
                return;
            case 116:
                LogI("全部心电接收完毕");
                sendData(CommandEcg.GET_GROUP_DATA(127));
                save_DeviceData_ECG();
                toUpload();
                delayTime(300);
                return;
            case 117:
                sendData(CommandEcg.GET_GROUP_DATA(0));
                delayTime(300);
                LogI("心电基本信息回复，请求具体数据");
                return;
            default:
                return;
        }
    }

    private void delayTime(int time) {
        try {
            Thread.sleep((long) time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void save_DeviceData_ECG() {
        ArrayList<DeviceDataECG> _ECGS = this.packManager.mEcgs;
        for (int i = 0; i < _ECGS.size(); i++) {
            DeviceData _ECG = new DeviceData();
            _ECG.setmDataType("ECG(PM10)");
            _ECG.mDate = new int[6];
            _ECG.mDate[0] = _ECGS.get(i).Point_data[0] + 2000;
            _ECG.mDate[1] = _ECGS.get(i).Point_data[1];
            _ECG.mDate[2] = _ECGS.get(i).Point_data[2];
            _ECG.mDate[3] = _ECGS.get(i).Point_data[3];
            _ECG.mDate[4] = _ECGS.get(i).Point_data[4];
            _ECG.mDate[5] = _ECGS.get(i).Point_data[5];
            _ECG.TrendData = _ECGS.get(i).Point_data;
            _ECG.CaseData = _ECGS.get(i).ECG_Data;
            addDataECG(_ECG);
        }
    }

    public void addDataECG(DeviceData deviceData) {
        DeviceData _data = (DeviceData) deviceData;
        if (_data.TrendData == null) {
            CLog.i("***************", "No New Datas");
            return;
        }
        CLog.d("***************", "存储数据***********");
        _data.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
        if (DatasContainer.mDeviceDatas == null) {
            DatasContainer.mDeviceDatas = new DataList();
        }
        DatasContainer.mDeviceDatas.add(deviceData);
        DatasContainer.mDeviceDatas.addCase(deviceData);
        update_db(deviceData);
    }

    public void update_db(DeviceData deviceData) {
        byte[] _temp = ((DeviceData) deviceData).TrendData;
        String _time = (_temp[0] + 2000) + "-" + _temp[1] + "-" + _temp[2] + " " + _temp[3] + ":" + _temp[4] + ":" + _temp[5];
        Date dDate = null;
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dDate = format2.parse(_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DeviceListDaoOperation.getInstance().updateReceiveDataStr(DeviceManager.m_DeviceBean.mMacAddr, format2.format(dDate), bs.b);
    }

    public void onEvent(Message message) {
    }

    public void LogI(String msg) {
        if (Constants.mTestFlag) {
            Log.i(TAG, msg);
        }
    }
}
