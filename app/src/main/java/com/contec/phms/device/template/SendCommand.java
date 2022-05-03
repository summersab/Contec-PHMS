package com.contec.phms.device.template;

import com.contec.phms.manager.device.ServiceBean;
import com.contec.phms.util.CLog;
import java.io.IOException;
import java.io.OutputStream;
import cn.com.contec.jar.util.PrintBytes;

public class SendCommand {
    public static void send(final byte[] cmd) {
        PrintBytes.printData(cmd);
        new Thread() {
            public void run() {
                super.run();
                try {
                    OutputStream _os = ServiceBean.getInstance().mSocket.getOutputStream();
                    _os.write(cmd);
                    _os.flush();
                    CLog.e("SendCommand", "发送 命令 完成 *********************");
                } catch (IOException e) {
                    e.printStackTrace();
                    CLog.e("SendCommand", "出现异常*********************");
                }
            }
        }.start();
    }

    public static void send(final byte cmd) {
        new Thread() {
            public void run() {
                super.run();
                try {
                    OutputStream _os = ServiceBean.getInstance().mSocket.getOutputStream();
                    _os.write(cmd);
                    _os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
