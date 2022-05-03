package com.contec.phms.device.wt;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DataFilter;
import com.contec.phms.device.template.SendCommand;
import com.contec.phms.manager.device.DeviceManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import cn.com.contec.jar.wt100.DeviceCommand;

public class DeviceService extends com.contec.phms.device.template.DeviceService {
    private boolean ISPRINTTAG = false;
    Handler mhandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (DeviceService.this.processtime == msg.what && ReceiveThread.RECEIVER_TIME == 0) {
                SendCommand.send(DeviceCommand.command_VerifyTime());
                DeviceService.this.writeToSDCard(String.valueOf(DeviceService.this.getcureentbytetime()) + "\t\t第一次发送了对时的命令 无 回应，再发送\t\t对时命令");
            }
        }
    };
    private int processtime = 100000;

    public void initObjects() {
        this.mReceiveThread = new ReceiveThread(this);
        this.mDataFilter = new DataFilter();
    }

    public void sendCommand() {
        byte[] _cmd = DeviceCommand.command_VerifyTime();
        DeviceManager.m_DeviceBean.mProgress = 0;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ReceiveThread.RECEIVER_TIME = 0;
        ReceiveThread.RECEIVER_DATA = 0;
        ReceiveThread.RECEIVER_DELETE = 0;
        SendCommand.send(_cmd);
        writeToSDCard(String.valueOf(getcureentbytetime()) + "\t\t发送\t\t对时命令");
        this.mhandler.sendEmptyMessageDelayed(this.processtime, 500);
    }

    public void saveSDCard() {
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
