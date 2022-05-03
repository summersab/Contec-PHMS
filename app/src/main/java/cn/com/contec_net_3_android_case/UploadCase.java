package cn.com.contec_net_3_android_case;

import android.os.Environment;
import java.io.IOException;

public class UploadCase {
    public String mCaseId;
    public String[] mCasePartPath;
    public int mCurrentTimes = 0;
    public byte[] mData;
    public String mFilePath;
    public String mPsw;
    public String mSessionID;
    public int mTimes;
    public String mUserID;

    public UploadCase(String pFilePath, String pSessionID, String pCaseId, String pUserID, String pPsw) {
        this.mFilePath = pFilePath;
        this.mSessionID = pSessionID;
        this.mCaseId = pCaseId;
        this.mUserID = pUserID;
        this.mPsw = pPsw;
    }

    public void init() {
        this.mData = File_Action.readFromSD(this.mFilePath);
        this.mTimes = computeTimes(this.mData.length, 524288);
        this.mCasePartPath = getUploadeUnitPath(this.mData, 524288, this.mTimes);
    }

    public static int computeTimes(int size, int everyTimes) {
        if (size % everyTimes == 0) {
            return size / everyTimes;
        }
        return (size / everyTimes) + 1;
    }

    public static String[] getUploadeUnitPath(byte[] pData, int pLength, int pTimes) {
        String[] mStrings = new String[pTimes];
        String m_FilePath = String.valueOf(Environment.getExternalStorageDirectory().toString()) + "/upload_case";
        File_Action.MakeDirectory(m_FilePath);
        for (int i = 0; i < pTimes; i++) {
            String _path = String.valueOf(m_FilePath) + "case" + i + ".txt";
            System.out.println(_path);
            if (i == pTimes - 1) {
                byte[] _data = new byte[(pData.length - (i * pLength))];
                System.arraycopy(pData, i * pLength, _data, 0, pData.length - (i * pLength));
                try {
                    File_Action.memoryData(_path, _data, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mStrings[i] = _path;
            } else {
                byte[] _data2 = new byte[pLength];
                System.arraycopy(pData, i * pLength, _data2, 0, pLength);
                try {
                    File_Action.memoryData(_path, _data2, false);
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                mStrings[i] = _path;
            }
        }
        return mStrings;
    }
}
