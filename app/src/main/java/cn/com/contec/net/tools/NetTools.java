package cn.com.contec.net.tools;

import com.j256.ormlite.stmt.query.SimpleComparison;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import u.aly.bs;

public class NetTools {
    public static final String MESSAGETYPE_ERR_BAD_PARAMS = "ERR_BAD_PARAMS";
    public static final String MESSAGETYPE_ERR_DB_UPDATE_PWD = "ERR_DB_UPDATE_PWD";
    public static final String MESSAGETYPE_ERR_INVALID_LOGIN_ID = "ERR_INVALID_LOGIN_ID";
    public static final String MESSAGETYPE_ERR_SESSION_INVALID = "ERR_SESSION_INVALID";

    public static void main(String[] args) {
        HttpClient _client = new HttpClient();
        try {
            String login = login(_client, "883350000071", "123456");
            uploadNibp08ADateToServer(_client, "883350000071", "123456", "120", "100", "78", "2012-03-23 10:31:45");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String login(HttpClient pClient, String pstrUsername, String pstrPsw) throws Exception {
        PostMethod postMethod = new PostMethod("http://data1.contec365.com/http/loginBackgroud.php");
        postMethod.setRequestBody(new NameValuePair[]{new NameValuePair("username", pstrUsername), new NameValuePair("password", pstrPsw)});
        try {
            pClient.executeMethod(postMethod);
            if (postMethod.getStatusCode() == 200) {
                String info = new String(postMethod.getResponseBodyAsString().getBytes("UTF-8"));
                int _begin = info.indexOf(SimpleComparison.EQUAL_TO_OPERATION);
                String _sessionIdString = info.substring(_begin + 1, _begin + 33);
                System.out.println(_sessionIdString);
                return _sessionIdString;
            }
            postMethod.releaseConnection();
            return bs.b;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return pstrUsername;
    }

    public static String[] getSessionIDFormServer(HttpClient pClient) throws Exception {
        String[] _return = {bs.b, bs.b};
        GetMethod _GetMethod = new GetMethod("http://www.contec365.com/getvalidatecode.php");
        pClient.executeMethod(_GetMethod);
        if (_GetMethod.getStatusCode() == 200) {
            _return[1] = _GetMethod.getResponseBodyAsString();
            Header _cookie = _GetMethod.getResponseHeader("Set-Cookie");
            String _res_head = _cookie.getValue();
            int _begin = _res_head.indexOf(SimpleComparison.EQUAL_TO_OPERATION);
            _return[0] = _res_head.substring(_begin + 1, _begin + 33);
            System.out.println(_cookie);
            System.out.println(_return[0]);
            System.out.println(_return[1]);
        }
        return _return;
    }

    public static String checkPhoneNumber(HttpClient pHttpClient, String pPhomeNumber) throws Exception {
        GetMethod _getMethod = new GetMethod(String.valueOf("http://www.contec365.com/cardUserIdCheck.php") + "?" + ("phone=" + pPhomeNumber));
        pHttpClient.executeMethod(_getMethod);
        if (_getMethod.getStatusCode() != 200) {
            return "0";
        }
        String _canUse = _getMethod.getResponseBodyAsString();
        System.out.println(_canUse);
        return _canUse;
    }

    public static String checkAuthCode(HttpClient pHttpClient, String pAuthCode) throws Exception {
        GetMethod _getMethod = new GetMethod(String.valueOf("http://www.contec365.com/validate.php") + "?" + ("validatecode=" + pAuthCode));
        pHttpClient.executeMethod(_getMethod);
        if (_getMethod.getStatusCode() != 200) {
            return "0";
        }
        String _right = _getMethod.getResponseBodyAsString();
        System.out.println(_right);
        return _right;
    }

    public static String sendAuthCodeToPhone(HttpClient pClient, String pAuthCode, String pPhoneNumber) throws Exception {
        GetMethod _getMethod = new GetMethod(String.valueOf("http://www.contec365.com/sendmsg.php") + "?" + ("phone=" + pPhoneNumber + "&validatecode=" + pAuthCode));
        pClient.executeMethod(_getMethod);
        if (_getMethod.getStatusCode() != 200) {
            return "0";
        }
        String _su_ok = _getMethod.getResponseBodyAsString();
        System.out.println("send message " + _su_ok);
        return _su_ok;
    }

    public static String addUser(HttpClient pClient, String pPhoneNumber, String pPwd, String pPhoneAuthCode) throws Exception {
        GetMethod _getMethod = new GetMethod(String.valueOf("http://www.contec365.com/adduser.php") + "?" + ("userphone=" + pPhoneNumber + "&password1=" + pPwd + "&phonecode1=" + pPhoneAuthCode + "&type=1"));
        pClient.executeMethod(_getMethod);
        if (_getMethod.getStatusCode() != 200) {
            return "0";
        }
        String _su_ok = _getMethod.getResponseBodyAsString();
        System.out.println("add user" + _su_ok);
        return _su_ok;
    }

    public static String changePassWord(HttpClient pClient, String pSessionid, String pOldPwd, String pNewPwd) throws Exception {
        PostMethod _postMethod = new PostMethod("http://data1.contec365.com/http/caseTransmission.php");
        _postMethod.setRequestBody(new NameValuePair[]{new NameValuePair("name", "update_pwd.xml." + pSessionid), new NameValuePair("content", "<cmdinfo><oldpwd>" + pOldPwd + "</oldpwd><pwd>" + pNewPwd + "</pwd></cmdinfo>"), new NameValuePair("aaaa", "submit")});
        pClient.executeMethod(_postMethod);
        if (_postMethod.getStatusCode() != 200) {
            return MESSAGETYPE_ERR_DB_UPDATE_PWD;
        }
        String _su_ok = _postMethod.getResponseBodyAsString();
        System.out.println("change pwd " + _su_ok);
        return _su_ok;
    }

    public static String logOut(HttpClient pClient, String pSessionid) throws Exception {
        PostMethod _postMethod = new PostMethod("http://data1.contec365.com/http/caseTransmission.php");
        _postMethod.setRequestBody(new NameValuePair[]{new NameValuePair("name", "logout.xml." + pSessionid), new NameValuePair("aaaa", "submit")});
        pClient.executeMethod(_postMethod);
        if (_postMethod.getStatusCode() != 200) {
            return bs.b;
        }
        String _su_ok = _postMethod.getResponseBodyAsString();
        System.out.println("logout " + _su_ok);
        return _su_ok;
    }

    public static String uploadNibp08ADateToServer(HttpClient pClient, String pUser, String pPwd, String pSys, String pMean, String pDia, String pDate) throws Exception {
        GetMethod _getMethod = new GetMethod(String.valueOf("http://data1.contec365.com/upload/uploadData.php") + "?" + ("user=" + pUser + "&pass=" + pPwd + "&sys=" + pSys + "&mean=" + pMean + "&dia=" + pDia + "&date=" + pDate));
        pClient.executeMethod(_getMethod);
        if (_getMethod.getStatusCode() != 200) {
            return "0";
        }
        String _su_ok = _getMethod.getResponseBodyAsString();
        System.out.println("upload nibp08adate" + _su_ok);
        return _su_ok;
    }
}
