package com.contec.phms.device.template;

import com.contec.phms.manager.device.ServiceBean;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public abstract class SaveSDCard {
    public String mDirPath = ServiceBean.getInstance().mDirPath;
    public String mFailedPath = (String.valueOf(this.mDirPath) + "/failed");
    public String mTempPath = (String.valueOf(this.mDirPath) + "/temp");
    public String mUploadedPath = (String.valueOf(this.mDirPath) + "/uploaded");

    public abstract void saveFailed();

    public abstract void saveFiles();

    public abstract void saveSDCard();

    public abstract void saveUploaded();

    public SaveSDCard() {
        initDir();
    }

    public void deleteTemp() {
        deleteFiles(this.mTempPath);
    }

    public void deleteHistory() {
    }

    public void deleteFiles(String path) {
        File _file = new File(path);
        if (_file.isDirectory()) {
            File[] _files = _file.listFiles();
            for (File path2 : _files) {
                deleteFiles(path2.getPath());
            }
        }
        _file.delete();
    }

    void initDir() {
        File _file = new File(this.mDirPath);
        if (!_file.exists()) {
            _file.mkdirs();
        }
    }

    public static void checkFile(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveDatas(String path, List<DeviceData> dataList) {
        File _file = new File(path);
        if (_file.exists()) {
            _file.delete();
        }
        try {
            _file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream _fos = new FileOutputStream(_file, false);
            _fos.write((byte) dataList.size());
            _fos.flush();
            for (int i = 0; i < dataList.size(); i++) {
                _fos.write(dataList.get(i).mSaveInfo);
                _fos.flush();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
