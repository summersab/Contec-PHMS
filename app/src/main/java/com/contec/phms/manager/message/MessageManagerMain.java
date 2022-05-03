package com.contec.phms.manager.message;

import android.os.Message;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.Constants;

public class MessageManagerMain {
    public static final int BluetoothLeDeviceService_cms50k = 30;

    public static void msg_ConnectNextDeviceOrPollingOrCallBack() {
        Message msgManager = new Message();
        msgManager.what = 66;
        msgManager.arg2 = 14;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgManager);
        msg_ChangeUI();
    }

    public static void msg_BleDisconnect() {
        Message message = new Message();
        message.arg2 = Constants.EVENT_BLUETOOTHLEDEVICESERVICE_CMS50K;
        message.what = Constants.EVENT_BLEDISCONNECT;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(message);
    }

    public static void msg_ChangeUI() {
        Message message = new Message();
        message.arg2 = 1;
        message.what = Constants.EVENT_DEVICEMANAGERSTATE;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(message);
    }

    public static void msg_ui_receive_data_fail() {
        DeviceManager.mDeviceBeanList.mState = 5;
        DeviceManager.m_DeviceBean.mState = 5;
        DeviceManager.m_DeviceBean.mProgress = 0;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
    }

    public static void msg_ui_connect_device_fail() {
        DeviceManager.mDeviceBeanList.mState = 3;
        DeviceManager.m_DeviceBean.mState = 3;
        DeviceManager.m_DeviceBean.mProgress = 0;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
    }

    public static void msg_ui_connect_device_success() {
        DeviceManager.mDeviceBeanList.mState = 2;
        DeviceManager.m_DeviceBean.mState = 2;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
    }

    public static void msg_ui_receive_data_success() {
        DeviceManager.m_DeviceBean.mState = 6;
        DeviceManager.mDeviceBeanList.mState = 6;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
    }

    public static void msg_ui_upload_dataing() {
        DeviceManager.m_DeviceBean.mState = 7;
        DeviceManager.mDeviceBeanList.mState = 7;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
    }

    public static void msg_ui_no_new_data() {
        DeviceManager.m_DeviceBean.mState = 10;
        DeviceManager.m_DeviceBean.mProgress = 0;
        DeviceService.mReceiveFinished = true;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
    }

    public static void msg_ui_receive_dataing() {
        DeviceManager.m_DeviceBean.mState = 4;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
    }
}
