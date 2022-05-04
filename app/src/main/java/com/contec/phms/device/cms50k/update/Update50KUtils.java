package com.contec.phms.device.cms50k.update;

import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import com.alibaba.cchannel.push.receiver.CPushMessageCodec;
import com.contec.phms.App_phms;
import com.contec.phms.util.Constants;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;
import u.aly.bs;

/* loaded from: classes.dex */
public class Update50KUtils {
    public static final String MainURl = "http://data2.contec365.com";
    public static final String XmlURL = "http://data2.contec365.com/updatesoftware/androidsoftware/4020/current.xml";
    private static FileInputStream instream = null;
    public static final String updateFilename = "update.bin";
    public static final String updateXMlFilename = "updateFile.xml";
    public static final String TAG = Update50KUtils.class.getSimpleName();
    public static long mUpdateFileSize = 0;
    public static final String fileUrl = Environment.getExternalStorageDirectory() + CookieSpec.PATH_DELIM + "CYw/";
    public static boolean flagClose = false;
    public static RandomAccessFile randomFile = null;
    public static long islong = 0;

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static List<Xml50KUpdateBean> XMLParser(String xml) {
        IOException e;
        XmlPullParserException e2;
        int eventType = 0;
        ByteArrayInputStream tInputStringStream = null;
        Xml50KUpdateBean bean = null;
        List<Xml50KUpdateBean> beans = null;
        if (xml != null) {
            try {
                if (!xml.trim().equals(bs.b)) {
                    tInputStringStream = new ByteArrayInputStream(xml.getBytes());
                }
            } catch (Exception e3) {
                Log.i("info", new StringBuilder(String.valueOf(e3.getMessage())).toString());
                return null;
            }
        }
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(tInputStringStream, "UTF-8");
            eventType = parser.getEventType();
        } catch (XmlPullParserException e5) {
            e2 = e5;
        }
        while (true) {
            List<Xml50KUpdateBean> beans2 = beans;
            Xml50KUpdateBean bean2 = bean;
            if (eventType == 1) {
                try {
                    tInputStringStream.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                beans = beans2;
                return beans;
            }
            switch (eventType) {
                case 0:
                    beans = new ArrayList<>();
                    bean = bean2;
                    break;
                case 1:
                default:
                    beans = beans2;
                    bean = bean2;
                    break;
                case 2:
                    String name = parser.getName();
                    if (name.equalsIgnoreCase("file")) {
                        bean = new Xml50KUpdateBean();
                    } else {
                        bean = bean2;
                    }
                    try {
                        if (!name.equalsIgnoreCase("type")) {
                            if (!name.equalsIgnoreCase("fname")) {
                                if (!name.equalsIgnoreCase("md5")) {
                                    if (!name.equalsIgnoreCase("path")) {
                                        if (!name.equalsIgnoreCase("size")) {
                                            if (!name.equalsIgnoreCase("typecode")) {
                                                if (!name.equalsIgnoreCase("version")) {
                                                    if (!name.equalsIgnoreCase("uploaddate")) {
                                                        if (!name.equalsIgnoreCase("description")) {
                                                            beans = beans2;
                                                            break;
                                                        } else {
                                                            bean.description = parser.nextText();
                                                            beans = beans2;
                                                            break;
                                                        }
                                                    } else {
                                                        bean.uploaddate = parser.nextText();
                                                        beans = beans2;
                                                        break;
                                                    }
                                                } else {
                                                    bean.version = parser.nextText();
                                                    beans = beans2;
                                                    break;
                                                }
                                            } else {
                                                bean.typecode = parser.nextText();
                                                beans = beans2;
                                                break;
                                            }
                                        } else {
                                            bean.size = parser.nextText();
                                            beans = beans2;
                                            break;
                                        }
                                    } else {
                                        bean.path = parser.nextText();
                                        beans = beans2;
                                        break;
                                    }
                                } else {
                                    bean.md5 = parser.nextText();
                                    beans = beans2;
                                    break;
                                }
                            } else {
                                bean.fname = parser.nextText();
                                beans = beans2;
                                break;
                            }
                        } else {
                            bean.type = parser.nextText();
                            beans = beans2;
                            break;
                        }
                    } catch (IOException e8) {
                        e = e8;
                        beans = beans2;
                        e.printStackTrace();
                        return beans;
                    } catch (XmlPullParserException e9) {
                        e2 = e9;
                        beans = beans2;
                        e2.printStackTrace();
                        return beans;
                    }
                case 3:
                    if (parser.getName().equalsIgnoreCase("file")) {
                        beans2.add(bean2);
                        bean = null;
                        beans = beans2;
                        break;
                    }
                    beans = beans2;
                    bean = bean2;
                    break;
            }
            try {
                eventType = parser.next();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (XmlPullParserException xmlPullParserException) {
                xmlPullParserException.printStackTrace();
            }
        }
    }

    public static byte[] doPackIsUpdateCommand(byte[] pack) {
        int hiPre = ((pack[7] << 8) | (pack[6] & 255)) & 65535;
        String.valueOf(hiPre);
        int hiPre1 = (((pack[11] & 255) << 24) | ((pack[10] & 255) << 16) | ((pack[9] & 255) << 8) | (pack[8] & 255)) & (-1);
        mUpdateFileSize = hiPre1;
        Log.e("wsd1", "String  long >>" + Integer.toHexString(hiPre1));
        byte[] pack1 = {-76, 0, (byte) (hiPre & 127), (byte) ((hiPre >> 7) & 127), (byte) ((hiPre >> 14) & 127), (byte) (hiPre1 & 127), (byte) ((hiPre1 >> 7) & 127), (byte) ((hiPre1 >> 14) & 127), 0};
        return pack1;
    }

    public static byte[] doPackUpdateData(byte[] pack) {
        byte mm;
        byte mm2;
        byte mm3;
        byte mm4;
        byte mm5;
        byte mm6;
        byte[] pack1 = new byte[50];
        pack1[0] = -75;
        for (int i = 0; i < 7; i++) {
            byte mm7 = pack[i];
            if (mm7 < 0) {
                mm6 = (byte) ((((byte) (((byte) ((pack[i] << 0) & 128)) >> (7 - i))) ^ (-1)) + 1);
                int i2 = i + 7;
                pack1[i2] = (byte) (pack1[i2] | (mm7 & Byte.MAX_VALUE));
            } else {
                mm6 = (byte) (((byte) ((mm7 << 0) & 128)) >> (7 - i));
                int i3 = i + 7;
                pack1[i3] = (byte) (pack1[i3] | (mm7 >> 0));
            }
            pack1[1] = (byte) (pack1[1] | mm6);
        }
        for (int i4 = 0; i4 < 7; i4++) {
            byte mm8 = pack[i4 + 7];
            if (mm8 < 0) {
                mm5 = (byte) ((((byte) (((byte) ((mm8 << 0) & 128)) >> (7 - i4))) ^ (-1)) + 1);
                int i5 = i4 + 14;
                pack1[i5] = (byte) (pack1[i5] | (mm8 & Byte.MAX_VALUE));
            } else {
                mm5 = (byte) (((byte) ((mm8 << 0) & 128)) >> (7 - i4));
                int i6 = i4 + 14;
                pack1[i6] = (byte) (pack1[i6] | (mm8 >> 0));
            }
            pack1[2] = (byte) (pack1[2] | mm5);
        }
        for (int i7 = 0; i7 < 7; i7++) {
            byte mm9 = pack[i7 + 14];
            if (mm9 < 0) {
                mm4 = (byte) ((((byte) (((byte) ((mm9 << 0) & 128)) >> (7 - i7))) ^ (-1)) + 1);
                int i8 = i7 + 21;
                pack1[i8] = (byte) (pack1[i8] | (mm9 & Byte.MAX_VALUE));
            } else {
                mm4 = (byte) (((byte) ((mm9 << 0) & 128)) >> (7 - i7));
                int i9 = i7 + 21;
                pack1[i9] = (byte) (pack1[i9] | (mm9 >> 0));
            }
            pack1[3] = (byte) (pack1[3] | mm4);
        }
        for (int i10 = 0; i10 < 7; i10++) {
            byte mm10 = pack[i10 + 21];
            if (mm10 < 0) {
                mm3 = (byte) ((((byte) (((byte) ((mm10 << 0) & 128)) >> (7 - i10))) ^ (-1)) + 1);
                int i11 = i10 + 28;
                pack1[i11] = (byte) (pack1[i11] | (mm10 & Byte.MAX_VALUE));
            } else {
                mm3 = (byte) (((byte) ((mm10 << 0) & 128)) >> (7 - i10));
                int i12 = i10 + 28;
                pack1[i12] = (byte) (pack1[i12] | (mm10 >> 0));
            }
            pack1[4] = (byte) (pack1[4] | mm3);
        }
        for (int i13 = 0; i13 < 7; i13++) {
            byte mm11 = pack[i13 + 28];
            if (mm11 < 0) {
                mm2 = (byte) ((((byte) (((byte) ((mm11 << 0) & 128)) >> (7 - i13))) ^ (-1)) + 1);
                int i14 = i13 + 35;
                pack1[i14] = (byte) (pack1[i14] | (mm11 & Byte.MAX_VALUE));
            } else {
                mm2 = (byte) (((byte) ((mm11 << 0) & 128)) >> (7 - i13));
                int i15 = i13 + 35;
                pack1[i15] = (byte) (pack1[i15] | (mm11 >> 0));
            }
            pack1[5] = (byte) (pack1[5] | mm2);
        }
        for (int i16 = 0; i16 < 7; i16++) {
            byte mm12 = pack[i16 + 35];
            if (mm12 < 0) {
                mm = (byte) ((((byte) (((byte) ((mm12 << 0) & 128)) >> (7 - i16))) ^ (-1)) + 1);
                int i17 = i16 + 42;
                pack1[i17] = (byte) (pack1[i17] | (mm12 & Byte.MAX_VALUE));
            } else {
                mm = (byte) (((byte) ((mm12 << 0) & 128)) >> (7 - i16));
                int i18 = i16 + 42;
                pack1[i18] = (byte) (pack1[i18] | (mm12 >> 0));
            }
            pack1[6] = (byte) (pack1[6] | mm);
        }
        return pack1;
    }

    public static byte[] readSDFile(String fileName) {
        Throwable th;
        IOException e;
        FileNotFoundException e2;
        FileInputStream fis = null;
        byte[] buffer = null;
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        } else {
            FileInputStream fis2 = null;
            try {
                fis = new FileInputStream(file);
            } catch (Throwable th2) {
                th = th2;
            }
            try {
                int length = fis.available();
                buffer = new byte[length];
                fis.read(buffer);
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e6) {
                        e6.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e7) {
                e2 = e7;
                fis2 = fis;
                e2.printStackTrace();
                buffer = null;
                if (fis2 != null) {
                    try {
                        fis2.close();
                    } catch (IOException e8) {
                        e8.printStackTrace();
                    }
                }
                return buffer;
            } catch (IOException e9) {
                e = e9;
                fis2 = fis;
                e.printStackTrace();
                buffer = null;
                if (fis2 != null) {
                    try {
                        fis2.close();
                    } catch (IOException e10) {
                        e10.printStackTrace();
                    }
                }
                return buffer;
            } catch (Throwable th3) {
                th = th3;
                fis2 = fis;
                if (fis2 != null) {
                    try {
                        fis2.close();
                    } catch (IOException e11) {
                        e11.printStackTrace();
                    }
                }
                try {
                    throw th;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
        return buffer;
    }

    public static List<Xml50KUpdateBean> pullParseXmlFile() {
        IOException e;
        XmlPullParserException e2;
        List<Xml50KUpdateBean> xmlBeanLists = null;
        Xml50KUpdateBean bean = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parse = factory.newPullParser();
            File xmlFile = new File(fileUrl, updateXMlFilename);
            InputStream in = new FileInputStream(xmlFile);
            parse.setInput(in, CPushMessageCodec.UTF8);
            int eventType = parse.getEventType();
            while (true) {
                Xml50KUpdateBean bean2 = bean;
                List<Xml50KUpdateBean> xmlBeanLists2 = xmlBeanLists;
                if (1 == eventType) {
                    return xmlBeanLists2;
                }
                try {
                    String nodeName = parse.getName();
                    switch (eventType) {
                        case 0:
                            xmlBeanLists = new ArrayList<>();
                            bean = bean2;
                            break;
                        case 1:
                            bean = bean2;
                            xmlBeanLists = xmlBeanLists2;
                            break;
                        case 2:
                            if ("file".equals(nodeName)) {
                                bean = new Xml50KUpdateBean();
                            } else {
                                bean = bean2;
                            }
                            try {
                                if ("fname".equals(nodeName)) {
                                    bean.setFname(parse.nextText());
                                }
                                if ("md5".equals(nodeName)) {
                                    bean.setMd5(parse.nextText());
                                }
                                if ("path".equals(nodeName)) {
                                    bean.setPath(parse.nextText());
                                }
                                if ("size".equals(nodeName)) {
                                    bean.setSize(parse.nextText());
                                }
                                if ("version".equals(nodeName)) {
                                    bean.setVersion(parse.nextText());
                                }
                                if ("typecode".equals(nodeName)) {
                                    bean.setType(parse.nextText());
                                }
                                if ("description".equals(nodeName)) {
                                    bean.setDescription(parse.nextText());
                                }
                                if (!"uploaddate".equals(nodeName)) {
                                    xmlBeanLists = xmlBeanLists2;
                                    break;
                                } else {
                                    bean.setUploaddate(parse.nextText());
                                    xmlBeanLists = xmlBeanLists2;
                                    break;
                                }
                            } catch (IOException e3) {
                                e = e3;
                                xmlBeanLists = xmlBeanLists2;
                                e.printStackTrace();
                                return xmlBeanLists;
                            } catch (XmlPullParserException e4) {
                                e2 = e4;
                                xmlBeanLists = xmlBeanLists2;
                                e2.printStackTrace();
                                return xmlBeanLists;
                            }
                        case 3:
                            if ("file".equals(nodeName)) {
                                xmlBeanLists2.add(bean2);
                                bean = null;
                                xmlBeanLists = xmlBeanLists2;
                                break;
                            }
                        default:
                            bean = bean2;
                            xmlBeanLists = xmlBeanLists2;
                            break;
                    }
                    eventType = parse.next();
                } catch (IOException e5) {
                    e = e5;
                    xmlBeanLists = xmlBeanLists2;
                } catch (XmlPullParserException e6) {
                    e2 = e6;
                    xmlBeanLists = xmlBeanLists2;
                }
            }
        } catch (IOException e7) {
            e = e7;
        } catch (XmlPullParserException e8) {
            e2 = e8;
        }
        return xmlBeanLists;
    }

    public static void createUpdateXmlFile(List<Xml50KUpdateBean> xmlBeanLists) {
        Throwable th;
        IOException e;
        IllegalStateException e2;
        IllegalArgumentException e3;
        OutputStream os = null;
        if (Environment.getExternalStorageState().equals("mounted")) {
            XmlSerializer ser = Xml.newSerializer();
            String mSavePath = fileUrl;
            File file = new File(mSavePath);
            if (!file.exists()) {
                file.mkdir();
            }
            File xmlFile = new File(mSavePath, updateXMlFilename);
            OutputStream os2 = null;
            try {
                try {
                    os = new FileOutputStream(xmlFile);
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (IllegalArgumentException e6) {
                e3 = e6;
            } catch (IllegalStateException e7) {
                e2 = e7;
            }
            try {
                ser.setOutput(os, CPushMessageCodec.UTF8);
                ser.startDocument(CPushMessageCodec.UTF8, true);
                ser.startTag(null, "response");
                for (Xml50KUpdateBean s : xmlBeanLists) {
                    ser.startTag(null, "file");
                    ser.startTag(null, "fname");
                    ser.text(new StringBuilder(String.valueOf(s.getFname())).toString());
                    ser.endTag(null, "fname");
                    ser.startTag(null, "md5");
                    ser.text(new StringBuilder(String.valueOf(s.getMd5())).toString());
                    ser.endTag(null, "md5");
                    ser.startTag(null, "path");
                    ser.text(s.getPath());
                    ser.endTag(null, "path");
                    ser.startTag(null, "size");
                    ser.text(new StringBuilder(String.valueOf(s.getSize())).toString());
                    ser.endTag(null, "size");
                    ser.startTag(null, "version");
                    ser.text(new StringBuilder(String.valueOf(s.getVersion())).toString());
                    ser.endTag(null, "version");
                    ser.startTag(null, "typecode");
                    ser.text(new StringBuilder(String.valueOf(s.getTypecode())).toString());
                    ser.endTag(null, "typecode");
                    ser.startTag(null, "description");
                    ser.text(new StringBuilder(String.valueOf(s.getDescription())).toString());
                    ser.endTag(null, "description");
                    ser.startTag(null, "uploaddate");
                    ser.text(new StringBuilder(String.valueOf(s.getUploaddate())).toString());
                    ser.endTag(null, "uploaddate");
                    ser.endTag(null, "file");
                }
                ser.endTag(null, "response");
                ser.endDocument();
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e8) {
                        e8.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e9) {
                e = e9;
                os2 = os;
                e.printStackTrace();
                if (os2 != null) {
                    try {
                        os2.close();
                    } catch (IOException e10) {
                        e10.printStackTrace();
                    }
                }
            } catch (IOException e11) {
                e = e11;
                os2 = os;
                e.printStackTrace();
                if (os2 != null) {
                    try {
                        os2.close();
                    } catch (IOException e12) {
                        e12.printStackTrace();
                    }
                }
            } catch (IllegalArgumentException e13) {
                e3 = e13;
                os2 = os;
                e3.printStackTrace();
                if (os2 != null) {
                    try {
                        os2.close();
                    } catch (IOException e14) {
                        e14.printStackTrace();
                    }
                }
            } catch (IllegalStateException e15) {
                e2 = e15;
                os2 = os;
                e2.printStackTrace();
                if (os2 != null) {
                    try {
                        os2.close();
                    } catch (IOException e16) {
                        e16.printStackTrace();
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                os2 = os;
                if (os2 != null) {
                    try {
                        os2.close();
                    } catch (IOException e17) {
                        e17.printStackTrace();
                    }
                }
                try {
                    throw th;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }

    public static String getFileMD5(File file) {
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        byte[] buffer = new byte[1024];
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            FileInputStream in = new FileInputStream(file);
            while (true) {
                try {
                    int len = in.read(buffer, 0, 1024);
                    if (len == -1) {
                        in.close();
                        BigInteger bigInt = new BigInteger(1, digest.digest());
                        return bigInt.toString(16);
                    }
                    digest.update(buffer, 0, len);
                } catch (Exception e) {
                    e = e;
                    e.printStackTrace();
                    return null;
                }
            }
        } catch (Exception e2) {
            Exception e = e2;
        }
        return null;
    }

    public static byte[] readFileByRandomAccess(String fileName) {
        return null;
    }

    public static byte[] sum_Check(byte[] pack) {
        int CHECK_SUM = 0;
        int _size = pack.length - 1;
        for (int i = 0; i < _size; i++) {
            CHECK_SUM += pack[i] & 255;
        }
        byte _return = (byte) (CHECK_SUM & 127);
        pack[pack.length - 1] = _return;
        return pack;
    }

    public static List<Xml50KUpdateBean> getXmlBean(List<Xml50KUpdateBean> result) {
        return null;
    }

    public static byte[] readFile() {
        byte[] data = new byte[42];
        byte[] cmd = new byte[50];
        if (instream != null) {
            cmd[0] = -75;
            try {
                instream.read(data, 0, 42);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 1; i < 7; i++) {
                for (int j = 0; j < 7; j++) {
                    cmd[i] = (byte) (cmd[i] | ((data[((i - 1) * 7) + j] & 128) >> (7 - j)));
                }
                cmd[i] = (byte) (cmd[i] & Byte.MAX_VALUE);
            }
            for (int i2 = 0; i2 < 42; i2++) {
                cmd[i2 + 7] = (byte) (data[i2] & Byte.MAX_VALUE);
            }
            for (int i3 = 0; i3 < 49; i3++) {
                cmd[49] = (byte) (cmd[49] + cmd[i3]);
            }
            cmd[49] = (byte) (cmd[49] & Byte.MAX_VALUE);
        }
        return cmd;
    }

    public static byte[] ReadTxtFile(String strFilePath, boolean blag) {
        byte[] back = null;
        File file = new File(strFilePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                Log.i("info", "我的测试  ===========文件不存在");
                App_phms.preferences.edit().putString("isfinish", "1").commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (file.isDirectory()) {
            Log.d("TestFile", "The File doesn't not exist.");
        } else {
            back = TestRead3(file, blag);
        }
        Log.i("info", bs.b);
        return back;
    }

    public static byte[] TestRead3(File file, boolean flag) {
        byte[] string3 = null;
        try {
            instream = new FileInputStream(file);
            ByteArrayOutputStream bos = null;
            byte[] buffer = new byte[32];
            byte[] buffer2 = new byte[42];
            if (!flag) {
                for (int len = -1; len == -1; len = 1) {
                    int len2 = instream.read(buffer);
                    bos = new ByteArrayOutputStream();
                    bos.write(buffer, 0, len2);
                    string3 = doPackIsUpdateCommand(bos.toByteArray());
                }
            } else if (flag) {
                while (flag) {
                    int len22 = instream.read(buffer2);
                    bos = new ByteArrayOutputStream();
                    bos.write(buffer2, 0, len22);
                    byte[] string2 = bos.toByteArray();
                    Log.i("info", "=============" + string2);
                    bytes2HexString(string2);
                    string3 = doPackUpdateData(string2);
                    flag = false;
                }
            }
            instream.close();
            bos.close();
        } catch (Exception e) {
        }
        return string3;
    }

    public static String bytes2HexString(byte[] b) {
        String ret = bs.b;
        for (byte b2 : b) {
            String hex = Integer.toHexString(b2 & 255);
            if (hex.length() == 1) {
                hex = String.valueOf('0') + hex;
            }
            ret = String.valueOf(ret) + hex.toUpperCase() + Constants.DOUHAO;
        }
        int i = 0 + 1;
        Log.i("info", String.valueOf(ret) + "=======0");
        return ret;
    }
}