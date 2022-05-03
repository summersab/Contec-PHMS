package com.contec.phms.device.cms50k;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.alibaba.fastjson.asm.Opcodes;
import com.contec.jar.cms50k.DeviceCommand;
import com.contec.jar.cms50k.DeviceDataECG;
import com.contec.jar.cms50k.DevicePackManager;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.device.cms50k.update.Update50KUtils;
import com.contec.phms.device.pm10.DeviceData;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.eventbus.EventFinish;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.message.MessageManagerMain;
import com.contec.phms.manager.message.OrderList;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.JiXinTestUtils;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.PhmsSharedPreferences;
import com.contec.phms.widget.CancelUpdate50kDialog;
import com.contec.phms.widget.DialogClass;
import com.contec.phms.widget.IsUpdate50kDialog;
import com.contec.phms.widget.Update50kDialog;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import u.aly.bs;
import u.aly.dp;

public class BluetoothLeDeviceService extends com.contec.phms.device.template.BluetoothLeDeviceService {
    private static final String TAG = "BluetoothLeDeviceService_cms50k";
    private static final int UPDATE50K_END = 36;
    private static final int UPDATE50K_ING = 35;
    private static final int UPDATE50K_NO = 37;
    private static final int UPDATE_CANCLE = 32;
    private static final int UPDATE_FAILED = 31;
    private static final int UPDATE_SUCCESS = 33;
    private static boolean islast = true;
    private static int update50k_state = 37;
    private int Progress = 0;
    private int _stepEnd;
    private int _stepStart;
    private int dateLength = 0;
    private boolean isHaveData = false;
    private int lastData;
    private boolean mBleConnected = false;
    private CancelUpdate50kDialog mCancelUpdate50kDialog;
    private DeviceDataSpO2Fragment mDataSpO2Fragment;
    private boolean mHaveMoveData = false;
    private IsUpdate50kDialog mIsUpdate50kDialog;
    private boolean mNoData = false;
    private DeviceDataPedometer mPedometer;
    private DeviceDataPedometerMin mPedometerMin;
    private PhmsSharedPreferences mPhmsSharedPreferences;
    private SpO2Fragment mSpO2Fragment;
    private DeviceDataSpo2Point mSpo2Point;
    private boolean mToUpload = false;
    private Update50kDialog mUpdate50kDialog;
    private byte[] mUpdateDatas;
    private DevicePackManager m_DevicePackManager;
    private Context mcontext;
    private LoginUserDao mloginUserInfo;
    private boolean oldWatch = false;
    private int readLen = 32;
    private Timer responseTime;
    private int screen;
    private int updateLength = 0;

    public void onCreate() {
        super.onCreate();
        this.mloginUserInfo = PageUtil.getLoginUserInfo();
        this.screen = getSharedPreferences("preference", 0).getInt("ScreenLight", 2);
        this.mPhmsSharedPreferences = PhmsSharedPreferences.getInstance(this);
        new DatasContainer();
    }

    public void bleServiceNofityOpen() {
        delayTime(500);
        this.isHaveData = false;
        this.mHaveMoveData = false;
        islast = false;
        this.m_DevicePackManager = new DevicePackManager();
        if (this.mToUpload) {
            this.mToUpload = false;
        }
        sendData(DeviceCommand.getCompareVesion());
        DeviceManager.mDeviceBeanList.mState = 4;
        DeviceManager.m_DeviceBean.mState = 4;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
    }

