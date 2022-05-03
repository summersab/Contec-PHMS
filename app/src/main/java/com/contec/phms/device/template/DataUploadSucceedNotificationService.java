package com.contec.phms.device.template;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.activity.MainActivityNew;
import com.contec.phms.fragment.FragmentHealthReport;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.DeviceNameUtils;
import com.contec.phms.util.PageUtil;
import com.contec.phms.widget.JumpHealthReportDialog;
import u.aly.bs;

public class DataUploadSucceedNotificationService extends Service {
    public static String TAG = DataUploadSucceedNotificationService.class.getSimpleName();
    private JumpHealthReportDialog jumpHealthReportDialog;

    public void onCreate() {
        super.onCreate();
        LogI("******数据上传成功服务类开启**********");
        App_phms.getInstance().mEventBus.register(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        LogI("******数据上传成功服务类的onStartCommand method**********");
        LogI("currentTab:" + MainActivityNew.currentTab);
        return super.onStartCommand(intent, flags, startId);
    }

    public void onEvent(Message msg) {
        switch (msg.what) {
            case 15:
                switch (msg.arg2) {
                    case 15:
                        if (DeviceManager.m_DeviceBean != null) {
                            String mDeviceName = DeviceManager.m_DeviceBean.mDeviceName;
                            sendNotification(String.valueOf(matchDevice(getApplicationContext(), mDeviceName)) + DeviceManager.m_DeviceBean.mCode + "数据上传成功");
                            return;
                        }
                        sendNotification("数据上传成功");
                        return;
                    case 16:
                        showDialogPrepare();
                        return;
                    default:
                        return;
                }
            default:
                return;
        }
    }

    private void showDialogPrepare() {
        String info;
        LogI("call showDialogPrepare method-----");
        if (MainActivityNew.currentTab != 0) {
            if (MainActivityNew.currentTab != 1) {
                info = getResources().getString(R.string.str_jump_health_report);
            } else if (FragmentHealthReport.mHealthReportIndex == 0) {
                info = getResources().getString(R.string.str_refresh_health_report);
            } else {
                info = getResources().getString(R.string.str_jump_health_report);
            }
            CLog.i("jxx", "info:" + info + "mHealthReportIndex:" + FragmentHealthReport.mHealthReportIndex);
            Context topActivity = PageUtil.getTopActivity();
            if (topActivity != null && this.jumpHealthReportDialog == null) {
                this.jumpHealthReportDialog = new JumpHealthReportDialog(topActivity, info);
                this.jumpHealthReportDialog.getmDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        DataUploadSucceedNotificationService.this.jumpHealthReportDialog = null;
                    }
                });
            }
        }
    }

    private void sendNotification(String str) {
        Notification notification = new Notification();
        notification.icon = R.drawable.ic_launcher;
        notification.flags = 16;
        notification.tickerText = str;
        MainActivityNew.mIsFromNotification = true;
        notification.contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivityNew.class), Intent.FLAG_ACTIVITY_NEW_TASK);
        notification.contentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.layout_notification_data_transmission);
        notification.contentView.setTextViewText(R.id.tv_notification_data, str);
        ((NotificationManager) getApplicationContext().getSystemService("notification")).notify(str, 0, notification);
    }

    private String matchDevice(Context context, String _device) {
        if (Constants.DEVICE_8000GW_NAME.equalsIgnoreCase(_device)) {
            return context.getResources().getString(R.string.str_8000G);
        }
        if (Constants.PM10_NAME.equalsIgnoreCase(_device)) {
            return context.getResources().getString(R.string.device_productname_pm10);
        }
        if ("TEMP01".equalsIgnoreCase(_device)) {
            return context.getResources().getString(R.string.device_productname_hc06);
        }
        if ("BC01".equalsIgnoreCase(_device)) {
            return context.getResources().getString(R.string.device_productname_bc01);
        }
        if ("CMS50D".equalsIgnoreCase(_device)) {
            return context.getResources().getString(R.string.device_productname_cmd50d);
        }
        if ("CMS50IW".equals(_device)) {
            return context.getResources().getString(R.string.device_productname_50IW);
        }
        if ("cmssxt".equalsIgnoreCase(_device)) {
            return context.getResources().getString(R.string.device_productname_SXT);
        }
        if ("ABPM50W".equalsIgnoreCase(_device)) {
            return context.getResources().getString(R.string.device_productname_M50W);
        }
        if (DeviceNameUtils.PM50.equalsIgnoreCase(_device)) {
            return context.getResources().getString(R.string.device_productname_pm50);
        }
        if (DeviceNameUtils.TEMP03.equalsIgnoreCase(_device)) {
            return context.getResources().getString(R.string.device_productname_temp03);
        }
        if ("CONTEC08AW".equalsIgnoreCase(_device)) {
            return context.getResources().getString(R.string.device_productname_08AW);
        }
        if ("CONTEC08C".equalsIgnoreCase(_device)) {
            return context.getResources().getString(R.string.device_productname_08AW);
        }
        if ("CMS50F".equalsIgnoreCase(_device)) {
            return context.getResources().getString(R.string.device_name_50IW);
        }
        if (Constants.CMS50EW.equalsIgnoreCase(_device) || "CMS50DW".equalsIgnoreCase(_device)) {
            return context.getResources().getString(R.string.device_productname_50EW);
        }
        if ("sp10w".equalsIgnoreCase(_device)) {
            return context.getResources().getString(R.string.device_productname_SP10W);
        }
        if ("cmsvesd".equalsIgnoreCase(_device)) {
            return context.getResources().getString(R.string.device_productname_VESD);
        }
        if ("wt".equalsIgnoreCase(_device)) {
            return context.getResources().getString(R.string.device_productname_WT);
        }
        if ("FHR01".equalsIgnoreCase(_device)) {
            return context.getResources().getString(R.string.device_productname_Fhr01);
        }
        if (Constants.PM85_NAME.equalsIgnoreCase(_device)) {
            return context.getResources().getString(R.string.device_productname_Pm85);
        }
        if (Constants.CMS50K_NAME.equalsIgnoreCase(_device)) {
            return context.getResources().getString(R.string.device_productname_cms50k);
        }
        return bs.b;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        super.onDestroy();
        App_phms.getInstance().mEventBus.unregister(this);
    }

    public static void stopService(Context pContext) {
        CLog.e(TAG, "停止数据上传通知服务类");
        pContext.stopService(new Intent(pContext, DataUploadSucceedNotificationService.class));
    }

    public void LogI(String msg) {
        if (Constants.mTestFlag) {
            CLog.i(TAG, msg);
        }
    }

    public void LogE(String msg) {
        if (Constants.mTestFlag) {
            CLog.e(TAG, msg);
        }
    }
}
