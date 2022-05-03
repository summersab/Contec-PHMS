package cn.com.contec.net.util;

import android.content.Context;
import com.alibaba.cchannel.push.receiver.CPushMessageCodec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class XMLparser {
    public static String getVaueByOneTag(Context pContext, String pFilePath, String pWhat) {
        FileInputStream _inStream = null;
        try {
            FileInputStream _inStream2 = pContext.openFileInput(pFilePath);
            XmlPullParser xmlpull = XmlPullParserFactory.newInstance().newPullParser();
            xmlpull.setInput(_inStream2, CPushMessageCodec.UTF8);
            for (int eventCode = xmlpull.getEventType(); eventCode != 1; eventCode = xmlpull.next()) {
                switch (eventCode) {
                    case 2:
                        if (!pWhat.equals(xmlpull.getName())) {
                            break;
                        } else {
                            return xmlpull.nextText();
                        }
                }
            }
            _inStream2.close();
            return null;
        } catch (Exception e) {
            if (_inStream != null) {
                try {
                    _inStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
            return null;
        }
    }

    public static String getVaueByOneTag(Context pContext, InputStream _inputstream, String pWhat) {
        String _str = null;
        InputStream _inStream = _inputstream;
        try {
            XmlPullParser xmlpull = XmlPullParserFactory.newInstance().newPullParser();
            xmlpull.setInput(_inputstream, CPushMessageCodec.UTF8);
            int eventCode = xmlpull.getEventType();
            while (true) {
                if (eventCode != 1) {
                    switch (eventCode) {
                        case 2:
                            if (pWhat.equals(xmlpull.getName())) {
                                _str = xmlpull.nextText();
                                break;
                            }
                            break;
                    }
                    eventCode = xmlpull.next();
                    if (_str != null) {
                    }
                }
            }
            //_inStream.close();
            //return _str;
        } catch (Exception e) {
            if (_inStream != null) {
                try {
                    _inStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
            return null;
        }
    }

    public static boolean saveXMLtoFile(Context pContext, String pContent, String pFileName, int pFileType) throws Exception {
        FileOutputStream outStream = pContext.openFileOutput(pFileName, pFileType);
        try {
            outStream.write(pContent.getBytes());
            outStream.close();
            return true;
        } catch (Exception e) {
            outStream.close();
            e.printStackTrace();
            return false;
        }
    }
}
