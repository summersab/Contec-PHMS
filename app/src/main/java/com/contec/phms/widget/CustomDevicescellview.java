package com.contec.phms.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Message;
import androidx.core.internal.view.SupportMenu;
//import android.support.v4.media.TransportMediator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.fastjson.asm.Opcodes;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.SearchDevice.SortDeviceContainer;
import com.contec.phms.SearchDevice.SortDeviceContainerMap;
import com.contec.phms.device.template.BluetoothServerService;
import com.contec.phms.device.template.PollingService;
import com.contec.phms.domain.WaitConnectDeviceBean;
import com.contec.phms.manager.device.DeviceBean;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.ScreenAdapter;
import java.util.HashMap;
import u.aly.bs;

public class CustomDevicescellview extends RelativeLayout {
    private final String TAG = CustomDevicescellview.class.getSimpleName();
    private TextView _poptextview;
    private int[] location = new int[2];
    private Context mContext;
    private String mDeviceCode;
    private int mDeviceID;
    private String mDeviceName;
    private String mErrorStr;
    private int mFailedReasons;
    private boolean mHasFailedFile = false;
    private PopupWindow mPop;
    private HashMap<String, SortDeviceContainer> mSortContainer;
    private int mState;
    private int mViewTextcolor = Color.rgb(Opcodes.DCMPL, Opcodes.IFNE, Opcodes.IFNE);
    private ImageButton madddevicessmallitemdelbtn;
    private TextView madddevicessmallitemnametext;
    private ProgressBar madddevicessmallitemprogressbar;
    private ImageView mcellerrorpoint;
    private DeviceBean mdevicebean;
    private int mgarycolor = Color.rgb(Opcodes.DCMPL, Opcodes.IFNE, Opcodes.IFLT);
    private int morangecolor = Color.rgb(254, 126, 13);
    private ImageView mpriorityDeviceFlag;
    private int msoftorangecolor = Color.argb(Opcodes.FCMPG, 254, 126, 13);
    private View mview;

    public DeviceBean getMdevicebean() {
        return this.mdevicebean;
    }

    public void setMdevicebean(DeviceBean mdevicebean2) {
        this.mdevicebean = mdevicebean2;
        this.mSortContainer = SortDeviceContainerMap.getInstance().getmSortDeviceMap();
        if (this.mSortContainer.containsKey(String.valueOf(getmDeviceName()) + getItemCode())) {
            CLog.i("zlxianshi", "里面有它，显示把 = " + getmDeviceName() + getItemCode());
            this.mpriorityDeviceFlag.setVisibility(View.VISIBLE);
        }
    }

    public int getmFailedReasons() {
        return this.mFailedReasons;
    }

    public void setmFailedReasons(int mFailedReasons2) {
        this.mFailedReasons = mFailedReasons2;
    }

    public int getmViewTextcolor() {
        return this.mViewTextcolor;
    }

    public void setmViewTextcolor(int mViewTextcolor2) {
        this.mViewTextcolor = mViewTextcolor2;
    }

    public void setmDeviceID(int mDeviceID2) {
        this.mDeviceID = mDeviceID2;
    }

    public int getmDeviceID() {
        return this.mDeviceID;
    }

    public void setmHasFailedFile(boolean _HasFailedFile) {
        this.mHasFailedFile = _HasFailedFile;
    }

    public boolean getmHasFailedFileflag() {
        return this.mHasFailedFile;
    }

    public String getmErrorStr() {
        if (this.mErrorStr == null) {
            return geterrorstringfromint(R.string.devices_havefailduploadfile);
        }
        return this.mErrorStr;
    }

    public void setmDeviceName(String mDeviceName2) {
        this.mDeviceName = mDeviceName2;
    }

    public String getmDeviceName() {
        return this.mDeviceName;
    }

    public void setmErrorStr(String pErrorStr) {
        this.mErrorStr = pErrorStr;
    }

