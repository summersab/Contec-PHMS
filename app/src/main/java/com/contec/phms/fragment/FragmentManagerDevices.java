package com.contec.phms.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.contec.circleimage.widget.CircularImage;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.device.template.PollingService;
import com.contec.phms.domain.WaitConnectDeviceBean;
import com.contec.phms.manager.device.DeviceBean;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.db.DeviceListDaoOperation;
import com.contec.phms.db.DeviceListItemBeanDao;
import com.contec.phms.db.LocalLoginInfoManager;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.DeviceNameUtils;
import com.contec.phms.util.FileOperation;
import com.contec.phms.util.PageUtil;
import com.contec.phms.widget.DialogClass;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.httpclient.cookie.CookieSpec;
import u.aly.bs;

public class FragmentManagerDevices extends FragmentBase {
    private String SDCARD_PATH = "/mnt/sdcard/phms/";
    private CircularImage _devicelist_change_user;
    private ListView _managerdevicelist;
    String headName = "imagehead.png";
    private ManagerDevicesAdapter mManagerdeviceAdapter;
    private View mView;
    private Button madddevices;
    private TextView mdevicelist_data_collection;
    private Button mreturnbtn;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.layout_manager_devices, container, false);
        init_view(this.mView);
        return this.mView;
    }

    public void onResume() {
        super.onResume();
        CLog.i("jxx", "call 设备管理页面的onResume method");
        getSdcardPhoto(App_phms.getInstance().GetUserInfoNAME(), this.headName);
        ArrayList<DeviceBean> _DeviceBeanlist = DeviceListDaoOperation.getInstance().getDevice();
        if (this.mManagerdeviceAdapter != null) {
            this.mManagerdeviceAdapter.setManagerDeviceslist(_DeviceBeanlist);
            this.mManagerdeviceAdapter.notifyDataSetChanged();
        }
        languageAdapter();
    }

    public void onStop() {
        super.onStop();
    }

    private void languageAdapter() {
        this.mdevicelist_data_collection.setText(getActivity().getString(R.string.device_manager));
        this.mreturnbtn.setText(getActivity().getString(R.string.back_btn_text));
        this.madddevices.setText(getActivity().getString(R.string.add_device_btn));
    }

    private void init_view(View pView) {
        this.mdevicelist_data_collection = (TextView) pView.findViewById(R.id.devicelist_data_collection);
        this._managerdevicelist = (ListView) pView.findViewById(R.id.managerdevicelist);
        this.mManagerdeviceAdapter = new ManagerDevicesAdapter(getActivity(), DeviceListDaoOperation.getInstance().getDevice());
        this._managerdevicelist.setAdapter(this.mManagerdeviceAdapter);
        this._devicelist_change_user = (CircularImage) pView.findViewById(R.id.devicelist_change_user);
        getSdcardPhoto(App_phms.getInstance().GetUserInfoNAME(), "imagehead.jpg");
        this.mreturnbtn = (Button) pView.findViewById(R.id.returnbtn);
        this.mreturnbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Message msg = new Message();
                msg.what = Constants.CLOSE_DEVICE_MANAGEDEVICE;
                msg.arg2 = 1;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
            }
        });
        this.madddevices = (Button) pView.findViewById(R.id.adddevices);
        this.madddevices.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Message msg = new Message();
                msg.what = Constants.OPEN_SEARCHDEVICE_FRAGMENT;
                msg.arg2 = 1;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
            }
        });
        TextView mUserNameTextView = (TextView) this.mView.findViewById(R.id.user_name_text);
        String _userName = PageUtil.getLoginUserInfo().mUserName;
        if (_userName == null || _userName.equals(bs.b)) {
            String name = PageUtil.getLoginUserInfo().mUID;
            if (LocalLoginInfoManager.getInstance().querySqlThirdCode(name)) {
                String _userName2 = LocalLoginInfoManager.getInstance().findByCardId(name).mThirdCode;
                if (_userName2.contains("@")) {
                    String[] targetQ = _userName2.split("@");
                    _userName = targetQ[0].substring(targetQ[0].length() - 4);
                } else {
                    _userName = name.substring(name.length() - 4);
                }
            } else {
                _userName = name.substring(name.length() - 4);
            }
        }
        mUserNameTextView.setText(_userName);
    }

    private void getSdcardPhoto(String puserflag, String picturename) {
        String pSdCardPath = Environment.getExternalStorageDirectory() + CookieSpec.PATH_DELIM;
        String _append = "contec/userinfo/" + puserflag + CookieSpec.PATH_DELIM;
        FileOperation.makeDirs(String.valueOf(pSdCardPath) + _append);
        try {
            if (new File(String.valueOf(pSdCardPath) + _append + picturename).exists()) {
                this._devicelist_change_user.setImageBitmap(BitmapFactory.decodeFile(String.valueOf(pSdCardPath) + _append + picturename));
                return;
            }
            this._devicelist_change_user.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_140915_twelve));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ManagerDevicesAdapter extends BaseAdapter {
        private Holder _holder;
        private Context mContext;
        private ArrayList<DeviceBean> managerDeviceslist;
        DialogClass mremovedialog;

        public ManagerDevicesAdapter(Context mContext2, ArrayList<DeviceBean> managerDeviceslist2) {
            this.mContext = mContext2;
            this.managerDeviceslist = managerDeviceslist2;
        }

        public void setManagerDeviceslist(ArrayList<DeviceBean> managerDeviceslist2) {
            this.managerDeviceslist = managerDeviceslist2;
        }

        public int getCount() {
            return this.managerDeviceslist.size();
        }

        public Object getItem(int arg0) {
            return Integer.valueOf(arg0);
        }

        public long getItemId(int arg0) {
            return (long) arg0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            this._holder = new Holder(this, (Holder) null);
            if (convertView == null) {
                convertView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_manager_device_item, (ViewGroup) null);
                this._holder.mnametv = (TextView) convertView.findViewById(R.id.managerDevicenametv);
                this._holder.mdeliamgebtn = (ImageButton) convertView.findViewById(R.id.managerDeviceDelBtn);
                this._holder.mrefreshiamgebtn = (ImageButton) convertView.findViewById(R.id.managerDeviceRefreshBtn);
                this._holder.mIconiamgebtn = (ImageView) convertView.findViewById(R.id.managerDeviceIconBtn);
                convertView.setTag(this._holder);
            } else {
                this._holder = (Holder) convertView.getTag();
            }
            DeviceBean _devicebean = this.managerDeviceslist.get(position);
            matchDevice(this._holder.mnametv, this._holder.mIconiamgebtn, new StringBuilder(String.valueOf(_devicebean.mDeviceName)).toString());
            this._holder.mnametv.setText(new StringBuilder(String.valueOf(_devicebean.mCode)).toString());
            this._holder.mdeliamgebtn.setOnClickListener(new View.OnClickListener() {
                @SuppressLint({"NewApi"})
                public void onClick(View arg0) {
                    ManagerDevicesAdapter managerDevicesAdapter = ManagerDevicesAdapter.this;
                    FragmentActivity activity = FragmentManagerDevices.this.getActivity();
                    String string = FragmentManagerDevices.this.getActivity().getString(R.string.removeDevice);
                    String string2 = FragmentManagerDevices.this.getActivity().getString(R.string.str_askdeletedevice);
                    final int i = position;
                    managerDevicesAdapter.mremovedialog = new DialogClass((Context) activity, string, string2, (DialogInterface.OnKeyListener) null, (View.OnClickListener) new View.OnClickListener() {
                        public void onClick(View arg0) {
                            DeviceBean _devicebeanDel = (DeviceBean) ManagerDevicesAdapter.this.managerDeviceslist.get(i);
                            try {
                                App_phms.getInstance().mHelper.getDeviceListItemDao().delete(App_phms.getInstance().mHelper.getDeviceListItemDao().queryBuilder().where().eq("UserName", App_phms.getInstance().mUserInfo.mUserID).and().eq(DeviceListItemBeanDao.DeviceMAC, _devicebeanDel.mMacAddr).query());
                                for (int i = 0; i < DeviceManager.mDeviceList.getListDevice().size(); i++) {
                                    if (DeviceManager.mDeviceList.getDevice(i).mDeviceName.equals(_devicebeanDel.mDeviceName)) {
                                        int j = 0;
                                        while (true) {
                                            if (j >= DeviceManager.mDeviceList.getDevice(i).mBeanList.size()) {
                                                break;
                                            }
                                            if (_devicebeanDel.mMacAddr.equals(DeviceManager.mDeviceList.getDevice(i).mBeanList.get(j).mMacAddr)) {
                                                DeviceManager.mDeviceList.getDevice(i).mBeanList.remove(j);
                                            }
                                            if (DeviceManager.mDeviceList.getDevice(i).mBeanList.size() == 0) {
                                                DeviceManager.mDeviceList.getListDevice().remove(i);
                                                break;
                                            }
                                            j++;
                                        }
                                    }
                                }
                                if (_devicebeanDel.mMacAddr.equalsIgnoreCase(DeviceManager.m_DeviceBean.mMacAddr)) {
                                    DeviceManager.m_DeviceBean = new DeviceBean(bs.b, bs.b);
                                }
                                for (int i2 = 0; i2 < App_phms.getInstance().showBeans.size(); i2++) {
                                    if (App_phms.getInstance().showBeans.get(i2).mMacAddr.equalsIgnoreCase(_devicebeanDel.mMacAddr)) {
                                        App_phms.getInstance().showBeans.remove(i2);
                                    }
                                }
                                List<WaitConnectDeviceBean> getmWaitConnectDeviceBeanLists = App_phms.getInstance().getmWaitConnectDeviceBeanLists();
                                int i3 = 0;
                                while (true) {
                                    if (i3 >= getmWaitConnectDeviceBeanLists.size()) {
                                        break;
                                    }
                                    if (_devicebeanDel.mMacAddr.equals(getmWaitConnectDeviceBeanLists.get(i3).getmDevicebean().mMacAddr)) {
                                        App_phms.getInstance().getmWaitConnectDeviceBeanLists().remove(i3);
                                        break;
                                    }
                                    i3++;
                                }
                                if (Constants.DEVICE_BLUEBOOTH_TYPE_CLASSIC.equals(_devicebeanDel.mBluetoothType)) {
                                    int index = -1;
                                    List<WaitConnectDeviceBean> getmRemoveConnectDeviceBeanLists = App_phms.getInstance().getmRemoveConnectDeviceBeanLists();
                                    int i4 = 0;
                                    while (true) {
                                        if (i4 >= getmRemoveConnectDeviceBeanLists.size()) {
                                            break;
                                        }
                                        String mMacAddr = getmRemoveConnectDeviceBeanLists.get(i4).getmDevicebean().mMacAddr;
                                        if (mMacAddr != null && mMacAddr.equals(_devicebeanDel.mMacAddr)) {
                                            index = i4;
                                            break;
                                        }
                                        i4++;
                                    }
                                    if (index != -1) {
                                        App_phms.getInstance().getmRemoveConnectDeviceBeanLists().remove(index);
                                    }
                                }
                                CLog.i("jxx", "call `PollingService.stopService()` method3");
                                PollingService.stopService(ManagerDevicesAdapter.this.mContext);
                                ManagerDevicesAdapter.this.mContext.startService(new Intent(ManagerDevicesAdapter.this.mContext, PollingService.class));
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            if (DeviceManager.mDeviceList == null || DeviceManager.mDeviceList.size() <= 0) {
                                Message msg = new Message();
                                msg.what = Constants.CLOSE_MANAGERDEVICE_FRAGMENT;
                                msg.arg2 = 1;
                                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
                                ManagerDevicesAdapter.this.notifyDataSetChanged();
                            } else {
                                Message msgs = new Message();
                                msgs.what = Constants.Del_Device;
                                msgs.arg2 = 1;
                                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs);
                                ManagerDevicesAdapter.this.managerDeviceslist.remove(i);
                                ManagerDevicesAdapter.this.notifyDataSetChanged();
                            }
                            ManagerDevicesAdapter.this.mremovedialog.dismiss();
                        }
                    }, (View.OnClickListener) new View.OnClickListener() {
                        public void onClick(View arg0) {
                            ManagerDevicesAdapter.this.mremovedialog.dismiss();
                        }
                    });
                }
            });
            this._holder.mrefreshiamgebtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    DeviceBean _devicebeanRefresh = (DeviceBean) ManagerDevicesAdapter.this.managerDeviceslist.get(position);
                    _devicebeanRefresh.ifAddNew = true;
                    DeviceManager.mRefreshBean = new DeviceBean(bs.b, bs.b);
                    DeviceManager.mRefreshBean = _devicebeanRefresh;
                    DeviceManager.mRefreshBean.mState = 1;
                    Message msg = new Message();
                    msg.what = Constants.CLOSE_MANAGERDEVICE_FRAGMENT;
                    msg.arg2 = 1;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
                }
            });
            return convertView;
        }

        private void matchDevice(TextView name, ImageView deviceimags, String _device) {
            if (Constants.DEVICE_8000GW_NAME.equalsIgnoreCase(_device)) {
                name.setText(this.mContext.getResources().getString(R.string.str_8000G));
                deviceimags.setImageResource(R.drawable.drawable_data_device_ecg);
            } else if (Constants.PM10_NAME.equalsIgnoreCase(_device)) {
                name.setText(this.mContext.getResources().getString(R.string.device_productname_pm10));
                deviceimags.setImageResource(R.drawable.drawable_device_pm10);
            } else if ("TEMP01".equalsIgnoreCase(_device)) {
                name.setText(this.mContext.getResources().getString(R.string.device_productname_hc06));
                deviceimags.setImageResource(R.drawable.drawable_device_ear_temperature);
            } else if ("BC01".equalsIgnoreCase(_device)) {
                name.setText(this.mContext.getResources().getString(R.string.device_productname_bc01));
                deviceimags.setImageResource(R.drawable.drawable_device_bc01);
            } else if ("CMS50D".equalsIgnoreCase(_device)) {
                name.setText(this.mContext.getResources().getString(R.string.device_productname_cmd50d));
                deviceimags.setImageResource(R.drawable.drawable_device_cms50d);
            } else if ("CMS50IW".equals(_device)) {
                name.setText(this.mContext.getResources().getString(R.string.device_productname_50IW));
                deviceimags.setImageResource(R.drawable.cms50iw);
            } else if ("cmssxt".equalsIgnoreCase(_device)) {
                name.setText(this.mContext.getResources().getString(R.string.device_productname_SXT));
                deviceimags.setImageResource(R.drawable.drawable_data_device_cmxxst);
            } else if ("ABPM50W".equalsIgnoreCase(_device)) {
                name.setText(this.mContext.getResources().getString(R.string.device_productname_M50W));
                deviceimags.setImageResource(R.drawable.drawable_data_device_abpm50);
            } else if (DeviceNameUtils.PM50.equalsIgnoreCase(_device)) {
                name.setText(this.mContext.getResources().getString(R.string.device_productname_pm50));
                deviceimags.setImageResource(R.drawable.drawable_data_device_pm50);
            } else if (DeviceNameUtils.TEMP03.equalsIgnoreCase(_device)) {
                name.setText(this.mContext.getResources().getString(R.string.device_productname_temp03));
                deviceimags.setImageResource(R.drawable.drawable_data_device_temp03);
            } else if ("CONTEC08AW".equalsIgnoreCase(_device)) {
                name.setText(this.mContext.getResources().getString(R.string.device_productname_08AW));
                deviceimags.setImageResource(R.drawable.drawable_data_device_a08);
            } else if ("CONTEC08C".equalsIgnoreCase(_device)) {
                name.setText(this.mContext.getResources().getString(R.string.device_productname_08AW));
                deviceimags.setImageResource(R.drawable.drawable_device_c08);
            } else if ("CMS50F".equalsIgnoreCase(_device)) {
                name.setText(this.mContext.getResources().getString(R.string.device_name_50IW));
                deviceimags.setImageResource(R.drawable.drawable_device_cms50f);
            } else if (Constants.CMS50EW.equalsIgnoreCase(_device) || "CMS50DW".equalsIgnoreCase(_device)) {
                name.setText(this.mContext.getResources().getString(R.string.device_productname_50EW));
                deviceimags.setImageResource(R.drawable.drawable_data_device_cms50ew);
            } else if ("sp10w".equalsIgnoreCase(_device)) {
                name.setText(this.mContext.getResources().getString(R.string.device_productname_SP10W));
                deviceimags.setImageResource(R.drawable.drawable_data_device_sp10w);
            } else if ("cmsvesd".equalsIgnoreCase(_device)) {
                name.setText(this.mContext.getResources().getString(R.string.device_productname_VESD));
                deviceimags.setImageResource(R.drawable.drawable_device_cmsvesd);
            } else if ("wt".equalsIgnoreCase(_device)) {
                name.setText(this.mContext.getResources().getString(R.string.device_productname_WT));
                deviceimags.setImageResource(R.drawable.drawable_data_device_wt);
            } else if ("FHR01".equalsIgnoreCase(_device)) {
                name.setText(this.mContext.getResources().getString(R.string.device_productname_Fhr01));
                deviceimags.setImageResource(R.drawable.drawable_data_device_fhr01);
            } else if (Constants.PM85_NAME.equalsIgnoreCase(_device)) {
                name.setText(this.mContext.getResources().getString(R.string.device_productname_Pm85));
                deviceimags.setImageResource(R.drawable.drawable_data_device_pm85);
            } else if (Constants.CMS50K_NAME.equals(_device)) {
                name.setText(this.mContext.getResources().getString(R.string.device_productname_cms50k));
                deviceimags.setImageResource(R.drawable.cms50k);
            } else if (Constants.CMS50K1_NAME.equals(_device)) {
                name.setText(this.mContext.getResources().getString(R.string.device_productname_cms50k));
                deviceimags.setImageResource(R.drawable.cms50k1);
            }
        }

        private class Holder {
            ImageView mIconiamgebtn;
            ImageButton mdeliamgebtn;
            TextView mnametv;
            ImageButton mrefreshiamgebtn;

            private Holder() {
            }

            /* synthetic */ Holder(ManagerDevicesAdapter managerDevicesAdapter, Holder holder) {
                this();
            }
        }
    }
}
