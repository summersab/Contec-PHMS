package com.contec.phms.upload.trend;

import android.util.Base64;

import com.contec.cms50dj_jar.DeviceData50DJ_Jar;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.db.localdata.Spo2DataDao;
import com.contec.phms.db.localdata.opration.Spo2DataDaoOperation;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.CLog;
import com.contec.phms.util.FileOperation;
import com.contec.phms.util.PageUtil;
import org.apache.commons.httpclient.HttpMethodBase;

public class Cms50DJ_Trend extends Trend {
    private final String TAG = "SpO208Trend";
    public DeviceData mData;

    public Cms50DJ_Trend(DeviceData datas) {
        this.mData = datas;
        getContent();
    }

    public HttpMethodBase getMethod() {
        return this.mMethod;
    }

    @Override // com.contec.phms.upload.trend.Trend
    public String makeContect() {
        byte[] pack;
        if (this.mData == null || this.mData.mDataList.get(0) == null) {
            pack = new byte[]{0};
            CLog.e("*****************************", "**pack**pack***pack**pack*********");
        } else {
            DeviceData50DJ_Jar _djData = (DeviceData50DJ_Jar) this.mData.mDataList.get(0);
            byte _size = (byte) _djData.getmSp02DataList().size();
            int _length = (_size * 8) + 3;
            pack = new byte[_length];
            pack[0] = 1;
            pack[1] = _size;
            pack[2] = 8;
            int i = 3;
            for (int j = 0; j < _size; j++) {
                if (DeviceManager.mDeviceBeanList != null) {
                    DeviceManager.m_DeviceBean.mProgress = (j / _size) * 100;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    CLog.i("SpO208Trend", "SpO2上传进度百分比：" + ((j / _size) * 100));
                }
                byte[] mSpoData = _djData.getmSp02DataList().get(j);
                String _strDataDate = PageUtil.getDateFormByte(mSpoData[0], mSpoData[1], mSpoData[2], mSpoData[3], mSpoData[4], mSpoData[5]);
                FileOperation.writeToSDCard("上传*******数据时间：" + _strDataDate, "Cms50D_BT");
                CLog.dT("SpO208Trend", "判断时间是否合法：" + _strDataDate);
                byte[] provalue = PageUtil.compareDate(_strDataDate);
                if (provalue != null) {
                    mSpoData[0] = provalue[0];
                    mSpoData[1] = provalue[1];
                    mSpoData[2] = provalue[2];
                    mSpoData[3] = provalue[3];
                    mSpoData[4] = provalue[4];
                    mSpoData[5] = provalue[5];
                    FileOperation.writeToSDCard("上传*****时间不合法骄正之后的数据时间：" + PageUtil.getDateFormByte(mSpoData[0], mSpoData[1], mSpoData[2], mSpoData[3], mSpoData[4], mSpoData[5]), "Cms50D_BT");
                }
                String data = "year:" + ((int) mSpoData[0]) + " month:" + ((int) mSpoData[1]) + "  day:" + ((int) mSpoData[2]) + " hour:" + ((int) mSpoData[3]) + "  min:" + ((int) mSpoData[4]) + "  second:" + ((int) mSpoData[5]) + "  spo:" + ((int) mSpoData[6]) + "  pluse:" + ((int) mSpoData[7]);
                CLog.e("SpO208Trend", "血氧数据：" + data);
                byte[] _temp = _djData.getmSp02DataList().get(j);
                int i2 = i + 1;
                pack[i] = _temp[0];
                int i3 = i2 + 1;
                pack[i2] = _temp[1];
                int i4 = i3 + 1;
                pack[i3] = _temp[2];
                int i5 = i4 + 1;
                pack[i4] = _temp[3];
                int i6 = i5 + 1;
                pack[i5] = _temp[4];
                int i7 = i6 + 1;
                pack[i6] = _temp[5];
                int i8 = i7 + 1;
                pack[i7] = _temp[6];
                i = i8 + 1;
                pack[i8] = _temp[7];
            }
            if (_size != 0) {
                doPack(pack);
            }
        }
        FileOperation.writeToSDCard("************************>>>>>>>>>>>>>>>>>>>>>***********************************", "Cms50D_BT");
        return encodeBASE64(pack);
    }

    public String encodeBASE64(byte[] pack) {
        return Base64.encodeToString(pack, 0);
    }

    public void doPack(byte[] pack) {
        int count = pack.length / 8;
        for (int i = 0; i < count; i++) {
            int index = (i * 8) + 3;
            String dateTime = PageUtil.process_Date(pack[index] + 2000, pack[index + 1], pack[index + 2], pack[index + 3], pack[index + 4], pack[index + 5]);
            byte spo2 = pack[index + 6];
            byte pr = pack[index + 7];
            Spo2DataDaoOperation operation = Spo2DataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext());
            if (!Boolean.valueOf(operation.querySql(dateTime)).booleanValue()) {
                Spo2DataDao data = new Spo2DataDao();
                data.mTime = dateTime;
                data.mSpo2 = new StringBuilder(String.valueOf(spo2)).toString();
                data.mPr = new StringBuilder(String.valueOf(pr)).toString();
                data.mUnique = this.mData.mFileName;
                data.mFlag = "0";
                operation.insertSpo2DataDao(data);
            }
            CLog.e("SpO208TrendgotU", "The uploaded blood oxygen data is: " + spo2 + "_" + pr + " time: " + dateTime);
        }
    }
}
