package com.contec.phms.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import u.aly.bs;

@DatabaseTable(tableName = "loginuserinfo")
public class LoginUserDao {
    public static final String Address = "address";
    public static final String AnotherLoginInfo = "AnotherLoginInfo";
    public static final String Are = "are";
    public static final String AreID = "areid";
    public static final String Birthday = "birthday";
    public static final String CardType = "cardtype";
    public static final String CreateDate = "createdate";
    public static final String DiskSpace = "diskSpace";
    public static final String EndDate = "enddate";
    public static final String EthrID = "EthrID";
    public static final String HGroupID = "HGroupID";
    public static final String HGroupName = "HGroupName";
    public static final String HospitalID = "HospitalID";
    public static final String HospitalName = "HospitalName";
    public static final String PID = "PID";
    public static final String Phone = "phone";
    public static final String SID = "SID";
    public static final String SenderId = "senderid";
    public static final String ServerID = "serverid";
    public static final String StartDate = "startdate";
    public static final String State = "State";
    public static final String Total = "total";
    public static final String TransType = "TransType";
    public static final String UID = "UID";
    public static final String Used = "used";
    public static final String UserName = "username";
    public static final String UserSex = "usersex";
    public static final String amactivity = "amactivity";
    public static final String datetime = "datetime";
    public static final String height = "height";
    public static final String pmactivity = "pmactivity";
    public static final String psw = "passwd";
    public static final String sportdays = "sportdays";
    public static final String sporttarget = "sporttarget";
    public static final String weight = "weight";
    @DatabaseField(columnName = "address", id = false)
    public String mAddress;
    public String mAge = bs.b;
    @DatabaseField(columnName = "amactivity", id = false)
    public String mAmactivity;
    @DatabaseField(columnName = "AnotherLoginInfo", id = false)
    public String mAnotherLoginInfo;
    @DatabaseField(columnName = "are", id = false)
    public String mAre;
    @DatabaseField(columnName = "areid", id = false)
    public String mAreID;
    public String mBHReview = bs.b;
    @DatabaseField(columnName = "birthday", id = false)
    public String mBirthday;
    @DatabaseField(columnName = "cardtype", id = false)
    public String mCardType;
    @DatabaseField(columnName = "createdate", id = false)
    public String mCreateDate;
    @DatabaseField(columnName = "datetime", id = false)
    public String mDateTime = bs.b;
    @DatabaseField(columnName = "diskSpace", id = false)
    public String mDiskSpace;
    @DatabaseField(columnName = "enddate", id = false)
    public String mEndDate;
    @DatabaseField(columnName = "EthrID", id = false)
    public String mEthrID;
    @DatabaseField(columnName = "HGroupID", id = false)
    public String mHGroupID;
    @DatabaseField(columnName = "HGroupName", id = false)
    public String mHGroupName;
    @DatabaseField(columnName = "height", id = false)
    public String mHeight = bs.b;
    @DatabaseField(columnName = "HospitalID", id = false)
    public String mHospitalID;
    @DatabaseField(columnName = "HospitalName", id = false)
    public String mHospitalName;
    @DatabaseField(columnName = "serverid", id = false)
    public String mID;
    public String mIsActivation = bs.b;
    @DatabaseField(columnName = "PID", id = false)
    public String mPID;
    public String mPersonID = bs.b;
    @DatabaseField(columnName = "phone", id = false)
    public String mPhone;
    @DatabaseField(columnName = "pmactivity", id = false)
    public String mPmactivity;
    @DatabaseField(columnName = "passwd", id = false)
    public String mPsw;
    @DatabaseField(columnName = "SID", id = false)
    public String mSID;
    @DatabaseField(columnName = "senderid", id = false)
    public String mSenderId;
    @DatabaseField(columnName = "usersex", id = false)
    public String mSex = bs.b;
    @DatabaseField(columnName = "sporttarget", id = false)
    public String mSportTargetCal;
    @DatabaseField(columnName = "sportdays", id = false)
    public String mSportdays;
    @DatabaseField(columnName = "startdate", id = false)
    public String mStartDate;
    @DatabaseField(columnName = "State", id = false)
    public String mState;
    @DatabaseField(columnName = "total", id = false)
    public String mTotal;
    @DatabaseField(columnName = "TransType", id = false)
    public String mTransType;
    @DatabaseField(columnName = "UID", id = false)
    public String mUID;
    @DatabaseField(columnName = "used", id = false)
    public String mUsed;
    @DatabaseField(columnName = "username", id = false)
    public String mUserName;
    @DatabaseField(columnName = "weight", id = false)
    public String mWeight = bs.b;

    public String toString() {
        return "mID:" + this.mID + "  mUID:" + this.mUID + " mPID:" + this.mPID + " mUserName:" + this.mUserName + "  mSex:" + this.mSex + "  mPhone:" + this.mPhone + "  mBirthday:" + this.mBirthday + " mAddress:" + this.mAddress + "  mAre:" + this.mAre + " mAreID: " + this.mAreID + " mCreateDate:" + this.mCreateDate + " mSenderId:" + this.mSenderId + " mStartDate:" + this.mStartDate + " mEndDate:" + this.mEndDate + "  mCardType:" + this.mCardType + "  mUsed:" + this.mUsed + " mTotal:" + this.mTotal + " mState:" + this.mState + " mHospitalID:" + this.mHospitalID + " mHospitalName" + this.mHospitalName + " mTransType:" + this.mTransType + " mSID:" + this.mSID + "  mPsw:" + this.mPsw + "  mDateTime:" + this.mDateTime + " mHeight:" + this.mHeight + " mWeight" + this.mWeight + "  mSportTargetCal:" + this.mSportTargetCal + " mAmactivity:" + this.mAmactivity + "mPmactivity:" + this.mPmactivity + " mSportdays:" + this.mSportdays;
    }
}
