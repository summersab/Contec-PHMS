package cn.com.contec_net_3_android_case;

import java.util.ArrayList;
import cn.com.contec.net.util.MD5_encoding;

public class Case_MD5 {
    public static ArrayList<Case> getMD5(byte[] pData) {
        ArrayList<Case> mArrayList = new ArrayList<>();
        int _size = pData.length;
        int _length = _size / 3;
        if (_length < 1024) {
            for (int i = 0; i < 3; i++) {
                Case _Case = new Case();
                if (i != 2) {
                    byte[] _data = new byte[_length];
                    System.arraycopy(pData, i * _length, _data, 0, _length);
                    String _md5 = MD5_encoding.MD5(_data);
                    _Case.start = i * _length;
                    _Case.end = ((i + 1) * _length) - 1;
                    _Case.MD5 = _md5;
                    mArrayList.add(_Case);
                    System.out.println("<><><>::" + (i * _length) + "----->::" + (((i + 1) * _length) - 1));
                } else {
                    byte[] _data2 = new byte[(_size - (_length * 2))];
                    System.arraycopy(pData, i * _length, _data2, 0, _size - (_length * 2));
                    String _md52 = MD5_encoding.MD5(_data2);
                    _Case.start = i * _length;
                    _Case.end = _size - 1;
                    _Case.MD5 = _md52;
                    mArrayList.add(_Case);
                    System.out.println("<><><>::" + (i * _length) + "----->::" + (_size - 1));
                }
            }
        } else {
            for (int i2 = 0; i2 < 3; i2++) {
                Case _Case2 = new Case();
                byte[] _data3 = new byte[1024];
                System.arraycopy(pData, i2 * _length, _data3, 0, 1024);
                String _md53 = MD5_encoding.MD5(_data3);
                _Case2.start = i2 * _length;
                _Case2.end = (i2 * _length) + 1023;
                _Case2.MD5 = _md53;
                mArrayList.add(_Case2);
                System.out.println(">>>>>>::" + (i2 * _length) + "     >::" + ((i2 * _length) + 1023));
            }
        }
        return mArrayList;
    }
}
