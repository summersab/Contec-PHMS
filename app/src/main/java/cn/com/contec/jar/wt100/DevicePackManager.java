package cn.com.contec.jar.wt100;

import android.os.Environment;
import android.util.Log;
import com.contec.phms.db.localdata.PluseDataDao;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import u.aly.bs;

public class DevicePackManager {
    private static final String TAG = "cn.com.contec";
    protected static int mCount = 0;
    protected static int mPackLen;
    public static byte[] mSynchronizationTime = new byte[6];
    private HashMap<String, String> _resultmap = new HashMap<>();
    int mIsInput = 0;
    protected byte[] mPack = new byte[1024];
    private ArrayList<String> mReceiveContainer = new ArrayList<>();
    public ArrayList<WTDeviceDataJar> m_DeviceDatas = new ArrayList<>();

    public HashMap<String, String> arrangeMessage(byte[] buf, int length) {
        this._resultmap.clear();
        int i = 0;
        while (i < length) {
            byte[] bArr = this.mPack;
            int i2 = mCount;
            mCount = i2 + 1;
            bArr[i2] = buf[i];
            if (mCount == 3) {
                mPackLen = buf[i] & 255;
                Log.i(TAG, "�������ݳ��ȣ�" + mPackLen);
            }
            if (mCount <= 2 || mCount <= mPackLen + 2) {
                i++;
            } else {
                Log.i(TAG, "������ܹ������������ݣ�" + mCount + "   " + mPackLen);
                processPack(this.mPack, mCount);
                mCount = 0;
                mPackLen = 0;
                return this._resultmap;
            }
        }
        this._resultmap.put(PluseDataDao.RESULT, "8");
        return this._resultmap;
    }

