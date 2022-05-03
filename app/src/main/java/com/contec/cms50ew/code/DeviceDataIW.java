package com.contec.cms50ew.code;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class DeviceDataIW implements Serializable {
    public int _DataLen;
    public int _Day;
    public int _Hour;
    public int _Min;
    public int _Month;
    public int _Patch_Count = 0;
    public int _Pi;
    public int _Sec;
    public int _Year;
    public byte[] _value_ew;
    public String mUserName;
    public ArrayList<byte[]> valueList = new ArrayList<>();

    public String getDescription() {
        String _return = "Time:" + this._Year + " - " + this._Month + " - " + this._Day + "   " + this._Hour + " : " + this._Min + " : " + this._Sec;
        for (int i = 0; i < this.valueList.size(); i++) {
            _return = String.valueOf(_return) + ("\nSpO2:" + this.valueList.get(i)[0] + "\n" + "pluse:" + this.valueList.get(i)[1]);
        }
        return _return;
    }

    public Calendar get_start_Calendar() {
        Calendar _Calendar = Calendar.getInstance();
        _Calendar.set(this._Year, this._Month, this._Day, this._Hour, this._Min, this._Sec);
        return _Calendar;
    }

    public Calendar get_end_Calendar() {
        Calendar _Calendar = Calendar.getInstance();
        _Calendar.set(this._Year, this._Month, this._Day, this._Hour, this._Min, this._Sec);
        _Calendar.add(13, this.valueList.size() * 5 * 1000);
        return _Calendar;
    }
}
