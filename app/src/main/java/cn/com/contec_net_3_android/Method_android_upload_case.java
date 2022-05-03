package cn.com.contec_net_3_android;

import android.util.Log;
import java.util.ArrayList;
import cn.com.contec_net_3_android_case.Case;
import cn.com.contec_net_3_android_case.Case_MD5;
import cn.com.contec_net_3_android_case.MethodUploadFile;
import cn.com.contec_net_3_android_case.UploadCase;

public class Method_android_upload_case {
    public String mCaseId;
    public String mCasePath;
    public String mFilePath;
    public String mPsw;
    public String mSessionID;
    public String mURL;
    public UploadCase mUploadCase;
    public String mUserID;

    public Method_android_upload_case(String pCasePath, String pFilePath, String pSessionID, String pCaseId, String pUserID, String pPsw, String pURL) {
        this.mFilePath = pFilePath;
        this.mSessionID = pSessionID;
        this.mCaseId = pCaseId;
        this.mUserID = pUserID;
        this.mPsw = pPsw;
        this.mCasePath = pCasePath;
        this.mURL = pURL;
    }

    public String init() {
        String _order;
        String _order2;
        this.mUploadCase = new UploadCase(this.mFilePath, this.mSessionID, this.mCaseId, this.mUserID, this.mPsw);
        this.mUploadCase.init();
        int pToatalTimes = this.mUploadCase.mTimes;
        int pCurrentTimes = this.mUploadCase.mCurrentTimes;
        byte[] pBytes = this.mUploadCase.mData;
        if (pCurrentTimes != pToatalTimes - 1) {
            byte[] _bytes = new byte[((pCurrentTimes + 1) * 524288)];
            System.out.println("----------------->" + ((pCurrentTimes + 1) * 524288));
            System.arraycopy(pBytes, 0, _bytes, 0, (pCurrentTimes + 1) * 524288);
            ArrayList<Case> _List = Case_MD5.getMD5(_bytes);
            if (pToatalTimes <= 1 || pCurrentTimes != 0) {
                _order2 = "2";
            } else {
                _order2 = "1";
            }
            String _result = MethodUploadFile.uploadCase(this.mUploadCase.mSessionID, this.mUploadCase.mCaseId, this.mUploadCase.mData.length, _List, this.mCasePath, this.mUploadCase.mUserID, this.mPsw, _order2, this.mUploadCase.mCasePartPath[pCurrentTimes], this.mURL);
            Log.e("UploadCase", "mCaseId:" + this.mCaseId + " mCasePath:" + this.mCasePath + " mUserID:" + this.mUserID + "  mPsw:" + this.mPsw + this.mUploadCase.mCasePartPath[pCurrentTimes]);
            this.mUploadCase.mCurrentTimes++;
            return _result;
        }
        if (pToatalTimes == 1) {
            _order = "0";
        } else {
            _order = "3";
        }
        Log.e("UploadCase", "over" + this.mUploadCase.mCasePartPath[pCurrentTimes]);
        byte[] _bytes2 = new byte[(pBytes.length - (524288 * pCurrentTimes))];
        System.arraycopy(pBytes, 0, _bytes2, 0, pBytes.length - (524288 * pCurrentTimes));
        String _result2 = MethodUploadFile.uploadCase(this.mUploadCase.mSessionID, this.mUploadCase.mCaseId, this.mUploadCase.mData.length, Case_MD5.getMD5(_bytes2), this.mCasePath, this.mUploadCase.mUserID, this.mPsw, _order, this.mUploadCase.mCasePartPath[pCurrentTimes], this.mURL);
        Log.e("UploadCase", "over  ������Ϣ��" + _result2);
        this.mUploadCase.mCurrentTimes++;
        return _result2;
    }
}
