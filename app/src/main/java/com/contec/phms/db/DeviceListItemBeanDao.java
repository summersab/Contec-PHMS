package com.contec.phms.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "DEVICE_LIST_ITEM_BEAN")
public class DeviceListItemBeanDao {
    public static final String BluetoothType = "BluetoothType";
    public static final String BroadcastPacketFiled = "BroadcastPacketFiled";
    public static final String DB_TABLE = "DEVICE_LIST_ITEM_BEAN";
    public static final String DataTime = "DataTime";
    public static final String DeviceCode = "DeviceCode";
    public static final String DeviceMAC = "DeviceMAC";
    public static final String DeviceName = "DeviceName";
    public static final String IsNew = "IsNew";
    public static final String ReceiveData = "ReceiveData";
    public static final String UseNum = "UseNum";
    public static final String UserName = "UserName";
    @DatabaseField(columnName = "IsNew")
    public boolean isNew;
    @DatabaseField(columnName = "BluetoothType")
    public String mBluetoothType;
    @DatabaseField(columnName = "BroadcastPacketFiled")
    public String mBroadcastPacketFiled;
    @DatabaseField(columnName = "DataTime")
    public String mDataTime;
    @DatabaseField(columnName = "DeviceCode")
    public String mDeviceCode;
    @DatabaseField(columnName = "DeviceMAC")
    public String mDeviceMac;
    @DatabaseField(columnName = "DeviceName")
    public String mDeviceName;
    @DatabaseField(generatedId = true)
    public int mId;
    @DatabaseField(columnName = "ReceiveData")
    public String mReceiveDataStr;
    @DatabaseField(columnName = "UseNum")
    public int mUseNum;
    @DatabaseField(columnName = "UserName")
    public String mUserName;

    public String toString() {
        return "mid:" + this.mId + " devicename:" + this.mDeviceName + "  deviceCode:" + this.mDeviceCode + "  mdeviceMac:" + this.mDeviceMac + "  UseNum: " + this.mUseNum + "  isNew:" + this.isNew + "  mUserName:" + this.mUserName;
    }
}
