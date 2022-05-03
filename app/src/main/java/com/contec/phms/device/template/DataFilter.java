package com.contec.phms.device.template;

import com.contec.phms.manager.datas.DataObject;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.apache.commons.httpclient.cookie.CookieSpec;

public class DataFilter {
    private static final String TAG = "DataFilter";
    static String[] mUploadedList;

    public void filterDatas() {
        getUploaded();
    }

    public static void moveDataToFailedDir() {
        String[] _files;
        CLog.i(TAG, "将Datas目录下的文件移动到uploadFailedDatas目录下");
        File _dir = new File(Constants.DataPath);
        if (_dir.isDirectory() && (_files = _dir.list()) != null && _files.length > 0) {
            for (int i = 0; i < _files.length; i++) {
                if (!checkUploaded(_files[i])) {
                    DeviceData _deviceData = (DeviceData) DataObject.readObject(new File(String.valueOf(Constants.DataPath) + CookieSpec.PATH_DELIM + _files[i]));
                    if (_deviceData != null) {
                        DataObject.makeObjectFile_failed(_deviceData, Constants.UPLOAD_FIAIL_DADT);
                    }
                    CLog.i(TAG, "文件没有上传过，移动到uploadFailedDatas目录下，并删除Datas目录下的文件" + Constants.DataPath + CookieSpec.PATH_DELIM + _files[i]);
                    DataObject.remove(String.valueOf(Constants.DataPath) + CookieSpec.PATH_DELIM + _files[i]);
                } else {
                    CLog.i(TAG, "文件上传过，删除Datas或uploadFailedDatas目录下的文件" + Constants.DataPath + CookieSpec.PATH_DELIM + _files[i]);
                    DataObject.remove(String.valueOf(Constants.DataPath) + CookieSpec.PATH_DELIM + _files[i]);
                    DataObject.remove(String.valueOf(Constants.UPLOAD_FIAIL_DADT) + CookieSpec.PATH_DELIM + _files[i]);
                }
            }
        }
    }

    public static void removeData() {
        File _dir = new File(Constants.DataPath);
        if (_dir.isDirectory()) {
            String[] _files = _dir.list();
            for (int i = 0; i < _files.length; i++) {
                DataObject.remove(String.valueOf(Constants.DataPath) + CookieSpec.PATH_DELIM + _files[i]);
            }
        }
    }

    static boolean checkUploaded(String data) {
        if (mUploadedList == null) {
            return false;
        }
        for (String equals : mUploadedList) {
            if (data.equals(equals)) {
                CLog.e(TAG, "Data:" + data + "  had uploaded!");
                return true;
            }
        }
        return false;
    }

    void getUploaded() {
        try {
            File _file = new File(Constants.UploadedDatas);
            StringBuffer _buf = new StringBuffer();
            if (_file.exists()) {
                BufferedReader _br = new BufferedReader(new FileReader(_file));
                while (true) {
                    String _temp = _br.readLine();
                    if (_temp == null) {
                        mUploadedList = new String(_buf).split(";");
                        return;
                    }
                    _buf.append(_temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
