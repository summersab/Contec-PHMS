package com.contec.phms.util;

import android.os.Environment;
import com.contec.phms.manager.device.ServiceBean;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.httpclient.cookie.CookieSpec;

public class FileOperation {
    public static void makeDirs(String path) {
        File _temp = new File(path);
        if (!_temp.exists()) {
            if (!path.endsWith(File.separator)) {
                _temp = _temp.getParentFile();
            }
            _temp.mkdirs();
        }
    }

    public static void caseMakeDirs(String path) {
        File _temp = new File(path);
        if (!_temp.exists()) {
            _temp.mkdirs();
        }
    }

    public static void createFile(String path) {
        File _temp = new File(path);
        if (!_temp.exists() || !_temp.isFile()) {
            makeDirs(path);
            try {
                _temp.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void initDirs() {
        makeDirs(Constants.ContecPath);
        makeDirs(String.valueOf(Constants.ContecPath) + "/temp/");
        makeDirs(String.valueOf(Constants.DataPath) + CookieSpec.PATH_DELIM);
    }

    public static void clearTempFiles() {
        deleteFiles(new File(ServiceBean.getInstance().mTempPath));
    }

    public static void deleteUploadFiledFile() {
        File _file = new File(Constants.UPLOAD_FIAIL_DADT);
        if (_file.exists()) {
            CLog.dT("OPERATION", "删除上传失败的文件");
            deleteFiles(_file);
            CLog.dT("OPERATION", "删除上传失败的文件");
        }
    }

    public static void deleteFiles(File file) {
        if (file.isDirectory()) {
            File[] _files = file.listFiles();
            for (File deleteFiles : _files) {
                deleteFiles(deleteFiles);
            }
            return;
        }
        file.delete();
    }

    public static String getSdcardUserHeadPath(String puserflag) {
        String pSdCardPath = Environment.getExternalStorageDirectory() + CookieSpec.PATH_DELIM;
        String _append = "contec/userinfo/" + puserflag + CookieSpec.PATH_DELIM;
        makeDirs(String.valueOf(pSdCardPath) + _append);
        return String.valueOf(pSdCardPath) + _append;
    }

    public static void writeToSDCard(String str, String deviceName) {
        String str2 = "\n" + str;
        if ("mounted".equals(Environment.getExternalStorageState())) {
            File file = new File(Environment.getExternalStorageDirectory(), String.valueOf(deviceName) + "log.txt");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileOutputStream outStream = new FileOutputStream(file, true);
                outStream.write(str2.getBytes());
                outStream.close();
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
    }
}
