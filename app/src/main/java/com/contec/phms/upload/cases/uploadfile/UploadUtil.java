package com.contec.phms.upload.cases.uploadfile;

import com.contec.phms.App_phms;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.CLog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import u.aly.bs;

public class UploadUtil {
    static final String TAG = "UploadUtil";
    final String UPLOAD_URI = "http://DATA1.contec365.com/http/caseTransmission.php";

    public String uploadFile(String sid, String filePath) {
        try {
            FileInputStream fis = new FileInputStream(new File(filePath));
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            Map<String, String> map = new HashMap<>();
            map.put("PHPSESSID", sid);
            return PostFile.post("http://DATA1.contec365.com/http/caseTransmission.php", map, new FormFile[]{new FormFile(filePath, data, "filename", FilePart.DEFAULT_CONTENT_TYPE)});
        } catch (Exception e) {
            e.printStackTrace();
            return bs.b;
        }
    }

    public String uploadCaseFile(File caseFile, String phpsession, String casePath) {
        try {
            FileInputStream fis = new FileInputStream(caseFile);
            int size = fis.available();
            byte[] data = new byte[size];
            fis.read(data);
            fis.close();
            Map<String, String> map = new HashMap<>();
            map.put("PHPSESSID", phpsession);
            CLog.i(TAG, PostFile.post("http://DATA1.contec365.com/http/caseTransmission.php", map, new FormFile[]{new FormFile(casePath, data, "filename", FilePart.DEFAULT_CONTENT_TYPE)}));
            return new StringBuilder(String.valueOf(size)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String uploadDataXML(String filePath, String phpsession) throws Exception {
        DeviceManager.m_DeviceBean.mProgress = 95;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        String _fileName = filePath.substring(filePath.lastIndexOf(CookieSpec.PATH_DELIM) + 1);
        FileReader _fr = new FileReader(new File(filePath));
        char[] _buf = new char[1024];
        int _len = _fr.read(_buf);
        _fr.close();
        String _content = new String(_buf, 0, _len);
        Map<String, String> dataParamMap = new HashMap<>();
        dataParamMap.put("PHPSESSID", phpsession);
        dataParamMap.put("name", _fileName);
        dataParamMap.put("content", _content);
        return PostFile.post("http://DATA1.contec365.com/http/caseTransmission.php", dataParamMap, (FormFile[]) null);
    }
}