    public void writeToSDCard(String str) {
        String str2 = "\n" + str;
        if ("mounted".equals(Environment.getExternalStorageState())) {
            File directory = new File(Environment.getExternalStorageDirectory(), "CONTEC/WT/");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(Environment.getExternalStorageDirectory(), "CONTEC/WT/specfical.txt");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileOutputStream outStream = new FileOutputStream(file, true);
                outStream.write(str2.getBytes());
                outStream.close();
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
    }

    public void writeObject(ArrayList<String> Arraystr) {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            File directory = new File(Environment.getExternalStorageDirectory(), "CONTEC/WT/");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(Environment.getExternalStorageDirectory(), "CONTEC/WT/correlation_data.txt");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileOutputStream outStream = new FileOutputStream(file);
                new ObjectOutputStream(outStream).writeObject(Arraystr);
                outStream.close();
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
    }

    public ArrayList<String> readObject() {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            File file = new File(Environment.getExternalStorageDirectory(), "CONTEC/WT/correlation_data.txt");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                new ArrayList();
                return (ArrayList) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e2) {
            }
        }
        new ArrayList();
        return this.mReceiveContainer;
    }

    void processPack(byte[] pack, int p_length) {
        printPack(pack, p_length);
        if (pack == null || pack.length < 5) {
            Log.i(TAG, "Invalid Package!");
        }
        switch (pack[5]) {
            case 0:
                this.mIsInput = 7;
                this._resultmap.put(PluseDataDao.RESULT, new StringBuilder(String.valueOf(this.mIsInput)).toString());
                return;
            case 5:
                Log.i(TAG, "pack [5] is 0x05");
                if (checkDeviceTime(pack)) {
                    Log.i(TAG, "���ؼƵı�־ = " + pack[11] + "   " + pack[12]);
                    if (pack[11] == 1 || pack[12] == 1) {
                        this._resultmap.put("version", "1");
                    } else {
                        this._resultmap.put("version", "0");
                    }
                    this.mIsInput = 3;
                    this._resultmap.put(PluseDataDao.RESULT, new StringBuilder(String.valueOf(this.mIsInput)).toString());
                    return;
                }
                this.mIsInput = 4;
                this._resultmap.put(PluseDataDao.RESULT, new StringBuilder(String.valueOf(this.mIsInput)).toString());
                return;
            case 7:
                this.mReceiveContainer.clear();
                writeObject(this.mReceiveContainer);
                this.mIsInput = 5;
                this._resultmap.put(PluseDataDao.RESULT, new StringBuilder(String.valueOf(this.mIsInput)).toString());
                return;
            case 8:
                Log.i(TAG, "pack [5] is 0x08");
                if (pack[6] == 0) {
                    Log.i(TAG, "pack [6] is 0  no data");
                    this.mIsInput = 6;
                    this._resultmap.put(PluseDataDao.RESULT, new StringBuilder(String.valueOf(this.mIsInput)).toString());
                    return;
                }
                processData(pack);
                return;
            case 11:
                Log.i(TAG, "pack [5] is 0x08");
                if (pack[6] == 0) {
                    Log.i(TAG, "pack [6] is 0  no data");
                    this.mIsInput = 6;
                    this._resultmap.put(PluseDataDao.RESULT, new StringBuilder(String.valueOf(this.mIsInput)).toString());
                    return;
                }
                processDataHaveSec(pack);
                return;
            default:
                return;
        }
    }

    private boolean checkDeviceTime(byte[] pack) {
        int _count = 0;
        for (int i = 6; i < 11; i++) {
            if (mSynchronizationTime[_count] != pack[i]) {
                return false;
            }
            _count++;
        }
        return true;
    }

    void processData(byte[] pack) {
        byte _count = pack[8];
        for (int i = 0; i < _count; i++) {
            int _start = (i * 8) + 9;
            WTDeviceDataJar _data = new WTDeviceDataJar(getDataPack(pack, _start, 9));
            writeToSDCard("��  " + i + "  �� �ӵ�����������  " + _data.getobj());
            boolean _isHas = false;
            this.mReceiveContainer = readObject();
            for (int j = 0; j < this.mReceiveContainer.size(); j++) {
                String _str = this.mReceiveContainer.get(j);
                writeToSDCard("     ������   " + j + "��������" + _str);
                if (_data.getobj() != null && _data.getobj().equals(_str)) {
                    _isHas = true;
                    writeToSDCard("****************** �������������ظ���*********");
                }
            }
            if (this.mReceiveContainer.size() == 0) {
                writeToSDCard("     ��������û������");
            }
            if (!_isHas) {
                this.m_DeviceDatas.add(_data);
                Log.i("jarwt", "  _data = " + _data.getUserMeasureTime() + "   _start:" + _start);
            }
        }
        if (pack[7] == pack[6]) {
            this.mReceiveContainer = readObject();
            for (int i2 = 0; i2 < this.m_DeviceDatas.size(); i2++) {
                this.mReceiveContainer.add(this.m_DeviceDatas.get(i2).getobj());
            }
            writeObject(this.mReceiveContainer);
            Log.i(TAG, "+++++++++++++++++data process over++++++++++++++++++");
            this.mIsInput = 2;
            this._resultmap.put(PluseDataDao.RESULT, new StringBuilder(String.valueOf(this.mIsInput)).toString());
        }
    }

    void processDataHaveSec(byte[] pack) {
        byte _count = pack[8];
        for (int i = 0; i < _count; i++) {
            int _start = (i * 9) + 9;
            WTDeviceDataJar _data = new WTDeviceDataJar(getDataPack(pack, _start, 9));
            writeToSDCard(String.valueOf(getcureentbytetime()) + "   �ӵ�����������  " + _data.getobj());
            boolean _isHas = false;
            this.mReceiveContainer = readObject();
            for (int j = 0; j < this.mReceiveContainer.size(); j++) {
                String _str = this.mReceiveContainer.get(j);
                writeToSDCard("     ������   " + j + "��������" + _str);
                if (_data.getobj() != null && _data.getobj().equals(_str)) {
                    _isHas = true;
                    writeToSDCard("****************** ���������ظ���********* ��������: " + _str);
                }
            }
            if (this.mReceiveContainer.size() == 0) {
                writeToSDCard("     ��������û������");
            }
            if (!_isHas) {
                this.m_DeviceDatas.add(_data);
                Log.i("jarwt", "  _data = " + _data.getUserMeasureTime() + "   _start:" + _start);
            }
        }
        if (pack[7] == pack[6]) {
            this.mReceiveContainer = readObject();
            for (int i2 = 0; i2 < this.m_DeviceDatas.size(); i2++) {
                this.mReceiveContainer.add(this.m_DeviceDatas.get(i2).getobj());
            }
            writeObject(this.mReceiveContainer);
            Log.i(TAG, "+++++++++++++++++data process over++++++++++++++++++");
            this.mIsInput = 2;
            this._resultmap.put(PluseDataDao.RESULT, new StringBuilder(String.valueOf(this.mIsInput)).toString());
        }
    }

    private String getcureentbytetime() {
        return new SimpleDateFormat("yyyy��MM��dd��   HH:mm:ss:SSS ").format(new Date(System.currentTimeMillis()));
    }

    static byte[] getDataPack(byte[] pack, int start, int length) {
        byte[] _data = new byte[length];
        for (int i = 0; i < _data.length; i++) {
            _data[i] = pack[start + i];
        }
        return _data;
    }

    static void printPack(byte[] pack, int p_length) {
        if (pack == null) {
            Log.i(TAG, "param pack is null");
            return;
        }
        String packStr = bs.b;
        for (int i = 0; i < p_length; i++) {
            packStr = String.valueOf(String.valueOf(packStr) + Integer.toHexString(pack[i])) + " ";
        }
        Log.i(TAG, "this is printpack :" + packStr);
    }

    static void printPackBySec(byte[] pack, int p_length) {
        if (pack == null) {
            Log.i("lztext", "param pack is null");
            return;
        }
        String packStr = bs.b;
        for (int i = 0; i < p_length; i++) {
            packStr = String.valueOf(String.valueOf(packStr) + Integer.toHexString(pack[i])) + " ";
        }
        Log.i("lztext", String.valueOf(((double) (((pack[7] & 255) << 8) | (pack[8] & 255))) / 100.0d) + "  this is printpack :" + packStr);
    }
}