    /* JADX WARNING: type inference failed for: r36v165, types: [byte] */
    /* JADX WARNING: type inference failed for: r36v169, types: [byte] */
    /* JADX WARNING: type inference failed for: r36v173, types: [byte] */
    /* JADX WARNING: type inference failed for: r36v177, types: [byte] */
    /* JADX WARNING: type inference failed for: r36v181, types: [byte] */
    /* JADX WARNING: type inference failed for: r36v185, types: [byte] */
    public void arrangeMessage(byte[] data, int length) {
        cancelResonseTimer();
        responseTimer();
        if (data != null && data.length > 0) {
            byte _back = this.m_DevicePackManager.arrangeMessage(data, data.length);
            if (!(_back == 0 || (_back & 255) == 136)) {
                CLog.e("cms50k", Integer.toHexString(_back));
            }
            CLog.e(TAG, "jar包返回的信息：" + _back);
            switch (_back) {
                case -126:
                    CLog.i(TAG, "连续脉率接收中");
                    if (this.Progress < 60) {
                        this.Progress++;
                        DeviceManager.mDeviceBeanList.mState = 4;
                        DeviceManager.m_DeviceBean.mState = 4;
                        DeviceManager.m_DeviceBean.mProgress = this.Progress;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                        return;
                    }
                    return;
                case -125:
                    CLog.i(TAG, "连续脉率接收完毕");
                    this.Progress = 70;
                    DeviceManager.mDeviceBeanList.mState = 4;
                    DeviceManager.m_DeviceBean.mState = 4;
                    DeviceManager.m_DeviceBean.mProgress = this.Progress;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.mSpO2Fragment.PulseSegment = this.m_DevicePackManager.mSpo2h.PulseSegment;
                    this.mSpO2Fragment.PulseTime = this.m_DevicePackManager.mSpo2h.PulseTime;
                    this.mSpO2Fragment.PulseTime[0] = this.mSpO2Fragment.PulseTime[0] + 2000;
                    sendData(DeviceCommand.GET_FRAGMENT_SIZE(1));
                    return;
                case -112:
                    this.screen = this.mPhmsSharedPreferences.getInt("ScreenLight" + this.mloginUserInfo.mUID, 2);
                    sendData(DeviceCommand.SET_INIT(this.screen, 0));
                    return;
                case -111:
                    setParameterError();
                    return;
                case -110:
                    if (this.Progress < 80) {
                        this.Progress++;
                        DeviceManager.m_DeviceBean.mProgress = this.Progress;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                        return;
                    }
                    return;
                case -109:
                    CLog.i(TAG, "连续血氧接收完毕");
                    this.Progress = 80;
                    DeviceManager.mDeviceBeanList.mState = 4;
                    DeviceManager.m_DeviceBean.mState = 4;
                    DeviceManager.m_DeviceBean.mProgress = this.Progress;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                    this.mSpO2Fragment.Spo2Segment = this.m_DevicePackManager.mSpo2h.Spo2Segment;
                    for (int j = 0; j < this.mSpO2Fragment.Spo2Segment.length; j++) {
                        Log.e("wsd1", "血氧数据:" + this.mSpO2Fragment.Spo2Segment[j]);
                    }
                    this.mSpO2Fragment.Spo2Time = this.m_DevicePackManager.mSpo2h.Spo2Time;
                    this.mSpO2Fragment.Spo2Time[0] = this.mSpO2Fragment.Spo2Time[0] + 2000;
                    sendData(DeviceCommand.GetCode(this.m_DevicePackManager.mSpo2h.Spo2Segment));
                    return;
                case -80:
                    this.mSpO2Fragment = new SpO2Fragment();
                    this.mDataSpO2Fragment = new DeviceDataSpO2Fragment();
                    sendData(DeviceCommand.GET_PULSE_FRAGMENT(0));
                    this.isHaveData = true;
                    return;
                case -79:
                    if (this.isHaveData) {
                        this.Progress = 100;
                        DeviceManager.mDeviceBeanList.mState = 4;
                        DeviceManager.m_DeviceBean.mState = 4;
                        DeviceManager.m_DeviceBean.mProgress = this.Progress;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                        DeviceManager.mDeviceBeanList.mState = 6;
                        DeviceManager.m_DeviceBean.mState = 6;
                        DeviceManager.m_DeviceBean.mProgress = 0;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                        toUpload();
                        return;
                    }
                    noNewData();
                    return;
                case -78:
                    sendData(DeviceCommand.GET_SPO2_FRAGMENT(0));
                    return;
                case -77:
                    if (this.isHaveData) {
                        this.Progress = 100;
                        DeviceManager.mDeviceBeanList.mState = 6;
                        DeviceManager.m_DeviceBean.mState = 6;
                        DeviceManager.m_DeviceBean.mProgress = 0;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                        toUpload();
                        return;
                    }
                    noNewData();
                    return;
                case -76:
                    CLog.e(TAG, "连续体动有数据");
                    this.mHaveMoveData = true;
                    sendData(DeviceCommand.GET_MOVE_FRAGMENT(0));
                    return;
                case -75:
                    CLog.e(TAG, "连续体动无数据");
                    dealSpO2Fragment(this.mSpO2Fragment);
                    sendData(DeviceCommand.DEL_CONTINUOUS_DATA(0));
                    return;
                case -74:
                    this.Progress = 100;
                    DeviceManager.mDeviceBeanList.mState = 4;
                    DeviceManager.m_DeviceBean.mState = 4;
                    DeviceManager.m_DeviceBean.mProgress = this.Progress;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    this.mSpO2Fragment.MovementPoint = this.m_DevicePackManager.mSpo2h.MovementPoint;
                    this.mSpO2Fragment.MovementTime = this.m_DevicePackManager.mSpo2h.MovementTime;
                    this.mSpO2Fragment.MovementTime[0] = this.mSpO2Fragment.MovementTime[0] + 2000;
                    this.mSpO2Fragment.MovementStart = this.m_DevicePackManager.mSpo2h.MovementStart;
                    this.mSpO2Fragment.MovementEnd = this.m_DevicePackManager.mSpo2h.MovementEnd;
                    dealSpO2Fragment(this.mSpO2Fragment);
                    DeviceManager.mDeviceBeanList.mState = 6;
                    DeviceManager.m_DeviceBean.mState = 6;
                    DeviceManager.m_DeviceBean.mProgress = 0;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    sendData(DeviceCommand.DEL_CONTINUOUS_DATA(0));
                    return;
                case -64:
                    Log.e(TAG, "删除连续脉率成功");
                    sendData(DeviceCommand.DEL_CONTINUOUS_DATA(1));
                    return;
                case -63:
                    Log.e(TAG, "删除连续血氧成功");
                    if (this.mHaveMoveData) {
                        sendData(DeviceCommand.DEL_CONTINUOUS_DATA(2));
                        return;
                    }
                    this.Progress = 100;
                    DeviceManager.mDeviceBeanList.mState = 4;
                    DeviceManager.m_DeviceBean.mState = 4;
                    DeviceManager.m_DeviceBean.mProgress = this.Progress;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    DeviceManager.mDeviceBeanList.mState = 6;
                    DeviceManager.m_DeviceBean.mState = 6;
                    DeviceManager.m_DeviceBean.mProgress = 0;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    toUpload();
                    return;
                case -62:
                    this.Progress = 100;
                    Log.e(TAG, "删除体动数据成功");
                    DeviceManager.mDeviceBeanList.mState = 4;
                    DeviceManager.m_DeviceBean.mState = 4;
                    DeviceManager.m_DeviceBean.mProgress = this.Progress;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    DeviceManager.mDeviceBeanList.mState = 6;
                    DeviceManager.m_DeviceBean.mState = 6;
                    DeviceManager.m_DeviceBean.mProgress = 0;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    toUpload();
                    return;
                case -49:
                    this.mSpO2Fragment.mCode = this.m_DevicePackManager.mSpo2h.mCodedata;
                    sendData(DeviceCommand.GET_FRAGMENT_SIZE(2));
                    return;
                case 16:
                    this._stepStart = 0;
                    this._stepEnd = 0;
                    if (this.mloginUserInfo.mAmactivity != null && !this.mloginUserInfo.mAmactivity.equalsIgnoreCase(bs.b)) {
                        this._stepStart = Integer.parseInt(this.mloginUserInfo.mAmactivity);
                    }
                    if (this.mloginUserInfo.mPmactivity != null && !this.mloginUserInfo.mPmactivity.equalsIgnoreCase(bs.b)) {
                        this._stepEnd = Integer.parseInt(this.mloginUserInfo.mPmactivity);
                    }
                    if (this._stepStart == 0 && this._stepEnd == 0) {
                        this._stepStart = 8;
                        this._stepEnd = 20;
                    }
                    this.m_DevicePackManager.mSpo2h.MovementStart = this._stepStart;
                    this.m_DevicePackManager.mSpo2h.MovementEnd = this._stepEnd;
                    if (this.oldWatch) {
                        sendData(DeviceCommand.SET_STEP_TIME(0, 24));
                        return;
                    } else {
                        sendData(DeviceCommand.SET_STEP_TIME(this._stepStart, this._stepEnd));
                        return;
                    }
                case 17:
                    setParameterError();
                    return;
                case 21:
                    CLog.e(TAG, "升级文件校验成功-开始给设备发送升级需要的数据---");
                    byte[] pack_15 = new byte[42];
                    System.arraycopy(this.mUpdateDatas, this.readLen, pack_15, 0, 42);
                    byte[] aa = DeviceCommand.setInitDate(Update50KUtils.doPackUpdateData(pack_15));
                    this.readLen += 42;
                    byte[] pack11 = new byte[20];
                    byte[] pack12 = new byte[20];
                    byte[] pack13 = new byte[10];
                    System.arraycopy(aa, 0, pack11, 0, 20);
                    sendData(pack11);
                    delayTime(20);
                    System.arraycopy(aa, 20, pack12, 0, 20);
                    sendData(pack12);
                    delayTime(20);
                    System.arraycopy(aa, 40, pack13, 0, 10);
                    sendData(pack13);
                    this.dateLength++;
                    return;
                case 22:
                    CLog.i("info", "=========0x16===========");
                    CLog.e(TAG, "升级文件校验失败");
                    return;
                case 24:
                    sendData(DeviceCommand.GET_DATA_SIZE(0));
                    return;
                case Opcodes.ALOAD:
                    setParameterError();
                    return;
                case 32:
                    int _weight = 0;
                    if (this.mloginUserInfo.mWeight != null && !this.mloginUserInfo.mWeight.equalsIgnoreCase(bs.b)) {
                        _weight = (int) (Double.parseDouble(this.mloginUserInfo.mWeight) * 1000.0d);
                    }
                    sendData(DeviceCommand.SET_WEIGHT(_weight));
                    return;
                case 33:
                    setParameterError();
                    return;
                case 37:
                    CLog.e(TAG, "接收成功继续写入数据-dateLength:" + this.dateLength + " updateLength:" + this.updateLength);
                    if (islast) {
                        CLog.e(TAG, "最后一组数据接收成功");
                        Update50KUtils.flagClose = true;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(new EventFinish("1", 100));
                        return;
                    }
                    if (this.dateLength == this.updateLength) {
                        byte[] pack = new byte[42];
                        byte[] cmd = new byte[50];
                        cmd[0] = -75;
                        System.arraycopy(this.mUpdateDatas, this.readLen, pack, 0, this.lastData);
                        for (int i = 1; i < 7; i++) {
                            for (int j2 = 0; j2 < 7; j2++) {
                                cmd[i] = (byte) (cmd[i] | ((pack[((i - 1) * 7) + j2] & 128) >> (7 - j2)));
                            }
                            cmd[i] = (byte) (cmd[i] & Byte.MAX_VALUE);
                        }
                        for (int i2 = 0; i2 < 42; i2++) {
                            cmd[i2 + 7] = (byte) (pack[i2] & Byte.MAX_VALUE);
                        }
                        for (int i3 = 0; i3 < 49; i3++) {
                            cmd[49] = (byte) (cmd[49] + cmd[i3]);
                        }
                        cmd[49] = (byte) (cmd[49] & Byte.MAX_VALUE);
                        byte[] pack21 = new byte[20];
                        byte[] pack22 = new byte[20];
                        byte[] pack23 = new byte[10];
                        System.arraycopy(cmd, 0, pack21, 0, 20);
                        sendData(pack21);
                        delayTime(50);
                        System.arraycopy(cmd, 20, pack22, 0, 20);
                        sendData(pack22);
                        delayTime(50);
                        System.arraycopy(cmd, 40, pack23, 0, 10);
                        sendData(pack23);
                        delayTime(100);
                        islast = true;
                        CLog.e(TAG, "最后一组数据");
                        return;
                    }
                    CLog.e(TAG, "接收成功继续写入数据===========");
                    byte[] pack_25 = new byte[42];
                    System.arraycopy(this.mUpdateDatas, this.readLen, pack_25, 0, 42);
                    byte[] aa1 = DeviceCommand.setInitDate(Update50KUtils.doPackUpdateData(pack_25));
                    this.readLen += 42;
                    this.dateLength++;
                    byte[] pack212 = new byte[20];
                    byte[] pack222 = new byte[20];
                    byte[] pack232 = new byte[10];
                    System.arraycopy(aa1, 0, pack212, 0, 20);
                    sendData(pack212);
                    delayTime(50);
                    System.arraycopy(aa1, 20, pack222, 0, 20);
                    sendData(pack222);
                    delayTime(50);
                    System.arraycopy(aa1, 40, pack232, 0, 10);
                    sendData(pack232);
                    int a = 0;
                    if (Update50KUtils.mUpdateFileSize != 0) {
                        a = (int) (((long) (this.readLen * 100)) / Update50KUtils.mUpdateFileSize);
                    }
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(new EventFinish("0", a));
                    return;
                case OrderList.DS_PROCCESS_RESULT /*38*/:
                    CLog.e(TAG, "重新发送数据---");
                    if (this.dateLength == this.updateLength) {
                        byte[] pack2 = new byte[42];
                        byte[] cmd2 = new byte[50];
                        cmd2[0] = -75;
                        System.arraycopy(this.mUpdateDatas, this.readLen - 42, pack2, 0, this.lastData);
                        for (int i4 = 1; i4 < 7; i4++) {
                            for (int j3 = 0; j3 < 7; j3++) {
                                cmd2[i4] = (byte) (cmd2[i4] | ((pack2[((i4 - 1) * 7) + j3] & 128) >> (7 - j3)));
                            }
                            cmd2[i4] = (byte) (cmd2[i4] & Byte.MAX_VALUE);
                        }
                        for (int i5 = 0; i5 < 42; i5++) {
                            cmd2[i5 + 7] = (byte) (pack2[i5] & Byte.MAX_VALUE);
                        }
                        for (int i6 = 0; i6 < 49; i6++) {
                            cmd2[49] = (byte) (cmd2[49] + cmd2[i6]);
                        }
                        cmd2[49] = (byte) (cmd2[49] & Byte.MAX_VALUE);
                        byte[] pack213 = new byte[20];
                        byte[] pack223 = new byte[20];
                        byte[] pack233 = new byte[10];
                        System.arraycopy(cmd2, 0, pack213, 0, 20);
                        sendData(pack213);
                        delayTime(50);
                        System.arraycopy(cmd2, 20, pack223, 0, 20);
                        sendData(pack223);
                        delayTime(50);
                        System.arraycopy(cmd2, 40, pack233, 0, 10);
                        sendData(pack233);
                        delayTime(100);
                        islast = true;
                        CLog.e(TAG, "最后一组数据");
                        return;
                    }
                    byte[] pack_26 = new byte[42];
                    System.arraycopy(this.mUpdateDatas, this.readLen - 42, pack_26, 0, 42);
                    byte[] aa12 = DeviceCommand.setInitDate(Update50KUtils.doPackUpdateData(pack_26));
                    this.readLen += 42;
                    byte[] pack214 = new byte[20];
                    byte[] pack224 = new byte[20];
                    byte[] pack234 = new byte[10];
                    System.arraycopy(aa12, 0, pack214, 0, 20);
                    sendData(pack214);
                    delayTime(40);
                    System.arraycopy(aa12, 20, pack224, 0, 20);
                    sendData(pack224);
                    delayTime(40);
                    System.arraycopy(aa12, 40, pack234, 0, 10);
                    sendData(pack234);
                    int a2 = 0;
                    if (Update50KUtils.mUpdateFileSize != 0) {
                        a2 = (int) (((long) (this.readLen * 100)) / Update50KUtils.mUpdateFileSize);
                    }
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(new EventFinish("0", a2));
                    return;
                case OrderList.DS_FINISHED /*39*/:
                    CLog.e(TAG, "终止升级");
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(new EventFinish("2"));
                    setUpdatState(31);
                    return;
                case 40:
                    byte[] pack1 = new byte[16];
                    CLog.e("jxx", "mUpdateDatas长度:" + this.mUpdateDatas.length);
                    if (this.mUpdateDatas != null) {
                        System.arraycopy(this.mUpdateDatas, 0, pack1, 0, 16);
                        byte[] back0 = Update50KUtils.doPackIsUpdateCommand(pack1);
                        int fileSize = (((pack1[11] & 255) << 24) | ((pack1[10] & 255) << dp.n) | ((pack1[9] & 255) << 8) | (pack1[8] & 255)) & 16777215;
                        CLog.e("jxx", "文件大小：" + fileSize);
                        this.updateLength = fileSize / 42;
                        this.lastData = fileSize % 42;
                        CLog.e("jxx", "文件大小：" + fileSize + " updateLength:" + this.updateLength + " lastData: " + this.lastData);
                        if (back0.length > 1) {
                            CLog.e(TAG, "给设备发送命令是否需要升级就是发送B4命令。。。");
                            sendData(DeviceCommand.setUpdate(back0));
                            return;
                        }
                        CLog.e(TAG, "这里不能进行升级    因为木有下载完成数据");
                        return;
                    }
                    return;
                case Opcodes.FALOAD:
                    int targetCalorie = 0;
                    if (this.mloginUserInfo.mSportTargetCal != null && !this.mloginUserInfo.mSportTargetCal.equalsIgnoreCase(bs.b)) {
                        targetCalorie = Integer.parseInt(this.mloginUserInfo.mSportTargetCal);
                    }
                    if (this.oldWatch) {
                        sendData(DeviceCommand.GET_DATA_SIZE(0));
                        return;
                    } else {
                        sendData(DeviceCommand.SET_TARGET_CALORIE(targetCalorie));
                        return;
                    }
                case 49:
                    setParameterError();
                    return;
                case 64:
                    sendData(DeviceCommand.GET_SPO2_POINT(0));
                    this.isHaveData = true;
                    this.Progress = 10;
                    DeviceManager.mDeviceBeanList.mState = 4;
                    DeviceManager.m_DeviceBean.mState = 4;
                    DeviceManager.m_DeviceBean.mProgress = this.Progress;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    return;
                case 65:
                    sendData(DeviceCommand.GET_DATA_SIZE(1));
                    return;
                case OrderList.DM_NEXT_DEVICE_OR_POLLING /*66*/:
                    sendData(DeviceCommand.GET_SPO2_POINT(1));
                    if (this.Progress < 20) {
                        this.Progress++;
                    }
                    DeviceManager.mDeviceBeanList.mState = 4;
                    DeviceManager.m_DeviceBean.mState = 4;
                    DeviceManager.m_DeviceBean.mProgress = this.Progress;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    return;
                case 67:
                    this.Progress = 20;
                    DeviceManager.mDeviceBeanList.mState = 4;
                    DeviceManager.m_DeviceBean.mState = 4;
                    DeviceManager.m_DeviceBean.mProgress = this.Progress;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    sendData(DeviceCommand.GET_SPO2_POINT(127));
                    if (this.m_DevicePackManager.mSpo2h.Spo2Point.size() < 30) {
                        this.mSpo2Point = new DeviceDataSpo2Point();
                        this.mSpo2Point.mDataList = this.m_DevicePackManager.mSpo2h.Spo2Point;
                        addDataPoint(this.mSpo2Point);
                    } else {
                        int _size = this.m_DevicePackManager.mSpo2h.Spo2Point.size();
                        ArrayList<Object> _dataAll = this.m_DevicePackManager.mSpo2h.Spo2Point;
                        this.mSpo2Point = new DeviceDataSpo2Point();
                        for (int i7 = 0; i7 < _size; i7++) {
                            this.mSpo2Point.mDataList.add(_dataAll.get(i7));
                            if (i7 == _size - 1) {
                                addDataPoint(this.mSpo2Point);
                            } else if (this.mSpo2Point.mDataList.size() == 30) {
                                addDataPoint(this.mSpo2Point);
                                this.mSpo2Point = new DeviceDataSpo2Point();
                            }
                        }
                    }
                    delayTime(300);
                    sendData(DeviceCommand.GET_DATA_SIZE(1));
                    return;
                case 69:
                    CLog.e(TAG, "版本号验证成功 ");
                    int localVersion = (((data[6] & 255) << dp.n) | ((data[5] & 255) << 8) | data[4]) & 16777215;
                    int lovalVersion_hard = (((data[3] & 255) << dp.n) | ((data[2] & 255) << 8) | data[1]) & 16777215;
                    CLog.e("jxx", "50k硬件版本：" + lovalVersion_hard + " 软件版本：" + localVersion);
                    if (localVersion <= 94 || lovalVersion_hard < 92) {
                        this.oldWatch = true;
                    } else {
                        this.oldWatch = false;
                    }
                    if (!ifUpdateCms50k(localVersion) || this.oldWatch) {
                        sendData(DeviceCommand.SET_TIME());
                        return;
                    }
                    cancelResonseTimer();
                    CLog.i(TAG, "要开始跳出提示框mIsUpdate50kDialog: " + this.mIsUpdate50kDialog);
                    if (this.mIsUpdate50kDialog == null) {
                        this.mIsUpdate50kDialog = new IsUpdate50kDialog(this.mcontext) {
                            public void sureUpdate50k() {
                                CLog.i(BluetoothLeDeviceService.TAG, "开始升级50k....");
                                if (BluetoothLeDeviceService.this.mIsUpdate50kDialog != null) {
                                    BluetoothLeDeviceService.this.mIsUpdate50kDialog.dismiss();
                                    BluetoothLeDeviceService.this.mIsUpdate50kDialog = null;
                                }
                                startUpdate50k();
                            }

                            public void cancelUpdate50k() {
                                CLog.i(BluetoothLeDeviceService.TAG, "不升级50k....");
                                if (BluetoothLeDeviceService.this.mIsUpdate50kDialog != null) {
                                    BluetoothLeDeviceService.this.mIsUpdate50kDialog.dismiss();
                                    BluetoothLeDeviceService.this.mIsUpdate50kDialog = null;
                                }
                                BluetoothLeDeviceService.this.responseTimer();
                                BluetoothLeDeviceService.this.sendData(DeviceCommand.SET_TIME());
                            }

                            private void startUpdate50k() {
                                if (BluetoothLeDeviceService.this.mUpdate50kDialog == null) {
                                    BluetoothLeDeviceService.update50k_state = 35;
                                    BluetoothLeDeviceService.this.mUpdate50kDialog = new Update50kDialog(BluetoothLeDeviceService.this.mcontext) {
                                        public void cancelUpdate50k() {
                                            if (BluetoothLeDeviceService.this.mUpdate50kDialog != null) {
                                                BluetoothLeDeviceService.this.mUpdate50kDialog.dismiss();
                                            }
                                            if (BluetoothLeDeviceService.this.mCancelUpdate50kDialog == null) {
                                                BluetoothLeDeviceService.this.mCancelUpdate50kDialog = new CancelUpdate50kDialog(BluetoothLeDeviceService.this.mcontext) {
                                                    public void cancelUpdate50k() {
                                                        if (BluetoothLeDeviceService.this.mCancelUpdate50kDialog != null) {
                                                            BluetoothLeDeviceService.this.mCancelUpdate50kDialog.dismiss();
                                                            BluetoothLeDeviceService.this.mCancelUpdate50kDialog = null;
                                                        }
                                                        BluetoothLeDeviceService.this.resetDialog();
                                                        BluetoothLeDeviceService.this.setUpdatState(32);
                                                    }

                                                    public void continueUpdate50k() {
                                                        if (BluetoothLeDeviceService.this.mCancelUpdate50kDialog != null) {
                                                            BluetoothLeDeviceService.this.mCancelUpdate50kDialog.dismiss();
                                                            BluetoothLeDeviceService.this.mCancelUpdate50kDialog = null;
                                                        }
                                                        BluetoothLeDeviceService.this.mUpdate50kDialog.show();
                                                    }
                                                };
                                            }
                                        }
                                    };
                                    BluetoothLeDeviceService.this.mUpdate50kDialog.show();
                                    BluetoothLeDeviceService.this.sendData(DeviceCommand.set_SJ());
                                    BluetoothLeDeviceService.this.delayTime(20);
                                    BluetoothLeDeviceService.this.sendData(DeviceCommand.set_SJ());
                                    CLog.e(BluetoothLeDeviceService.TAG, "要开始升级11。。。");
                                }
                            }
                        };
                        return;
                    }
                    return;
                case 71:
                    this.oldWatch = true;
                    sendData(DeviceCommand.SET_TIME());
                    return;
                case 80:
                    sendData(DeviceCommand.GET_STEP_DAY(0));
                    this.isHaveData = true;
                    return;
                case 81:
                    this.Progress = 35;
                    DeviceManager.m_DeviceBean.mProgress = this.Progress;
                    sendData(DeviceCommand.GET_DATA_SIZE(3));
                    return;
                case 82:
                    if (this.Progress < 30) {
                        this.Progress++;
                    }
                    DeviceManager.mDeviceBeanList.mState = 4;
                    DeviceManager.m_DeviceBean.mState = 4;
                    DeviceManager.m_DeviceBean.mProgress = this.Progress;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    sendData(DeviceCommand.GET_STEP_DAY(1));
                    return;
                case 83:
                    sendData(DeviceCommand.GET_STEP_DAY(127));
                    this.mPedometer = new DeviceDataPedometer();
                    this.mPedometer.mDataList = this.m_DevicePackManager.mPedometerJar.getmPedometerDataDayList();
                    addDataStepDay(this.mPedometer);
                    sendData(DeviceCommand.GET_DATA_SIZE(2));
                    return;
                case Opcodes.IADD:
                    this.Progress = 30;
                    DeviceManager.mDeviceBeanList.mState = 4;
                    DeviceManager.m_DeviceBean.mState = 4;
                    DeviceManager.m_DeviceBean.mProgress = this.Progress;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    this.isHaveData = true;
                    sendData(DeviceCommand.GET_STEP_MIN(1));
                    return;
                case Opcodes.LADD:
                    this.Progress = 35;
                    DeviceManager.m_DeviceBean.mProgress = this.Progress;
                    CLog.i(TAG, "全天5分钟数据无,请求心电");
                    sendData(DeviceCommand.GET_DATA_SIZE(3));
                    return;
                case 98:
                    sendData(DeviceCommand.GET_STEP_MIN_DATA(1));
                    return;
                case 99:
                    if (this.Progress < 40) {
                        this.Progress++;
                    }
                    DeviceManager.mDeviceBeanList.mState = 4;
                    DeviceManager.m_DeviceBean.mState = 4;
                    DeviceManager.m_DeviceBean.mProgress = this.Progress;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    sendData(DeviceCommand.GET_STEP_MIN_DATA(127));
                    delayTime(300);
                    sendData(DeviceCommand.GET_STEP_MIN(1));
                    return;
                case 100:
                    sendData(DeviceCommand.GET_STEP_MIN_DATA(0));
                    return;
                case 101:
                    this.Progress = 40;
                    DeviceManager.mDeviceBeanList.mState = 4;
                    DeviceManager.m_DeviceBean.mState = 4;
                    DeviceManager.m_DeviceBean.mProgress = this.Progress;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    sendData(DeviceCommand.GET_STEP_MIN_DATA(127));
                    this.mPedometerMin = new DeviceDataPedometerMin();
                    this.mPedometerMin.mDataList = this.m_DevicePackManager.minDatas;
                    addDataStepMin(this.mPedometerMin);
                    delayTime(300);
                    sendData(DeviceCommand.GET_DATA_SIZE(3));
                    return;
                case 112:
                    CLog.i(TAG, "心电片段有");
                    sendData(DeviceCommand.GET_ECG_INFO(1));
                    this.Progress = 45;
                    DeviceManager.mDeviceBeanList.mState = 4;
                    DeviceManager.m_DeviceBean.mState = 4;
                    DeviceManager.m_DeviceBean.mProgress = this.Progress;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    this.isHaveData = true;
                    return;
                case 113:
                    CLog.i(TAG, "心电片段无，请求脉率连续数据");
                    this.Progress = 30;
                    DeviceManager.m_DeviceBean.mProgress = this.Progress;
                    sendData(DeviceCommand.GET_FRAGMENT_SIZE(0));
                    return;
                case 114:
                    if (this.Progress < 50) {
                        this.Progress++;
                        DeviceManager.mDeviceBeanList.mState = 4;
                        DeviceManager.m_DeviceBean.mState = 4;
                        DeviceManager.m_DeviceBean.mProgress = this.Progress;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    }
                    sendData(DeviceCommand.GET_ECG_DATA(1));
                    return;
                case 115:
                    sendData(DeviceCommand.GET_ECG_DATA(127));
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e3) {
                    }
                    sendData(DeviceCommand.GET_ECG_INFO(1));
                    return;
                case 116:
                    Log.i("jxx", "全部心电接收完毕");
                    this.Progress = 50;
                    DeviceManager.mDeviceBeanList.mState = 4;
                    DeviceManager.m_DeviceBean.mState = 4;
                    DeviceManager.m_DeviceBean.mProgress = this.Progress;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    sendData(DeviceCommand.GET_ECG_DATA(127));
                    ArrayList<DeviceDataECG> _ECGS = this.m_DevicePackManager.mEcgs;
                    for (int i8 = 0; i8 < _ECGS.size(); i8++) {
                        DeviceData _ECG = new DeviceData();
                        _ECG.mDate = new int[6];
                        _ECG.mDate[0] = _ECGS.get(i8).Point_data[0];
                        _ECG.mDate[1] = _ECGS.get(i8).Point_data[1];
                        _ECG.mDate[2] = _ECGS.get(i8).Point_data[2];
                        _ECG.mDate[3] = _ECGS.get(i8).Point_data[3];
                        _ECG.mDate[4] = _ECGS.get(i8).Point_data[4];
                        _ECG.mDate[5] = _ECGS.get(i8).Point_data[5];
                        _ECG.TrendData = _ECGS.get(i8).Point_data;
                        _ECG.CaseData = _ECGS.get(i8).ECG_Data;
                        addDataECG(_ECG);
                    }
                    delayTime(200);
                    DeviceManager.mDeviceBeanList.mState = 6;
                    DeviceManager.m_DeviceBean.mState = 6;
                    DeviceManager.m_DeviceBean.mProgress = this.Progress;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    sendData(DeviceCommand.GET_FRAGMENT_SIZE(0));
                    return;
                case 117:
                    sendData(DeviceCommand.GET_ECG_DATA(0));
                    return;
                default:
                    return;
            }
        }
    }

    protected void noNewData() {
        super.noNewData();
        JiXinTestUtils.LogE("call noData method");
        this.mNoData = true;
        this.isHaveData = false;
        cancelResonseTimer();
    }

    private void resetDialog() {
        if (this.mUpdate50kDialog != null) {
            this.mUpdate50kDialog = null;
        }
        if (this.mCancelUpdate50kDialog != null) {
            this.mCancelUpdate50kDialog = null;
        }
        if (this.mIsUpdate50kDialog != null) {
            this.mIsUpdate50kDialog = null;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r17v27, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v38, resolved type: byte} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean ifUpdateCms50k(int r22) {
        /*
            r21 = this;
            java.lang.StringBuilder r17 = new java.lang.StringBuilder
            java.lang.String r18 = com.contec.phms.device.cms50k.update.Update50KUtils.fileUrl
            java.lang.String r18 = java.lang.String.valueOf(r18)
            r17.<init>(r18)
            java.lang.String r18 = "update.bin"
            java.lang.StringBuilder r17 = r17.append(r18)
            java.lang.String r17 = r17.toString()
            byte[] r17 = com.contec.phms.device.cms50k.update.Update50KUtils.readSDFile(r17)
            r0 = r17
            r1 = r21
            r1.mUpdateDatas = r0
            r9 = 0
            android.content.SharedPreferences r17 = com.contec.phms.App_phms.preferences
            java.lang.String r18 = "CMS50K_Server_Version"
            r19 = 999(0x3e7, float:1.4E-42)
            int r12 = r17.getInt(r18, r19)
            r17 = 999(0x3e7, float:1.4E-42)
            r0 = r17
            if (r12 != r0) goto L_0x00b6
            r0 = r21
            byte[] r0 = r0.mUpdateDatas
            r17 = r0
            if (r17 == 0) goto L_0x0191
            r17 = 16
            r0 = r17
            byte[] r11 = new byte[r0]
            r0 = r21
            byte[] r0 = r0.mUpdateDatas
            r17 = r0
            r18 = 0
            r19 = 0
            r20 = 16
            r0 = r17
            r1 = r18
            r2 = r19
            r3 = r20
            java.lang.System.arraycopy(r0, r1, r11, r2, r3)
            r17 = 7
            byte r17 = r11[r17]
            int r17 = r17 << 8
            r18 = 6
            byte r18 = r11[r18]
            r0 = r18
            r0 = r0 & 255(0xff, float:3.57E-43)
            r18 = r0
            r17 = r17 | r18
            r18 = 65535(0xffff, float:9.1834E-41)
            r4 = r17 & r18
            java.lang.String r17 = "jxx"
            java.lang.StringBuilder r18 = new java.lang.StringBuilder
            java.lang.String r19 = "版本号："
            r18.<init>(r19)
            r0 = r18
            java.lang.StringBuilder r18 = r0.append(r4)
            java.lang.String r19 = "--"
            java.lang.StringBuilder r18 = r18.append(r19)
            r19 = 6
            byte r19 = r11[r19]
            java.lang.String r19 = java.lang.Integer.toHexString(r19)
            java.lang.StringBuilder r18 = r18.append(r19)
            java.lang.String r19 = " "
            java.lang.StringBuilder r18 = r18.append(r19)
            r19 = 7
            byte r19 = r11[r19]
            java.lang.String r19 = java.lang.Integer.toHexString(r19)
            java.lang.StringBuilder r18 = r18.append(r19)
            java.lang.String r18 = r18.toString()
            android.util.Log.i(r17, r18)
            android.content.SharedPreferences r17 = com.contec.phms.App_phms.preferences
            android.content.SharedPreferences$Editor r6 = r17.edit()
            java.lang.String r17 = "CMS50K_Server_Version"
            r0 = r17
            r6.putInt(r0, r4)
            r6.commit()
        L_0x00b6:
            android.content.SharedPreferences r17 = com.contec.phms.App_phms.preferences
            java.lang.String r18 = "CMS50K_Server_Version"
            r19 = 999(0x3e7, float:1.4E-42)
            int r12 = r17.getInt(r18, r19)
            r17 = 999(0x3e7, float:1.4E-42)
            r0 = r17
            if (r12 == r0) goto L_0x0194
            r0 = r22
            if (r12 <= r0) goto L_0x0194
            r9 = 1
            r0 = r21
            byte[] r0 = r0.mUpdateDatas
            r17 = r0
            if (r17 == 0) goto L_0x0197
            r9 = 1
            r0 = r21
            byte[] r0 = r0.mUpdateDatas
            r17 = r0
            if (r17 == 0) goto L_0x018f
            java.util.List r16 = com.contec.phms.device.cms50k.update.Update50KUtils.pullParseXmlFile()
            if (r16 == 0) goto L_0x01c8
            r13 = 0
            android.content.SharedPreferences r17 = com.contec.phms.App_phms.preferences
            java.lang.String r18 = "currentVersion"
            r19 = 999(0x3e7, float:1.4E-42)
            int r5 = r17.getInt(r18, r19)
            r17 = 999(0x3e7, float:1.4E-42)
            r0 = r17
            if (r5 == r0) goto L_0x01c6
            r8 = 0
        L_0x00f4:
            int r17 = r16.size()
            r0 = r17
            if (r8 < r0) goto L_0x019a
        L_0x00fc:
            r0 = r21
            byte[] r0 = r0.mUpdateDatas
            r17 = r0
            r0 = r17
            int r0 = r0.length
            r17 = r0
            r0 = r17
            if (r0 != r13) goto L_0x01c4
            r0 = r21
            byte[] r0 = r0.mUpdateDatas
            r17 = r0
            r18 = 11
            byte r17 = r17[r18]
            r0 = r17
            r0 = r0 & 255(0xff, float:3.57E-43)
            r17 = r0
            int r17 = r17 << 24
            r0 = r21
            byte[] r0 = r0.mUpdateDatas
            r18 = r0
            r19 = 10
            byte r18 = r18[r19]
            r0 = r18
            r0 = r0 & 255(0xff, float:3.57E-43)
            r18 = r0
            int r18 = r18 << 16
            r17 = r17 | r18
            r0 = r21
            byte[] r0 = r0.mUpdateDatas
            r18 = r0
            r19 = 9
            byte r18 = r18[r19]
            r0 = r18
            r0 = r0 & 255(0xff, float:3.57E-43)
            r18 = r0
            int r18 = r18 << 8
            r17 = r17 | r18
            r0 = r21
            byte[] r0 = r0.mUpdateDatas
            r18 = r0
            r19 = 8
            byte r18 = r18[r19]
            r0 = r18
            r0 = r0 & 255(0xff, float:3.57E-43)
            r18 = r0
            r17 = r17 | r18
            r18 = 16777215(0xffffff, float:2.3509886E-38)
            r7 = r17 & r18
            r0 = r21
            byte[] r0 = r0.mUpdateDatas
            r17 = r0
            r0 = r17
            int r0 = r0.length
            r17 = r0
            r0 = r17
            if (r0 != r7) goto L_0x01c2
            java.lang.String r17 = "jxx"
            java.lang.String r18 = "相等======"
            com.contec.phms.util.CLog.e(r17, r18)
            r9 = 1
            r0 = r21
            byte[] r0 = r0.mUpdateDatas
            r17 = r0
            r0 = r21
            r1 = r17
            boolean r9 = r0.ifUpdateCms50k_JudgeDeviceType(r1)
            r0 = r21
            byte[] r0 = r0.mUpdateDatas
            r17 = r0
            r0 = r21
            r1 = r17
            boolean r9 = r0.ifUpdateCms50k_SumCheck(r1)
        L_0x018f:
            r10 = r9
        L_0x0190:
            return r10
        L_0x0191:
            r9 = 0
            r10 = r9
            goto L_0x0190
        L_0x0194:
            r9 = 0
            r10 = r9
            goto L_0x0190
        L_0x0197:
            r9 = 0
            r10 = r9
            goto L_0x0190
        L_0x019a:
            r0 = r16
            java.lang.Object r15 = r0.get(r8)
            com.contec.phms.device.cms50k.update.Xml50KUpdateBean r15 = (com.contec.phms.device.cms50k.update.Xml50KUpdateBean) r15
            java.lang.String r14 = r15.version
            java.lang.Integer r17 = java.lang.Integer.valueOf(r14)
            int r17 = r17.intValue()
            r0 = r17
            if (r0 != r5) goto L_0x01be
            java.lang.String r0 = r15.size
            r17 = r0
            java.lang.Integer r17 = java.lang.Integer.valueOf(r17)
            int r13 = r17.intValue()
            goto L_0x00fc
        L_0x01be:
            int r8 = r8 + 1
            goto L_0x00f4
        L_0x01c2:
            r9 = 0
            goto L_0x018f
        L_0x01c4:
            r9 = 0
            goto L_0x018f
        L_0x01c6:
            r9 = 0
            goto L_0x018f
        L_0x01c8:
            r9 = 0
            goto L_0x018f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.contec.phms.device.cms50k.BluetoothLeDeviceService.ifUpdateCms50k(int):boolean");
    }

    public void onEvent(EventFinish e) {
        if (e.getMsg().equals("1")) {
            setUpdatState(33);
            update50k_state = 36;
            resetDialog();
        } else if (e.getMsg().equals("2")) {
            update50k_state = 35;
            setUpdatState(31);
        } else if (e.getMsg().equals("3")) {
            Toast.makeText(this, "不需要更新", Toast.LENGTH_SHORT).show();
        } else if (e.getMsg().equals("0")) {
            if (this.mUpdate50kDialog != null) {
                this.mUpdate50kDialog.setProgress(e.getLeng());
            }
            update50k_state = 35;
        }
    }

    private boolean ifUpdateCms50k_SumCheck(byte[] mUpdateDatas2) {
        if (mUpdateDatas2 == null || mUpdateDatas2.length <= 21) {
            return false;
        }
        int sumCheckFile = (((mUpdateDatas2[20] & 255) << 24) | ((mUpdateDatas2[19] & 255) << dp.n) | ((mUpdateDatas2[18] & 255) << 8) | (mUpdateDatas2[17] & 255)) & -1;
        int sumCheck = 0;
        if (mUpdateDatas2.length > 1024) {
            for (int i = 0; i < mUpdateDatas2.length; i++) {
                if (i == 0) {
                    byte one = mUpdateDatas2[i];
                    byte two = mUpdateDatas2[i + 1];
                    int temp = (((mUpdateDatas2[i + 3] & 255) << 24) | ((mUpdateDatas2[i + 2] & 255) << dp.n) | ((two & 255) << 8) | (one & 255)) & -1;
                    CLog.e("jxx", "temp1:" + temp);
                    sumCheck += temp;
                }
                if (i > 1023 && i % 1024 == 0) {
                    byte one2 = mUpdateDatas2[i];
                    byte two2 = mUpdateDatas2[i + 1];
                    int temp2 = (((mUpdateDatas2[i + 3] & 255) << 24) | ((mUpdateDatas2[i + 2] & 255) << dp.n) | ((two2 & 255) << 8) | (one2 & 255)) & -1;
                    CLog.e("jxx", "temp:" + temp2);
                    sumCheck += temp2;
                }
            }
            sumCheck *= ((mUpdateDatas2[7] << 8) | (mUpdateDatas2[6] & 255)) & 65535;
        }
        CLog.e("jxx", "计算的校验码：" + sumCheck + " 文件里面的校验码：" + sumCheckFile);
        if (sumCheckFile == sumCheck) {
            return true;
        }
        return false;
    }

    private boolean ifUpdateCms50k_JudgeDeviceType(byte[] mUpdateDatas2) {
        if (mUpdateDatas2 == null || mUpdateDatas2.length <= 17) {
            return true;
        }
        byte data15 = mUpdateDatas2[15];
        byte data16 = mUpdateDatas2[16];
        CLog.e("jxx", "设备类型data15:" + Integer.toHexString(data15) + "-data16:" + Integer.toHexString(data16));
        switch (data15) {
            case 1:
                switch (data16) {
                }
                return true;
            case 2:
                switch (data16) {
                }
                return true;
            default:
                return true;
        }
    }

    private void setUpdatState(int state) {
        if (state == 33) {
            DeviceManager.m_DeviceBean.mState = 33;
            DeviceManager.mDeviceBeanList.mState = 33;
            if (this.mUpdate50kDialog != null) {
                this.mUpdate50kDialog.dismiss();
            }
            final DialogClass dialogClass = new DialogClass(this.mcontext, this.mcontext.getResources().getString(R.string.update_successful));
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    dialogClass.dismiss();
                }
            }, 3000);
            MessageManagerMain.msg_ConnectNextDeviceOrPollingOrCallBack();
        } else if (state == 31) {
            DeviceManager.m_DeviceBean.mState = 31;
            DeviceManager.mDeviceBeanList.mState = 31;
        } else if (state == 32) {
            DeviceManager.m_DeviceBean.mState = 32;
            DeviceManager.mDeviceBeanList.mState = 32;
            update50k_state = 37;
        }
        cancelResonseTimer();
        Update50KUtils.mUpdateFileSize = 0;
        Update50KUtils.flagClose = true;
        this.readLen = 32;
        this.updateLength = 0;
        this.dateLength = 0;
        islast = false;
        DeviceService.mReceiveFinished = true;
        DeviceManager.m_DeviceBean.mProgress = 0;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        delayTime(80);
        disConnectBleDevice();
    }

    public void dealSpO2Fragment(SpO2Fragment pFragment) {
        if (pFragment != null) {
            this.mDataSpO2Fragment = new DeviceDataSpO2Fragment();
            this.mDataSpO2Fragment.mDate = new int[6];
            this.mDataSpO2Fragment.mDate = pFragment.Spo2Time;
            LoginUserDao _loginUserInfo = PageUtil.getLoginUserInfo();
            this.mDataSpO2Fragment.mUserName = _loginUserInfo.mUserName;
            if (_loginUserInfo.mBirthday != null && !_loginUserInfo.mBirthday.equalsIgnoreCase(bs.b)) {
                this.mDataSpO2Fragment.mAge = ((long) Calendar.getInstance().get(1)) - Long.parseLong(_loginUserInfo.mBirthday.substring(0, 4));
            }
            if (_loginUserInfo.mHeight != null && !_loginUserInfo.mHeight.equalsIgnoreCase(bs.b)) {
                this.mDataSpO2Fragment.mHeight = ((long) Double.parseDouble(_loginUserInfo.mHeight)) * 10;
            }
            if (_loginUserInfo.mWeight != null && !_loginUserInfo.mWeight.equalsIgnoreCase(bs.b)) {
                this.mDataSpO2Fragment.mWeight = ((long) Double.parseDouble(_loginUserInfo.mWeight)) * 10;
            }
            if (_loginUserInfo.mSex != null && !_loginUserInfo.mSex.equalsIgnoreCase(bs.b)) {
                this.mDataSpO2Fragment.mSex = Long.parseLong(_loginUserInfo.mSex) + 1;
            }
            this.mDataSpO2Fragment.mDataList.add(pFragment);
            this.mDataSpO2Fragment.makeInfos();
            this.mDataSpO2Fragment.setSaveDate();
            this.mDataSpO2Fragment.setmUploadType("case");
            if (DeviceManager.m_DeviceBean.mDeviceName.equalsIgnoreCase(Constants.CMS50K1_NAME)) {
                this.mDataSpO2Fragment.setmDataType("ECG(CMS50K1)");
            } else if (DeviceManager.m_DeviceBean.mDeviceName.equalsIgnoreCase(Constants.CMS50K_NAME)) {
                this.mDataSpO2Fragment.setmDataType("ECG(CMS50K)");
            }
            this.mDataSpO2Fragment.setIs50KContinuityOxygenData(true);
            this.mDataSpO2Fragment.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
            this.mDataSpO2Fragment.makeSaveTime();
            addDataFragment(this.mDataSpO2Fragment);
        }
    }

    public void addDataECG(com.contec.phms.device.template.DeviceData deviceData) {
        DeviceData _data = (DeviceData) deviceData;
        CLog.i(TAG, "call addDataECG method");
        if (_data.TrendData == null) {
            CLog.i(TAG, "No New Datas");
            return;
        }
        _data.setIs50KContinuityOxygenData(false);
        if (DeviceManager.m_DeviceBean.mDeviceName.equalsIgnoreCase(Constants.CMS50K1_NAME)) {
            _data.setmDataType("ECG(CMS50K1)");
        } else if (DeviceManager.m_DeviceBean.mDeviceName.equalsIgnoreCase(Constants.CMS50K_NAME)) {
            _data.setmDataType("ECG(CMS50K)");
        }
        CLog.d(TAG, "存储数据***********");
        _data.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
        DatasContainer.mDeviceDatas.add(deviceData);
    }

    public void addDataPoint(com.contec.phms.device.template.DeviceData deviceData) {
        DeviceDataSpo2Point _data = (DeviceDataSpo2Point) deviceData;
        if (_data.mDataList.size() == 0) {
            CLog.i(TAG, "No New Datas");
            return;
        }
        CLog.d(TAG, "存储数据***********");
        _data.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
        DatasContainer.mDeviceDatas.add(deviceData);
    }

    public void addDataStepMin(com.contec.phms.device.template.DeviceData deviceData) {
        DeviceDataPedometerMin _data = (DeviceDataPedometerMin) deviceData;
        if (_data.mDataList.size() == 0) {
            CLog.i(TAG, "No New Datas");
            return;
        }
        CLog.d(TAG, "存储数据***********");
        _data.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
        DatasContainer.mDeviceDatas.add(deviceData);
    }

    public void addDataStepDay(com.contec.phms.device.template.DeviceData deviceData) {
        DeviceDataPedometer _data = (DeviceDataPedometer) deviceData;
        if (_data.mDataList.size() == 0) {
            CLog.i(TAG, "No New Datas");
            return;
        }
        CLog.d(TAG, "存储数据***********");
        _data.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
        DatasContainer.mDeviceDatas.add(deviceData);
    }

    public void addDataFragment(com.contec.phms.device.template.DeviceData deviceData) {
        if (((DeviceDataSpO2Fragment) deviceData).mDataList.size() == 0) {
            CLog.i(TAG, "No New Datas");
            return;
        }
        CLog.d(TAG, "存储数据***********");
        DatasContainer.mDeviceDatas.add(deviceData);
    }

    private void cancelResonseTimer() {
        if (this.responseTime != null) {
            this.responseTime.cancel();
            this.responseTime.purge();
            this.responseTime = null;
        }
    }

    private void responseTimer() {
        this.responseTime = new Timer();
        this.responseTime.schedule(new TimerTask() {
            public void run() {
                BluetoothLeDeviceService.this.collectFailed();
                if (BluetoothLeDeviceService.this.responseTime != null) {
                    BluetoothLeDeviceService.this.responseTime.cancel();
                    BluetoothLeDeviceService.this.responseTime.purge();
                    BluetoothLeDeviceService.this.responseTime = null;
                }
            }
        }, 10000);
    }

    private void delayTime(int time) {
        try {
            Thread.sleep((long) time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void collectFailed() {
        disConnectBleDevice();
        this.mBleConnected = false;
        DeviceService.mReceiveFinished = true;
        DeviceManager.mDeviceBeanList.mState = 5;
        DeviceManager.m_DeviceBean.mState = 5;
        DeviceManager.m_DeviceBean.mProgress = 0;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        Message msgManager = new Message();
        msgManager.what = 41;
        msgManager.arg2 = 4;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgManager);
        Constants.isSuccessOperationDevice = false;
        MessageManagerMain.msg_ConnectNextDeviceOrPollingOrCallBack();
    }

    private void setParameterError() {
        Log.e("wsd", "setParameterError....");
        DeviceManager.mDeviceBeanList.mState = 5;
        DeviceManager.m_DeviceBean.mState = 5;
        DeviceManager.m_DeviceBean.mProgress = 0;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        disConnectBleDevice();
        cancelResonseTimer();
        this.isHaveData = false;
        Constants.isSuccessOperationDevice = false;
        MessageManagerMain.msg_ConnectNextDeviceOrPollingOrCallBack();
    }

    public void onEvent(Message message) {
        if (message.arg2 == 552) {
            switch (message.what) {
                case Constants.EVENT_BLEDISCONNECT /*553*/:
                    bleDisconnected();
                    return;
                default:
                    return;
            }
        }
    }

    private void bleDisconnected() {
        Log.e("wsd", "bleDisconnected");
        if (this.mBleConnected && !this.mToUpload && !this.mNoData && !DeviceService.mReceiveFinished) {
            collectFailed();
        }
        if (update50k_state == 35) {
            update50k_state = 37;
            resetDialog();
            final DialogClass dialogClass = new DialogClass(this.mcontext, this.mcontext.getResources().getString(R.string.update_filed));
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    dialogClass.dismiss();
                }
            }, 3000);
        }
    }

    protected void toUpload() {
        super.toUpload();
        Constants.BLUETOOTHSTAT = 4;
        this.isHaveData = false;
        this.mHaveMoveData = false;
        this.mToUpload = true;
        cancelResonseTimer();
    }

    public void onDestroy() {
        super.onDestroy();
        LogE("call BluetoothLeDeviceService_cms50k 的onDestroy method");
    }
}
