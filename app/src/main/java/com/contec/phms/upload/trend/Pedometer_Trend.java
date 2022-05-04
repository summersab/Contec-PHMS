package com.contec.phms.upload.trend;

import android.content.Context;
import android.os.Message;
import android.util.Base64;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.eventbus.EventFragment;
import com.contec.phms.db.HistoryDao;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.db.PedometerHistoryDao;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import java.util.List;
import cn.com.contec_net_3_android.Method_android_upload_trend;

public class Pedometer_Trend extends Trend {
    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private List<PedometerHistoryDao> mList;

    public Pedometer_Trend(Context pContext, List<PedometerHistoryDao> plist) {
        this.mContext = pContext;
        this.mList = plist;
        getContent();
        upload();
    }

    private boolean uploadTrend(String _content) {
        LoginUserDao _userinfo = PageUtil.getLoginUserInfo();
        try {
            String _code = Method_android_upload_trend.upLoadThrendTwo(_userinfo.mSID, _userinfo.mUID, App_phms.getInstance().mUserInfo.mPassword, _content, String.valueOf(Constants.URL) + "/main.php").substring(34, 40);
            if (_code.equals(Constants.SUCCESS)) {
                return true;
            }
            if (_code.equals(Constants.LOGIN_IN_ANOTHER_PLACE)) {
                Message msgs = new Message();
                msgs.what = Constants.Login_In_Another_Place;
                msgs.arg2 = 1;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs);
            } else if (!_code.equals(Constants.CARD_USE_NUM_EXPIRED)) {
                _code.equals(Constants.PASSED_VALIDITY);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean upload() {
        boolean _request = false;
        if (this.mList.size() > 0) {
            _request = uploadTrend(this.mContent);
            if (_request) {
                int i = 0;
                while (i < this.mList.size()) {
                    try {
                        PedometerHistoryDao _Dao = this.mList.get(i);
                        HistoryDao mHistoryDao = new HistoryDao();
                        mHistoryDao.setContent(this.mContext.getResources().getString(R.string.str_upload_pedometer_content));
                        mHistoryDao.setDate(_Dao.getmUploadDate());
                        mHistoryDao.setUser(App_phms.getInstance().mUserInfo.mUserID);
                        App_phms.getInstance().mHelper.getHistoryDao().create(mHistoryDao);
                        App_phms.getInstance().mHelper.getPedometerhistoryDao().delete(_Dao);
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                CLog.d(this.TAG, "计步器数据上传成功.........");
                EventFragment _fragment = new EventFragment();
                _fragment.setmWhichCommand(2);
                _fragment.setmProgress(100);
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(_fragment);
            } else {
                int i2 = 0;
                while (i2 < this.mList.size()) {
                    try {
                        PedometerHistoryDao _Dao2 = this.mList.get(i2);
                        _Dao2.setmUserID(App_phms.getInstance().mUserInfo.mUserID);
                        App_phms.getInstance().mHelper.getPedometerhistoryDao().update(_Dao2);
                        i2++;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                EventFragment _fragment2 = new EventFragment();
                _fragment2.setmWhichCommand(2);
                _fragment2.setmProgress(110);
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(_fragment2);
            }
        }
        return _request;
    }

    @Override // com.contec.phms.upload.trend.Trend
    public String makeContect() {
        byte[] pack;
        int i;
        if (this.mList != null) {
            byte _size = (byte) this.mList.size();
            int _length = (_size * 0) + 3 + 4;
            pack = new byte[_length];
            pack[0] = 8;
            pack[1] = _size;
            pack[2] = 0;
            int i2 = 3;
            int _per = 80 / _size;
            int sumSteps = 0;
            int cal = 0;
            int j = 0;
            while (true) {
                i = i2;
                if (j >= _size) {
                    break;
                }
                PedometerHistoryDao _pedomterData = this.mList.get(j);
                CLog.i("more", "makecontent  _pedomterData list = " + _pedomterData.toString());
                String _time = _pedomterData.getmStartTime();
                byte _year = (byte) Integer.parseInt(_time.substring(2, 4).toString());
                byte _month = (byte) Integer.parseInt(_time.substring(5, 7).toString());
                byte _day = (byte) Integer.parseInt(_time.substring(8, 10).toString());
                byte _hour = (byte) Integer.parseInt(_time.substring(11, 13).toString());
                byte _mm = (byte) Integer.parseInt(_time.substring(14, 16).toString());
                CLog.i("more", "makecontent  _pedomterData time = " + ((int) _year) + "  " + ((int) _month) + "  " + ((int) _day) + "  " + ((int) _hour) + "  " + ((int) _mm));
                int i3 = i + 1;
                pack[i] = _year;
                int i4 = i3 + 1;
                pack[i3] = _month;
                int i5 = i4 + 1;
                pack[i4] = _day;
                int i6 = i5 + 1;
                pack[i5] = _hour;
                int i7 = i6 + 1;
                pack[i6] = _mm;
                int _step = _pedomterData.getmStep();
                int _calories = _pedomterData.getmCalories();
                int i8 = i7 + 1;
                pack[i7] = (byte) ((_calories >> 8) & 255);
                int i9 = i8 + 1;
                pack[i8] = (byte) (_calories & 255);
                int i10 = i9 + 1;
                pack[i9] = (byte) ((_step >> 8) & 255);
                i2 = i10 + 1;
                pack[i10] = (byte) (_step & 255);
                EventFragment _fragment = new EventFragment();
                _fragment.setmWhichCommand(2);
                _fragment.setmProgress(((j + 1) * _per) + 10);
                try {
                    App_phms.getInstance().mEventBus.post(_fragment);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                if (j == this.mList.size() - 1) {
                    sumSteps = _pedomterData.getmSumStep();
                    cal = (int) _pedomterData.getmSumCalories();
                }
                j++;
            }
            int i11 = i + 1;
            pack[i] = (byte) ((cal >> 8) & 255);
            int i12 = i11 + 1;
            pack[i11] = (byte) (cal & 255);
            int i13 = i12 + 1;
            pack[i12] = (byte) ((sumSteps >> 8) & 255);
            int i14 = i13 + 1;
            pack[i13] = (byte) (sumSteps & 255);
        } else {
            pack = new byte[]{0};
        }
        return encodeBASE64(pack);
    }

    public String encodeBASE64(byte[] pack) {
        return Base64.encodeToString(pack, 0);
    }
}
