package com.contec.phms.fragment;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import androidx.fragment.app.FragmentActivity;
import androidx.core.internal.view.SupportMenu;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.contec.circleimage.widget.CircularImage;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.SearchDevice.SearchDevice;
import com.contec.phms.Server_Main;
import com.contec.phms.activity.ActivityChooseHead;
import com.contec.phms.device.template.BluetoothServerService;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.eventbus.EventShowLastData;
import com.contec.phms.manager.device.DeviceBean;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.message.MessageManager;
import com.contec.phms.db.DeviceListDaoOperation;
import com.contec.phms.db.DeviceListItemBeanDao;
import com.contec.phms.upload.UploadService;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.DeviceNameUtils;
import com.contec.phms.util.FileOperation;
import com.contec.phms.util.PageUtil;
import com.google.zxing.client.result.ExpandedProductParsedResult;
//import com.sun.mail.iap.Response;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.apache.commons.httpclient.cookie.CookieSpec;
import u.aly.bs;

@SuppressLint({"NewApi"})
public class FragmentdeviceNew extends FragmentBase implements View.OnClickListener {
    public static FragmentdeviceNew mFragmentdevicelist;
    private String TAG = FragmentdeviceNew.class.getSimpleName();
    private CircularImage _devicelist_change_user;
    private ImageButton _imagebtn_startmanagerdevice;
    DeviceBean _showBean;
    private Context context;
    private BluetoothAdapter mBluetoothAdapter;
    private ImageView mImageView_Device;
    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(intent.getAction()) && FragmentdeviceNew.this.mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                if (DeviceManager.mDeviceList != null && DeviceManager.mDeviceList.size() > 0) {
                    Constants.BLUETOOTHSTAT = 1;
                    context.startService(new Intent(context, Server_Main.class));
                }
                context.unregisterReceiver(this);
            }
        }
    };
    public final BroadcastReceiver mReceiverBluetoothOnOff = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(intent.getAction())) {
                int _state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
                if (_state == 12) {
                    CLog.i(FragmentdeviceNew.this.TAG, "Start search from  enable================");
                    BluetoothServerService.stopServer(FragmentdeviceNew.this.getActivity());
                    Intent _intentBServer = new Intent(FragmentdeviceNew.this.getActivity(), BluetoothServerService.class);
                    _intentBServer.putExtra("start_thread", true);
                    App_phms.getInstance().getApplicationContext().startService(_intentBServer);
                    context.unregisterReceiver(FragmentdeviceNew.this.mReceiverBluetoothOnOff);
                } else if (_state == 10) {
                    CLog.i(FragmentdeviceNew.this.TAG, "Open Buletooth================");
                }
            }
        }
    };
    private TextView mTextView_device_progress;
    private TextView mTextView_device_state;
    private TextView mTextView_number;
    private TextView mUserNameTextView;
    private View mView;
    private RelativeLayout mdevice_allvalue_framelayout;
    private RelativeLayout mfirst_search_new_devices_layout;
    private RelativeLayout mhavedevicesLiealy;
    private TextView mlastvaluetimetv;
    private LinearLayout monlyONEValueDeviceLy;
    private LinearLayout monlyTHREEValueDeviceLy;
    private LinearLayout monlyTWOValueDeviceLy;
    private TextView monlyTWOvaleFirstVaule;
    private TextView monlyTWOvaleFirstVauleName;
    private TextView monlyTWOvaleFirstVauleunit;
    private TextView monlyTWOvaleSecondVaule;
    private TextView monlyTWOvaleSecondVauleName;
    private TextView monlyTWOvaleSecondVauleunit;
    private TextView monlyThreevalueFirstVaule;
    private TextView monlyThreevalueFirstVauleName;
    private TextView monlyThreevalueFirstVauleUnit;
    private TextView monlyThreevalueSecondVaule;
    private TextView monlyThreevalueSecondVauleName;
    private TextView monlyThreevalueSecondVauleUnit;
    private TextView monlyThreevalueThirdVaule;
    private TextView monlyThreevalueThirdVauleName;
    private TextView monlyThreevalueThirdVauleUnit;
    private TextView monlyonevalesecondunit;
    private TextView monlyonevaletv;
    private TextView monlyonevaleunittv;
    private ImageButton msearch_new_devices_bt;
    private TextView mtextView_name;
    private float ux;
    private float x;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        App_phms.getInstance().mEventBus.register(this);
        this.mView = inflater.inflate(R.layout.layout_callback_temp, container, false);
        init_view(this.mView);
        initList();
        return this.mView;
    }

    public void onResume() {
        super.onResume();
        FragmentActivity activity = getActivity();
        getActivity();
        ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.mView.getWindowToken(), 0);
        CLog.i("lzerror", "FragmentdeviceNew onResume.... ");
        initList();
        if (Constants.ISFROMSERACHDEVICE) {
            CLog.i(this.TAG, "FragmentdeviceNew onResume  主动连接模式");
            Constants.ISFROMSERACHDEVICE = false;
            checkBlueTooth();
        } else {
            CLog.i(this.TAG, "FragmentdeviceNew onResume: unknown");
            if (DeviceManager.mRefreshBean != null) {
                SearchDevice.stopServer(getActivity());
                DeviceManager.stopServer(getActivity());
                UploadService.stopServer(getActivity());
                MessageManager.stopServer(getActivity());
                DeviceService.stopServer(getActivity());
                Server_Main.stopServer(getActivity());
                CLog.i(this.TAG, "FragmentdeviceNew onResume: refresh");
                init_value(DeviceManager.mRefreshBean);
                BluetoothServerService.stopServer(getActivity());
                Constants.BLUETOOTHSTAT = 1;
                this.context.startService(new Intent(this.context, Server_Main.class));
            } else {
                CLog.i(this.TAG, "FragmentdeviceNew onResume  打开回连模式");
                startcallback();
            }
        }
        judgeShowSearch();
    }

    private void judgeShowSearch() {
        if (DeviceListDaoOperation.getInstance().getDeviceListItem() == null || DeviceListDaoOperation.getInstance().getDeviceListItem().size() <= 0) {
            this.msearch_new_devices_bt.setVisibility(View.VISIBLE);
            this.mfirst_search_new_devices_layout.setVisibility(View.VISIBLE);
            this.mhavedevicesLiealy.setVisibility(View.GONE);
            this._imagebtn_startmanagerdevice.setVisibility(View.GONE);
            return;
        }
        this.msearch_new_devices_bt.setVisibility(View.GONE);
        this.mfirst_search_new_devices_layout.setVisibility(View.GONE);
        this.mhavedevicesLiealy.setVisibility(View.VISIBLE);
        this._imagebtn_startmanagerdevice.setVisibility(View.VISIBLE);
    }

    public void onStop() {
        super.onStop();
    }

    public void setTypeFace(TextView ptextview) {
        ptextview.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/stencil.ttf"));
    }

    private void init_view(View pView) {
        this.context = getActivity();
        pView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (FragmentdeviceNew.this._showBean == null || FragmentdeviceNew.this._showBean.mState == 0 || FragmentdeviceNew.this._showBean.mState == 1 || FragmentdeviceNew.this._showBean.mState == 2 || FragmentdeviceNew.this._showBean.mState == 4 || FragmentdeviceNew.this._showBean.mState == 6 || FragmentdeviceNew.this._showBean.mState == 7 || FragmentdeviceNew.this._showBean.mState == 18 || FragmentdeviceNew.this._showBean.mState == 19) {
                    if (FragmentdeviceNew.this._showBean != null && event.getAction() == 0) {
                        Toast.makeText(FragmentdeviceNew.this.getActivity(), "设备处于活动状态，请稍后切换", Toast.LENGTH_SHORT).show();
                        CLog.e(FragmentdeviceNew.this.TAG, "-----------show toast----------");
                    }
                } else if (event.getAction() == 0) {
                    FragmentdeviceNew.this.x = event.getX();
                } else if (event.getAction() == 1) {
                    FragmentdeviceNew.this.ux = event.getX();
                    if (Math.abs(FragmentdeviceNew.this.x - FragmentdeviceNew.this.ux) > 20.0f) {
                        if (FragmentdeviceNew.this.x - FragmentdeviceNew.this.ux > 0.0f) {
                            if (FragmentdeviceNew.this._showBean != null) {
                                int _index = 0;
                                boolean _hasnext = false;
                                ArrayList<DeviceBean> _listBeans = App_phms.getInstance().showBeans;
                                for (int i = 0; i < _listBeans.size(); i++) {
                                    if (FragmentdeviceNew.this._showBean.mMacAddr.equalsIgnoreCase(_listBeans.get(i).mMacAddr)) {
                                        _index = i + 1;
                                        _hasnext = true;
                                    }
                                }
                                if (_hasnext) {
                                    if (_index == _listBeans.size()) {
                                        FragmentdeviceNew.this.init_value(_listBeans.get(0));
                                        FragmentdeviceNew.this._showBean = _listBeans.get(0);
                                    } else {
                                        FragmentdeviceNew.this.init_value(_listBeans.get(_index));
                                        FragmentdeviceNew.this._showBean = _listBeans.get(_index);
                                    }
                                }
                            }
                            CLog.e(FragmentdeviceNew.this.TAG, "----------------onTouch Layout-------------");
                        } else if (FragmentdeviceNew.this._showBean != null) {
                            int _index2 = 0;
                            boolean _hasnext2 = false;
                            ArrayList<DeviceBean> _listBeans2 = App_phms.getInstance().showBeans;
                            for (int i2 = 0; i2 < _listBeans2.size(); i2++) {
                                if (FragmentdeviceNew.this._showBean.mMacAddr.equalsIgnoreCase(_listBeans2.get(i2).mMacAddr)) {
                                    _index2 = i2 - 1;
                                    _hasnext2 = true;
                                }
                            }
                            if (_hasnext2) {
                                if (_index2 == -1) {
                                    FragmentdeviceNew.this._showBean = _listBeans2.get(_listBeans2.size() - 1);
                                    FragmentdeviceNew.this.init_value(_listBeans2.get(_listBeans2.size() - 1));
                                } else {
                                    FragmentdeviceNew.this._showBean = _listBeans2.get(_index2);
                                    FragmentdeviceNew.this.init_value(_listBeans2.get(_index2));
                                }
                            }
                        }
                    }
                } else {
                    event.getAction();
                }
                return true;
            }
        });
        this.mtextView_name = (TextView) pView.findViewById(R.id.textView_name);
        this.mtextView_name.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        this.mTextView_number = (TextView) pView.findViewById(R.id.TextView_number);
        this.mTextView_device_state = (TextView) pView.findViewById(R.id.TextView_device_state);
        this.mTextView_device_progress = (TextView) pView.findViewById(R.id.TextView_device_progress);
        this.mImageView_Device = (ImageView) pView.findViewById(R.id.imageView_device);
        this._devicelist_change_user = (CircularImage) pView.findViewById(R.id.devicelist_change_user);
        getSdcardPhoto(App_phms.getInstance().GetUserInfoNAME(), "imagehead.png");
        this._devicelist_change_user.setOnClickListener(this);
        this._imagebtn_startmanagerdevice = (ImageButton) pView.findViewById(R.id.imagebtn_startmanagerdevice);
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.msearch_new_devices_bt = (ImageButton) this.mView.findViewById(R.id.search_new_devices_bt);
        this.msearch_new_devices_bt.getLayoutParams().height = Constants.M_SCREENWEIGH / 2;
        this.msearch_new_devices_bt.getLayoutParams().width = Constants.M_SCREENWEIGH / 2;
        this.mlastvaluetimetv = (TextView) this.mView.findViewById(R.id.lastvaluetimetv);
        setTypeFace(this.mlastvaluetimetv);
        this.mhavedevicesLiealy = (RelativeLayout) this.mView.findViewById(R.id.havedevicesLiealy);
        this.mfirst_search_new_devices_layout = (RelativeLayout) this.mView.findViewById(R.id.first_search_new_devices_layout);
        this.mdevice_allvalue_framelayout = (RelativeLayout) this.mView.findViewById(R.id.device_allvalue_framelayout);
        this.monlyONEValueDeviceLy = (LinearLayout) this.mView.findViewById(R.id.onlyONEValueDeviceLy);
        this.monlyonevaleunittv = (TextView) this.mView.findViewById(R.id.onlyonevaleunittv);
        this.monlyonevaletv = (TextView) this.mView.findViewById(R.id.onlyonevaletv);
        this.monlyonevalesecondunit = (TextView) this.mView.findViewById(R.id.onlyonevalesecondunit);
        this.monlyTWOValueDeviceLy = (LinearLayout) this.mView.findViewById(R.id.onlyTWOValueDeviceLy);
        this.monlyTWOvaleFirstVauleName = (TextView) this.mView.findViewById(R.id.onlyTWOvaleFirstVauleName);
        this.monlyTWOvaleFirstVaule = (TextView) this.mView.findViewById(R.id.onlyTWOvaleFirstVaule);
        this.monlyTWOvaleFirstVauleunit = (TextView) this.mView.findViewById(R.id.onlyTWOvaleFirstVauleunit);
        this.monlyTWOvaleSecondVauleName = (TextView) this.mView.findViewById(R.id.onlyTWOvaleSecondVauleName);
        this.monlyTWOvaleSecondVaule = (TextView) this.mView.findViewById(R.id.onlyTWOvaleSecondVaule);
        this.monlyTWOvaleSecondVauleunit = (TextView) this.mView.findViewById(R.id.onlyTWOvaleSecondVauleunit);
        this.monlyTHREEValueDeviceLy = (LinearLayout) this.mView.findViewById(R.id.onlyTHREEValueDeviceLy);
        this.monlyThreevalueFirstVauleName = (TextView) this.mView.findViewById(R.id.onlyThreevalueFirstVauleName);
        this.monlyThreevalueFirstVaule = (TextView) this.mView.findViewById(R.id.onlyThreevalueFirstVaule);
        this.monlyThreevalueFirstVauleUnit = (TextView) this.mView.findViewById(R.id.onlyThreevalueFirstVauleUnit);
        this.monlyThreevalueSecondVauleName = (TextView) this.mView.findViewById(R.id.onlyThreevalueSecondVauleName);
        this.monlyThreevalueSecondVaule = (TextView) this.mView.findViewById(R.id.onlyThreevalueSecondVaule);
        this.monlyThreevalueSecondVauleUnit = (TextView) this.mView.findViewById(R.id.onlyThreevalueSecondVauleUnit);
        this.monlyThreevalueThirdVauleName = (TextView) this.mView.findViewById(R.id.onlyThreevalueThirdVauleName);
        this.monlyThreevalueThirdVaule = (TextView) this.mView.findViewById(R.id.onlyThreevalueThirdVaule);
        this.monlyThreevalueThirdVauleUnit = (TextView) this.mView.findViewById(R.id.onlyThreevalueThirdVauleUnit);
        this.msearch_new_devices_bt.setOnClickListener(this);
        this._imagebtn_startmanagerdevice.setOnClickListener(this);
        this.mUserNameTextView = (TextView) this.mView.findViewById(R.id.user_name_text);
        String _userName = PageUtil.getLoginUserInfo().mUserName;
        if (_userName == null || _userName.equals(bs.b)) {
            _userName = PageUtil.getLoginUserInfo().mUID.substring(PageUtil.getLoginUserInfo().mUID.length() - 4);
        }
        this.mUserNameTextView.setText(_userName);
    }

    private void startcallback() {
        if (DeviceManager.mDeviceList != null && DeviceManager.mDeviceList.size() > 0) {
            if (!this.mBluetoothAdapter.isEnabled()) {
                this.context.registerReceiver(this.mReceiverBluetoothOnOff, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
                this.mBluetoothAdapter.enable();
                return;
            }
            BluetoothServerService.stopServer(getActivity());
            Intent _intentBServer = new Intent(getActivity(), BluetoothServerService.class);
            _intentBServer.putExtra("start_thread", true);
            App_phms.getInstance().getApplicationContext().startService(_intentBServer);
        }
    }

    private void init_value(DeviceBean pBean) {
        ArrayList<DeviceBean> _showList = App_phms.getInstance().showBeans;
        int _size = _showList.size();
        for (int i = 0; i < _size; i++) {
            if (_showList.get(i).mMacAddr.equalsIgnoreCase(pBean.mMacAddr)) {
                _showList.get(i).mState = pBean.mState;
            }
        }
        if (pBean != null && pBean.mDeviceName.length() > 0) {
            init_name_image(pBean.mDeviceName);
            this.mTextView_number.setText(new StringBuilder(String.valueOf(pBean.mCode)).toString());
            int _state = pBean.mState;
            int _progress = pBean.mProgress;
            this.mTextView_device_progress.setText(_progress + "%");
            if (_progress > 100) {
                this.mTextView_device_progress.setText("100%");
            }
            switchTextClor(_state, this.mTextView_device_state);
            DeviceListItemBeanDao _deviceBean = DeviceListDaoOperation.getInstance().getReceiveDataStr(pBean.mMacAddr);
            CLog.e(this.TAG, "init_value" + pBean.mMacAddr);
            if (_deviceBean != null) {
                String _lastData = _deviceBean.mReceiveDataStr;
                String _lastDate = _deviceBean.mDataTime;
                CLog.e(this.TAG, "接受完数据通知更新界面" + _lastData + " 那个设备： " + pBean.mDeviceName + "  lasteDate:" + _lastDate);
                if (_lastData != null) {
                    this.mlastvaluetimetv.setText(_lastDate);
                    matchDeviceName(pBean.mDeviceName, _lastData);
                    return;
                }
                this.mdevice_allvalue_framelayout.setVisibility(View.GONE);
                return;
            }
            this.mdevice_allvalue_framelayout.setVisibility(View.GONE);
        }
    }

    private void init_name_image(String pName) {
        if ("CMS50IW".equals(pName)) {
            this.mtextView_name.setText(this.context.getResources().getString(R.string.device_productname_50EW));
            this.mImageView_Device.setBackgroundResource(R.drawable.drawable_data_device_cms50iw);
        } else if ("cmssxt".equalsIgnoreCase(pName)) {
            this.mtextView_name.setText(this.context.getResources().getString(R.string.device_productname_SXT));
            this.mImageView_Device.setBackgroundResource(R.drawable.drawable_data_device_cmxxst);
        } else if ("ABPM50W".equalsIgnoreCase(pName)) {
            this.mtextView_name.setText(this.context.getResources().getString(R.string.device_productname_M50W));
            this.mImageView_Device.setBackgroundResource(R.drawable.drawable_data_device_abpm50);
        } else if (DeviceNameUtils.PM50.equalsIgnoreCase(pName)) {
            this.mtextView_name.setText(this.context.getResources().getString(R.string.device_productname_pm50));
            this.mImageView_Device.setBackgroundResource(R.drawable.drawable_data_device_pm50);
        } else if (DeviceNameUtils.TEMP03.equalsIgnoreCase(pName)) {
            this.mtextView_name.setText(this.context.getResources().getString(R.string.device_productname_temp03));
            this.mImageView_Device.setBackgroundResource(R.drawable.drawable_data_device_temp03);
        } else if ("CONTEC08AW".equalsIgnoreCase(pName)) {
            this.mtextView_name.setText(this.context.getResources().getString(R.string.device_productname_08AW));
            this.mImageView_Device.setBackgroundResource(R.drawable.drawable_data_device_a08);
        } else if ("CMS50F".equalsIgnoreCase(pName)) {
            this.mtextView_name.setText(this.context.getResources().getString(R.string.device_name_50IW));
            this.mImageView_Device.setImageResource(R.drawable.drawable_device_cms50f);
        } else if ("CONTEC08C".equalsIgnoreCase(pName)) {
            this.mtextView_name.setText(this.context.getResources().getString(R.string.device_productname_08AW));
            this.mImageView_Device.setBackgroundResource(R.drawable.drawable_device_c08);
        } else if (Constants.CMS50EW.equalsIgnoreCase(pName) || "CMS50DW".equalsIgnoreCase(pName)) {
            this.mtextView_name.setText(this.context.getResources().getString(R.string.device_productname_50EW));
            this.mImageView_Device.setBackgroundResource(R.drawable.drawable_data_device_cms50ew);
        } else if ("sp10w".equalsIgnoreCase(pName)) {
            this.mtextView_name.setText(this.context.getResources().getString(R.string.device_productname_SP10W));
            this.mImageView_Device.setBackgroundResource(R.drawable.drawable_data_device_sp10w);
        } else if ("cmsvesd".equalsIgnoreCase(pName)) {
            this.mtextView_name.setText(this.context.getResources().getString(R.string.device_productname_VESD));
            this.mImageView_Device.setBackgroundResource(R.drawable.drawable_device_cmsvesd);
        } else if ("wt".equalsIgnoreCase(pName)) {
            this.mtextView_name.setText(this.context.getResources().getString(R.string.device_productname_WT));
            this.mImageView_Device.setBackgroundResource(R.drawable.drawable_data_device_wt);
        } else if ("FHR01".equalsIgnoreCase(pName)) {
            this.mtextView_name.setText(this.context.getResources().getString(R.string.device_productname_Fhr01));
            this.mImageView_Device.setBackgroundResource(R.drawable.drawable_data_device_fhr01);
        } else if (Constants.PM85_NAME.equalsIgnoreCase(pName)) {
            this.mtextView_name.setText(this.context.getResources().getString(R.string.device_productname_Pm85));
            this.mImageView_Device.setBackgroundResource(R.drawable.drawable_data_device_pm85);
        } else if (Constants.CMS50K_NAME.equalsIgnoreCase(pName)) {
            this.mtextView_name.setText(this.context.getResources().getString(R.string.device_productname_Pm85));
            this.mImageView_Device.setBackgroundResource(R.drawable.cms50k);
        } else if (Constants.CMS50K1_NAME.equalsIgnoreCase(pName)) {
            this.mtextView_name.setText(this.context.getResources().getString(R.string.device_productname_Pm85));
            this.mImageView_Device.setBackgroundResource(R.drawable.cms50k1);
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.search_new_devices_bt) {
            Message msg = new Message();
            msg.what = Constants.OPEN_SEARCHDEVICE_FRAGMENT;
            msg.arg2 = 1;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
        } else if (v.getId() == R.id.imagebtn_startmanagerdevice) {
            BluetoothServerService.stopServer(getActivity());
            Message msg2 = new Message();
            msg2.what = Constants.OPEN_DEVICE_MANAGEDEVICE;
            msg2.arg2 = 1;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg2);
        } else if (v.getId() == R.id.devicelist_change_user) {
            startActivityForResult(new Intent(getActivity(), ActivityChooseHead.class), 1);
        }
    }

    private void getSdcardPhoto(String puserflag, String picturename) {
        String pSdCardPath = Environment.getExternalStorageDirectory() + CookieSpec.PATH_DELIM;
        String _append = "contec/userinfo/" + puserflag + CookieSpec.PATH_DELIM;
        FileOperation.makeDirs(String.valueOf(pSdCardPath) + _append);
        try {
            if (new File(String.valueOf(pSdCardPath) + _append + picturename).exists()) {
                this._devicelist_change_user.setImageBitmap(BitmapFactory.decodeFile(String.valueOf(pSdCardPath) + _append + picturename));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                if (!(data == null || data.getExtras().getParcelable("data") == null)) {
                    Bitmap tempdata = (Bitmap) data.getExtras().getParcelable("data");
                    Bitmap photo = Bitmap.createScaledBitmap(tempdata, 200, 200, true);
                    SaveHeadiconToSDCard(tempdata);
                    this._devicelist_change_user.setBackgroundResource(0);
                    this._devicelist_change_user.setImageBitmap(photo);
                    break;
                }
            case 2:
                if (data != null) {
                    int extras = data.getIntExtra("grid_item", 1);
                    SaveHeadiconToSDCard(extras);
                    this._devicelist_change_user.setBackgroundResource(0);
                    this._devicelist_change_user.setImageResource(extras);
                    break;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void SaveHeadiconToSDCard(int pictureSource) {
        Bitmap photo = BitmapFactory.decodeResource(getActivity().getResources(), pictureSource);
        String _path = FileOperation.getSdcardUserHeadPath(App_phms.getInstance().GetUserInfoNAME());
        File file = new File(_path);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            photo.compress(Bitmap.CompressFormat.PNG, 60, new FileOutputStream(String.valueOf(_path) + "imagehead.png", false));
            photo.compress(Bitmap.CompressFormat.PNG, 60, new ByteArrayOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SaveHeadiconToSDCard(Bitmap _bitmap) {
        Bitmap photo = _bitmap;
        String _path = FileOperation.getSdcardUserHeadPath(App_phms.getInstance().GetUserInfoNAME());
        File file = new File(_path);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            photo.compress(Bitmap.CompressFormat.PNG, 60, new FileOutputStream(String.valueOf(_path) + "imagehead.png", false));
            photo.compress(Bitmap.CompressFormat.PNG, 60, new ByteArrayOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startAddDevice(boolean ifCircle) {
        BluetoothAdapter mBluetoothAdapter2 = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter2.isDiscovering()) {
            mBluetoothAdapter2.cancelDiscovery();
            Constants.GO_TO_ADD_DEVICE = true;
            SearchDevice.stopServer(getActivity());
            DeviceManager.stopServer(getActivity());
            UploadService.stopServer(getActivity());
            MessageManager.stopServer(getActivity());
            DeviceService.stopServer(getActivity());
            Server_Main.stopServer(getActivity());
            BluetoothServerService.stopServer(getActivity());
            startActivity(new Intent(this.context, FragmentDataSerchDevice.class));
        } else if (ifCircle) {
            if (DeviceService.mReceiveFinished || DeviceManager.mDeviceList == null || DeviceManager.mDeviceList.size() <= 0) {
                SearchDevice.stopServer(getActivity());
                DeviceManager.stopServer(getActivity());
                UploadService.stopServer(getActivity());
                MessageManager.stopServer(getActivity());
                DeviceService.stopServer(getActivity());
                Server_Main.stopServer(getActivity());
                Constants.GO_TO_ADD_DEVICE = false;
                startActivity(new Intent(this.context, FragmentDataSerchDevice.class));
                return;
            }
            Constants.GO_TO_ADD_DEVICE = true;
            Message msgs = new Message();
            msgs.what = Constants.V_SHOW_ADD_DEVICE_DIALOG;
            msgs.arg2 = 1;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs);
        } else if (!DeviceService.mReceiveFinished) {
            Constants.GO_TO_ADD_DEVICE = true;
            Message msgs2 = new Message();
            msgs2.what = Constants.V_SHOW_ADD_DEVICE_DIALOG;
            msgs2.arg2 = 1;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs2);
        } else {
            SearchDevice.stopServer(getActivity());
            DeviceManager.stopServer(getActivity());
            UploadService.stopServer(getActivity());
            MessageManager.stopServer(getActivity());
            DeviceService.stopServer(getActivity());
            Server_Main.stopServer(getActivity());
            Constants.GO_TO_ADD_DEVICE = false;
            startActivity(new Intent(this.context, FragmentDataSerchDevice.class));
        }
    }

    public void onDestroy() {
        super.onDestroy();
        App_phms.getInstance().mEventBus.unregister(this);
    }

    public void onEvent(DeviceBean mBean) {
    }

    public void onEvent(EventShowLastData pshowlast) {
        DeviceListItemBeanDao _deviceBean = DeviceListDaoOperation.getInstance().getReceiveDataStr(DeviceManager.m_DeviceBean.mMacAddr);
        if (_deviceBean != null) {
            String _lastData = _deviceBean.mReceiveDataStr;
            String _lastDate = _deviceBean.mDataTime;
            CLog.e(this.TAG, "接受完数据通知更新界面" + _lastData + " 那个设备： " + DeviceManager.m_DeviceBean.mDeviceName + "  lasteDate:" + _lastDate);
            if (_lastData != null) {
                this.mlastvaluetimetv.setText(_lastDate);
                matchDeviceName(DeviceManager.m_DeviceBean.mDeviceName, _lastData);
                return;
            }
            this.mdevice_allvalue_framelayout.setVisibility(View.GONE);
            return;
        }
        this.mdevice_allvalue_framelayout.setVisibility(View.GONE);
    }

    private void matchDeviceName(String pDeviceName, String pReceviceStr) {
        String[] _data = pReceviceStr.split(";");
        if (Constants.CMS50EW.equalsIgnoreCase(pDeviceName)) {
            this.mdevice_allvalue_framelayout.setVisibility(View.VISIBLE);
            this.monlyONEValueDeviceLy.setVisibility(View.GONE);
            this.monlyTWOValueDeviceLy.setVisibility(View.VISIBLE);
            this.monlyTHREEValueDeviceLy.setVisibility(View.GONE);
            this.monlyTWOvaleFirstVauleName.setText("血氧");
            if (Integer.parseInt(_data[0]) < 93) {
                this.monlyTWOvaleFirstVaule.setTextColor(SupportMenu.CATEGORY_MASK);
            } else {
                this.monlyTWOvaleFirstVaule.setTextColor(getActivity().getResources().getColor(R.color.green_pro_normal));
            }
            this.monlyTWOvaleFirstVaule.setText(_data[0]);
            this.monlyTWOvaleFirstVauleunit.setText("%");
            this.monlyTWOvaleSecondVauleName.setText("脉率");
            int _lv = Integer.parseInt(_data[1]);
            if (60 >= _lv || _lv >= 100) {
                this.monlyTWOvaleSecondVaule.setTextColor(SupportMenu.CATEGORY_MASK);
            } else {
                this.monlyTWOvaleSecondVaule.setTextColor(getActivity().getResources().getColor(R.color.green_pro_normal));
            }
            this.monlyTWOvaleSecondVaule.setText(_data[1]);
            this.monlyTWOvaleSecondVauleunit.setText("bmp");
        } else if ("CMS50IW".equalsIgnoreCase(pDeviceName)) {
            this.mdevice_allvalue_framelayout.setVisibility(View.VISIBLE);
            this.monlyONEValueDeviceLy.setVisibility(View.GONE);
            this.monlyTWOValueDeviceLy.setVisibility(View.VISIBLE);
            this.monlyTHREEValueDeviceLy.setVisibility(View.GONE);
            this.monlyTWOvaleFirstVauleName.setText("血氧");
            if (Integer.parseInt(_data[0]) < 93) {
                this.monlyTWOvaleFirstVaule.setTextColor(SupportMenu.CATEGORY_MASK);
            } else {
                this.monlyTWOvaleFirstVaule.setTextColor(getActivity().getResources().getColor(R.color.green_pro_normal));
            }
            this.monlyTWOvaleFirstVaule.setText(_data[0]);
            this.monlyTWOvaleFirstVauleunit.setText("%");
            this.monlyTWOvaleFirstVauleunit.setTextColor(-16777216);
            this.monlyTWOvaleSecondVauleName.setText("脉率");
            int _lv2 = Integer.parseInt(_data[1]);
            if (60 >= _lv2 || _lv2 >= 100) {
                this.monlyTWOvaleSecondVaule.setTextColor(SupportMenu.CATEGORY_MASK);
            } else {
                this.monlyTWOvaleSecondVaule.setTextColor(getActivity().getResources().getColor(R.color.green_pro_normal));
            }
            this.monlyTWOvaleSecondVaule.setText(_data[1]);
            this.monlyTWOvaleSecondVauleunit.setText("bmp");
            this.monlyTWOvaleSecondVauleunit.setTextColor(-16777216);
        } else if ("SP10W".equalsIgnoreCase(pDeviceName)) {
            this.mdevice_allvalue_framelayout.setVisibility(View.GONE);
            this.monlyONEValueDeviceLy.setVisibility(View.GONE);
            this.monlyTWOValueDeviceLy.setVisibility(View.GONE);
            this.monlyTHREEValueDeviceLy.setVisibility(View.GONE);
            this.monlyThreevalueFirstVauleName.setText("FVC");
            this.monlyThreevalueFirstVaule.setText(_data[0]);
            this.monlyThreevalueFirstVauleUnit.setText("L");
            this.monlyThreevalueSecondVauleName.setText("PEF");
            this.monlyThreevalueSecondVaule.setText(_data[1]);
            this.monlyThreevalueSecondVauleUnit.setText("L/s");
            this.monlyThreevalueThirdVauleName.setText("FEV1");
            this.monlyThreevalueThirdVaule.setText(_data[2]);
            this.monlyThreevalueThirdVauleUnit.setText("L/s");
        } else if ("CMSSXT".equalsIgnoreCase(pDeviceName)) {
            this.mdevice_allvalue_framelayout.setVisibility(View.VISIBLE);
            this.monlyONEValueDeviceLy.setVisibility(View.VISIBLE);
            this.monlyTWOValueDeviceLy.setVisibility(View.GONE);
            this.monlyTHREEValueDeviceLy.setVisibility(View.GONE);
            this.monlyonevaletv.setText("血糖");
            this.monlyonevaleunittv.setText(_data[0]);
            this.monlyonevalesecondunit.setText("mmol/L");
        } else if ("ABPM50W".equalsIgnoreCase(pDeviceName)) {
            this.mdevice_allvalue_framelayout.setVisibility(View.VISIBLE);
            this.monlyONEValueDeviceLy.setVisibility(View.GONE);
            this.monlyTWOValueDeviceLy.setVisibility(View.VISIBLE);
            this.monlyTHREEValueDeviceLy.setVisibility(View.GONE);
        } else if (DeviceNameUtils.PM50.equalsIgnoreCase(pDeviceName)) {
            this.mdevice_allvalue_framelayout.setVisibility(View.VISIBLE);
            this.monlyONEValueDeviceLy.setVisibility(View.GONE);
            this.monlyTWOValueDeviceLy.setVisibility(View.VISIBLE);
            this.monlyTHREEValueDeviceLy.setVisibility(View.GONE);
        } else if ("CONTEC08AW".equalsIgnoreCase(pDeviceName)) {
            this.mdevice_allvalue_framelayout.setVisibility(View.VISIBLE);
            this.monlyONEValueDeviceLy.setVisibility(View.GONE);
            this.monlyTWOValueDeviceLy.setVisibility(View.VISIBLE);
            this.monlyTHREEValueDeviceLy.setVisibility(View.GONE);
            this.monlyTWOvaleFirstVauleName.setText("高压");
            int _bo = Integer.parseInt(_data[0]);
            if (_bo < 90 || _bo > 140) {
                this.monlyTWOvaleFirstVaule.setTextColor(SupportMenu.CATEGORY_MASK);
            } else {
                this.monlyTWOvaleFirstVaule.setTextColor(getActivity().getResources().getColor(R.color.green_pro_normal));
            }
            this.monlyTWOvaleFirstVaule.setText(_data[0]);
            this.monlyTWOvaleFirstVauleunit.setText("mmHg");
            this.monlyTWOvaleSecondVauleName.setText("低压");
            int _lv3 = Integer.parseInt(_data[1]);
            if (60 >= _lv3 || _lv3 >= 90) {
                this.monlyTWOvaleSecondVaule.setTextColor(SupportMenu.CATEGORY_MASK);
            } else {
                this.monlyTWOvaleSecondVaule.setTextColor(getActivity().getResources().getColor(R.color.green_pro_normal));
            }
            this.monlyTWOvaleSecondVaule.setText(_data[1]);
            this.monlyTWOvaleSecondVauleunit.setText("mmHg");
        } else if ("WT".equalsIgnoreCase(pDeviceName)) {
            this.mdevice_allvalue_framelayout.setVisibility(View.VISIBLE);
            this.monlyONEValueDeviceLy.setVisibility(View.VISIBLE);
            this.monlyTWOValueDeviceLy.setVisibility(View.GONE);
            this.monlyTHREEValueDeviceLy.setVisibility(View.GONE);
            this.monlyonevaleunittv.setText("体重");
            this.monlyonevaletv.setText(_data[0]);
            this.monlyonevaletv.setTextColor(getResources().getColor(R.color.green_pro_normal));
            this.monlyonevalesecondunit.setText(ExpandedProductParsedResult.KILOGRAM);
        } else if ("FHR01".equalsIgnoreCase(pDeviceName)) {
            this.mdevice_allvalue_framelayout.setVisibility(View.VISIBLE);
            this.monlyONEValueDeviceLy.setVisibility(View.VISIBLE);
            this.monlyTWOValueDeviceLy.setVisibility(View.GONE);
            this.monlyTHREEValueDeviceLy.setVisibility(View.GONE);
            this.monlyonevaleunittv.setText("胎心率");
            this.monlyonevaletv.setTextColor(-16777216);
            this.monlyonevaletv.setText(_data[0]);
            this.monlyonevalesecondunit.setText("bmp");
            if (Integer.parseInt(_data[0]) > 160 || Integer.parseInt(_data[0]) < 120) {
                this.monlyonevaletv.setTextColor(SupportMenu.CATEGORY_MASK);
            } else {
                this.monlyonevaletv.setTextColor(getResources().getColor(R.color.green_pro_normal));
            }
        } else if (Constants.PM85_NAME.equalsIgnoreCase(pDeviceName)) {
            this.mdevice_allvalue_framelayout.setVisibility(View.GONE);
        }
    }

    void initList() {
        if (DeviceManager.mDeviceBeanList != null) {
            if (DeviceManager.mDeviceBeanList.mBeanList != null) {
                DeviceManager.mDeviceBeanList.mBeanList.clear();
            }
            DeviceManager.mWhichDevice = 0;
            DeviceManager.mWhichItem = 0;
            DeviceManager.mWhichSortIndex = 0;
        }
        if (DeviceManager.mDeviceList != null) {
            if (DeviceManager.mDeviceList.getListDevice() != null) {
                for (int i = 0; i < DeviceManager.mDeviceList.getListDevice().size(); i++) {
                    DeviceManager.mDeviceList.getListDevice().get(i).mBeanList.clear();
                }
                DeviceManager.mDeviceList.getListDevice().clear();
            }
            DeviceManager.mDeviceList.removeAll();
            DeviceManager.mDeviceList = null;
        }
        DeviceManager.mDeviceList = DeviceListDaoOperation.getInstance().getDeviceListItem();
        if (!DeviceManager.m_DeviceBean.mMacAddr.equalsIgnoreCase(bs.b)) {
            this._showBean = DeviceManager.m_DeviceBean;
            if (this._showBean != null) {
                init_value(this._showBean);
            }
        } else if (DeviceManager.mDeviceList != null && DeviceManager.mDeviceList.size() > 0) {
            init_value(DeviceManager.mDeviceList.getDevice(0).mBeanList.get(0));
            this._showBean = DeviceManager.mDeviceList.getDevice(0).mBeanList.get(0);
        }
    }

    private void checkBlueTooth() {
        CLog.e(this.TAG, "FragmentdeviceNew onResume checkBlueTooth....");
        if (!this.mBluetoothAdapter.isEnabled()) {
            this.context.registerReceiver(this.mReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
            this.mBluetoothAdapter.enable();
        } else if (DeviceManager.mDeviceList != null && DeviceManager.mDeviceList.size() > 0) {
            Constants.BLUETOOTHSTAT = 1;
            this.context.startService(new Intent(this.context, Server_Main.class));
        }
    }

    public static FragmentdeviceNew getInterface() {
        if (mFragmentdevicelist == null) {
            return new FragmentdeviceNew();
        }
        return mFragmentdevicelist;
    }

    private void switchTextClor(int p_state, TextView p_TV) {
        p_TV.setText("— —");
        CLog.e(this.TAG, "-------------------------->" + p_state);
        switch (p_state) {
            case 0:
                p_TV.setText(R.string.waiting_connection);
                p_TV.setTextColor(this.context.getResources().getColor(R.color.blue_1));
                this._imagebtn_startmanagerdevice.setClickable(false);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_gear_setting_sgray));
                this.mTextView_device_progress.setVisibility(View.INVISIBLE);
                return;
            case 1:
                p_TV.setText(R.string.connectting);
                p_TV.setTextColor(this.context.getResources().getColor(R.color.blue_1));
                this._imagebtn_startmanagerdevice.setClickable(false);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_gear_setting_sgray));
                this.mTextView_device_progress.setVisibility(View.INVISIBLE);
                return;
            case 2:
                p_TV.setText(R.string.collecting_datas);
                p_TV.setTextColor(this.context.getResources().getColor(R.color.blue_1));
                this._imagebtn_startmanagerdevice.setClickable(false);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_gear_setting_sgray));
                this.mTextView_device_progress.setVisibility(View.VISIBLE);
                return;
            case 3:
                p_TV.setText(this.context.getResources().getString(R.string.please_research));
                this._imagebtn_startmanagerdevice.setClickable(true);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawable_gotodevicemanger_settings));
                return;
            case 4:
                p_TV.setText(R.string.collecting_datas);
                p_TV.setTextColor(this.context.getResources().getColor(R.color.blue_1));
                this._imagebtn_startmanagerdevice.setClickable(false);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_gear_setting_sgray));
                this.mTextView_device_progress.setVisibility(View.VISIBLE);
                return;
            case 5:
                p_TV.setText(this.context.getResources().getString(R.string.nodata_or_data_erro));
                this._imagebtn_startmanagerdevice.setClickable(true);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawable_gotodevicemanger_settings));
                this.mTextView_device_progress.setVisibility(View.INVISIBLE);
                return;
            case 6:
                p_TV.setText(R.string.wait_uploading);
                p_TV.setTextColor(this.context.getResources().getColor(R.color.blue_1));
                this._imagebtn_startmanagerdevice.setClickable(false);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_gear_setting_sgray));
                this.mTextView_device_progress.setVisibility(View.INVISIBLE);
                return;
            case 7:
                p_TV.setText(R.string.supload);
                this._imagebtn_startmanagerdevice.setClickable(false);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_gear_setting_sgray));
                this.mTextView_device_progress.setVisibility(View.VISIBLE);
                return;
            case 8:
                p_TV.setText(this.context.getResources().getString(R.string.str_upload_success));
                this._imagebtn_startmanagerdevice.setClickable(true);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawable_gotodevicemanger_settings));
                this.mTextView_device_progress.setVisibility(View.INVISIBLE);
                return;
            case 9:
                p_TV.setText(this.context.getResources().getString(R.string.str_upload_failed));
                this._imagebtn_startmanagerdevice.setClickable(true);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawable_gotodevicemanger_settings));
                this.mTextView_device_progress.setVisibility(View.INVISIBLE);
                return;
            case 10:
                p_TV.setText(this.context.getResources().getString(R.string.no_data_device));
                this._imagebtn_startmanagerdevice.setClickable(true);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawable_gotodevicemanger_settings));
                this.mTextView_device_progress.setVisibility(View.INVISIBLE);
                return;
            case 11:
                p_TV.setText(this.context.getResources().getString(R.string.this_uploaded));
                this._imagebtn_startmanagerdevice.setClickable(true);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawable_gotodevicemanger_settings));
                this.mTextView_device_progress.setVisibility(View.INVISIBLE);
                return;
            case 12:
                p_TV.setText(this.context.getResources().getString(R.string.str_upload_failed));
                this._imagebtn_startmanagerdevice.setClickable(true);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawable_gotodevicemanger_settings));
                this.mTextView_device_progress.setVisibility(View.INVISIBLE);
                return;
            case 13:
                p_TV.setText(this.context.getResources().getString(R.string.devices_not_found));
                this._imagebtn_startmanagerdevice.setClickable(true);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawable_gotodevicemanger_settings));
                this.mTextView_device_progress.setVisibility(View.INVISIBLE);
                return;
            case 14:
                p_TV.setText(this.context.getResources().getString(R.string.s_d_i));
                this._imagebtn_startmanagerdevice.setClickable(true);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawable_gotodevicemanger_settings));
                this.mTextView_device_progress.setVisibility(View.INVISIBLE);
                return;
            case 15:
                p_TV.setText(this.context.getResources().getString(R.string.deviceerror_pair_error));
                this._imagebtn_startmanagerdevice.setClickable(true);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawable_gotodevicemanger_settings));
                this.mTextView_device_progress.setVisibility(View.INVISIBLE);
                return;
            case 16:
                p_TV.setText(this.context.getResources().getString(R.string.Up_loadthe_limited_number_of));
                this._imagebtn_startmanagerdevice.setClickable(true);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawable_gotodevicemanger_settings));
                this.mTextView_device_progress.setVisibility(View.INVISIBLE);
                return;
            case 17:
                p_TV.setText(this.context.getResources().getString(R.string.bt_pairing));
                this._imagebtn_startmanagerdevice.setClickable(true);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawable_gotodevicemanger_settings));
                this.mTextView_device_progress.setVisibility(View.INVISIBLE);
                return;
            case 18:
                p_TV.setText(this.context.getResources().getString(R.string.str_upload_case));
                this._imagebtn_startmanagerdevice.setClickable(false);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_gear_setting_sgray));
                this.mTextView_device_progress.setVisibility(View.VISIBLE);
                return;
            case 19:
                p_TV.setText(this.context.getResources().getString(R.string.str_upload_case));
                this._imagebtn_startmanagerdevice.setClickable(false);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_gear_setting_sgray));
                this.mTextView_device_progress.setVisibility(View.VISIBLE);
                return;
            case 20:
                this._imagebtn_startmanagerdevice.setClickable(true);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawable_gotodevicemanger_settings));
                this.mTextView_device_progress.setVisibility(View.INVISIBLE);
                return;
            case 21:
                this._imagebtn_startmanagerdevice.setClickable(true);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawable_gotodevicemanger_settings));
                this.mTextView_device_progress.setVisibility(View.INVISIBLE);
                return;
            case 24:
                p_TV.setText(this.context.getResources().getString(R.string.str_upload_case));
                this._imagebtn_startmanagerdevice.setClickable(true);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawable_gotodevicemanger_settings));
                this.mTextView_device_progress.setVisibility(View.VISIBLE);
                return;
            case 27:
                this._imagebtn_startmanagerdevice.setClickable(true);
                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawable_gotodevicemanger_settings));
                this.mTextView_device_progress.setVisibility(View.INVISIBLE);
                return;
//            case Response.TYPE_MASK /*28*/:
//                this._imagebtn_startmanagerdevice.setClickable(true);
//                this._imagebtn_startmanagerdevice.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawable_gotodevicemanger_settings));
//                this.mTextView_device_progress.setVisibility(View.INVISIBLE);
//                return;
            default:
                return;
        }
    }
}
