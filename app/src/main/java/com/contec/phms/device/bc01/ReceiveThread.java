package com.contec.phms.device.bc01;

import android.util.Log;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.device.template.SendCommand;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.CLog;
import com.contec.phms.util.FileOperation;
import com.example.bm77_bc_code.BC401_Data;
import com.example.bm77_bc_code.BC401_Struct;
import com.example.bm77_bc_code.DeviceCommand;
import com.example.bm77_bc_code.DevicePackManager;
import java.sql.Date;
import java.text.SimpleDateFormat;
import u.aly.bs;

public class ReceiveThread extends com.contec.phms.device.template.ReceiveThread {
    private DeviceData mData;
    private DevicePackManager m_DevicePackManager = new DevicePackManager();

    public ReceiveThread(DeviceService mDeviceService) {
        super(mDeviceService);
    }

    public static String printData(byte[] pack, int count) {
        CLog.i("***********************", "************************");
        String _temp = bs.b;
        for (int i = 0; i < count; i++) {
            _temp = String.valueOf(_temp) + " " + Integer.toHexString(pack[i]);
        }
        return _temp;
    }

    private String getcureentbytetime() {
        return new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss:SSS ").format(new Date(System.currentTimeMillis()));
    }

    public void arrangeMessage(byte[] buf, int length) {
        Log.i("ReceiveThread", "信息返回");
        printData(buf, length);
        byte _receiveNum = this.m_DevicePackManager.arrangeMessage(buf, length);
        Log.i("ReceiveThread", "jar包返回的信息：" + _receiveNum);
        switch (_receiveNum) {
            case 2:
                FileOperation.writeToSDCard(String.valueOf(getcureentbytetime()) + " 校时成功，请求数据", "BC401");
                DeviceService.synchronous = true;
                Log.i("ReceiveThread", "校时成功：" + _receiveNum);
                DeviceManager.mDeviceBeanList.mState = 4;
                DeviceManager.m_DeviceBean.mState = 4;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (length < 14) {
                    SendCommand.send(DeviceCommand.Request_AllData());
                    return;
                } else {
                    SendCommand.send(DeviceCommand.Request_AllData_all());
                    return;
                }
            case 5:
                int version = this.m_DevicePackManager.mVersion;
                if (this.m_DevicePackManager.Percent == 100) {
                    Log.e("输出返回的值", "旧版传统蓝牙的版本号:" + version);
                    FileOperation.writeToSDCard(String.valueOf(getcureentbytetime()) + " 数据返回", "BC401");
                    Log.i("ReceiveThread", "返回数据包：" + _receiveNum);
                    this.mData = new DeviceData();
                    BC401_Data _data = this.m_DevicePackManager.mBc401_Data;
                    this.mData.mDataList = _data.Structs;
                    int _dataSize = this.mData.mDataList.size();
                    for (int j = 0; j < _dataSize; j++) {
                        BC401_Struct _dataBC = (BC401_Struct) this.mData.mDataList.get(j);
                        String _time = (_dataBC.Year + 2000) + "-" + _dataBC.Month + "-" + _dataBC.Date + " " + _dataBC.Hour + ":" + _dataBC.Min + ":" + _dataBC.Sec;
                        String _value = "0" + _dataBC.URO + "0" + _dataBC.BLD + "0" + _dataBC.BIL + "0" + _dataBC.KET + "0" + _dataBC.GLU + "0" + _dataBC.PRO + "0" + _dataBC.PH + "0" + _dataBC.NIT + "0" + _dataBC.LEU + "0" + _dataBC.SG + "0" + _dataBC.VC + "9" + _dataBC.MAL + "9" + _dataBC.CR + "9" + _dataBC.UCA;
                        FileOperation.writeToSDCard("time: " + _time + "  " + _value, "BC401");
                        Log.e("%%%%%%%%%%%%%%", "%%%%%%%%%%%%%%");
                        Log.e("输出不兼容14试纸的数据", "time: " + _time + ";" + _value + ";" + _dataSize);
                        Log.e("%%%%%%%%%%%%%%", "%%%%%%%%%%%%%%");
                    }
                    this.mData.mDate = new int[6];
                    //this.mData.mDate[0] = _data.Structs.get(0).Year;
                    //this.mData.mDate[1] = _data.Structs.get(0).Month;
                    //this.mData.mDate[2] = _data.Structs.get(0).Date;
                    //this.mData.mDate[3] = _data.Structs.get(0).Hour;
                    //this.mData.mDate[4] = _data.Structs.get(0).Min;
                    //this.mData.mDate[5] = _data.Structs.get(0).Sec;
                    this.mData.setmUploadType("trend");
                    this.mData.setmDataType("bc01");
                    this.mData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                    addData(this.mData);
                    DeviceManager.m_DeviceBean.mProgress = this.m_DevicePackManager.Percent;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    Log.i("ReceiveThread", "删除数据" + _receiveNum);
                    SendCommand.send(DeviceCommand.Delete_AllData());
                    return;
                }
                return;
            case 6:
                Log.e("怎么没执行这个0x06", "删除命令呢");
                FileOperation.writeToSDCard(String.valueOf(getcureentbytetime()) + " 数据接收完毕并且删除成功", "BC401");
                Log.i("ReceiveThread", "接收完毕，等待上传：" + _receiveNum);
                DeviceManager.mDeviceBeanList.mState = 6;
                DeviceManager.m_DeviceBean.mState = 6;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                return;
            case 8:
                FileOperation.writeToSDCard(String.valueOf(getcureentbytetime()) + " 无新数据", "BC401");
                Log.i("ReceiveThread", "无新数据：" + _receiveNum);
                DeviceManager.mDeviceBeanList.mState = 10;
                DeviceManager.m_DeviceBean.mState = 10;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                return;
            case 21:
                FileOperation.writeToSDCard(String.valueOf(getcureentbytetime()) + " 数据返回", "BC401");
                this.mData = new DeviceData();
                BC401_Data _alldata = this.m_DevicePackManager.mBc401_Data;
                this.mData.mDataList = _alldata.Structs;
                int _alldataSize = this.mData.mDataList.size();
                Log.e("执行这个里面的数据了吗", "什么情况" + _alldataSize);
                for (int j2 = 0; j2 < _alldataSize; j2++) {
                    BC401_Struct _alldataBC = (BC401_Struct) this.mData.mDataList.get(j2);
                    String _time2 = (_alldataBC.Year + 2000) + "-" + _alldataBC.Month + "-" + _alldataBC.Date + " " + _alldataBC.Hour + ":" + _alldataBC.Min + ":" + _alldataBC.Sec;
                    String _valuenew = String.valueOf(dataLength(_alldataBC.URO1)) + dataLength(_alldataBC.BLD1) + dataLength(_alldataBC.BIL1) + dataLength(_alldataBC.KET1) + dataLength(_alldataBC.GLU1) + dataLength(_alldataBC.PRO1) + dataLength(_alldataBC.PH1) + dataLength(_alldataBC.NIT1) + dataLength(_alldataBC.LEU1) + dataLength(_alldataBC.SG1) + dataLength(_alldataBC.VC1) + dataLength(_alldataBC.MAL1) + dataLength(_alldataBC.CR1) + dataLength(_alldataBC.UCA1);
                    Log.e("%%%%%%%%%%%%%%", "%%%%%%%%%%%%%%");
                    Log.e("输出兼容14试纸的数据", "time: " + _time2 + ";" + _valuenew + ";" + _alldataSize);
                    Log.e("%%%%%%%%%%%%%%", "%%%%%%%%%%%%%%");
                    FileOperation.writeToSDCard("time: " + _time2 + "  " + ("0" + _alldataBC.URO + "0" + _alldataBC.BLD + "0" + _alldataBC.BIL + "0" + _alldataBC.KET + "0" + _alldataBC.GLU + "0" + _alldataBC.PRO + "0" + _alldataBC.PH + "0" + _alldataBC.NIT + "0" + _alldataBC.LEU + "0" + _alldataBC.SG + "0" + _alldataBC.VC + "0" + _alldataBC.MAL + "0" + _alldataBC.CR + "0" + _alldataBC.UCA) + "  " + _valuenew, "BC401");
                }
                this.mData.mDate = new int[6];
                //this.mData.mDate[0] = _alldata.Structs.get(0).Year;
                //this.mData.mDate[1] = _alldata.Structs.get(0).Month;
                //this.mData.mDate[2] = _alldata.Structs.get(0).Date;
                //this.mData.mDate[3] = _alldata.Structs.get(0).Hour;
                //this.mData.mDate[4] = _alldata.Structs.get(0).Min;
                //this.mData.mDate[5] = _alldata.Structs.get(0).Sec;
                this.mData.setmUploadType("trend");
                this.mData.setmDataType("bc01");
                this.mData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                addData(this.mData);
                DeviceManager.m_DeviceBean.mProgress = this.m_DevicePackManager.PercentAll;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                if (this.m_DevicePackManager.PercentAll == 100) {
                    SendCommand.send(DeviceCommand.Delete_AllData());
                    return;
                }
                return;
            default:
                return;
        }
    }

    private String dataLength(int uRO1) {
        if (uRO1 < 10) {
            return "00" + uRO1;
        }
        if (uRO1 >= 10 && uRO1 <= 99) {
            return "0" + uRO1;
        }
        if (uRO1 >= 99) {
            return "0" + uRO1;
        }
        return bs.b;
    }

    public void addData(DeviceData deviceData) {
        DeviceData _data = (DeviceData) deviceData;
        if (_data.mDataList.size() == 0) {
            CLog.i("ReceiveThread", "No New Datas");
        } else if (_data.mDataList.size() > 0) {
            Log.i("$$$$$$$$$$$$$$$$$$$$$$", "$$$$$$$$$$$$$$$$$$$$$$$$$$");
            Log.i("ReceiveThread", "旧版传统蓝牙不兼容14试纸的长度" + _data.mDataList.size());
            Log.i("$$$$$$$$$$$$$$$$$$$$$$", "$$$$$$$$$$$$$$$$$$$$$$$$$$");
            DatasContainer.mDeviceDatas.add(deviceData);
        }
    }
}
