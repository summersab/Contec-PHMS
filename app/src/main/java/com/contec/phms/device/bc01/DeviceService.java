package com.contec.phms.device.bc01;

import android.util.Log;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DataFilter;
import com.contec.phms.device.template.SendCommand;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.FileOperation;
import com.example.bm77_bc_code.DeviceCommand;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class DeviceService extends com.contec.phms.device.template.DeviceService {
    public static boolean synchronous = false;

    public void initObjects() {
        this.mReceiveThread = new ReceiveThread(this);
        this.mDataFilter = new DataFilter();
    }

    public void sendCommand() {
        DeviceManager.m_DeviceBean.mProgress = 0;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        FileOperation.writeToSDCard(String.valueOf(getcureentbytetime()) + " 发送校时命令", "BC401");
        SendCommand.send(DeviceCommand.Synchronous_Time_NEW());
        Log.e("时间校正测试", "--------------------->>:发送等待");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!synchronous) {
            FileOperation.writeToSDCard(String.valueOf(getcureentbytetime()) + " 旧设备，重新发送校时命令", "BC401");
            SendCommand.send(DeviceCommand.Synchronous_Time());
        }
        Log.e("时间校正测试", "--------------------->>:等待完毕");
    }

    private String getcureentbytetime() {
        return new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss:SSS ").format(new Date(System.currentTimeMillis()));
    }

    public void saveSDCard() {
    }
}
