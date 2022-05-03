package com.contec.phms.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.contec.circleimage.widget.CircularImage;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.manager.device.DeviceBean;
import com.contec.phms.manager.device.DeviceBeanList;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.db.DeviceListDaoOperation;
import com.contec.phms.db.DeviceListItemBeanDao;
import com.contec.phms.db.localdata.PluseDataDao;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.DeviceNameUtils;
import com.contec.phms.util.FileOperation;
import com.contec.phms.util.PageUtil;
import com.zxing.android.CaptureActivity;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.httpclient.cookie.CookieSpec;
import u.aly.bs;

public class FragmentSerialNumber extends FragmentBase {
    private String TAG = "FragmentSerialNumber";
    private CircularImage _devicelist_change_user;
    private boolean ifscanTag = false;
    private EditText mDeviceTag;
    private ImageView mImageView;
    private String mMac;
    private String mName;
    private Button mOK;
    private int mPosition;
    private Button mScanBarCode;
    private View mView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.layout_data_serchdevice_barcode, container, false);
        return this.mView;
    }

    public void onResume() {
        super.onResume();
        CLog.e(this.TAG, "onResume***********************" + this.ifscanTag);
        this.mMac = FragmentDataSerchDevice.mMac;
        this.mName = FragmentDataSerchDevice.mDeviceName;
        this.mPosition = FragmentDataSerchDevice.mPosition;
        init_view(this.mView);
        if (this.ifscanTag) {
            this.ifscanTag = false;
        } else {
            this.mDeviceTag.setText(bs.b);
        }
    }

    private void init_view(View pview) {
        this.mScanBarCode = (Button) pview.findViewById(R.id.hand_barcode_btn);
        this.mDeviceTag = (EditText) pview.findViewById(R.id.baredittext);
        this.mOK = (Button) pview.findViewById(R.id.inout_entrue);
        this.mImageView = (ImageView) pview.findViewById(R.id.device_image);
        init_name_image(this.mName);
        this.mScanBarCode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                FragmentSerialNumber.this.ifscanTag = true;
                FragmentSerialNumber.this.startActivityForResult(new Intent(FragmentSerialNumber.this.getActivity(), CaptureActivity.class), 0);
            }
        });
        this.mOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentSerialNumber.this.ifscanTag = false;
                Message msg = new Message();
                msg.what = Constants.CLOSE_SERIALNUMBER_FRAGMENT;
                msg.arg2 = 1;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
                FragmentSerialNumber.this.saveDevice(FragmentSerialNumber.this.getActivity(), FragmentSerialNumber.this.mName, FragmentSerialNumber.this.mMac, FragmentSerialNumber.this.mDeviceTag.getText().toString(), FragmentSerialNumber.this.mPosition);
                FragmentActivity activity = FragmentSerialNumber.this.getActivity();
                FragmentSerialNumber.this.getActivity();
                ((InputMethodManager) activity.getSystemService("input_method")).hideSoftInputFromWindow(FragmentSerialNumber.this.mView.getWindowToken(), 0);
            }
        });
        this._devicelist_change_user = (CircularImage) pview.findViewById(R.id.devicelist_change_user);
        getSdcardPhoto(App_phms.getInstance().GetUserInfoNAME(), "imagehead.jpg");
        TextView mUserNameTextView = (TextView) this.mView.findViewById(R.id.user_name_text);
        String _userName = PageUtil.getLoginUserInfo().mUserName;
        if (_userName == null || _userName.equals(bs.b)) {
            _userName = PageUtil.getLoginUserInfo().mUID.substring(PageUtil.getLoginUserInfo().mUID.length() - 4);
        }
        mUserNameTextView.setText(_userName);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        if (resultCode == -1) {
            this.mDeviceTag.setText(data.getExtras().getString(PluseDataDao.RESULT));
            return;
        }
        this.mDeviceTag.setText(bs.b);
    }

    private void init_name_image(String pName) {
        if ("CMS50IW".equals(pName)) {
            this.mImageView.setImageResource(R.drawable.drawable_data_device_cms50iw);
        } else if ("cmssxt".equalsIgnoreCase(pName)) {
            this.mImageView.setImageResource(R.drawable.drawable_data_device_cmxxst);
        } else if ("ABPM50W".equalsIgnoreCase(pName)) {
            this.mImageView.setImageResource(R.drawable.drawable_data_device_abpm50);
        } else if (DeviceNameUtils.PM50.equalsIgnoreCase(pName)) {
            this.mImageView.setImageResource(R.drawable.drawable_data_device_pm50);
        } else if (DeviceNameUtils.TEMP03.equalsIgnoreCase(pName)) {
            this.mImageView.setImageResource(R.drawable.drawable_data_device_temp03);
        } else if ("CONTEC08AW".equalsIgnoreCase(pName)) {
            this.mImageView.setImageResource(R.drawable.drawable_data_device_a08);
        } else if ("CONTEC08C".equalsIgnoreCase(pName)) {
            this.mImageView.setImageResource(R.drawable.drawable_device_c08);
        } else if ("CMS50F".equalsIgnoreCase(pName)) {
            this.mImageView.setImageResource(R.drawable.drawable_device_cms50f);
        } else if (Constants.CMS50EW.equalsIgnoreCase(pName) || "CMS50DW".equalsIgnoreCase(pName)) {
            this.mImageView.setImageResource(R.drawable.drawable_data_device_cms50ew);
        } else if ("sp10w".equalsIgnoreCase(pName)) {
            this.mImageView.setImageResource(R.drawable.drawable_data_device_sp10w);
        } else if ("cmsvesd".equalsIgnoreCase(pName)) {
            this.mImageView.setImageResource(R.drawable.drawable_device_cmsvesd);
        } else if ("wt".equalsIgnoreCase(pName)) {
            this.mImageView.setImageResource(R.drawable.drawable_data_device_wt);
        } else if ("FHR01".equalsIgnoreCase(pName)) {
            this.mImageView.setImageResource(R.drawable.drawable_data_device_fhr01);
        } else if (Constants.PM85_NAME.equalsIgnoreCase(pName)) {
            this.mImageView.setImageResource(R.drawable.drawable_data_device_pm85);
        }
    }

    private void saveDevice(Context pcontext, String pName, String pMac, String pCode, int pPosition) {
        try {
            List<DeviceListItemBeanDao> _beanList = App_phms.getInstance().mHelper.getDeviceListItemDao().queryBuilder().where().eq(DeviceListItemBeanDao.DeviceName, pName).and().eq("UserName", App_phms.getInstance().mUserInfo.mUserID).and().eq(DeviceListItemBeanDao.DeviceCode, pCode).query();
            if (_beanList == null || _beanList.size() <= 0) {
                DeviceListItemBeanDao _beanDao = new DeviceListItemBeanDao();
                _beanDao.mDeviceName = pName;
                _beanDao.mDeviceCode = pCode;
                _beanDao.mUserName = App_phms.getInstance().mUserInfo.mUserID;
                _beanDao.mDeviceMac = pMac;
                _beanDao.mUseNum = 0;
                _beanDao.isNew = true;
                if (_beanDao.isNew) {
                    _beanDao.isNew = false;
                    DeviceListDaoOperation.getInstance().insertDevice(_beanDao);
                    boolean _ifadd = true;
                    for (int i = 0; i < DeviceManager.mDeviceList.getListDevice().size(); i++) {
                        if (DeviceManager.mDeviceList.getDevice(i).mDeviceName.equals(_beanDao.mDeviceName)) {
                            DeviceManager.mDeviceList.getDevice(i).mBeanList.add(new DeviceBean(_beanDao.mDeviceName, _beanDao.mDeviceMac, _beanDao.mDeviceCode, _beanDao.mId, bs.b));
                            _ifadd = false;
                        }
                    }
                    if (_ifadd) {
                        DeviceBeanList _DeviceBeanList = new DeviceBeanList();
                        _DeviceBeanList.mDeviceName = _beanDao.mDeviceName;
                        _DeviceBeanList.mState = 27;
                        DeviceBean _devicebean = new DeviceBean(_beanDao.mDeviceName, _beanDao.mDeviceMac, _beanDao.mDeviceCode, _beanDao.mId, bs.b);
                        _DeviceBeanList.mBeanList.add(_devicebean);
                        DeviceManager.mDeviceList.getListDevice().add(_DeviceBeanList);
                        App_phms.getInstance().showBeans.add(_devicebean);
                    }
                    Constants.ISFROMSERACHDEVICE = true;
                    Message msg = new Message();
                    msg.what = 1;
                    msg.arg1 = pPosition;
                    FragmentDataSerchDevice.mHandler.sendMessage(msg);
                    return;
                }
                return;
            }
            FragmentDataSerchDevice.mHandler.sendEmptyMessage(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
