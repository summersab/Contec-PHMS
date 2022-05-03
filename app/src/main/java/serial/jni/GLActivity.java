package serial.jni;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.FileOperation;

import org.apache.commons.httpclient.cookie.CookieSpec;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

import u.aly.bs;

public class GLActivity extends AppCompatActivity {
    private static final int MESSAGE_UPDATE_HR = 0;
    private String TAG = "GLActivity";
    String address = bs.b;
    private DataUtils data;
    private GLView glView;
    private Context mContext;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    GLActivity.this.textHR.setText(msg.obj.toString());
                    CLog.dT(GLActivity.this.TAG, "更新新率");
                    return;
                case 256:
                    DeviceManager.mDeviceBeanList.mState = 4;
                    DeviceManager.m_DeviceBean.mState = 4;
                    DeviceManager.m_DeviceBean.mProgress = 20;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    Toast.makeText(GLActivity.this, GLActivity.this.getResources().getString(R.string.str_linked), 1000).show();
                    CLog.dT(GLActivity.this.TAG, "连接成功了8000G***********");
                    return;
                case 512:
                    CLog.dT(GLActivity.this.TAG, "连接失败了***********");
                    DeviceManager.mDeviceBeanList.mState = 3;
                    DeviceManager.m_DeviceBean.mState = 3;
                    DeviceManager.m_DeviceBean.mProgress = 0;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    Constants.START_8000GW = false;
                    Toast.makeText(GLActivity.this, GLActivity.this.getResources().getString(R.string.please_research), 1000).show();
                    GLActivity.this.finish();
                    return;
                case Constants.PROGRESS_8000GW:
                    if (GLActivity.this.mProgressLinearLayout == null) {
                        return;
                    }
                    if (GLActivity.this.mProgressLinearLayout.getVisibility() == 8 || GLActivity.this.mProgressLinearLayout.getVisibility() == 4) {
                        GLActivity.this.mProgressLinearLayout.setVisibility(View.VISIBLE);
                        return;
                    }
                    return;
                case BluetoothConnect.MESSAGE_CONNECT_INTERRUPTED:
                    CLog.dT(GLActivity.this.TAG, "连接中断了***********");
                    DeviceManager.mDeviceBeanList.mState = 5;
                    DeviceManager.m_DeviceBean.mState = 5;
                    DeviceManager.m_DeviceBean.mProgress = 20;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    Toast.makeText(GLActivity.this, GLActivity.this.getResources().getString(R.string.please_research), 1000).show();
                    Constants.START_8000GW = false;
                    GLActivity.this.finish();
                    return;
                default:
                    return;
            }
        }
    };
    private ProgressBar mProgressBar;
    private LinearLayout mProgressLinearLayout;
    private String mSaveFileName;
    private int mSaveFileSuccess = 1;
    private TextView textHR;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.glsurfaceview);
        this.mContext = this;
        this.address = getIntent().getExtras().getString("device_address");
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        Log.e("Activity WxH", String.valueOf(width) + "x" + dm.heightPixels);
        Log.e("Density", new StringBuilder().append(dm.densityDpi).toString());
        this.mProgressLinearLayout = (LinearLayout) findViewById(R.id.progress_linearlayout);
        this.mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        this.mSaveFileSuccess = 1;
        ((Button) findViewById(R.id.save_btn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            }
        });
        this.data = new DataUtils(this.mContext, this.address, this.mHandler);
        this.glView = (GLView) findViewById(R.id.GLWave);
        this.glView.setBackground(0, Color.rgb(111, 110, 110));
        this.glView.setGather(this.data);
        this.glView.setZOrderOnTop(true);
        this.glView.getHolder().setFormat(-3);
        this.textHR = (TextView) findViewById(R.id.textHR);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        if (Constants.BLUETOOTHSTAT != 4) {
            Constants.BLUETOOTHSTAT = 4;
            if (this.data != null) {
                CLog.dT(this.TAG, "取消保存文件");
                this.data.cancelCase();
            }
            Constants.START_8000GW = true;
        }
        finish();
        return false;
    }

    private void exitTransmission() {
        if (this.data != null) {
            this.data.gatherEnd();
        }
        if (this.glView != null) {
            this.glView.destroyDrawingCache();
        }
        if (this.mSaveFileSuccess == 2) {
            CLog.dT(this.TAG, "删除未存完的数据文件" + this.mSaveFileName);
            if (this.mSaveFileName != null && !this.mSaveFileName.equals(bs.b)) {
                FileOperation.deleteFiles(new File(String.valueOf(Constants.UploadedDatas_8000GW) + CookieSpec.PATH_DELIM + this.mSaveFileName + ".c8k"));
            }
        }
    }

    public void finish() {
        CLog.dT(this.TAG, "复写的finish method***************");
        exitTransmission();
        super.finish();
    }

    protected void onPause() {
        super.onPause();
        this.glView.onPause();
        this.data.gatherEnd();
    }

    protected void onResume() {
        super.onResume();
        this.glView.onResume();
        this.data.gatherStart(new nativeMsg());
    }

    class nativeMsg extends NativeCallBack {
        nativeMsg() {
        }

        public void callHRMsg(short hr) {
            GLActivity.this.mHandler.obtainMessage(0, Short.valueOf(hr)).sendToTarget();
        }

        public void callLeadOffMsg(String flagOff) {
            Log.e("LF", flagOff);
            CLog.dT(GLActivity.this.TAG, "导联脱落**********" + flagOff);
            DeviceManager.mDeviceBeanList.mState = 5;
            DeviceManager.m_DeviceBean.mState = 5;
            DeviceManager.m_DeviceBean.mProgress = 20;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
            Toast.makeText(GLActivity.this, String.valueOf(flagOff) + " 导联脱落", 1000).show();
            Constants.START_8000GW = false;
        }

        public void callProgressMsg(short progress) {
            CLog.eT(GLActivity.this.TAG, "文件存储进度**********" + progress);
            GLActivity.this.mProgressBar.setProgress(progress);
        }

        public void callCaseStateMsg(short state) {
            if (state == 0) {
                GLActivity.this.mSaveFileSuccess = 2;
                CLog.eT(GLActivity.this.TAG, "开始存储文件**********");
                return;
            }
            GLActivity.this.mSaveFileSuccess = 3;
            CLog.eT(GLActivity.this.TAG, " 存储完成**********");
            DeviceManager.mDeviceBeanList.mState = 6;
            DeviceManager.m_DeviceBean.mState = 6;
            DeviceManager.m_DeviceBean.mProgress = 50;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
            Constants.BLUETOOTHSTAT = 4;
            GLActivity.this.finish();
        }

        public void callHBSMsg(short hbs) {
            CLog.dT(GLActivity.this.TAG, " 心率 hbs = 1表示有心跳**********hbs=" + hbs);
        }

        public void callBatteryMsg(short per) {
            CLog.dT(GLActivity.this.TAG, " 采集盒电量:" + per);
            if (per < 1) {
                DeviceManager.mDeviceBeanList.mState = 5;
                DeviceManager.m_DeviceBean.mState = 5;
                DeviceManager.m_DeviceBean.mProgress = 20;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                Toast.makeText(GLActivity.this, "采集盒电量过低", 1000).show();
                Constants.START_8000GW = false;
                GLActivity.this.finish();
            }
        }

        public void callCountDownMsg(short per) {
            CLog.dT(GLActivity.this.TAG, " 剩余存储时长:" + per);
        }

        public void callWaveColorMsg(boolean flag) {
            if (flag) {
                GLActivity.this.glView.setRendererColor(0.0f, 1.0f, 0.0f, 0.0f);
                CLog.dT(GLActivity.this.TAG, "波形稳定了 颜色改变 开始存储数据");
                if (GLActivity.this.mHandler != null) {
                    GLActivity.this.mHandler.sendEmptyMessage(Constants.PROGRESS_8000GW);
                }
                GLActivity.this.mSaveFileName = String.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis()))) + DeviceManager.m_DeviceBean.getMacString() + DeviceManager.m_DeviceBean.mDeviceName + App_phms.getInstance().GetUserInfoNAME();
                File _file = new File(Constants.UploadedDatas_8000GW);
                if (_file != null && !_file.exists()) {
                    _file.mkdirs();
                }
                GLActivity.this.data.saveCase(String.valueOf(Constants.UploadedDatas_8000GW) + CookieSpec.PATH_DELIM, GLActivity.this.mSaveFileName, 10);
            }
        }
    }
}