    public CustomDevicescellview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomDevicescellview(Context context) {
        super(context);
        init(context);
    }

    private void init(final Context context) {
        this.mContext = context;
        this.mview = LayoutInflater.from(context).inflate(R.layout.layout_device_item_label_item, this, true);
        ImageButton managerDeviceRefreshBtn = (ImageButton) this.mview.findViewById(R.id.managerDeviceRefreshBtn);
        this.mcellerrorpoint = (ImageView) this.mview.findViewById(R.id.adddevicessmallerrorpoint);
        this.madddevicessmallitemnametext = (TextView) this.mview.findViewById(R.id.adddevicessmallitemnametext);
        this.madddevicessmallitemprogressbar = (ProgressBar) this.mview.findViewById(R.id.adddevicessmallitemprogressbar);
        this.mview.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        PageUtil.measureView(this.mview);
        ViewGroup.LayoutParams layoutParams = this.madddevicessmallitemprogressbar.getLayoutParams();
        layoutParams.width = this.mview.getMeasuredWidth();
        this.madddevicessmallitemprogressbar.setLayoutParams(layoutParams);
        this.madddevicessmallitemdelbtn = (ImageButton) this.mview.findViewById(R.id.adddevicessmallitemdelbtn);
        this.mpriorityDeviceFlag = (ImageView) this.mview.findViewById(R.id.priorityDeviceFlag);
        if (Constants.IS_PAD_NEW) {
            this.mcellerrorpoint.getLayoutParams().width = ScreenAdapter.dip2px(context, 12.0f);
            this.mcellerrorpoint.getLayoutParams().height = ScreenAdapter.dip2px(context, 12.0f);
        }
        this.mview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (CustomDevicescellview.this.mcellerrorpoint.getVisibility() == View.VISIBLE) {
                    v.getLocationOnScreen(CustomDevicescellview.this.location);
                    CustomDevicescellview.this.initErroPopupTip();
                    if (CustomDevicescellview.this.mPop != null) {
                        CustomDevicescellview.this.mPop.dismiss();
                    }
                    CustomDevicescellview.this.mPop.showAtLocation(v, 0, (CustomDevicescellview.this.location[0] - (CustomDevicescellview.this.mPop.getWidth() / 2)) - 15, CustomDevicescellview.this.location[1] - CustomDevicescellview.this.mPop.getHeight());
                    new DismissPopup(CustomDevicescellview.this.mPop).execute(new String[]{bs.b});
                }
            }
        });
        managerDeviceRefreshBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CLog.d(CustomDevicescellview.this.TAG, "DeviceManager.m_DeviceBean.mState:" + DeviceManager.m_DeviceBean.mState);
                if (DeviceManager.m_DeviceBean.mState == 1 || DeviceManager.m_DeviceBean.mState == 2 || DeviceManager.m_DeviceBean.mState == 4 || DeviceManager.m_DeviceBean.mState == 6 || DeviceManager.m_DeviceBean.mState == 7) {
                    CLog.e(CustomDevicescellview.this.TAG, "setOnClickListener*********************");
                    return;
                }
                DeviceBean _devicebeanRefresh = null;
                int i = 0;
                while (i < DeviceManager.mDeviceList.getListDevice().size()) {
                    try {
                        if (DeviceManager.mDeviceList.getDevice(i).mDeviceName.equals(CustomDevicescellview.this.getmDeviceName())) {
                            for (int j = 0; j < DeviceManager.mDeviceList.getDevice(i).mBeanList.size(); j++) {
                                DeviceBean _devicebean = DeviceManager.mDeviceList.getDevice(i).mBeanList.get(j);
                                if (CustomDevicescellview.this.madddevicessmallitemnametext.getText().toString().equals(_devicebean.mCode)) {
                                    _devicebeanRefresh = _devicebean;
                                }
                            }
                        }
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
                if (_devicebeanRefresh != null) {
                    _devicebeanRefresh.ifAddNew = true;
                    DeviceManager.mRefreshBean = new DeviceBean(bs.b, bs.b);
                    DeviceManager.mRefreshBean = _devicebeanRefresh;
                    DeviceManager.mRefreshBean.mState = 1;
                    BluetoothServerService.stopServer(CustomDevicescellview.this.mContext);
                    final Context context = CustomDevicescellview.this.mContext;
                    new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(500);
                                CLog.d(CustomDevicescellview.this.TAG, "状态符合连接需求 ***开始链接");
                                CLog.i("jxx", "call 停止轮询服务类 method1");
                                PollingService.stopService(context);
                                App_phms.getInstance().getmWaitConnectDeviceBeanLists().add(new WaitConnectDeviceBean(DeviceManager.mRefreshBean, 0, true));
                                CLog.i("jxx", "xx集合的大小1: " + App_phms.getInstance().getmWaitConnectDeviceBeanLists().size());
                                context.startService(new Intent(context, PollingService.class));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            }
        });
        this.mview.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                CLog.d(CustomDevicescellview.this.TAG, "DeviceManager.m_DeviceBean.mState:" + DeviceManager.m_DeviceBean.mState);
                if (DeviceManager.m_DeviceBean.mState == 1 || DeviceManager.m_DeviceBean.mState == 2 || DeviceManager.m_DeviceBean.mState == 4 || DeviceManager.m_DeviceBean.mState == 6 || DeviceManager.m_DeviceBean.mState == 7) {
                    CLog.e(CustomDevicescellview.this.TAG, "setOnLongClickListener*********************");
                    return false;
                }
                CustomDevicescellview.this.madddevicessmallitemprogressbar.setBackgroundColor(SupportMenu.CATEGORY_MASK);
                DeviceBean _devicebeanRefresh = null;
                int i = 0;
                while (i < DeviceManager.mDeviceList.getListDevice().size()) {
                    try {
                        if (DeviceManager.mDeviceList.getDevice(i).mDeviceName.equals(CustomDevicescellview.this.getmDeviceName())) {
                            for (int j = 0; j < DeviceManager.mDeviceList.getDevice(i).mBeanList.size(); j++) {
                                DeviceBean _devicebean = DeviceManager.mDeviceList.getDevice(i).mBeanList.get(j);
                                if (CustomDevicescellview.this.madddevicessmallitemnametext.getText().toString().equals(_devicebean.mCode)) {
                                    _devicebeanRefresh = _devicebean;
                                }
                            }
                        }
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                if (_devicebeanRefresh == null) {
                    return false;
                }
                _devicebeanRefresh.ifAddNew = true;
                DeviceManager.mRefreshBean = new DeviceBean(bs.b, bs.b);
                DeviceManager.mRefreshBean = _devicebeanRefresh;
                DeviceManager.mRefreshBean.mState = 1;
                BluetoothServerService.stopServer(CustomDevicescellview.this.mContext);
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(500);
                            CLog.d(CustomDevicescellview.this.TAG, "状态符合连接需求 ***开始链接");
                            Message msgs = new Message();
                            msgs.what = Constants.CONNECT_DEVICE_HM;
                            msgs.arg2 = 1;
                            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return false;
            }
        });
    }

    public void setdelbtnvisiable(boolean pisshow) {
        if (pisshow) {
            this.madddevicessmallitemdelbtn.setVisibility(View.VISIBLE);
        } else {
            this.madddevicessmallitemdelbtn.setVisibility(View.GONE);
        }
    }

    public void setprogressbarprogress(int _pg, int pState) {
        this.madddevicessmallitemprogressbar.setProgress(_pg);
    }

    public int getprogressbarprogress() {
        return this.madddevicessmallitemprogressbar.getProgress();
    }

    class DismissPopup extends AsyncTask<String, String, String> {
        PopupWindow _popupwindow;

        public DismissPopup(PopupWindow _popupwindow2) {
            this._popupwindow = _popupwindow2;
        }

        protected String doInBackground(String... params) {
            for (int i = 0; i < 6; i++) {
                if (this._popupwindow.isShowing()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        protected void onPostExecute(String result) {
            if (this._popupwindow != null && this._popupwindow.isShowing()) {
                this._popupwindow.dismiss();
            }
        }
    }

    public void setProgressBack_value1() {
        this.madddevicessmallitemprogressbar.setBackgroundColor(this.mContext.getResources().getColor(R.color.gray_bg));
        this.madddevicessmallitemprogressbar.setProgressDrawable(this.mContext.getResources().getDrawable(R.drawable.drawable_deviceslist_item_bg_green2));
    }

    public void setProgressBack_value2() {
        this.madddevicessmallitemprogressbar.setBackgroundColor(this.mContext.getResources().getColor(R.color.gray_bg));
        this.madddevicessmallitemprogressbar.setProgressDrawable(this.mContext.getResources().getDrawable(R.drawable.drawable_deviceslist_item_bg_green2));
    }

    public void setProgressBack_gray() {
        this.madddevicessmallitemprogressbar.setBackgroundColor(this.mContext.getResources().getColor(R.color.gray_bg));
        this.madddevicessmallitemprogressbar.setProgressDrawable(this.mContext.getResources().getDrawable(R.drawable.drawable_deviceslist_item_bg_gray));
    }

    public void setItemGraybg() {
    }

    public void setItemTest(String pstr) {
        if (pstr == null) {
        }
    }

    public void setItemOrangebg() {
    }

    public void setItemerrorpointVisible(int flag) {
        this.mcellerrorpoint.setVisibility(View.VISIBLE);
    }

    public void setItemerrorpointInVisible() {
        this.mcellerrorpoint.setVisibility(View.INVISIBLE);
    }

    public void setItemText(String _str) {
        if (_str != null) {
            this.madddevicessmallitemnametext.setText(_str);
            this.mDeviceCode = _str.trim();
        }
    }

    private String getItemCode() {
        if (this.mDeviceCode != null) {
            return this.mDeviceCode;
        }
        return null;
    }

    public int getmState() {
        return this.mState;
    }

    public void setmState(int mState2) {
        switch (mState2) {
            case 0:
                setProgressBack_gray();
                narrowview();
                setmViewTextcolor(this.msoftorangecolor);
                setItemerrorpointInVisible();
                break;
            case 1:
                setProgressBack_gray();
                amplifyview();
                setmViewTextcolor(this.msoftorangecolor);
                setItemerrorpointInVisible();
                this.mpriorityDeviceFlag.setVisibility(View.INVISIBLE);
                break;
            case 2:
                setProgressBack_gray();
                setItemOrangebg();
                amplifyview();
                setmViewTextcolor(this.msoftorangecolor);
                setItemerrorpointInVisible();
                this.mpriorityDeviceFlag.setVisibility(View.INVISIBLE);
                break;
            case 3:
                setProgressBack_gray();
                narrowview();
                setmErrorStr(geterrorstringfromint(R.string.please_research));
                setItemerrorpointVisible(3);
                this.mpriorityDeviceFlag.setVisibility(View.INVISIBLE);
                break;
            case 4:
                setProgressBack_value1();
                amplifyview();
                setmViewTextcolor(this.morangecolor);
                setItemerrorpointInVisible();
                break;
            case 5:
                setProgressBack_gray();
                narrowview();
                setmErrorStr(geterrorstringfromint(R.string.nodata_or_data_erro));
                setItemerrorpointVisible(5);
                break;
            case 6:
                setProgressBack_value2();
                amplifyview();
                setmViewTextcolor(this.morangecolor);
                setItemerrorpointInVisible();
                break;
            case 7:
                setProgressBack_value2();
                amplifyview();
                setmViewTextcolor(this.morangecolor);
                setItemerrorpointInVisible();
                this.mpriorityDeviceFlag.setVisibility(View.INVISIBLE);
                break;
            case 8:
                setProgressBack_gray();
                narrowview();
                setItemerrorpointInVisible();
                this.mpriorityDeviceFlag.setVisibility(View.INVISIBLE);
                break;
            case 9:
                setProgressBack_gray();
                narrowview();
                setItemerrorpointVisible(9);
                this.mpriorityDeviceFlag.setVisibility(View.INVISIBLE);
                break;
            case 10:
                narrowview();
                setItemerrorpointInVisible();
                this.mpriorityDeviceFlag.setVisibility(View.INVISIBLE);
                break;
            case 11:
                setProgressBack_gray();
                narrowview();
                setItemerrorpointInVisible();
                break;
            case 12:
                setProgressBack_gray();
                narrowview();
                setmErrorStr(geterrorstringfromint(R.string.Up_loadthe_limited_number_of));
                setItemerrorpointVisible(12);
                break;
            case 13:
                setProgressBack_gray();
                narrowview();
                setItemerrorpointInVisible();
                this.mpriorityDeviceFlag.setVisibility(View.INVISIBLE);
                break;
            case 14:
                setProgressBack_gray();
                narrowview();
                setItemerrorpointInVisible();
                break;
            case 15:
                setProgressBack_gray();
                narrowview();
                setmErrorStr(geterrorstringfromint(R.string.deviceerror_pair_error));
                setItemerrorpointVisible(15);
                break;
            case 16:
                setProgressBack_gray();
                narrowview();
                setmErrorStr(geterrorstringfromint(R.string.Up_loadthe_limited_number_of));
                setItemerrorpointVisible(16);
                break;
            case 17:
                setProgressBack_gray();
                amplifyview();
                setItemerrorpointInVisible();
                break;
            case 18:
                setProgressBack_value2();
                amplifyview();
                setmViewTextcolor(this.morangecolor);
                setItemerrorpointInVisible();
                break;
            case 19:
                setProgressBack_value2();
                amplifyview();
                setmViewTextcolor(this.morangecolor);
                setItemerrorpointInVisible();
                break;
            case 24:
                setProgressBack_gray();
                narrowview();
                setItemerrorpointInVisible();
                break;
            case 27:
                narrowview();
                break;
        }
        this.mState = mState2;
        if (getmHasFailedFileflag()) {
            if (getmFailedReasons() == 1) {
                setmErrorStr(geterrorstringfromint(R.string.Up_loadthe_limited_number_of));
            } else if (getmFailedReasons() == 0) {
                setmErrorStr(getmErrorStr());
            }
            setItemerrorpointVisible(1111);
        }
    }

    public void amplifyview() {
    }

    private void narrowview() {
    }

    private int dipTopx(Context context, float dpValue) {
        return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    private void initErroPopupTip() {
        if (this.mPop == null) {
            View _popview = inflate(this.mContext, R.layout.layout_errorpopuptip, (ViewGroup) null);
            this._poptextview = (TextView) _popview.findViewById(R.id.poperrortext);
            this._poptextview.setText(getmErrorStr());
            int _popWidth = 85;
            if (getmErrorStr() != null && getmErrorStr().length() > 4) {
            //    _popWidth = TransportMediator.KEYCODE_MEDIA_RECORD;
            }
            this.mPop = new PopupWindow(_popview, ScreenAdapter.dip2px(this.mContext, (float) _popWidth), ScreenAdapter.dip2px(this.mContext, 58.0f));
            this.mPop.setBackgroundDrawable(new ColorDrawable(0));
            this.mPop.setOutsideTouchable(true);
            this.mPop.setFocusable(true);
        }
        if (this.mPop.isShowing()) {
            this.mPop.dismiss();
        }
    }

    private String geterrorstringfromint(int pstringid) {
        if (this.mContext != null) {
            return this.mContext.getString(pstringid);
        }
        return "Unkow Error";
    }
}
