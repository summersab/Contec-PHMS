package com.contec.phms.manager.datas;

import android.util.Log;
import com.conect.json.CLog;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.upload.trend.PM10Trend_XML;
import com.contec.phms.util.Constants;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class DataList implements Serializable {
    private static final long serialVersionUID = 1;
    public final String TAG = "DataList";
    public ArrayList<String> mList = new ArrayList<>();

    public boolean add(Object object) {
        DeviceData data = (DeviceData) object;
        if (data.mDataType.equals("ECG(CMS50K)") || data.mDataType.equals("ECG(PM10)") || data.mDataType.equals("ECG(CMS50K1)")) {
            CLog.e("pm10", new StringBuilder(String.valueOf(data.mDataType)).toString());
            new PM10Trend_XML(data);
        }
        String _fileName = DataObject.makeObjectFile_add(object, Constants.DataPath);
        DeviceManager.m_DeviceBean.addDataPath(_fileName);
        boolean _add = this.mList.add(_fileName);
        com.contec.phms.util.CLog.i("DataList", _fileName);
        return _add;
    }

    public boolean addFileName(String fileName) {
        return this.mList.add(fileName);
    }

    public boolean addAll(ArrayList<byte[]> list) {
        //return this.mList.addAll(list.mList);
        return true;
    }

    public boolean remove(int index) {
        boolean _result = DataObject.remove(this.mList.get(index));
        return this.mList.remove(index) != null;
    }

    public void rmFileName(int index) {
        this.mList.remove(index);
    }

    public void clear() {
        this.mList.clear();
    }

    public int size() {
        return this.mList.size();
    }

    public boolean isEmpty() {
        return this.mList.isEmpty();
    }

    public DeviceData get(int index) {
        return (DeviceData) DataObject.readObject(new File(this.mList.get(index)));
    }

    public DeviceData get(String p_path) {
        return (DeviceData) DataObject.readObject(new File(p_path));
    }

    public boolean addCase(Object object) {
        Log.i("info", DataObject.makeObjectFile_addCase(object, Constants.casePath));
        return true;
    }
}
