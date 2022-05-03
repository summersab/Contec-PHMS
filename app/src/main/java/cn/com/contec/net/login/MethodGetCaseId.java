package cn.com.contec.net.login;

import android.util.Base64;
import cn.com.contec.net.util.Constants_jar;
import cn.com.contec.net.util.MD5_encoding;
import u.aly.bs;

public class MethodGetCaseId {
    public static String[] getCaseID(String pSessionID, String pHospitalname, String pHospitalID, String pUserName, String pDataTypeName, String pDataType, String pUserID, String pPSW, String pCheckTime, String pSex, String pIsautomatic, String pOtherparams, String pLocalCasePath, String pDoctorID, String pDeviceID) throws Exception {
        String _MessageHeader = "101006" + pSessionID + "11";
        String _psw_user = String.valueOf(pUserID) + pPSW;
        if (pDoctorID == null) {
            pDoctorID = bs.b;
        }
        if (pDeviceID == null) {
            pDeviceID = bs.b;
        }
        StringBuffer _md5Stb = new StringBuffer();
        _md5Stb.append(pLocalCasePath).append(pHospitalID).append(pDoctorID).append(pDeviceID);
        String _MessageForm = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><request><name>" + pUserName + "</name>" + "<thirdid>" + pUserID + "</thirdid>" + "<datatype>" + pDataType + "</datatype>" + "<datatypename>" + pDataTypeName + "</datatypename>" + "<notes>" + "</notes>" + "<hospitalid>" + pHospitalID + "</hospitalid>" + "<hname>" + pHospitalname + "</hname>" + "<doctorid>" + "</doctorid>" + "<dname>" + "</dname>" + "<sendername>" + "</sendername>" + "<checktime>" + pCheckTime + "</checktime>" + "<otherparams>" + pOtherparams + "</otherparams>" + "<deviceid>" + pDeviceID + "</deviceid>" + "<devicename>" + "</devicename>" + "<md5>" + MD5_encoding.MD5(_md5Stb.toString()) + "</md5>" + "<caseorigin>" + "</caseorigin>" + "<sex>" + pSex + "</sex>" + "<applicationno>" + "</applicationno>" + "<isautomatic>" + pIsautomatic + "</isautomatic>" + "</request>";
        System.out.println(_MessageForm);
        String _MD5psw_user = MD5_encoding.MD5(_psw_user);
        String _base64Message = Base64.encodeToString(_MessageForm.getBytes(), 0);
        return new String[]{Constants_jar.MSG_STRING, String.valueOf(String.valueOf(MD5_encoding.MD5(String.valueOf(_MD5psw_user) + _MessageHeader + _base64Message)) + _MessageHeader) + _base64Message};
    }

    public static String[] newGetCaseID(String mSessionID, String mUserName, String mUserId, String mUserPsw, String mDataType, String mDataTypeName, String mNotes, String mHospitalId, String mHospitalName, String mDoctorId, String mDoctorName, String mCheckTime, String mOtherParams, String mDeviceId, String mDeviceName, String mMd5, String mCaseOrigin, String mYzlb, String mSex, String mApplicationno, String mIsautomatic) throws Exception {
        String _MessageHeader = "101006" + mSessionID + "11";
        String _MessageForm = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><request><name>" + mUserName + "</name>" + "<thirdid>" + mUserId + "</thirdid>" + "<datatype>" + mDataType + "</datatype>" + "<datatypename>" + mDataTypeName + "</datatypename>" + "<notes>" + mNotes + "</notes>" + "<hospitalid>" + mHospitalId + "</hospitalid>" + "<hname>" + mHospitalName + "</hname>" + "<doctorid>" + mDeviceId + "</doctorid>" + "<dname>" + mDoctorName + "</dname>" + "<checktime>" + mCheckTime + "</checktime>" + "<otherparams>" + mOtherParams + "</otherparams>" + "<deviceid>" + "</deviceid>" + "<devicename>" + "</devicename>" + "<md5>" + "</md5>" + "<caseorigin>" + "</caseorigin>" + "yzlb" + "/yzlb" + "<sex>" + mSex + "</sex>" + "<applicationno>" + "</applicationno>" + "<isautomatic>" + mIsautomatic + "</isautomatic>" + "</request>";
        System.out.println(_MessageForm);
        String _MD5psw_user = MD5_encoding.MD5(String.valueOf(mUserId) + mUserPsw);
        String _base64Message = Base64.encodeToString(_MessageForm.getBytes(), 0);
        return new String[]{Constants_jar.MSG_STRING, String.valueOf(String.valueOf(MD5_encoding.MD5(String.valueOf(_MD5psw_user) + _MessageHeader + _base64Message)) + _MessageHeader) + _base64Message};
    }
}
