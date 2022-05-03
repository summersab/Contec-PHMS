package serial.jni;

import android.util.Xml;
import com.alibaba.cchannel.push.receiver.CPushMessageCodec;
import com.alibaba.fastjson.asm.Opcodes;
import com.contec.phms.manager.message.OrderList;
import com.contec.phms.db.LoginUserDao;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.lang.Character;
import org.xmlpull.v1.XmlSerializer;

import u.aly.bs;

public class XmlUtil {
    private String asection = "心电";
    private String caseAddr = "秦皇岛 开发区";
    private String caseName;
    private String caseNation = "汉";
    private String checkTime;
    private String chief = "新采集客户端";
    private String comment = "测试新的采集客户端病史描述";

    private String writeXml() {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            serializer.startDocument(CPushMessageCodec.UTF8, true);
            serializer.startTag(bs.b, "otherparam");
            serializer.startTag(bs.b, "language");
            serializer.text("chinese");
            serializer.endTag(bs.b, "language");
            serializer.startTag(bs.b, "caseName");
            serializer.text(this.caseName);
            serializer.endTag(bs.b, "caseName");
            serializer.startTag(bs.b, "age");
            serializer.text("26");
            serializer.endTag(bs.b, "age");
            serializer.startTag(bs.b, LoginUserDao.height);
            serializer.text("175");
            serializer.endTag(bs.b, LoginUserDao.height);
            serializer.startTag(bs.b, "weight");
            serializer.text("65");
            serializer.endTag(bs.b, "weight");
            serializer.startTag(bs.b, "sex");
            serializer.text("0");
            serializer.endTag(bs.b, "sex");
            serializer.startTag(bs.b, "nation");
            serializer.text(this.caseNation);
            serializer.endTag(bs.b, "nation");
            serializer.startTag(bs.b, "caseAddr");
            serializer.text(this.caseAddr);
            serializer.endTag(bs.b, "caseAddr");
            serializer.startTag(bs.b, "cop");
            serializer.text("lln");
            serializer.endTag(bs.b, "cop");
            serializer.startTag(bs.b, "comment");
            serializer.text(this.comment);
            serializer.endTag(bs.b, "comment");
            serializer.startTag(bs.b, "chief");
            serializer.text(this.chief);
            serializer.endTag(bs.b, "chief");
            serializer.startTag(bs.b, "yzlb");
            serializer.text("1");
            serializer.endTag(bs.b, "yzlb");
            serializer.startTag(bs.b, "appphy");
            serializer.text("test");
            serializer.endTag(bs.b, "appphy");
            serializer.startTag(bs.b, "asection");
            serializer.text(this.asection);
            serializer.endTag(bs.b, "asection");
            serializer.startTag(bs.b, "hr");
            serializer.text("4");
            serializer.endTag(bs.b, "hr");
            serializer.startTag(bs.b, "hb");
            serializer.text("5");
            serializer.endTag(bs.b, "hb");
            serializer.startTag(bs.b, "casesource");
            serializer.text("5");
            serializer.endTag(bs.b, "casesource");
            serializer.startTag(bs.b, "chan");
            serializer.text("1");
            serializer.endTag(bs.b, "chan");
            serializer.startTag(bs.b, "pace");
            serializer.text("0");
            serializer.endTag(bs.b, "pace");
            serializer.startTag(bs.b, "checktime");
            serializer.text(this.checkTime);
            serializer.endTag(bs.b, "checktime");
            serializer.startTag(bs.b, "devicename");
            serializer.text("Android 8000GW");
            serializer.endTag(bs.b, "devicename");
            serializer.startTag(bs.b, "deviceid");
            serializer.text("CONTEC");
            serializer.endTag(bs.b, "deviceid");
            serializer.endTag(bs.b, "otherparam");
            serializer.endDocument();
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean Write(String path, String txt) throws FileNotFoundException, IOException {
        OutputStream os = new FileOutputStream(path);
        OutputStreamWriter osw = new OutputStreamWriter(os);
        osw.write(utf82gbk(txt));
        osw.close();
        os.close();
        return true;
    }

    public boolean saveXml(String path, String name, String time) {
        this.caseName = name;
        this.checkTime = time;
        try {
            return Write(path, writeXml());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getXml(String name, String time) {
        this.caseName = name;
        this.checkTime = time;
        return writeXml().replace(CPushMessageCodec.UTF8, "GBK");
    }

    private String gbk2utf8(String gbk) {
        return unicodeToUtf8(GBK2Unicode(gbk));
    }

    private String utf82gbk(String utf) {
        return Unicode2GBK(utf8ToUnicode(utf));
    }

    private static String GBK2Unicode(String str) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char chr1 = str.charAt(i);
            if (!isNeedConvert(chr1)) {
                result.append(chr1);
            } else {
                result.append("\\u" + Integer.toHexString(chr1));
            }
        }
        return result.toString();
    }

    private static String Unicode2GBK(String dataStr) {
        int index = 0;
        StringBuffer buffer = new StringBuffer();
        int li_len = dataStr.length();
        while (index < li_len) {
            if (index >= li_len - 1 || !"\\u".equals(dataStr.substring(index, index + 2))) {
                buffer.append(dataStr.charAt(index));
                index++;
            } else {
                buffer.append((char) Integer.parseInt(dataStr.substring(index + 2, index + 6), 16));
                index += 6;
            }
        }
        return buffer.toString();
    }

    private static boolean isNeedConvert(char para) {
        return (para & 255) != para;
    }

    private static String utf8ToUnicode(String inStr) {
        char[] myBuffer = inStr.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < inStr.length(); i++) {
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(myBuffer[i]);
            if (ub == Character.UnicodeBlock.BASIC_LATIN) {
                sb.append(myBuffer[i]);
            } else if (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                sb.append((char) (myBuffer[i] - 65248));
            } else {
                sb.append(("\\u" + Integer.toHexString((short) myBuffer[i])).toLowerCase());
            }
        }
        return sb.toString();
    }

    private static String unicodeToUtf8(String theString) {
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        int x = 0;
        while (x < len) {
            int x2 = x + 1;
            char aChar = theString.charAt(x);
            if (aChar == '\\') {
                x = x2 + 1;
                char aChar2 = theString.charAt(x2);
                if (aChar2 == 'u') {
                    int value = 0;
                    int i = 0;
                    while (i < 4) {
                        int x3 = x + 1;
                        char aChar3 = theString.charAt(x);
                        switch (aChar3) {
                            case Opcodes.FALOAD:
                            case '1':
                            case Opcodes.AALOAD:
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = ((value << 4) + aChar3) - 48;
                                break;
                            case 'A':
                            case OrderList.DM_NEXT_DEVICE_OR_POLLING:
                            case 'C':
                            //case ShareSDK.SDK_VERSION_INT:
                            case 'E':
                            case 'F':
                                value = (((value << 4) + 10) + aChar3) - 65;
                                break;
                            case Opcodes.LADD:
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (((value << 4) + 10) + aChar3) - 97;
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
                        }
                        i++;
                        x = x3;
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar2 == 't') {
                        aChar2 = 9;
                    } else if (aChar2 == 'r') {
                        aChar2 = 13;
                    } else if (aChar2 == 'n') {
                        aChar2 = 10;
                    } else if (aChar2 == 'f') {
                        aChar2 = 12;
                    }
                    outBuffer.append(aChar2);
                }
            } else {
                outBuffer.append(aChar);
                x = x2;
            }
        }
        return outBuffer.toString();
    }
}
