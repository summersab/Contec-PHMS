package com.contec.phms.manager.device;

import com.contec.phms.device.template.DeviceData;
import com.contec.phms.manager.datas.DataObject;
import com.contec.phms.util.Constants;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.apache.commons.httpclient.cookie.CookieSpec;

public class DeviceListItem {
    List<DeviceBeanList> mList = new Stack();

    public void addDevice(DeviceBeanList mBean) {
        this.mList.add(mBean);
    }

    public DeviceBeanList popDevice(int p_index) {
        return this.mList.get(p_index);
    }

    public DeviceBeanList getDevice(int location) {
        return this.mList.get(location);
    }

    public List<DeviceBeanList> getListDevice() {
        return this.mList;
    }

    public int size() {
        return this.mList.size();
    }

    public void removeAll() {
        this.mList.removeAll(this.mList);
    }

    public void setObject(int position, DeviceBeanList pObject) {
        this.mList.set(position, pObject);
    }

    public boolean empty() {
        return this.mList.isEmpty();
    }

    public ArrayList<DeviceData> getDeviceData(int index) {
        DeviceData _deviceData;
        int _sizeList = this.mList.size();
        ArrayList<DeviceData> mDeviceDatas = new ArrayList<>();
        for (int n = 0; n < _sizeList; n++) {
            ArrayList<String> _path = this.mList.get(n).mBeanList.get(index).mDataPath;
            if (_path != null) {
                int _size = _path.size();
                for (int i = 0; i < _size; i++) {
                    File _file = new File(String.valueOf(Constants.DataPath) + CookieSpec.PATH_DELIM + _path.get(i));
                    if (_file.exists() && (_deviceData = (DeviceData) DataObject.readObject(_file)) != null) {
                        mDeviceDatas.add(_deviceData);
                    }
                }
            }
        }
        return mDeviceDatas;
    }
}
