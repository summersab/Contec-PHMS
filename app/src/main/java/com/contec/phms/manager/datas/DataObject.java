package com.contec.phms.manager.datas;

import android.os.Looper;

import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.util.AlertDialogUtil;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.FileOperation;
import com.contec.phms.util.JPageUtil;
import com.contec.phms.util.PageUtil;

import org.apache.commons.httpclient.cookie.CookieSpec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataObject {
    private static String TAG = "DataObject";

    public static String makeObjectFile_failed(Object p_obj, String p_path) {
        Exception e;
        ObjectOutputStream _oos = null;
        IOException e2;
        DeviceData _data = (DeviceData) p_obj;
        String _path = String.valueOf(p_path) + CookieSpec.PATH_DELIM + _data.getFileName();
        String _pathP = _data.getFileName();
        File _file = new File(_path);
        if (!_file.exists()) {
            int end = _path.lastIndexOf(CookieSpec.PATH_DELIM);
            String DirPath = _path.substring(0, end + 1);
            FileOperation.makeDirs(DirPath);
            try {
                File _file2 = new File(_path);
                try {
                    _file2.createNewFile();
                } catch (IOException e3) {
                    e2 = e3;
                    e2.printStackTrace();
                    CLog.e(TAG, "PageUtil.getSDFreeSize_BYTE():" + PageUtil.getSDFreeSize_BYTE());
                    ObjectOutputStream _oos2 = null;
                    _oos = new ObjectOutputStream(new FileOutputStream(_path));
                    _oos.writeObject(p_obj);
                    _oos.close();
                    return _pathP;
                }
            } catch (IOException e4) {
                e2 = e4;
            }
        }
        ObjectOutputStream _oos22 = null;
        try {
            _oos = new ObjectOutputStream(new FileOutputStream(_path));
        } catch (Exception e5) {
            e = e5;
        }
        try {
            _oos.writeObject(p_obj);
            _oos.close();
        } catch (Exception e6) {
            e = e6;
            _oos22 = _oos;
            if (_oos22 != null) {
            }
            if (p_obj != null) {
            }
            CLog.e(TAG, "---PageUtil.getSDFreeSize_BYTE()---:" + PageUtil.getSDFreeSize_BYTE());
            e.printStackTrace();
            AlertDialogUtil.alertDialog(App_phms.getInstance().getlastActivity(), R.string.sd_card_no_free_size, 17039370, null);
            CLog.e(TAG, "---PageUtil.getSDFreeSize_BYTE():" + PageUtil.getSDFreeSize_BYTE());
            return _pathP;
        }
        return _pathP;
    }

    public static String makeObjectFile_add(Object p_obj, String p_path) {
        Exception e;
        ObjectOutputStream _oos;
        IOException e2;
        DeviceData _data = (DeviceData) p_obj;
        String _path = String.valueOf(p_path) + CookieSpec.PATH_DELIM + _data.getFileName();
        String _pathP = _data.getFileName();
        File _file = new File(_path);
        if (!_file.exists()) {
            int end = _path.lastIndexOf(CookieSpec.PATH_DELIM);
            String DirPath = _path.substring(0, end + 1);
            FileOperation.makeDirs(DirPath);
            try {
                File _file2 = new File(_path);
                try {
                    _file2.createNewFile();
                } catch (IOException e3) {
                    e2 = e3;
                    e2.printStackTrace();
                    CLog.e(TAG, "PageUtil.getSDFreeSize_BYTE():" + PageUtil.getSDFreeSize_BYTE());
                    ObjectOutputStream _oos2 = null;
                    _oos = new ObjectOutputStream(new FileOutputStream(_path));
                    try {
                        _oos.writeObject(p_obj);
                        _oos.close();
                    } catch (Exception e4) {
                        e = e4;
                        _oos2 = _oos;
                        if (_oos2 != null) {
                        }
                        if (p_obj != null) {
                        }
                        e.printStackTrace();
                        if (Looper.myLooper() == null) {
                            Looper.prepare();
                        }
                        AlertDialogUtil.alertDialog(App_phms.getInstance().getlastActivity(), R.string.sd_card_no_free_size, 17039370, null);
                        Looper.loop();
                        CLog.e(TAG, "---PageUtil.getSDFreeSize_BYTE():" + PageUtil.getSDFreeSize_BYTE());
                        return _pathP;
                    }
                    return _pathP;
                }
            } catch (IOException e5) {
                e2 = e5;
            }
        }
        ObjectOutputStream _oos22 = null;
        try {
            _oos = new ObjectOutputStream(new FileOutputStream(_path));
            _oos.writeObject(p_obj);
            _oos.close();
        } catch (Exception e6) {
            e = e6;
        }
        return _pathP;
    }

    public static Object readObject(File file) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            Object _obj = ois.readObject();
            ois.close();
            return _obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object[] getObjects(DataList list) {
        File[] _files = new File(Constants.DataPath).listFiles();
        for (File name : _files) {
            list.mList.add(name.getName());
        }
        return null;
    }

    public static synchronized boolean remove(String path) {
        boolean _result;
        synchronized (DataObject.class) {
            _result = false;
            File _file = new File(path);
            CLog.i(TAG, "Deleted file path**********: " + path);
            if (_file.isFile() && _file.delete()) {
                CLog.i(TAG, "Deleted file path: " + path);
                _result = true;
            }
        }
        return _result;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0066 A[Catch: all -> 0x00b5, TRY_LEAVE, TryCatch #2 {, blocks: (B:4:0x0003, B:6:0x004d, B:7:0x0050, B:8:0x0057, B:10:0x005b, B:12:0x0066, B:13:0x0076, B:14:0x007b, B:17:0x0081, B:18:0x008b, B:23:0x0099, B:27:0x00b9, B:33:0x00dd), top: B:44:0x0003 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static synchronized String makeObjectFile_addCase(Object p_obj, String p_path) {
        String _pathP;
        File _file;
        Exception e;
        ObjectOutputStream _oos = null;
        IOException e2;
        File _file2;
        IOException e3;
        File _file1;
        synchronized (DataObject.class) {
            DeviceData _data = (DeviceData) p_obj;
            String _path = String.valueOf(String.valueOf(p_path) + CookieSpec.PATH_DELIM + _data.mDeviceType) + CookieSpec.PATH_DELIM + _data.getFileName();
            _pathP = _data.getFileName();
            File _file12 = new File(p_path);
            if (!_file12.exists()) {
                FileOperation.caseMakeDirs(p_path);
                _file1 = new File(p_path);
                try {
                    _file1.createNewFile();
                } catch (IOException e5) {
                    e3 = e5;
                    e3.printStackTrace();
                    CLog.e(TAG, "jPageUtil.getSDFreeSize_BYTE():" + JPageUtil.getSDFreeSize_BYTE());
                    _file = new File(_path);
                    if (!_file.exists()) {
                    }
                    ObjectOutputStream _oos2 = null;
                    try {
                        _oos = new ObjectOutputStream(new FileOutputStream(_path));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    try {
                        _oos.writeObject(p_obj);
                        _oos.close();
                    } catch (Exception e6) {
                        e = e6;
                        _oos2 = _oos;
                        if (_oos2 != null) {
                        }
                        if (p_obj != null) {
                        }
                        e.printStackTrace();
                        CLog.e(TAG, "---jPageUtil.getSDFreeSize_BYTE():" + JPageUtil.getSDFreeSize_BYTE());
                        return _pathP;
                    }
                    return _pathP;
                }
            }
            _file = new File(_path);
            if (!_file.exists()) {
                int end = _path.lastIndexOf(CookieSpec.PATH_DELIM);
                String DirPath = _path.substring(0, end + 1);
                FileOperation.caseMakeDirs(DirPath);
                _file2 = new File(_path);
                try {
                    _file2.createNewFile();
                } catch (IOException e8) {
                    e2 = e8;
                    e2.printStackTrace();
                    CLog.e(TAG, "jPageUtil.getSDFreeSize_BYTE():" + JPageUtil.getSDFreeSize_BYTE());
                    ObjectOutputStream _oos22 = null;
                    try {
                        _oos = new ObjectOutputStream(new FileOutputStream(_path));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    try {
                        _oos.writeObject(p_obj);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    try {
                        _oos.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    return _pathP;
                }
            }
            ObjectOutputStream _oos222 = null;
            try {
                _oos = new ObjectOutputStream(new FileOutputStream(_path));
                _oos.writeObject(p_obj);
                _oos.close();
            } catch (Exception e9) {
                e = e9;
            }
        }
        return _pathP;
    }
}
