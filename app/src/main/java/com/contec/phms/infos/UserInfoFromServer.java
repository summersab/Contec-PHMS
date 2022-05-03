package com.contec.phms.infos;

import com.alibaba.cchannel.CloudChannelConstants;
import com.conect.json.SqliteConst;
import com.contec.phms.App_phms;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.util.CLog;
import com.contec.phms.util.SaveHospitalUtils;
import java.io.ByteArrayInputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;

public class UserInfoFromServer {
    private String mPasswd;
    private String mUserId;

    public UserInfoFromServer(String respones, String pUserID, String pPsw) {
        this.mUserId = pUserID;
        this.mPasswd = pPsw;
        insertLoginInfoToDB(respones);
    }

    private void insertLoginInfoToDB(String pRespones) {
        LoginUserDao _lud = new LoginUserDao();
        DocumentBuilderFactory _factory = DocumentBuilderFactory.newInstance();
        try {
            Element _infoElement = _factory.newDocumentBuilder().parse(new ByteArrayInputStream(pRespones.getBytes())).getDocumentElement();
            _lud.mID = _infoElement.getElementsByTagName(SqliteConst.KEY_ID).item(0).getTextContent();
            _lud.mUID = this.mUserId;
            _lud.mPsw = this.mPasswd;
            _lud.mPID = _infoElement.getElementsByTagName("pid").item(0).getTextContent();
            _lud.mUserName = _infoElement.getElementsByTagName("name").item(0).getTextContent();
            _lud.mSex = _infoElement.getElementsByTagName("sex").item(0).getTextContent();
            _lud.mPhone = _infoElement.getElementsByTagName("tel").item(0).getTextContent();
            _lud.mBirthday = _infoElement.getElementsByTagName(LoginUserDao.Birthday).item(0).getTextContent();
            _lud.mAddress = _infoElement.getElementsByTagName(LoginUserDao.Address).item(0).getTextContent();
            _lud.mAre = _infoElement.getElementsByTagName("area").item(0).getTextContent();
            _lud.mAreID = _infoElement.getElementsByTagName("areaid").item(0).getTextContent();
            _lud.mCreateDate = _infoElement.getElementsByTagName(LoginUserDao.CreateDate).item(0).getTextContent();
            _lud.mSenderId = _infoElement.getElementsByTagName(LoginUserDao.SenderId).item(0).getTextContent();
            _lud.mStartDate = _infoElement.getElementsByTagName(LoginUserDao.StartDate).item(0).getTextContent();
            _lud.mEndDate = _infoElement.getElementsByTagName(LoginUserDao.EndDate).item(0).getTextContent();
            _lud.mCardType = _infoElement.getElementsByTagName(LoginUserDao.CardType).item(0).getTextContent();
            _lud.mDiskSpace = _infoElement.getElementsByTagName("diskspace").item(0).getTextContent();
            _lud.mUsed = _infoElement.getElementsByTagName(LoginUserDao.Used).item(0).getTextContent();
            _lud.mTotal = _infoElement.getElementsByTagName(LoginUserDao.Total).item(0).getTextContent();
            _lud.mState = _infoElement.getElementsByTagName("state").item(0).getTextContent();
            _lud.mHospitalID = _infoElement.getElementsByTagName("hospitalid").item(0).getTextContent();
            _lud.mHospitalName = _infoElement.getElementsByTagName(SaveHospitalUtils.spHospitalname).item(0).getTextContent();
            _lud.mEthrID = _infoElement.getElementsByTagName("ethrid").item(0).getTextContent();
            _lud.mTransType = _infoElement.getElementsByTagName("transtype").item(0).getTextContent();
            _lud.mHGroupID = _infoElement.getElementsByTagName("hgroupid").item(0).getTextContent();
            _lud.mHGroupName = _infoElement.getElementsByTagName("hgroupname").item(0).getTextContent();
            _lud.mSID = _infoElement.getElementsByTagName(CloudChannelConstants.SID).item(0).getTextContent();
            _lud.mAnotherLoginInfo = _infoElement.getElementsByTagName("anotherlogininfo").item(0).getTextContent();
            _lud.mDateTime = _infoElement.getElementsByTagName(LoginUserDao.datetime).item(0).getTextContent();
            _lud.mHeight = _infoElement.getElementsByTagName(LoginUserDao.height).item(0).getTextContent();
            _lud.mWeight = _infoElement.getElementsByTagName("weight").item(0).getTextContent();
            _lud.mSportTargetCal = _infoElement.getElementsByTagName(LoginUserDao.sporttarget).item(0).getTextContent();
            _lud.mAmactivity = _infoElement.getElementsByTagName(LoginUserDao.amactivity).item(0).getTextContent();
            _lud.mPmactivity = _infoElement.getElementsByTagName(LoginUserDao.pmactivity).item(0).getTextContent();
            _lud.mSportdays = _infoElement.getElementsByTagName(LoginUserDao.sportdays).item(0).getTextContent();
            App_phms.getInstance().mHelper.getLoginUserDao().deleteBuilder().delete();
            App_phms.getInstance().mHelper.getLoginUserDao().create(_lud);
            CLog.e("UserInfoFromServer", "size:" + App_phms.getInstance().mHelper.getLoginUserDao().queryForAll().size() + "  userinfo :" + _lud.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
