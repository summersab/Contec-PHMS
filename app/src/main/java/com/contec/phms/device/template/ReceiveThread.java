package com.contec.phms.device.template;

import android.os.Message;
import com.contec.phms.App_phms;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.device.ServiceBean;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public abstract class ReceiveThread extends Thread {
    public static final int MAX_COUNT = 15;
    public final String TAG = "ReceiveThread";
    protected int mCount;
    protected DeviceService mDeviceService;
    protected InputStream mIS = null;
    protected byte[] mPack;
    protected int mPackLen;
    private Timer mTimer;
    private boolean starttimer = false;

    public abstract void arrangeMessage(byte[] bArr, int i);

    public ReceiveThread(DeviceService mDeviceService2) {
        this.mDeviceService = mDeviceService2;
    }

    public void run() {
        setName("temple--recevicetread");
        byte[] buf = new byte[1024];
        this.mPack = new byte[1024];
        this.mCount = 0;
        this.mPackLen = 0;
        CLog.i("ReceiveThread", "Start get  Inputstream  =====");
        try {
            this.mIS = ServiceBean.getInstance().mSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            CLog.i("ReceiveThread", "Start get  Inputstream  异常=====");
        }
        this.starttimer = false;
        CLog.i("ReceiveThread", "Receiveing=====");
        while (!DeviceService.mReceiveFinished) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            try {
                timer();
                CLog.i("ReceiveThread", "Receiveing=====buf长度" + buf.length);
                int length = this.mIS.read(buf);
                if (this.mTimer != null) {
                    this.mTimer.cancel();
                    this.mTimer.purge();
                    this.mTimer = null;
                }
                arrangeMessage(buf, length);
            } catch (Exception e2) {
                e2.printStackTrace();
                DeviceService.mReceiveFinished = true;
                if (!this.starttimer) {
                    DeviceManager.mDeviceBeanList.mState = 5;
                    DeviceManager.m_DeviceBean.mState = 5;
                    DeviceManager.m_DeviceBean.mProgress = 0;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    Message msgManager = new Message();
                    msgManager.what = 41;
                    msgManager.arg2 = 4;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgManager);
                }
            }
        }
        if (this.mTimer != null) {
            this.mTimer.cancel();
            if (this.mTimer != null) {
                this.mTimer.purge();
            }
            this.mTimer = null;
        }
        if (!this.starttimer) {
            try {
                if (this.mIS != null) {
                    this.mIS.close();
                }
                if (ServiceBean.getInstance().mSocket != null) {
                    CLog.i("ReceiveThread", "Receive  mSocket=====，关闭socket流");
                    ServiceBean.getInstance().mSocket.close();
                }
            } catch (IOException e3) {
                e3.printStackTrace();
                CLog.i("ReceiveThread", "Receive  mSocket=====22222");
            }
            CLog.i("ReceiveThread", "Receive  Over=====");
            if (DeviceManager.m_DeviceBean != null && !DeviceManager.m_DeviceBean.mDeviceName.equalsIgnoreCase("SP10W")) {
                Constants.isSuccessOperationDevice = true;
                DeviceService.nextStep();
            }
        }
    }

    private void timer() {
        this.mTimer = new Timer();
        this.mTimer.schedule(new TimerTask() {
            public void run() {
                CLog.e("ReceiveThread", "receive timer wait over **********************");
                DeviceService.mReceiveFinished = true;
                ReceiveThread.this.starttimer = true;
                try {
                    ReceiveThread.interrupted();
                    if (ReceiveThread.this.mIS != null) {
                        ReceiveThread.this.mIS.close();
                    }
                    if (ServiceBean.getInstance().mSocket != null) {
                        CLog.i("ReceiveThread", "关闭socket桥梁-----");
                        ServiceBean.getInstance().mSocket.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (ReceiveThread.this.mTimer != null) {
                    CLog.e("ReceiveThread", "mtime clear --------");
                    if (ReceiveThread.this.mTimer != null) {
                        ReceiveThread.this.mTimer.cancel();
                    }
                    if (ReceiveThread.this.mTimer != null) {
                        ReceiveThread.this.mTimer.purge();
                    }
                    ReceiveThread.this.mTimer = null;
                }
                DeviceManager.mDeviceBeanList.mState = 5;
                DeviceManager.m_DeviceBean.mState = 5;
                DeviceManager.m_DeviceBean.mProgress = 0;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                DeviceService.mReceiveFinished = true;
                DeviceService.nextStep();
                Constants.isSuccessOperationDevice = false;
                CLog.e("ReceiveThread", "receive  timer  Over =====");
            }
        }, 20000);
    }

    public void timeOut() {
        try {
            ServiceBean.getInstance().mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
