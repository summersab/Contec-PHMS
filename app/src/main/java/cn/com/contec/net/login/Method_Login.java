package cn.com.contec.net.login;

import android.util.Log;
import cn.com.contec.net.util.Base64_encoding;
import cn.com.contec.net.util.Constants_jar;
import cn.com.contec.net.util.MD5_encoding;

public class Method_Login {
    public static String mregistHEAD = "000000000000000000000000000000001010700000000000000000000000000000000011";

    public static String[] login(String pstrUsername, String pstrPsw, int pType) {
        return new String[]{Constants_jar.MSG_STRING, Constants_jar.LOGINHEAD, Base64_encoding.encoding("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><request><role>" + pType + "</role><loginid>" + pstrUsername + "</loginid><pwd>" + pstrPsw + "</pwd></request>")};
    }

    public static String[] login_out(String pLogID, String pPass, String pSessionID) {
        String _sigin1 = MD5_encoding.MD5(String.valueOf(pLogID) + pPass);
        String _messageBody = "101003" + pSessionID + "10";
        return new String[]{Constants_jar.MSG_STRING, String.valueOf(MD5_encoding.MD5(String.valueOf(_sigin1) + _messageBody)) + _messageBody};
    }

    public static String[] sendMsgVerificationCode(String ptype, String pphonenumber) {
        String _messageBody = "<?xml version=\"1.0\" encoding=\"GBK\" ?><request><type>" + ptype + "</type><phone>" + pphonenumber + "</phone></request>";
        Log.i("lz", _messageBody);
        return new String[]{Constants_jar.MSG_STRING, mregistHEAD, Base64_encoding.encoding(_messageBody)};
    }
}
